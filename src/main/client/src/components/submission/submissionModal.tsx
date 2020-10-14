import { unwrapResult } from "@reduxjs/toolkit";
import { Formik } from "formik";
import { FormikControl } from "formik-react-bootstrap";
import React from "react";
import { Button, Form, Modal } from "react-bootstrap";
import { useSelector } from "react-redux";
import { Answer, ExamState, QuestionSubmission, selectExamById, submitExam, updateExamSubmission } from "../../redux/slices/examsSlice";
import { QuestionState, selectQuestionsForExam } from "../../redux/slices/questionsSlice";
import { selectMe, selectUserById, UserState } from "../../redux/slices/usersSlice";
import { RootState, useAppDispatch } from "../../redux/store";

interface SubmissionModalProps {
  show: boolean,
  onHide: () => any,
  examId: string,
  studentId: string,
  eTag?: string,
}

interface FormValues {
  answers: Answer[],
}

export const SubmissionModal = (props: SubmissionModalProps): JSX.Element => {
  const dispatch = useAppDispatch();
  const student = useSelector<RootState, UserState | undefined>(
    state => selectUserById(state, props.studentId)
  );
  const me = useSelector(selectMe);

  const questionSubmissions: Record<string, QuestionSubmission | undefined> = {};

  useSelector<RootState, ExamState | undefined>(
    state => selectExamById(state, props.examId)
  )
    ?.submissions
    ?.find(submission => submission.studentId === props.studentId)
    ?.questionSubmissions
    .forEach(questionSubmission => {
      questionSubmissions[questionSubmission.questionId] = questionSubmission;
    });

  const questionMap: Record<string, QuestionState> = {};
  const questions = useSelector(selectQuestionsForExam(props.examId));

  questions
    .forEach(question => {
      questionMap[question.id] = question;
    });

  const handleSubmit = (values: FormValues): void => {
    const newMarks = values.answers.reduce((a, b) => a + (b.marks ?? 0), 0);

    if (props.eTag === undefined) {
      dispatch(submitExam({
        examId: props.examId,
        studentId: props.studentId,
        marks: newMarks,
        answers: values.answers,
      }))
        .then(unwrapResult)
        .then(props.onHide)
        .catch(e => {
          console.error(e);
        });
    } else {
      dispatch(updateExamSubmission({
        examId: props.examId,
        studentId: props.studentId,
        marks: newMarks,
        eTag: props.eTag,
      }))
        .then(unwrapResult)
        // .then(()=> {
        //   return Promise.all(values.answers.map(answer => {
        //     dispatch(updateQuestionSubmission(
        //       ))
        //   }));
        // })
        .then(props.onHide)
        .catch(e => {
          console.error(e);
        });
    }
  };

  return (
    <Modal show={props.show} onHide={props.onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>
          {
            // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
            student!.name
          }&apos;s Exam
        </Modal.Title>
      </Modal.Header>
      <Formik
        initialValues={{
          answers: [],
          // answers: questions.map(question => ({
          //   questionId: question.id,
          //   answer: questionSubmissions[question.id]?.answer,
          //   marks: questionSubmissions[question.id]?.marks === undefined ? 0 : questionSubmissions[question.id]?.marks,
          // })),
        }}
        onSubmit={handleSubmit}>
        {
          ({
            isSubmitting,
            handleSubmit,
          }) => (
            <Form onSubmit={handleSubmit as any}>
              <fieldset disabled={me?.type === "STUDENT"}>
                <Modal.Body>
                  {
                    questions.map((question, i) => (
                      <React.Fragment key={question.id}>
                        <Form.Group>
                          <Form.Label>{question.question}</Form.Label>
                          <Form.Control
                            type="text"
                            placeholder={questionSubmissions[question.id]?.answer ?? ""}
                            readOnly />
                        </Form.Group>
                        <FormikControl
                          label="Marks"
                          min="0"
                          max={question.marks}
                          name={`answers[${i}].mark`}
                          //value={questionSubmissions[question.id]?.mark}
                          type="number"/>
                        <br />
                      </React.Fragment>
                    ))
                  }
                </Modal.Body>
              </fieldset>
              {
                me?.type !== "STUDENT" &&
                  <Modal.Footer>
                    <Button type="submit" disabled={isSubmitting}>Submit Marks</Button>
                  </Modal.Footer>
              }
            </Form>
          )
        }
      </Formik>
    </Modal>
  );
};

import { unwrapResult } from "@reduxjs/toolkit";
import { Formik } from "formik";
import { FormikControl } from "formik-react-bootstrap";
import React from "react";
import { Button, Form, Modal } from "react-bootstrap";
import { useSelector } from "react-redux";
import { ExamState, QuestionSubmission, selectExamById, SubmissionMark, submitExam1, updateExamSubmission } from "../../redux/slices/examsSlice";
import { QuestionState, selectQuestionsForExam } from "../../redux/slices/questionsSlice";
import { selectMe, selectUserById, UserState } from "../../redux/slices/usersSlice";
import { RootState, useAppDispatch } from "../../redux/store";

interface SubmissionModalProps {
  show: boolean,
  onHide: () => any,
  examId: string,
  studentId: string,
}

interface FormValues {
  marks: number[],
  submissionMark: SubmissionMark[],
}

export const SubmissionModal = (props: SubmissionModalProps): JSX.Element => {
  const dispatch = useAppDispatch();
  const student = useSelector<RootState, UserState | undefined>(
    state => selectUserById(state, props.studentId)
  );
  const me = useSelector(selectMe);

  const marks = useSelector<RootState, ExamState | undefined>(
    state => selectExamById(state, props.examId)
  )
    ?.submissions
    ?.find(submission => submission.studentId === props.studentId)
    ?.marks;

  const questionSubmissions: Record<string, QuestionSubmission> = {};

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
    let count = 0
    let newMarks = 0
    for(let i = 0 ; i < values.submissionMark.length ; i++)
    {
      count= values.submissionMark[i].mark
      newMarks += count
    }
    if (marks === undefined) {
      dispatch(submitExam1({
        examId: props.examId,
        studentId: props.studentId,
        marks: newMarks,
        answers: values.submissionMark,
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
        answers: values.submissionMark,
      }))
        .then(unwrapResult)
        .then(props.onHide)
        .catch(e => {
          console.error(e);
        });
    }
  };

  if (student === undefined) {
    return <></>;
  }

  return (
    <Modal show={props.show} onHide={props.onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>
          {student.name}&apos;s Exam
        </Modal.Title>
      </Modal.Header>
      <Formik
        initialValues={{ marks: [],
          submissionMark: questions.map(question => ({
            questionId: question.id,
            answer: questionSubmissions[question.id]?.answer,
            mark: questionSubmissions[question.id]?.mark === undefined ? 0 : questionSubmissions[question.id]?.mark,
          })),
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
                          name={`submissionMark[${i}].mark`}
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

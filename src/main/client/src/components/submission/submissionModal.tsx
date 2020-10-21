import { unwrapResult } from "@reduxjs/toolkit";
import { Formik } from "formik";
import { FormikControl } from "formik-react-bootstrap";
import React, { useState } from "react";
import { Alert, Button, Form, Modal } from "react-bootstrap";
import { useSelector } from "react-redux";
import { ExamState, selectExamById } from "../../redux/slices/examsSlice";
import { createExamSubmission, selectExamSubmissionsForExam, updateExamSubmission } from "../../redux/slices/examSubmissionsSlice";
import { QuestionState, selectQuestionsForExam } from "../../redux/slices/questionsSlice";
import { createQuestionSubmission, QuestionSubmission, QuestionSubmissionState, selectQuestionSubmissionsForExamSubmission, updateQuestionSubmission } from "../../redux/slices/questionSubmissionsSlice";
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
  answers: Omit<QuestionSubmission, "examSubmissionId">[],
}

export const SubmissionModal = (props: SubmissionModalProps): JSX.Element => {
  const dispatch = useAppDispatch();
  const student = useSelector<RootState, UserState | undefined>(
    state => selectUserById(state, props.studentId)
  );
  const me = useSelector(selectMe);
  const exam = useSelector<RootState, ExamState | undefined>(
    state => selectExamById(state, props.examId)
  );
  const examSubmissions = useSelector(selectExamSubmissionsForExam(exam?.id));
  const questionSubmissions = useSelector(
    selectQuestionSubmissionsForExamSubmission(examSubmissions[0]?.id)
  );
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const handleHide = (): void => {
    setErrorMessage(null);
    props.onHide();
  };

  const qSubmissions: Record<string, QuestionSubmissionState | undefined> = {};

  questionSubmissions.forEach(q => {
    qSubmissions[q.questionId] = q;
  });

  const questionMap: Record<string, QuestionState> = {};
  const questions = useSelector(selectQuestionsForExam(props.examId));

  questions
    .forEach(question => {
      questionMap[question.id] = question;
    });

  const handleSubmit = (values: FormValues): Promise<void> => {
    const newMarks = values.answers.reduce((a, b) => a + (b.marks ?? 0), 0);

    if (props.eTag === undefined) {
      return dispatch(createExamSubmission({
        examId: props.examId,
        studentId: props.studentId,
        marks: newMarks,
        answers: values.answers,
      }))
        .then(unwrapResult)
        .then(handleHide)
        .catch((e: Error) => {
          setErrorMessage("Failed to mark submission");
          console.error(e);
        });
    } else {
      return dispatch(updateExamSubmission({
        examId: props.examId,
        studentId: props.studentId,
        marks: newMarks,
        eTag: props.eTag,
      }))
        .then(unwrapResult)
        .then(() => {
          return Promise.all(values.answers
            .filter(answer => answer.marks !== undefined)
            .map(answer => {
              if (qSubmissions[answer.questionId] === undefined) {
                return dispatch(createQuestionSubmission({
                  questionId: answer.questionId,
                  marks: answer.marks,
                  answer: "",
                  examSubmissionId: examSubmissions[0].id,
                })).then(unwrapResult);
              } else {
                return dispatch(updateQuestionSubmission({
                  questionId: answer.questionId,
                  // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
                  marks: answer.marks!,
                  examSubmissionId: examSubmissions[0].id,
                  // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
                  eTag: qSubmissions[answer.questionId]!.meta.eTag,
                })).then(unwrapResult);
              }
            })
          );
        })
        .then(handleHide)
        .catch((e: Error) => {
          setErrorMessage("Failed to mark submission");
          console.error(e);
        });
    }
  };

  return (
    <Modal show={props.show} onHide={handleHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>
          {
            // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
            student!.name
          }&apos;s Exam
        </Modal.Title>
      </Modal.Header>
      {
        errorMessage !== null &&
          <Alert variant="danger">
            {errorMessage}
          </Alert>
      }
      <Formik
        initialValues={{
          answers: questions.map(question => ({
            questionId: question.id,
            answer: "",
            marks: qSubmissions[question.id]?.marks === null ? undefined : qSubmissions[question.id]?.marks,
          })),
        }}
        onSubmit={handleSubmit}
        enableReinitialize>
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
                            placeholder={qSubmissions[question.id]?.answer ?? ""}
                            readOnly />
                        </Form.Group>
                        <FormikControl
                          label="Marks"
                          min="0"
                          max={question.marks}
                          name={`answers[${i}].marks`}
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

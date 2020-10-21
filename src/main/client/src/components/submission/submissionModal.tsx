import { unwrapResult } from "@reduxjs/toolkit";
import { Formik } from "formik";
import { FormikControl } from "formik-react-bootstrap";
import React, { useState } from "react";
import { Alert, Button, Form, Modal } from "react-bootstrap";
import { useSelector } from "react-redux";
import { ExamState, selectExamById } from "../../redux/slices/examsSlice";
import { createExamSubmission, selectExamSubmissionsForExam, updateExamSubmission } from "../../redux/slices/examSubmissionsSlice";
import { selectQuestionsForExam } from "../../redux/slices/questionsSlice";
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
  const examSubmission = useSelector(selectExamSubmissionsForExam(exam?.id))
    .find(submission => submission.studentId === props.studentId);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const questionSubmissions: Record<string, QuestionSubmissionState | undefined> = {};

  useSelector(selectQuestionSubmissionsForExamSubmission(examSubmission?.id))
    .forEach(q => {
      questionSubmissions[q.questionId] = q;
    });

  console.log(props.studentId);
  console.log(questionSubmissions);

  const questions = useSelector(selectQuestionsForExam(props.examId));

  const handleHide = (): void => {
    setErrorMessage(null);
    props.onHide();
  };

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
              if (questionSubmissions[answer.questionId] === undefined) {
                return dispatch(createQuestionSubmission({
                  questionId: answer.questionId,
                  marks: answer.marks,
                  answer: "",
                  // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
                  examSubmissionId: examSubmission!.id,
                })).then(unwrapResult);
              } else {
                return dispatch(updateQuestionSubmission({
                  questionId: answer.questionId,
                  // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
                  marks: answer.marks!,
                  // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
                  examSubmissionId: examSubmission!.id,
                  // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
                  eTag: questionSubmissions[answer.questionId]!.meta.eTag,
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
            marks: questionSubmissions[question.id]?.marks === null ? undefined : questionSubmissions[question.id]?.marks,
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
                            placeholder={questionSubmissions[question.id]?.answer ?? ""}
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

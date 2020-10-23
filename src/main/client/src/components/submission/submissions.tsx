import { unwrapResult } from "@reduxjs/toolkit";
import { Formik } from "formik";
import React, { Fragment, useEffect, useState } from "react";
import { Alert, Button, Form, Table } from "react-bootstrap";
import { useSelector } from "react-redux";
import * as yup from "yup";
import { ExamState, selectExamById } from "../../redux/slices/examsSlice";
import { createExamSubmission, ExamSubmissionState, fetchExamSubmissions, selectExamSubmissionsForExam, updateExamSubmission } from "../../redux/slices/examSubmissionsSlice";
import { fetchQuestions, selectQuestionsForExam } from "../../redux/slices/questionsSlice";
import { selectSubjectById, SubjectState } from "../../redux/slices/subjectsSlice";
import { fetchStudentsForSubject, selectMe, selectUsersByIds, selectUsersStatus } from "../../redux/slices/usersSlice";
import { RootState, useAppDispatch } from "../../redux/store";
import { Loader } from "../loader/loader";
import { SubmissionModal } from "./submissionModal";

export interface SubmissionsProps {
  examId: string,
}

interface FormValues {
  marks: {
    studentId: string,
    marks?: number,
  }[],
}

const FormSchema = yup.object().shape({
  marks: yup.array().of(yup.object().shape({
    studentId: yup.string().required(),
    marks: yup.number(),
  })).required("Marks is a required field."),
});

export const Submissions = (props: SubmissionsProps): JSX.Element => {
  const dispatch = useAppDispatch();
  const usersStatus = useSelector(selectUsersStatus);
  const me = useSelector(selectMe);
  const exam = useSelector<RootState, ExamState | undefined>(
    state => selectExamById(state, props.examId)
  );
  const examSubmissions = useSelector(selectExamSubmissionsForExam(exam?.id));
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const questions = useSelector(selectQuestionsForExam(props.examId));
  const subject = useSelector<RootState, SubjectState | undefined>(
    state => selectSubjectById(state, exam?.subjectId ?? "")
  );
  const subjectId = subject?.id;
  const [examSubmissionsLoading, setExamSubmissionsLoading] = useState(true);
  const [questionsLoading, setQuestionsLoading] = useState(true);

  let students = useSelector(selectUsersByIds(subject?.students ?? []));

  const [
    submissionModalShow,
    setSubmissionModalShow,
  ] = useState<string | null>(null);

  if (me?.type === "STUDENT") {
    students = students.filter(s => s.id === me.id);
  }

  const submissions: Record<string, ExamSubmissionState | undefined> = {};

  examSubmissions.forEach(submission => {
    if (submission.marks !== undefined) {
      submissions[submission.studentId] = submission;
    }
  });

  useEffect(() => {
    dispatch(fetchExamSubmissions(props.examId))
      .then(unwrapResult)
      .then(() => {
        setExamSubmissionsLoading(false);
      })
      .catch((e: Error) => {
        if (e.message === "400") {
          setErrorMessage("Bad Request");
        } else if (e.message === "401") {
          setErrorMessage("Unauthorized Request");
        } else if (e.message === "404") {
          setErrorMessage("Failed to fetch exam submissions");
        } else if (e.message === "412") {
          setErrorMessage("Client Error");
        } else if (e.message === "500") {
          setErrorMessage("Internal Server Error");
        }
        console.error(e);
      });
  }, [dispatch, props.examId]);

  useEffect(() => {
    if (me?.type === "INSTRUCTOR" && subjectId !== undefined) {
      dispatch(fetchStudentsForSubject(subjectId))
        .then(unwrapResult)
        .catch((e: Error) => {
          if (e.message === "400") {
            setErrorMessage("Bad Request");
          } else if (e.message === "401") {
            setErrorMessage("Unauthorized Request");
          } else if (e.message === "412") {
            setErrorMessage("Client Error");
          } else if (e.message === "500") {
            setErrorMessage("Internal Server Error");
          }
          console.error(e);
        });
    }
  }, [dispatch, me, subjectId]);

  useEffect(() => {
    dispatch(fetchQuestions(props.examId))
      .then(unwrapResult)
      .then(() => {
        setQuestionsLoading(false);
      })
      .catch((e: Error) => {
        if (e.message === "400") {
          setErrorMessage("Bad Request");
        } else if (e.message === "401") {
          setErrorMessage("Unauthorized Request");
        } else if (e.message === "404") {
          setErrorMessage("Failed to fetch questions");
        } else if (e.message === "412") {
          setErrorMessage("Client Error");
        } else if (e.message === "500") {
          setErrorMessage("Internal Server Error");
        }
        console.error(e);
      });
  }, [props.examId, dispatch]);

  const handleSubmit = (values: FormValues): Promise<any> => {
    const promises: Promise<any>[] = values.marks.map(mark => {
      if (mark.marks === undefined) {
        return Promise.resolve();
      }

      if (submissions[mark.studentId] === undefined) {
        return dispatch(createExamSubmission({
          examId: props.examId,
          studentId: mark.studentId,
          marks: mark.marks,
          answers: [],
        }))
          .then(unwrapResult)
          .catch((e: Error) => {
            setErrorMessage("Failed to submit marks");
            console.error(e);
          });
      } else {
        return dispatch(updateExamSubmission({
          examId: props.examId,
          studentId: mark.studentId,
          marks: mark.marks,
          // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
          eTag: submissions[mark.studentId]!.meta.eTag,
        }))
          .then(unwrapResult)
          .catch((e: Error) => {
            setErrorMessage("Failed to submit marks");
            console.error(e);
          });
      }
    });

    return Promise.all(promises);
  };

  if (usersStatus !== "finished" || examSubmissionsLoading || questionsLoading) {
    return <Loader />;
  }

  if (errorMessage !== null) {
    return (
      <Alert variant="danger">
        {errorMessage}
      </Alert>
    );
  }

  return (
    <Formik
      initialValues={{ marks: students.map(s => ({
        studentId: s.id,
        // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
        marks: submissions[s.id] === undefined ? undefined : (submissions[s.id]!.marks ?? undefined),
      })) }}
      onSubmit={handleSubmit}
      validationSchema={FormSchema}
      enableReinitialize>
      {
        ({
          handleBlur,
          handleChange,
          handleSubmit,
          isSubmitting,
          touched,
          errors,
          values,
        }) => (
          <Form onSubmit={handleSubmit as any}>
            <Table striped bordered hover>
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Marks</th>
                  <th>Details</th>
                </tr>
              </thead>
              <tbody>
                {
                  students.map((student, i) => (
                    <Fragment key={student.id}>
                      <tr>
                        <td>
                          {student.name}
                        </td>
                        <td>
                          <Form.Control
                            type="number"
                            name={`marks[${i}].marks`}
                            value={values.marks[i]?.marks}
                            onBlur={handleBlur}
                            onChange={handleChange}
                            min="0"
                            max={questions.reduce((a, b) => a + b.marks, 0)}
                            disabled={isSubmitting || me?.type === "STUDENT"}
                            isInvalid={
                              !!(
                                touched.marks
                                  && touched.marks[i]
                                  && errors.marks
                                  && errors.marks[i]
                              )
                            } />
                        </td>
                        <td>
                          <Button onClick={() => setSubmissionModalShow(student.id)}>
                            Show Details
                          </Button>
                        </td>
                      </tr>
                      <SubmissionModal
                        show={submissionModalShow === student.id}
                        onHide={() => setSubmissionModalShow(null)}
                        examId={props.examId}
                        studentId={student.id}
                        eTag={submissions[student.id]?.meta.eTag} />
                    </Fragment>
                  ))
                }
              </tbody>
            </Table>
            {
              me?.type !== "STUDENT" &&
                <Button type="submit" disabled={isSubmitting}>
                  Submit Marks
                </Button>
            }
          </Form>
        )
      }
    </Formik>
  );
};

import { unwrapResult } from "@reduxjs/toolkit";
import { Formik } from "formik";
import React, { useEffect } from "react";
import { Button, Form, Table } from "react-bootstrap";
import { useSelector } from "react-redux";
import * as yup from "yup";
import { ExamState, fetchSubmissions, selectExamById, submitExam, updateExamSubmission } from "../../redux/slices/examsSlice";
import { selectSubjectById, SubjectState } from "../../redux/slices/subjectsSlice";
import { fetchUsers, selectMe, selectUsersByIds, selectUsersStatus } from "../../redux/slices/usersSlice";
import { RootState, useAppDispatch } from "../../redux/store";

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
  const subject = useSelector<RootState, SubjectState | undefined>(
    state => selectSubjectById(state, exam?.subjectId ?? "")
  );
  const students = useSelector(selectUsersByIds(subject?.students ?? []));

  const submissions: Record<string, number> = {};

  exam?.submissions?.forEach(submission => {
    submissions[submission.studentId] = submission.marks ?? -1;
  });

  useEffect(() => {
    dispatch(fetchSubmissions(props.examId))
      .then(unwrapResult)
      .catch(e => {
        console.error(e);
      });
  }, [dispatch, props.examId]);

  useEffect(() => {
    if (usersStatus === "idle" && me !== undefined) {
      dispatch(fetchUsers())
        .then(unwrapResult)
        .catch(e => {
          console.error(e);
        });
    }
  }, [dispatch, usersStatus, me]);

  const handleSubmit = (values: FormValues): void => {
    console.log(values);
    values.marks.forEach(mark => {
      if (mark.marks === undefined) {
        return;
      }

      if (submissions[mark.studentId] === undefined) {
        dispatch(submitExam({
          examId: props.examId,
          studentId: mark.studentId,
          marks: mark.marks,
          answers: [],
        }))
          .then(unwrapResult)
          .catch(e => {
            console.error(e);
          });
      } else {
        dispatch(updateExamSubmission({
          examId: props.examId,
          studentId: mark.studentId,
          marks: mark.marks,
        }))
          .then(unwrapResult)
          .catch(e => {
            console.error(e);
          });
      }
    });
  };

  if (exam?.submissions === undefined) {
    return <></>;
  }

  return (
    <Formik
      initialValues={{ marks: students.map(s => ({
        studentId: s.id,
        marks: submissions[s.id],
      })) }}
      onSubmit={handleSubmit}
      validationSchema={FormSchema}>
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
                    <tr key={student.id}>
                      <td>
                        {student.name}
                      </td>
                      <td>
                        <Form.Control
                          onClick={() => {}}
                          type="number"
                          name={`marks[${i}].marks`}
                          value={values.marks[i].marks}
                          onBlur={handleBlur}
                          onChange={handleChange}
                          disabled={isSubmitting}
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
                        <Button>
                          Show Details
                        </Button>
                      </td>
                    </tr>
                  ))
                }
              </tbody>
            </Table>
            <Button type="submit" disabled={isSubmitting}>
              Submit Marks
            </Button>
          </Form>
        )
      }
    </Formik>
  );
};

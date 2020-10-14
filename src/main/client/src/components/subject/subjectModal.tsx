/* eslint-disable @typescript-eslint/no-non-null-assertion */
import { unwrapResult } from "@reduxjs/toolkit";
import { Formik } from "formik";
import { FormikControl } from "formik-react-bootstrap";
import React, { useEffect } from "react";
import { Button, Form, Modal, Spinner } from "react-bootstrap";
import { useSelector } from "react-redux";
import Select from "react-select";
import * as yup from "yup";
import { addSubject, updateSubject } from "../../redux/slices/subjectsSlice";
import { fetchUsers, selectInstructors, selectMe, selectStudents, selectUsersStatus } from "../../redux/slices/usersSlice";
import { useAppDispatch } from "../../redux/store";

export interface SubjectModalProps {
  show: boolean,
  onHide: () => any,
}

interface UpdateSubjectModalProps extends SubjectModalProps {
  id: string,
  name: string,
  description: string,
  instructors: string[],
  students: string[],
  eTag: string,
}

interface FormValues {
  name: string,
  description: string,
  instructors: { label: string, value: string }[],
  students: { label: string, value: string }[],
}

const FormSchema = yup.object().shape({
  name: yup.string().required("Name is a required field."),
  description: yup.string(),
  instructors: yup.array().of(yup.object().shape({
    label: yup.string(),
    value: yup.string(),
  })).min(1, "At least one instructor must be assigned to the subject.").required(),
  students: yup.array().of(yup.object().shape({
    label: yup.string(),
    value: yup.string(),
  })).min(1, "At least one student must be assigned to the subject.").required(),
});

export const SubjectModal = (props: UpdateSubjectModalProps | SubjectModalProps): JSX.Element => {
  const dispatch = useAppDispatch();
  const usersStatus = useSelector(selectUsersStatus);
  const students = useSelector(selectStudents);
  const instructors = useSelector(selectInstructors);
  const me = useSelector(selectMe);

  useEffect(() => {
    if (usersStatus === "idle" && me !== undefined) {
      dispatch(fetchUsers())
        .then(unwrapResult)
        .catch(e => {
          console.error(e);
        });
    }
  }, [dispatch, usersStatus, me]);

  const onSubmit = (values: FormValues): void => {
    if ("id" in props) {
      const deletedInstructors = props.instructors.filter(i => !values.instructors.map(i => i.value).includes(i));
      const newInstructors = values.instructors.map(i => i.value).filter(i => !props.instructors.includes(i));
      const deletedStudents = props.students.filter(s => !values.students.map(s => s.value).includes(s));
      const newStudents = values.students.map(s => s.value).filter(s => !props.students.includes(s));

      dispatch(updateSubject({
        id: props.id,
        deletedInstructors,
        newInstructors,
        deletedStudents,
        newStudents,
        subject: {
          name: values.name,
          description: values.description,
        },
        eTag: props.eTag,
      }))
        .then(unwrapResult)
        .then(() => {
          props.onHide();
        })
        .catch(e => {
          console.error(e);
        });
    } else {
      dispatch(addSubject({
        name: values.name,
        description: values.description,
        instructors: values.instructors.map(i => i.value),
        students: values.students.map(s => s.value),
      }))
        .then(unwrapResult)
        .then(() => {
          props.onHide();
        })
        .catch(e => {
          console.error(e);
        });
    }
  };

  if (usersStatus === "loading") {
    return (
      <Spinner animation="border" role="status">
        <span className="sr-only">Loading...</span>
      </Spinner>
    );
  }

  return (
    <Modal show={props.show} onHide={props.onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>
          {
            "id" in props
              ? "Update Subject"
              : "Create Subject"
          }
        </Modal.Title>
      </Modal.Header>
      <Formik
        initialValues={{
          name: (props as UpdateSubjectModalProps).name ?? "",
          description: (props as UpdateSubjectModalProps).description ?? "",
          instructors: (props as UpdateSubjectModalProps).instructors?.map(i => {
            const instructor = instructors.find(i2 => i2.id === i);
            return {
              label: instructor!.name,
              value: instructor!.id,
            };
          }) ?? [],
          students: (props as UpdateSubjectModalProps).students?.map(s => {
            const student = students.find(s2 => s2.id === s);
            return {
              label: student!.name,
              value: student!.id,
            };
          }) ?? [],
        }}
        validationSchema={FormSchema}
        onSubmit={onSubmit}
      >
        {
          ({
            handleSubmit,
            isSubmitting,
            values,
            handleBlur,
            setFieldValue,
            errors,
            touched,
          }) => (
            <Form id="createSubject" onSubmit={handleSubmit as any}>
              <Modal.Body>
                <FormikControl
                  type="text"
                  label="Name"
                  name="name" />
                <FormikControl
                  as="textarea"
                  label="Description"
                  name="description" />
                <Form.Group>
                  <Form.Label>Instructors</Form.Label>
                  <Select
                    isMulti
                    options={instructors.map(i => ({ label: i.name, value: i.id }))}
                    name="instructors"
                    value={values.instructors as any}
                    onChange={option => setFieldValue("instructors", option ?? [])}
                    isDisabled={isSubmitting}
                    onBlur={handleBlur} />
                  <Form.Control.Feedback className={touched.instructors && errors.instructors && "d-block"} type="invalid">
                    {errors.instructors}
                  </Form.Control.Feedback>
                </Form.Group>
                <Form.Group>
                  <Form.Label>Students</Form.Label>
                  <Select
                    isMulti
                    options={students.map(s => ({ label: s.name, value: s.id }))}
                    name="students"
                    value={values.students as any}
                    onChange={option => setFieldValue("students", option ?? [])}
                    isDisabled={isSubmitting}
                    onBlur={handleBlur} />
                  <Form.Control.Feedback className={touched.instructors && errors.instructors && "d-block"} type="invalid">
                    {errors.students}
                  </Form.Control.Feedback>
                </Form.Group>
              </Modal.Body>
              <Modal.Footer>
                <Button type="submit" variant="success" disabled={isSubmitting}>
                  {
                    "id" in props
                      ? "Update Subject"
                      : "Create Subject"
                  }
                </Button>
              </Modal.Footer>
            </Form>
          )
        }
      </Formik>
    </Modal>
  );
};

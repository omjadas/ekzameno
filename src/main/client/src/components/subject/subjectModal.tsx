import { unwrapResult } from "@reduxjs/toolkit";
import { Formik } from "formik";
import { FormikControl } from "formik-react-bootstrap";
import React, { useEffect } from "react";
import { Button, Form, FormGroup, Modal } from "react-bootstrap";
import { useSelector } from "react-redux";
import Select from "react-select";
import * as yup from "yup";
import { addSubject } from "../../redux/slices/subjectsSlice";
import { fetchUsers, selectInstructors, selectStudents, selectUsersStatus } from "../../redux/slices/usersSlice";
import { useAppDispatch } from "../../redux/store";

export interface SubjectModalProps {
  show: boolean,
  onHide: () => any,
}

interface UpdateSubjectProps extends SubjectModalProps {
  name: string,
  slug: string,
  instructors: string[],
  students: string[],
  description: string,
}

interface FormValues {
  name: string,
  instructors: { label: string, value: string }[],
  students: { label: string, value: string }[],
  description: string,
}

const FormSchema = yup.object().shape({
  name: yup.string().required(),
  description: yup.string(),
  instructors: yup.array().of(yup.object().shape({
    label: yup.string(),
    value: yup.string(),
  })).min(1).required(),
  students: yup.array().of(yup.object().shape({
    label: yup.string(),
    value: yup.string(),
  })).min(1).required(),
});

export const SubjectModal = (props: UpdateSubjectProps | SubjectModalProps): JSX.Element => {
  const dispatch = useAppDispatch();
  const usersStatus = useSelector(selectUsersStatus);
  const students = useSelector(selectStudents);
  const instructors = useSelector(selectInstructors);

  useEffect(() => {
    if (usersStatus === "idle") {
      dispatch(fetchUsers());
    }
  }, [dispatch]);

  const onSubmit = (values: FormValues): void => {
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
  };

  return (
    <Modal show={props.show} onHide={props.onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>
          Create Subject
        </Modal.Title>
      </Modal.Header>
      <Formik
        initialValues={{
          name: (props as any).name ?? "",
          description: (props as any).description ?? "",
          instructors: (props as any).instructors ?? [],
          students: (props as any).students ?? [],
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
                <FormGroup>
                  <Form.Label>Instructors</Form.Label>
                  <Select
                    isMulti
                    options={instructors.map(i => ({ label: i.name, value: i.id }))}
                    name="instructors"
                    value={values.instructors as any}
                    onChange={option => setFieldValue("instructors", option)}
                    isDisabled={isSubmitting}
                    onBlur={handleBlur} />
                  <Form.Control.Feedback type="invalid">
                    {errors.instructors}
                  </Form.Control.Feedback>
                </FormGroup>
                <FormGroup>
                  <Form.Label>Students</Form.Label>
                  <Select
                    isMulti
                    options={students.map(s => ({ label: s.name, value: s.id }))}
                    name="students"
                    value={values.students as any}
                    onChange={option => setFieldValue("students", option)}
                    isDisabled={isSubmitting}
                    onBlur={handleBlur} />
                  <Form.Control.Feedback type="invalid">
                    {errors.students}
                  </Form.Control.Feedback>
                </FormGroup>
              </Modal.Body>
              <Modal.Footer>
                <Button type="submit" variant="success" disabled={isSubmitting}>
                  Create Subject
                </Button>
              </Modal.Footer>
            </Form>
          )
        }
      </Formik>
    </Modal>
  );
};

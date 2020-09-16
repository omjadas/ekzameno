import { unwrapResult } from "@reduxjs/toolkit";
import { Formik } from "formik";
import { FormikControl } from "formik-react-bootstrap";
import React from "react";
import { Button, Form, FormGroup, Modal } from "react-bootstrap";
import Select from "react-select";
import * as yup from "yup";
import { addUser, UserType } from "../../redux/slices/usersSlice";
import { useAppDispatch } from "../../redux/store";

export interface UserModalProps {
  show: boolean,
  onHide: () => any,
}

interface FormValues {
  name: string,
  email: string,
  password: string,
  confirmPassword: string,
  type: {
    label: string,
    value: UserType,
  },
}

const selectOptions = [
  { value: "ADMINISTRATOR", label: "Administrator" },
  { value: "INSTRUCTOR", label: "Instructor" },
  { value: "STUDENT", label: "Student" },
];

const FormSchema = yup.object().shape({
  name: yup.string().required(),
  email: yup.string().email().required(),
  password: yup.string().required(),
  confirmPassword: yup.string().test(
    "equal",
    "passwords do not match",
    function(password) {
      return password === this.resolve(yup.ref("password"));
    }
  ).required(""),
  type: yup.object().shape({
    label: yup.string().required(),
    value: yup.string().oneOf(["ADMINISTRATOR", "INSTRUCTOR", "STUDENT"]).required(),
  }).required(),
});

export const UserModal = (props: UserModalProps): JSX.Element => {
  const dispatch = useAppDispatch();

  const onSubmit = (values: FormValues): void => {
    dispatch(addUser({
      name: values.name,
      email: values.email,
      password: values.password,
      type: values.type.value,
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
          Create User
        </Modal.Title>
      </Modal.Header>
      <Formik
        initialValues={{
          name: "",
          email: "",
          password: "",
          confirmPassword: "",
          type: { label: "Student", value: "STUDENT" },
        }}
        validationSchema={FormSchema}
        onSubmit={onSubmit}>
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
            <Form id="createUser" onSubmit={handleSubmit as any}>
              <Modal.Body>
                <FormikControl
                  type="text"
                  label="Name"
                  name="name" />
                <FormikControl
                  type="text"
                  label="Email"
                  name="email" />
                <FormikControl
                  type="password"
                  label="Password"
                  name="password" />
                <FormikControl
                  type="password"
                  label="Confirm Password"
                  name="confirmPassword" />
                <FormGroup>
                  <Form.Label>Type</Form.Label>
                  <Select
                    options={selectOptions}
                    name="type"
                    value={values.type as any}
                    onChange={option => setFieldValue("type", option)}
                    isDisabled={isSubmitting}
                    onBlur={handleBlur} />
                  <Form.Control.Feedback className={touched.type && errors.type && "d-block"} type="invalid">
                    {errors.type}
                  </Form.Control.Feedback>
                </FormGroup>
              </Modal.Body>
              <Modal.Footer>
                <Button type="submit" variant="success" disabled={isSubmitting}>
                  Create User
                </Button>
              </Modal.Footer>
            </Form>
          )
        }
      </Formik>
    </Modal>
  );
};

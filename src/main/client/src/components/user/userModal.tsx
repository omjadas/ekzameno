import { unwrapResult } from "@reduxjs/toolkit";
import { Formik } from "formik";
import { FormikControl } from "formik-react-bootstrap";
import React, { useState } from "react";
import { Alert, Button, Form, Modal } from "react-bootstrap";
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
  name: yup.string().required("Name is a required field."),
  email: yup.string().email("Email must be an email.").required("Email is a required field."),
  password: yup.string().required("Password is a required field."),
  confirmPassword: yup.string().test(
    "equal",
    "Passwords do not match.",
    function(password) {
      return password === this.resolve(yup.ref("password"));
    }
  ).required(""),
  type: yup.object().shape({
    label: yup.string().required(),
    value: yup.string().oneOf(["ADMINISTRATOR", "INSTRUCTOR", "STUDENT"]).required(),
  }).required("You must select one of the roles."),
});

export const UserModal = (props: UserModalProps): JSX.Element => {
  const dispatch = useAppDispatch();
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const handleHide = (): void => {
    setErrorMessage(null);
    props.onHide();
  };

  const handleSubmit = (values: FormValues): Promise<void> => {
    return dispatch(addUser({
      name: values.name,
      email: values.email,
      password: values.password,
      type: values.type.value,
    }))
      .then(unwrapResult)
      .then(() => {
        handleHide();
      })
      .catch((e: Error) => {
        if (e.message === "409") {
          setErrorMessage("User already exists");
        } else {
          setErrorMessage("Failed to create user");
        }

        console.error(e);
      });
  };

  return (
    <Modal show={props.show} onHide={handleHide} centered>
      {
        errorMessage !== null &&
          <Alert variant="danger">
            {errorMessage}
          </Alert>
      }
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
        onSubmit={handleSubmit}>
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
                <Form.Group>
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
                </Form.Group>
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

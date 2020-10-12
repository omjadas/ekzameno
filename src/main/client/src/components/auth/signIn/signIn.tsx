import { unwrapResult } from "@reduxjs/toolkit";
import { Formik } from "formik";
import { FormikControl } from "formik-react-bootstrap";
import React from "react";
import { Button, Form, Modal, Spinner, Tab } from "react-bootstrap";
import { useSelector } from "react-redux";
import * as yup from "yup";
import { selectUsersStatus, signIn } from "../../../redux/slices/usersSlice";
import { useAppDispatch } from "../../../redux/store";

interface Props {
  onHide: () => any,
}

interface FormValues {
  email: string,
  password: string,
}

const FormSchema = yup.object().shape({
  email: yup.string()
    .email("Email must be a valid email.")
    .required("Email is a required field."),
  password: yup.string().required("Password is a required field."),
});

export const SignIn = (props: Props): JSX.Element => {
  const dispatch = useAppDispatch();

  const onSubmit = (values: FormValues): void => {
    dispatch(signIn(values))
      .then(unwrapResult)
      .then(() => {
        props.onHide();
      })
      .catch(e => {
        console.error(e);
      });
  };

  const usersStatus = useSelector(selectUsersStatus);

  if (usersStatus === "loading") {
    return (
      <Spinner animation="border" role="status">
        <span className="sr-only">Loading...</span>
      </Spinner>
    );
  }

  return (
    <Tab.Pane eventKey="signIn">
      <Formik
        initialValues={{ email: "", password: "" }}
        onSubmit={onSubmit}
        validationSchema={FormSchema}
      >
        {
          ({
            handleSubmit,
            isSubmitting,
          }) => (
            <Form
              id="signIn"
              onSubmit={handleSubmit as any}>
              <Modal.Body>
                <FormikControl
                  label="Email"
                  name="email"
                  placeholder="Enter email" />
                <FormikControl
                  label="Password"
                  name="password"
                  type="password"
                  placeholder="Enter password" />
              </Modal.Body>
              <Modal.Footer>
                <Button type="submit" variant="success" disabled={isSubmitting}>
                  Sign In
                </Button>
              </Modal.Footer>
            </Form>
          )
        }
      </Formik>
    </Tab.Pane>
  );
};

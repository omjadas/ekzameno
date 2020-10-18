import { unwrapResult } from "@reduxjs/toolkit";
import { Formik } from "formik";
import { FormikControl } from "formik-react-bootstrap";
import React, { useState } from "react";
import { Alert, Button, Form, Modal, Tab } from "react-bootstrap";
import * as yup from "yup";
import { signIn } from "../../../redux/slices/usersSlice";
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
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  
  const onSubmit = (values: FormValues): void => {
    dispatch(signIn(values))
      .then(unwrapResult)
      .then(() => {
        props.onHide();
      })
      .catch((e: Error) => {
        if (e.message === "400") {
          setErrorMessage("Bad Request");
        } else if (e.message === "401") {
          setErrorMessage("Unauthorized Request");
        } else if (e.message === "404") {
          setErrorMessage("User not found");
        } else if (e.message === "412") {
          setErrorMessage("Client Error");
        } else if (e.message === "500") {
          setErrorMessage("Internal Server Error");
        } else if (e.message === "408") {
          setErrorMessage("Couldn't find the user in time");
        }
        console.error(e);
      });
  };

  if (errorMessage  != null) {
    return (
      <Alert variant="danger">
        {errorMessage}
      </Alert>
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

import { unwrapResult } from "@reduxjs/toolkit";
import { Formik } from "formik";
import { FormikControl } from "formik-react-bootstrap";
import React from "react";
import { Button, Form, Modal, Tab } from "react-bootstrap";
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
  email: yup.string().email().required(),
  password: yup.string().required(),
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

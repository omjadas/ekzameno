import { Formik } from "formik";
import { FormikControl } from "formik-react-bootstrap";
import React from "react";
import { Button, Form, Modal, Tab } from "react-bootstrap";
import * as yup from "yup";

interface Props {
  onHide: () => any,
}

interface FormValues {
  email: string,
  password: string,
}

const FormSchema = yup.object().shape({
  email: yup.string().required(),
  password: yup.string().required(),
});

export const SignIn = (props: Props): JSX.Element => {
  const onSubmit = (values: FormValues): Promise<any> => {
    return fetch("/api/login", {
      method: "post",
      body: JSON.stringify({
        email: values.email,
        password: values.password,
      }),
      headers: {
        "Content-Type": "application/json",
      },
    }).then(res => {
      if (res.status === 200) {
        props.onHide();
      } else {
        // TODO: handle errors
      }
    }).catch();
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

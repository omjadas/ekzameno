import { Formik } from "formik";
import { FormikControl } from "formik-react-bootstrap";
import React, { useState } from "react";
import { Button, Form, FormGroup, Modal } from "react-bootstrap";
import Select from "react-select";
import yup from "yup";

export interface QuestionModalProps {
  show: boolean,
  onHide: () => any,
}

interface FormValues {
  question: string,
  description: string,
  type: {
    label: string,
    value: "MULTIPLE_CHOICE" | "SHORT_ANSWER",
  },
  answers: string[],
}

const selectOptions = [
  { value: "MULTIPLE_CHOICE", label: "Multiple Choice" },
  { value: "SHORT_ANSWER", label: "Short Answer" },
];

const FormSchema = yup.object().shape({
  name: yup.string().required(),
  description: yup.string(),
  type: yup.object().shape({
    label: yup.string(),
    value: yup.string().oneOf(["MULTIPLE_CHOICE", "SHORT_ANSWER"]),
  }),
  answers: yup.array().of(yup.string()),
});

export const QuestionModal = (props: QuestionModalProps): JSX.Element => {
  const [answers, setAnswers] = useState(1);

  const handleSubmit = (values: FormValues): void => {

  };

  return (
    <Modal show={props.show} onHide={props.onHide} centered>
      <Modal.Header>
        <Modal.Title>
          Create Question
        </Modal.Title>
      </Modal.Header>
      <Formik
        initialValues={{
          question: "",
          description: "",
          type: { label: "Multiple Choice", value: "MULTIPLE_CHOICE" },
          answers: [],
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
            <Form id="createQuestion" onSubmit={handleSubmit as any}>
              <Modal.Body>
                <FormikControl
                  type="text"
                  label="Question"
                  name="question" />
                <FormikControl
                  as="textarea"
                  label="Description"
                  name="description" />
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
                  {
                    values.type.value === "MULTIPLE_CHOICE" &&
                      [...Array(answers).keys()].map(i => (
                        <FormikControl
                          key={i}
                          type="text"
                          label={`Answer ${i}`}
                          name={`answers[${i}]`} />
                      ))
                  }
                </FormGroup>
              </Modal.Body>
              {
                values.type.value === "MULTIPLE_CHOICE" &&
                  <Modal.Footer>
                    <Button variant="primary" onClick={() => setAnswers(answers + 1)}>
                      Add Answer
                    </Button>
                  </Modal.Footer>
              }
            </Form>
          )
        }
      </Formik>
    </Modal>
  );
};

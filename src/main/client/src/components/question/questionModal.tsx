import { unwrapResult } from "@reduxjs/toolkit";
import { Formik } from "formik";
import { FormikControl } from "formik-react-bootstrap";
import React, { useState } from "react";
import { Button, Form, FormGroup, Modal } from "react-bootstrap";
import Select from "react-select";
import * as yup from "yup";
import { addQuestion, QuestionType } from "../../redux/slices/questionsSlice";
import { useAppDispatch } from "../../redux/store";

export interface QuestionModalProps {
  show: boolean,
  onHide: () => any,
  examId: string,
}

interface FormValues {
  question: string,
  description: string,
  type: {
    label: string,
    value: QuestionType,
  },
  answers: string[],
}

const selectOptions = [
  { value: "MULTIPLE_CHOICE", label: "Multiple Choice" },
  { value: "SHORT_ANSWER", label: "Short Answer" },
];

const FormSchema = yup.object().shape({
  question: yup.string().required(),
  description: yup.string(),
  type: yup.object().shape({
    label: yup.string(),
    value: yup.string().oneOf(["MULTIPLE_CHOICE", "SHORT_ANSWER"]),
  }),
  answers: yup.array().of(yup.string().required()),
});

export const QuestionModal = (props: QuestionModalProps): JSX.Element => {
  const [answers, setAnswers] = useState(1);
  const dispatch = useAppDispatch();

  const onSubmit = (values: FormValues): void => {
    dispatch(addQuestion({
      examId: props.examId,
      question: {
        question: values.question,
        type: values.type.value,
        answers: values.answers,
      },
    }))
      .then(unwrapResult)
      .then(props.onHide)
      .catch(e => {
        console.error(e);
      });
  };

  return (
    <Modal show={props.show} onHide={props.onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>
          Create Question
        </Modal.Title>
      </Modal.Header>
      <Formik
        initialValues={{
          question: "",
          description: "",
          type: { label: "Multiple Choice", value: "MULTIPLE_CHOICE" },
          answers: [""],
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
                </FormGroup>
                {
                  values.type.value === "MULTIPLE_CHOICE" &&
                    [...Array(answers).keys()].map(i => (
                      <FormikControl
                        key={i}
                        type="text"
                        label={`Answer ${i + 1}`}
                        name={`answers[${i}]`} />
                    ))
                }
              </Modal.Body>
              <Modal.Footer>
                {
                  values.type.value === "MULTIPLE_CHOICE" &&
                    <Button className="mr-auto" variant="primary" onClick={() => setAnswers(answers + 1)}>
                      Add Answer
                    </Button>
                }
                <Button type="submit" variant="success" disabled={isSubmitting}>
                  Create Question
                </Button>
              </Modal.Footer>
            </Form>
          )
        }
      </Formik>
    </Modal>
  );
};

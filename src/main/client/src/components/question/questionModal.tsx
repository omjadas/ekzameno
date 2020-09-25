import { unwrapResult } from "@reduxjs/toolkit";
import { Formik } from "formik";
import { FormikControl } from "formik-react-bootstrap";
import React, { useState } from "react";
import { Button, Form, FormGroup, Modal } from "react-bootstrap";
import Select from "react-select";
import * as yup from "yup";
import { addQuestion, QuestionType, updateQuestion } from "../../redux/slices/questionsSlice";
import { useAppDispatch } from "../../redux/store";

export interface QuestionModalProps {
  show: boolean,
  onHide: () => any,
}

interface CreateQuestionModalProps extends QuestionModalProps {
  examId: string,
}
interface UpdateQuestionModalProps extends QuestionModalProps {
  id: string,
  question: string,
  marks: number,
  type: QuestionType,
  // type: {
  //   label: string,
  //   value: QuestionType,
  // },
  options: string[],
  correct: number,
}
interface FormValues {
  question: string,
  marks: number,
  type: {
    label: string,
    value: QuestionType,
  },
  options: string[],
  correct: number,
}

const selectOptions = [
  { value: "MULTIPLE_CHOICE", label: "Multiple Choice" },
  { value: "SHORT_ANSWER", label: "Short Answer" },
];

const FormSchema = yup.object().shape({
  question: yup.string().required("Question is a required field."),
  marks: yup.number().min(1, "Marks must be greater than 1.").required("Marks is a required field."),
  type: yup.object().shape({
    label: yup.string(),
    value: yup.string().oneOf(["MULTIPLE_CHOICE", "SHORT_ANSWER"]),
  }).required("Type is a required field."),
  options: yup.array().of(yup.string().test(
    "requiredOption",
    "Option is a required field.",
    function(option) {
      console.error(option);
      if (this.resolve(yup.ref("type")).value === "MULTIPLE_CHOICE") {
        return option !== "" && option !== undefined;
      }

      return true;
    }
  )),
  correct: yup.number().test(
    "lessThanOptions",
    "Correct Option must be less than the number of options.",
    function(correct) {
      if (typeof correct === "number" && this.resolve(yup.ref("type")).value === "MULTIPLE_CHOICE") {
        return correct <= this.resolve(yup.ref("options")).length && correct >= 1;
      }

      return true;
    }
  ),
});

export const QuestionModal = (props: UpdateQuestionModalProps | CreateQuestionModalProps): JSX.Element => {
  const [options, setOptions] = useState(0);
  const dispatch = useAppDispatch();

  const onSubmit = (values: FormValues): void => {
    if ("id" in props) {
      dispatch(updateQuestion({
        id: props.id,
        question: {
          question: values.question,
          marks: values.marks,
          type: values.type.value,
          options: values.options.map(s => s),
        },
      }))
        .then(unwrapResult)
        .then(() => {
          props.onHide();
        })
        .catch(e => {
          console.error(e);
        });
    } else {
      dispatch(addQuestion({
        examId: props.examId,
        question: {
          question: values.question,
          marks: values.marks,
          type: values.type.value,
          answers: values.options.map((a, i) => ({
            answer: a,
            correct: i + 1 === values.correct,
          })),
        },
      }))
        .then(unwrapResult)
        .then(props.onHide)
        .catch(e => {
          console.error(e);
        });
    }
  };

  return (
    <Modal show={props.show} onHide={props.onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>
          {
            "id" in props ?
              "Update Question"
              :
              "Create Question"
          }
        </Modal.Title>
      </Modal.Header>
      <Formik
        initialValues={{
          question: "",
          marks: 1,
          type: { label: "Multiple Choice", value: "MULTIPLE_CHOICE" },
          options: [],
          correct: 1,
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
                  min="1"
                  type="number"
                  label="Marks"
                  name="marks" />
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
                    <>
                      <FormikControl
                        type="number"
                        label="Correct Option"
                        name="correct" />
                      {
                        [...Array(options).keys()].map(i => (
                          <FormikControl
                            key={i}
                            type="text"
                            label={`Option ${i + 1}`}
                            name={`options[${i}]`} />
                        ))
                      }
                    </>
                }
              </Modal.Body>
              <Modal.Footer>
                {
                  values.type.value === "MULTIPLE_CHOICE" &&
                    <Button className="mr-auto" variant="primary" onClick={() => setOptions(options + 1)}>
                      Add Option
                    </Button>
                }
                <Button type="submit" variant="success" disabled={isSubmitting}>
                  {
                    "id" in props ?
                      "Update Question"
                      :
                      "Create Question"
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

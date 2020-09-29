import { unwrapResult } from "@reduxjs/toolkit";
import { Formik } from "formik";
import { FormikControl } from "formik-react-bootstrap";
import React, { useEffect, useState } from "react";
import { Button, Form, FormGroup, Modal } from "react-bootstrap";
import { useSelector } from "react-redux";
import Select from "react-select";
import * as yup from "yup";
import { addOption, deleteOption, fetchOptions, selectOptionsByIds } from "../../redux/slices/optionsSlice";
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
  optionIds: string[],
}

interface FormValues {
  question: string,
  marks: number,
  type: {
    label: string,
    value: QuestionType,
  },
  options: string[],
  correctOption: number,
}

const labels: Record<QuestionType, string> = {
  MULTIPLE_CHOICE: "Multiple Choice",
  SHORT_ANSWER: "Short Answer",
};

const selectOptions: { value: QuestionType, label: string }[] = [
  { value: "MULTIPLE_CHOICE", label: "Multiple Choice" },
  { value: "SHORT_ANSWER", label: "Short Answer" },
];

const FormSchema = yup.lazy((obj: any) => {
  const common = {
    question: yup.string().required("Question is a required field."),
    marks: yup.number().min(1, "Marks must be greater than 1.").required("Marks is a required field."),
    type: yup.object().shape({
      label: yup.string(),
      value: yup.string().oneOf(["MULTIPLE_CHOICE", "SHORT_ANSWER"]),
    }).required("Type is a required field."),
  };

  if (obj.type.value === "MULTIPLE_CHOICE") {
    return yup.object().shape({
      ...common,
      options: yup.array().of(yup.string().required("Option is a required field."))
        .min(1, " ")
        .test(
          "unique",
          "Options must be unique.",
          options => {
            if (Array.isArray(options)) {
              return options.length === new Set(options).size;
            }

            return true;
          }
        )
        .required(""),
      correct: yup.number().test(
        "lessThanOptions",
        "Correct Option must be less than the number of options.",
        (correct) => {
          if (typeof correct === "number") {
            return correct <= obj.options.length && correct >= 1;
          }

          return false;
        }
      ).required(),
    });
  }

  return yup.object().shape(common);
});

export const QuestionModal = (props: UpdateQuestionModalProps | CreateQuestionModalProps): JSX.Element => {
  const [numOptions, setNumOptions] = useState(0);
  const dispatch = useAppDispatch();
  const options = useSelector(selectOptionsByIds((props as UpdateQuestionModalProps).optionIds ?? []));

  useEffect(() => {
    if ((props as UpdateQuestionModalProps).id !== undefined) {
      dispatch(fetchOptions((props as UpdateQuestionModalProps).id));
    }
  }, [(props as UpdateQuestionModalProps).id]);

  const onHide = (): void => {
    props.onHide();
    setNumOptions(0);
  };

  const onSubmit = (values: FormValues): void => {
    if ("id" in props) {
      const deletedOptions = options.filter(o => !values.options.includes(o.answer));
      const newOptions = values.options
        .map((o, i) => ({
          answer: o,
          correct: values.correctOption === i + 1,
        }))
        .filter(o => !options.find(o2 => o2.answer !== o.answer));

      dispatch(updateQuestion({
        id: props.id,
        question: {
          question: values.question,
          marks: values.marks,
          type: values.type.value,
        },
      }))
        .then(unwrapResult)
        .then(() => {
          const deletedPromises = deletedOptions.map(o => {
            return dispatch(deleteOption(o.id)).then(unwrapResult);
          });
          const newPromises = newOptions.map(o => {
            return dispatch(addOption({
              questionId: props.id,
              option: {
                answer: o.answer,
                correct: o.correct,
              },
            }))
              .then(unwrapResult);
          });
          // eslint-disable-next-line @typescript-eslint/ban-ts-ignore
          // @ts-ignore
          return Promise.all([...deletedPromises, ...newPromises]);
        })
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
          options: values.options.map((a, i) => ({
            answer: a,
            correct: i + 1 === values.correctOption,
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
    <Modal show={props.show} onHide={onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>
          {
            "id" in props
              ? "Update Question"
              : "Create Question"
          }
        </Modal.Title>
      </Modal.Header>
      <Formik
        initialValues={{
          question: (props as UpdateQuestionModalProps).question ?? "",
          marks: (props as UpdateQuestionModalProps).marks ?? 1,
          type: (props as UpdateQuestionModalProps).type === undefined
            ? { label: "Multiple Choice", value: "MULTIPLE_CHOICE" }
            : { label: labels[(props as UpdateQuestionModalProps).type], value: (props as UpdateQuestionModalProps).type },
          options: options.map(o => o.answer),
          correctOption: options.findIndex(o => o.correct === true) !== -1
            ? options.findIndex(o => o.correct === true) + 1
            : 1,
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
                        name="correctOption" />
                      {
                        [...Array(numOptions).keys()].map(i => (
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
                    <Button className="mr-auto" variant="primary" onClick={() => setNumOptions(numOptions + 1)}>
                      Add Option
                    </Button>
                }
                <Button type="submit" variant="success" disabled={isSubmitting}>
                  {
                    "id" in props
                      ? "Update Question"
                      : "Create Question"
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

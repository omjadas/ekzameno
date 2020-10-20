import { faTrash } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { unwrapResult } from "@reduxjs/toolkit";
import { FieldArray, Formik } from "formik";
import { FormikControl } from "formik-react-bootstrap";
import React, { useState } from "react";
import { Alert, Button, Form, InputGroup, Modal } from "react-bootstrap";
import { useSelector } from "react-redux";
import Select from "react-select";
import * as yup from "yup";
import { addOption, deleteOption, OptionState, selectOptionsByIds, updateOption } from "../../redux/slices/optionsSlice";
import { addQuestion, questionLabels, QuestionType, updateQuestion } from "../../redux/slices/questionsSlice";
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
  eTag: string,
}

interface FormValues {
  question: string,
  marks: number,
  type: {
    label: string,
    value: QuestionType,
  },
  options: ({ answer: string } | OptionState)[],
  correctOption: number,
}

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
      options: yup.array().of(
        yup.object().shape({
          id: yup.string(),
          answer: yup.string()
            .test(
              "unique",
              "Options must be unique.",
              answer => {
                if (typeof answer === "string") {
                  return (obj.options as { answer: string }[]).filter(o => o.answer === answer).length === 1;
                }

                return true;
              }
            )
            .required("Option is a required field."),
        })
      )
        .min(1)
        .required(""),
      correctOption: yup.number().test(
        "lessThanOptions",
        "Correct Option must be less than the number of options.",
        (correct) => {
          if (typeof correct === "number") {
            return correct <= obj.options.length && correct >= 1;
          }

          return true;
        }
      ).required("Correct Option is a required field."),
    });
  }

  return yup.object().shape(common);
});

export const QuestionModal = (
  props: UpdateQuestionModalProps | CreateQuestionModalProps
): JSX.Element => {
  const dispatch = useAppDispatch();
  const options = useSelector(selectOptionsByIds((props as UpdateQuestionModalProps).optionIds ?? []));
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const handleHide = (): void => {
    setErrorMessage(null);
    props.onHide();
  };

  const handleSubmit = (values: FormValues): Promise<void> => {
    if ("id" in props) {
      const deletedOptions = options.filter(o => !values.options.map((o: any) => o.id).includes(o.id));

      return dispatch(updateQuestion({
        id: props.id,
        question: {
          question: values.question,
          marks: values.marks,
          type: values.type.value,
        },
        eTag: props.eTag,
      }))
        .then(unwrapResult)
        .then(() => {
          const newPromises: Promise<any>[] = [];
          const updatedPromises: Promise<any>[] = [];
          values.options.forEach((option, i) => {
            if ("id" in option) {
              updatedPromises.push(
                dispatch(updateOption({
                  id: option.id,
                  option: {
                    answer: option.answer,
                    correct: values.correctOption === i + 1,
                  },
                  eTag: option.meta.eTag,
                }))
                  .then(unwrapResult)
              );
            } else {
              newPromises.push(
                dispatch(addOption({
                  questionId: props.id,
                  option: {
                    answer: option.answer,
                    correct: values.correctOption === i + 1,
                  },
                }))
                  .then(unwrapResult)
              );
            }
          });

          const deletedPromises: Promise<any>[] = deletedOptions.map(o => {
            return dispatch(deleteOption(o.id)).then(unwrapResult);
          });

          return Promise.all([...deletedPromises, ...newPromises, ...updatedPromises]);
        })
        .then(() => {
          handleHide();
        })
        .catch((e: Error) => {
          setErrorMessage("Failed to update question");
          console.error(e);
        });
    } else {
      return dispatch(addQuestion({
        examId: props.examId,
        question: {
          question: values.question,
          marks: values.marks,
          type: values.type.value,
          options: values.options.map((o, i) => ({
            answer: o.answer,
            correct: i + 1 === values.correctOption,
          })),
        },
      }))
        .then(unwrapResult)
        .then(handleHide)
        .catch((e: Error) => {
          setErrorMessage("Failed to create question");
          console.error(e);
        });
    }
  };

  return (
    <Modal show={props.show} onHide={handleHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>
          {
            "id" in props
              ? "Update Question"
              : "Create Question"
          }
        </Modal.Title>
      </Modal.Header>
      {
        errorMessage !== null &&
          <Alert variant="danger">
            {errorMessage}
          </Alert>
      }
      <Formik
        initialValues={{
          question: (props as UpdateQuestionModalProps).question ?? "",
          marks: (props as UpdateQuestionModalProps).marks ?? 1,
          type: (props as UpdateQuestionModalProps).type === undefined
            ? { label: "Multiple Choice", value: "MULTIPLE_CHOICE" }
            : { label: questionLabels[(props as UpdateQuestionModalProps).type], value: (props as UpdateQuestionModalProps).type },
          options: options.length === 0
            ? [{ answer: "" }]
            : options,
          correctOption: options.findIndex(o => o.correct === true) !== -1
            ? options.findIndex(o => o.correct === true) + 1
            : 1,
        }}
        validationSchema={FormSchema}
        onSubmit={handleSubmit}>
        {
          ({
            handleSubmit,
            isSubmitting,
            values,
            handleBlur,
            handleChange,
            setFieldValue,
            setValues,
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
                <Form.Group>
                  <Form.Label>Type</Form.Label>
                  <Select
                    options={selectOptions}
                    name="type"
                    value={values.type as any}
                    onChange={option => setFieldValue("type", option)}
                    isDisabled={"id" in props || isSubmitting}
                    onBlur={handleBlur} />
                  <Form.Control.Feedback className={touched.type && errors.type && "d-block"} type="invalid">
                    {errors.type}
                  </Form.Control.Feedback>
                </Form.Group>
                {
                  values.type.value === "MULTIPLE_CHOICE" &&
                    <>
                      <FormikControl
                        type="number"
                        label="Correct Option"
                        name="correctOption" />
                      <FieldArray
                        name="options">
                        {
                          arrayHelpers => (
                            values.options.map((option, i) => (
                              <Form.Group key={i}>
                                <Form.Label>
                                  {`Option ${i + 1}`}
                                </Form.Label>
                                <InputGroup>
                                  <Form.Control
                                    type="text"
                                    name={`options[${i}].answer`}
                                    value={values.options[i]?.answer}
                                    onBlur={handleBlur}
                                    disabled={isSubmitting}
                                    isInvalid={
                                      !!(
                                        touched.options
                                          && (touched.options as unknown as boolean[])[i]
                                          && errors.options
                                          && errors.options[i]
                                      )
                                    }
                                    onChange={handleChange} />
                                  <InputGroup.Append>
                                    <Button
                                      variant="outline-danger"
                                      onClick={() => arrayHelpers.remove(i)}
                                      disabled={isSubmitting}>
                                      <FontAwesomeIcon icon={faTrash} />
                                    </Button>
                                  </InputGroup.Append>
                                  <Form.Control.Feedback
                                    // eslint-disable-next-line @typescript-eslint/ban-ts-ignore
                                    // @ts-ignore
                                    className={
                                      touched.options
                                        && touched.options[i]
                                        && errors.options
                                        && errors.options[i]
                                        && "d-block"
                                    }
                                    type="invalid">
                                    {errors.options !== undefined && (errors.options[i] as { answer: string })?.answer}
                                  </Form.Control.Feedback>
                                </InputGroup>
                              </Form.Group>
                            ))
                          )
                        }
                      </FieldArray>
                    </>
                }
              </Modal.Body>
              <Modal.Footer>
                {
                  values.type.value === "MULTIPLE_CHOICE" &&
                    <Button className="mr-auto" variant="primary" onClick={() => {
                      values.options.push({ answer: "" });
                      setValues(values);
                    }}>
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

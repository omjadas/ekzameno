import React, { useEffect, useState } from "react";
import { Button, Card } from "react-bootstrap";
import { useSelector } from "react-redux";
import { FieldArray, Formik } from "formik";
import { FormikControl } from "formik-react-bootstrap";
import { Form, FormGroup, InputGroup, Modal } from "react-bootstrap";
import { ExamState, selectExamById } from "../../redux/slices/examsSlice";
import { deleteQuestion, fetchQuestions, questionLabels, selectQuestionsByIds } from "../../redux/slices/questionsSlice";
import { selectMe } from "../../redux/slices/usersSlice";
import { RootState, useAppDispatch } from "../../redux/store";
import { addQuestion, QuestionType, updateQuestion } from "../../redux/slices/questionsSlice";
import { QuestionModal } from "./questionModal";
import * as yup from "yup";
import styles from "./questions.module.scss";

interface QuestionProps {
  examId: string,
}

export const Questions = (props: QuestionProps): JSX.Element => {
  const dispatch = useAppDispatch();
  const exam = useSelector<RootState, ExamState | undefined>(
    state => selectExamById(state, props.examId)
  );
  const questions = useSelector(selectQuestionsByIds(exam?.questionIds ?? []));
  const [questionModalShow, setQuestionModalShow] = useState<string | null>(null);
  const me = useSelector(selectMe);

  useEffect(() => {
    dispatch(fetchQuestions(props.examId));
  }, [props.examId, dispatch]);

  const onClick = (id: string): void => {
    dispatch(deleteQuestion({
      questionId: id,
    }))
      .catch(e => {
        console.error(e);
      });
  };

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
          yup.string()
            .test(
              "unique",
              "Options must be unique.",
              option => {
                if (typeof option === "string") {
                  return (obj.options as string[]).filter(o => o === option).length === 1;
                }

                return true;
              },
            )
            .required("Option is a required field.")
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

            return false;
          }
        ).required(),
      });
    }

    return yup.object().shape(common);
  });
  const onSubmit = (values: FormValues): void => {

  }
  interface FormValues {
    question: string,
    marks: number,
    // type: {
    //   label: string,
    //   value: QuestionType,
    // },
    options: string[],
  }
  return (
    <>
      {
        me?.type === "INSTRUCTOR" &&
        <div className={styles.wrapper}>
          {
            questions.map(question => {
              return (
                <Card key={question.id}>
                  <Card.Header>{question.question}</Card.Header>
                  <Card.Body>
                    <Card.Text>
                      Marks: {question.marks}
                      <br />
                  Type: {questionLabels[question.type]}
                    </Card.Text>
                    <Button className="mr-2" onClick={() => setQuestionModalShow(question.id)}>
                      Edit
                  </Button>
                    <Button className="mr-2" onClick={() => onClick(question.id)}>
                      Delete
                  </Button>
                    <QuestionModal
                      show={questionModalShow === question.id}
                      onHide={() => setQuestionModalShow(null)}
                      id={question.id}
                      question={question.question}
                      marks={question.marks}
                      type={question.type}
                      optionIds={question.optionIds} />
                  </Card.Body>
                </Card>
              );
            })
          }
        </div>
      }
      {
        me?.type === "STUDENT" &&
        questions.map(question => {
          return (
            <Formik
              initialValues={{
                question: question.question,
                marks: question.marks,
                type: question.type,
                options: question.optionIds,
              }}
              validationSchema={FormSchema}
              onSubmit={onSubmit}>
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
                    <Form id="answerQuestion" onSubmit={handleSubmit as any}>
                      {

                        <Modal.Body>
                          <Card key={question.id}>
                            <Card.Header>{question.question}</Card.Header>
                            <Card.Body>
                              <Card.Text>
                                Marks: {question.marks}
                                <br />
                                Type: {questionLabels[question.type]}
                              </Card.Text>
                              {
                                question.type === "MULTIPLE_CHOICE" &&
                                <>
                                  <FieldArray
                                    name="options">
                                    {
                                      arrayHelpers => (
                                        <>
                                          {
                                           <Card.Text>
                                           option: {values.options.map((option,i) =>(
                                             <InputGroup>
                                                    <Form.Control
                                                      type="text"
                                                      name={`options[${i}]`}
                                                      value={values.options[i]}
                                                      onChange={handleChange} />
                                                  </InputGroup>
                                           ))}
                                         </Card.Text>
                                        // values.options.map((option, i) => (
                                        //   <Form.Group key={i}>
                                        //     <Form.Label>
                                        //       {`Option ${i + 1}`}
                                        //     </Form.Label>
                                        //     <InputGroup>
                                        //       <Form.Control
                                        //         type="text"
                                        //         name={`options[${i}]`}
                                        //         value={values.options[i]}
                                        //         onChange={handleChange} />
                                        //     </InputGroup>
                                        //   </Form.Group>
                                        //))
                                      }
                                        </>
                                      )
                                    }
                                  </FieldArray>
                                </>
                              }
                              {
                                question.type === "SHORT_ANSWER" &&
                                <>
                                  <Form.Control as="textarea" />
                                </>
                              }
                              </Card.Body>
                          </Card>
                        </Modal.Body>
                      }
                      < Modal.Footer >
                            <Button type="submit" variant="success" disabled={isSubmitting}>
                              Submit
                </Button>
                          </Modal.Footer>
                    </Form>
                  )
              }
            </Formik>
                  );
        })
      }
    </>
          );
        };

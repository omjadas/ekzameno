import { unwrapResult } from "@reduxjs/toolkit";
import { Formik } from "formik";
import { FormikControl } from "formik-react-bootstrap";
import React, { useEffect, useState } from "react";
import { Alert, Button, Card, Form } from "react-bootstrap";
import { useSelector } from "react-redux";
import Select from "react-select";
import { ExamState, selectExamById } from "../../redux/slices/examsSlice";
import { createExamSubmission, fetchExamSubmissions, selectExamSubmissionsForExam } from "../../redux/slices/examSubmissionsSlice";
import { fetchOptions, selectAllOptions } from "../../redux/slices/optionsSlice";
import { deleteQuestion, fetchQuestions, questionLabels, selectQuestionsForExam } from "../../redux/slices/questionsSlice";
import { selectQuestionSubmissionsForExamSubmission } from "../../redux/slices/questionSubmissionsSlice";
import { selectMe } from "../../redux/slices/usersSlice";
import { RootState, useAppDispatch } from "../../redux/store";
import { Loader } from "../loader/loader";
import { QuestionModal } from "./questionModal";
import styles from "./questions.module.scss";

interface QuestionProps {
  examId: string,
}

interface Answer {
  answer: string,
  questionId: string,
  marks?: number,
}

interface FormValues {
  answers: Answer[],
}

export const Questions = (props: QuestionProps): JSX.Element => {
  const dispatch = useAppDispatch();
  const exam = useSelector<RootState, ExamState | undefined>(
    state => selectExamById(state, props.examId)
  );
  const examSubmissions = useSelector(selectExamSubmissionsForExam(exam?.id));
  const questionSubmissions = useSelector(
    selectQuestionSubmissionsForExamSubmission(examSubmissions[0]?.id)
  );
  const questions = useSelector(selectQuestionsForExam(props.examId));
  const [questionModalShow, setQuestionModalShow] = useState<string | null>(null);
  const me = useSelector(selectMe);
  const options = useSelector(selectAllOptions);
  const multipleChoiceQuestionIds = questions
    .filter(q => q.type === "MULTIPLE_CHOICE")
    .map(q => q.id);

  const joinedMultipleChoiceQuestionIds = multipleChoiceQuestionIds.join("");
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  useEffect(() => {
    dispatch(fetchQuestions(props.examId))
      .then(unwrapResult)
      .catch(e => {
        console.error(e);
      });
  }, [props.examId, dispatch]);

  useEffect(() => {
    multipleChoiceQuestionIds.forEach(qid => {
      dispatch(fetchOptions(qid))
        .then(unwrapResult)
        .catch(e => {
          console.error(e);
        });
    });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [dispatch, joinedMultipleChoiceQuestionIds]);

  useEffect(() => {
    dispatch(fetchExamSubmissions(props.examId))
      .then(unwrapResult)
      .catch(e => {
        console.error(e);
      });
  }, [dispatch, props.examId]);

  if (me === undefined || (exam?.questionIds !== undefined && exam.questionIds.length !== questions.length)) {
    return <Loader />;
  }

  const answers: Record<string, string> = {};

  if (
    me?.type === "STUDENT"
  ) {
    questionSubmissions.forEach(q => {
      answers[q.questionId] = q.answer ?? "";
    });
  }

  const handleDelete = (id: string): Promise<unknown> => {
    return dispatch(deleteQuestion({
      questionId: id,
    }))
      .then(unwrapResult)
      .catch((e: Error) => {
        setErrorMessage("Failed to delete question");
        console.error(e);
      });
  };

  const handleSubmit = (values: FormValues): Promise<unknown> => {
    return dispatch(createExamSubmission({
      examId: props.examId,
      studentId: me.id,
      answers: values.answers,
    }))
      .then(unwrapResult)
      .catch((e: Error) => {
        setErrorMessage("Failed to submit exam");
        console.error(e);
      });
  };

  if (errorMessage !== null) {
    return (
      <Alert variant="danger">
        {errorMessage}
      </Alert>
    );
  }

  if (me?.type === "INSTRUCTOR" && exam !== undefined) {
    return (
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
                  {
                    new Date(exam.startTime) > new Date() &&
                      <>
                        <Button className="mr-2" onClick={() => setQuestionModalShow(question.id)}>
                          Edit
                        </Button>
                        <Button className="mr-2" onClick={() => handleDelete(question.id)}>
                          Delete
                        </Button>
                      </>
                  }
                  <QuestionModal
                    show={questionModalShow === question.id}
                    onHide={() => setQuestionModalShow(null)}
                    id={question.id}
                    question={question.question}
                    marks={question.marks}
                    type={question.type}
                    optionIds={question.optionIds}
                    eTag={question.meta.eTag} />
                </Card.Body>
              </Card>
            );
          })
        }
      </div>
    );
  } else if (me?.type === "STUDENT" && exam !== undefined) {
    const disabled = examSubmissions.length > 0 || new Date(exam.finishTime) < new Date();
    return (
      <Formik
        initialValues={{
          answers: questions.map(question => ({
            questionId: question.id,
            answer: answers[question.id] ?? "",
          })),
        }}
        onSubmit={handleSubmit}>
        {
          ({
            handleSubmit,
            isSubmitting,
            setFieldValue,
            handleBlur,
            values,
          }) => (
            <Form id="answerExam" onSubmit={handleSubmit as any}>
              <fieldset disabled={disabled}>
                {
                  questions.map((question, i) => {
                    if (question.type === "MULTIPLE_CHOICE") {
                      return (
                        <Form.Group key={question.id}>
                          <Form.Label>{question.question}</Form.Label>
                          <Select
                            options={options.filter(option => {
                              return option.questionId === question.id;
                            }).map(option => ({
                              value: option.answer,
                              label: option.answer,
                            }))}
                            name={`answers[${i}].answer`}
                            value={{
                              value: values.answers[i]?.answer,
                              label: values.answers[i]?.answer,
                            } as any}
                            onChange={option => setFieldValue(
                              `answers[${i}].answer`,
                              option.value,
                            )}
                            isDisabled={isSubmitting || disabled}
                            onBlur={handleBlur} />
                        </Form.Group>
                      );
                    } else if (question.type === "SHORT_ANSWER") {
                      return (
                        <FormikControl
                          key={question.id}
                          name={`answers[${i}].answer`}
                          label={question.question}
                          as="textarea" />
                      );
                    }

                    return <></>;
                  })
                }
              </fieldset>
              <Button type="submit" disabled={isSubmitting || disabled}>
                Submit Exam
              </Button>
            </Form>
          )
        }
      </Formik>
    );
  }

  return <></>;
};

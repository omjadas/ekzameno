import { unwrapResult } from "@reduxjs/toolkit";
import { Formik } from "formik";
import { FormikControl } from "formik-react-bootstrap";
import React, { useEffect, useState } from "react";
import { Button, Card, Form } from "react-bootstrap";
import { useSelector } from "react-redux";
import Select from "react-select";
import { Answer, ExamState, fetchSubmissions, selectExamById, submitExam } from "../../redux/slices/examsSlice";
import { fetchOptions, selectAllOptions } from "../../redux/slices/optionsSlice";
import { deleteQuestion, fetchQuestions, questionLabels, selectQuestionsForExam } from "../../redux/slices/questionsSlice";
import { selectMe } from "../../redux/slices/usersSlice";
import { RootState, useAppDispatch } from "../../redux/store";
import { Loader } from "../loader/loader";
import { QuestionModal } from "./questionModal";
import styles from "./questions.module.scss";

interface QuestionProps {
  examId: string,
}

interface FormValues {
  answers: Answer[],
}

export const Questions = (props: QuestionProps): JSX.Element => {
  const dispatch = useAppDispatch();
  const exam = useSelector<RootState, ExamState | undefined>(
    state => selectExamById(state, props.examId)
  );
  const questions = useSelector(selectQuestionsForExam(props.examId));
  const [questionModalShow, setQuestionModalShow] = useState<string | null>(null);
  const me = useSelector(selectMe);
  const options = useSelector(selectAllOptions);
  const multipleChoiceQuestionIds = questions
    .filter(q => q.type === "MULTIPLE_CHOICE")
    .map(q => q.id);

  const joinedMultipleChoiceQuestionIds = multipleChoiceQuestionIds.join("");

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
    dispatch(fetchSubmissions(props.examId))
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
    me?.type === "STUDENT" &&
    exam?.submissions !== undefined &&
    exam.submissions.length > 0
  ) {
    exam.submissions[0].questionSubmissions.forEach(q => {
      answers[q.questionId] = q.answer;
    });
  }

  const onClick = (id: string): void => {
    dispatch(deleteQuestion({
      questionId: id,
    }))
      .then(unwrapResult)
      .catch(e => {
        console.error(e);
      });
  };

  const onSubmit = (values: FormValues): void => {
    dispatch(submitExam({
      examId: props.examId,
      studentId: me.id,
      answers: values.answers,
    }))
      .then(unwrapResult)
      .catch(e => {
        console.error(e);
      });
  };

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
                        <Button className="mr-2" onClick={() => onClick(question.id)}>
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
                    optionIds={question.optionIds} />
                </Card.Body>
              </Card>
            );
          })
        }
      </div>
    );
  } else if (me?.type === "STUDENT" && exam?.submissions !== undefined) {
    const disabled = exam.submissions.length > 0 || new Date(exam.finishTime) < new Date();
    return (
      <Formik
        initialValues={{
          answers: questions.map(question => ({
            questionId: question.id,
            answer: answers[question.id] ?? "",
          })),
        }}
        onSubmit={onSubmit}>
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

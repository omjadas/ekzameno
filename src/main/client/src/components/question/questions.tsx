import React, { useEffect } from "react";
import { Button, Card } from "react-bootstrap";
import { useSelector } from "react-redux";
import { Link } from "react-router-dom";
import { ExamState, selectExamById } from "../../redux/slices/examsSlice";
import { fetchQuestions, selectAllQuestions, selectQuestionById, selectQuestionsByIds } from "../../redux/slices/questionsSlice";
import { RootState, useAppDispatch } from "../../redux/store";
import styles from "./questions.module.scss";

interface QuestionProps {
  examId: string,
}

export const Questions = (props: QuestionProps): JSX.Element => {
  const dispatch = useAppDispatch();
  // const exam = useSelector<RootState, ExamState | undefined>(
  //   state => selectExamById(state, props.examId)
  // );
  //const questions = useSelector(selectQuestionsByIds(exam?.questionIds ?? []));
  const questions = useSelector(selectAllQuestions);

  useEffect(() => {
    dispatch(fetchQuestions(props.examId));
  }, [props.examId, dispatch]);

  return (
    <div className={styles.wrapper}>
      {
        questions.map(question => {
          return (
            <Card key={question.id}>
              <Card.Header>{question.question}</Card.Header>
              <Card.Body>
                <Card.Title>Marks: {question.marks}</Card.Title>
                <Card.Text>
                  Question Type: {question.type}
                </Card.Text>
                <Button className="mr-2" variant="primary">Edit</Button>
                <Button className="mr-2" variant="primary">Delete</Button>
              </Card.Body>
            </Card>
          );
        })
      }
    </div>
  );
};

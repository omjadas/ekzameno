import React, { useEffect, useState } from "react";
import { Button, Card } from "react-bootstrap";
import { useSelector } from "react-redux";
import { Link } from "react-router-dom";
import { ExamState, selectExamById } from "../../redux/slices/examsSlice";
import { fetchQuestions, selectAllQuestions, deleteQuestion } from "../../redux/slices/questionsSlice";
import { selectMe } from "../../redux/slices/usersSlice";
import { RootState, useAppDispatch } from "../../redux/store";
import { QuestionModal } from "./questionModal";
import styles from "./questions.module.scss";

interface QuestionProps {
  examId: string,
}

export const Questions = (props: QuestionProps): JSX.Element => {
  const dispatch = useAppDispatch();
  // const exam = useSelector<RootState, ExamState | undefined>(
  //   state => selectExamById(state, props.examId)
  // );
  // const questions = useSelector(selectQuestionsByIds(exam?.questionIds ?? []));
  const questions = useSelector(selectAllQuestions);
  const [questionModalShow, setQuestionModalShow] = useState(false);
  const me = useSelector(selectMe);

  useEffect(() => {
    dispatch(fetchQuestions(props.examId));
  }, [props.examId, dispatch]);

  // const onClick = (): void => {
  //   dispatch(deleteQuestion({
  //     questionId: question.id,
  //   }))
  //     .catch(e => {
  //       console.error(e);
  //     });
  // };

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
                {
                  me?.type === "INSTRUCTOR" &&
                  <>
                    <Button className="mr-2" onClick={() => setQuestionModalShow(true)}>
                      Edit
                    </Button>
                    <Button className="mr-2">
                      Delete
                    </Button>
                  </>
                }
                <QuestionModal
                  show={questionModalShow}
                  onHide={() => setQuestionModalShow(false)}
                  id={question.id}
                  question={question.question}
                  marks={question.marks}
                  type={question.type}
                  //Not a correct type
                  options={question.options}
                  //pasing value to be checked
                  correct={2}/>
              </Card.Body>
            </Card>
          );
        })
      }
    </div>
  );
};

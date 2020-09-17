import React, { useState } from "react";
import { Button, Container, Jumbotron } from "react-bootstrap";
import { useSelector } from "react-redux";
import { useHistory, useParams } from "react-router-dom";
import { deleteExam, selectExamBySlug, updateExam } from "../../redux/slices/examsSlice";
import { useAppDispatch } from "../../redux/store";
import { ExamModal } from "../exam/examModal";
import styles from "../subject/subject.module.scss";

export const Exam = (): JSX.Element => {
  const { slug } = useParams<{ slug: string }>();
  const dispatch = useAppDispatch();
  const exam = useSelector(selectExamBySlug(slug));
  const [examModalShow, setExamModalShow] = useState(false);
  const history = useHistory();

  if (exam === undefined) {
    return (
      <div>Unable to find exam</div>
    );
  }

  const onClick = (): void => {
    dispatch(deleteExam({
      examId: exam.id,
    }))
      .then(() => {
        history.goBack();
      })
      .catch(e => {
        console.error(e);
      });
  };

  const currentTime = new Date();
  const startTime = new Date(exam.startTime);
  const finishTime = new Date(exam.finishTime);

  const publishNow = (): void => {
    dispatch(updateExam({
      id: exam.id,
      exam: {
        name: exam.name,
        description: exam.description,
        startTime: new Date(currenTime).toISOString(),
        finishTime: exam.finishTime,
      },
    }))
      .catch(e => {
        console.error(e);
      });
  };

  const closeNow = (): void => {
    dispatch(updateExam({
      id: exam.id,
      exam: {
        name: exam.name,
        description: exam.description,
        startTime: exam.startTime,
        finishTime: new Date(currenTime).toISOString(),
      },
    }))
      .catch(e => {
        console.error(e);
      });
  };

  return (
    <Container className={styles.margin}>
      <Jumbotron>
        <h1>{exam.name}</h1>
        <p>{exam.description}</p>
        <Button onClick={() => setExamModalShow(true)}>
          Edit Exam
        </Button>{" "}
        <Button onClick={onClick}>
          Delete Exam
        </Button>{" "}
        {
          startTime > currentTime &&
            <Button onClick={publishNow}>
              Publish Exam
            </Button>
        }
        {" "}
        {
          finishTime > currentTime &&
          startTime < currentTime &&
            <Button onClick={closeNow}>
              Close Exam
            </Button>
        }
        {" "}
      </Jumbotron>
      <ExamModal
        show={examModalShow}
        onHide={() => setExamModalShow(false)}
        id={exam.id}
        name={exam.name}
        description={exam.description}
        startTime={exam.startTime}
        finishTime={exam.finishTime}/>
    </Container>
  );
};

import React, { useEffect } from "react";
import { useSelector } from "react-redux";
import { useParams } from "react-router-dom";
import { Button, Container, Jumbotron } from "react-bootstrap";
import { fetchExams, selectExamsStatus, selectExamBySlug } from "../../redux/slices/examsSlice";
import { useAppDispatch } from "../../redux/store";
import styles from "../subject/subject.module.scss";

export const Exam = (): JSX.Element => {
  const { slug } = useParams<{ slug: string }>();
  const dispatch = useAppDispatch();
  const examsStatus = useSelector(selectExamsStatus);
  const exam = useSelector(selectExamBySlug(slug));

  useEffect(() => {
    if (examsStatus === "idle") {
      dispatch(fetchExams(slug));
    }
  }, [slug, dispatch, examsStatus]);

  if (exam === undefined) {
    return (
      <div>No exams created</div>
    );
  }
  return (
    <Container className={styles.margin}>
      <Jumbotron>
        <h1>{exam.name}</h1>
        <p>{exam.description}</p>
        <Button>
          Edit Exam
        </Button>
        <Button>
          Delete Exam
        </Button>
      </Jumbotron>
    </Container>
  );
};

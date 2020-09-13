import React, { useEffect, useState } from "react";
import { Button, Container, Jumbotron } from "react-bootstrap";
import { useSelector } from "react-redux";
import { useParams } from "react-router-dom";
import { fetchSubjects, selectSubjectBySlug, selectSubjectsStatus } from "../../redux/slices/subjectsSlice";
import { useAppDispatch } from "../../redux/store";
import { ExamModal } from "../exam/examModal";
import styles from "./subject.module.scss";

export const Subject = (): JSX.Element => {
  const { slug } = useParams<{slug: string}>();
  const dispatch = useAppDispatch();
  const subjectsStatus = useSelector(selectSubjectsStatus);
  const subject = useSelector(selectSubjectBySlug(slug));
  const [examModalShow, setExamModalShow] = useState(false);

  useEffect(() => {
    if (subjectsStatus === "idle") {
      dispatch(fetchSubjects());
    }
  }, [dispatch, subjectsStatus]);

  if (subject === undefined) {
    return (
      <div>Unable to find subject</div>
    );
  }

  return (
    <Container className={styles.margin}>
      <Jumbotron>
        <h1>{subject.name}</h1>
        <p>{subject.description}</p>
        <Button onClick={() => setExamModalShow(true)}>
          Create Exam
        </Button>
      </Jumbotron>
      <ExamModal show={examModalShow} onHide={() => setExamModalShow(false)} subjectId={subject.id} />
    </Container>
  );
};

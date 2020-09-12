import React, { useEffect, useState } from "react";
import { Button, Container, Jumbotron } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router-dom";
import { fetchSubjects, selectSubjectBySlug, selectSubjectsStatus } from "../../redux/slices/subjectsSlice";
import { ExamModal } from "../exam/examModal";

export const Subject = (): JSX.Element => {
  const { slug } = useParams<{slug: string}>();
  const dispatch = useDispatch();
  const subjectsStatus = useSelector(selectSubjectsStatus);
  const subject = useSelector(selectSubjectBySlug(slug));
  const [examModalShow, setExamModalShow] = useState(false);

  useEffect(() => {
    if (subjectsStatus === "idle") {
      dispatch(fetchSubjects());
    }
  }, [dispatch, subjectsStatus]);

  return (
    <Container>
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

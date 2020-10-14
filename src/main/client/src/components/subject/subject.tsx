import { unwrapResult } from "@reduxjs/toolkit";
import React, { useEffect, useState } from "react";
import { Button, Container, Jumbotron, Spinner } from "react-bootstrap";
import { useSelector } from "react-redux";
import { useParams } from "react-router-dom";
import { fetchSubject, selectSubjectBySlug, selectSubjectsStatus } from "../../redux/slices/subjectsSlice";
import { fetchInstructorsForSubject, fetchStudentsForSubject, selectMe } from "../../redux/slices/usersSlice";
import { useAppDispatch } from "../../redux/store";
import { ExamModal } from "../exam/examModal";
import { Exams } from "../exam/exams";
import { SubjectModal } from "../subject/subjectModal";
import styles from "./subject.module.scss";

export const Subject = (): JSX.Element => {
  const { slug } = useParams<{slug: string}>();
  const dispatch = useAppDispatch();
  const subjectsStatus = useSelector(selectSubjectsStatus);
  const subject = useSelector(selectSubjectBySlug(slug));
  const [examModalShow, setExamModalShow] = useState(false);
  const [subjectModalShow, setSubjectModalShow] = useState(false);
  const me = useSelector(selectMe);
  const subjectId = subject?.id;

  useEffect(() => {
    dispatch(fetchSubject(slug))
      .then(unwrapResult)
      .catch(e => {
        console.error(e);
      });
  }, [dispatch, slug]);

  useEffect(() => {
    if (subjectId !== undefined) {
      dispatch(fetchInstructorsForSubject(subjectId))
        .then(unwrapResult)
        .catch(e => {
          console.error(e);
        });
      dispatch(fetchStudentsForSubject(subjectId))
        .then(unwrapResult)
        .catch(e => {
          console.error(e);
        });
    }
  }, [dispatch, subjectId, subjectsStatus]);

  if (subject === undefined) {
    return (
      <div>Unable to find subject</div>
    );
  }

  if (subject === undefined) {
    return (
      <Spinner animation="border" role="status">
        <span className="sr-only">Loading...</span>
      </Spinner>
    );
  }

  return (
    <Container className={styles.margin}>
      <Jumbotron>
        <h1>{subject.name}</h1>
        <p>{subject.description}</p>
        {
          me?.type === "INSTRUCTOR" &&
            <>
              <Button onClick={() => setSubjectModalShow(true)} className="mr-2">
                Edit Subject
              </Button>
              <Button onClick={() => setExamModalShow(true)}>
                Create Exam
              </Button>
            </>
        }
      </Jumbotron>
      <SubjectModal
        show={subjectModalShow}
        onHide={() => setSubjectModalShow(false)}
        id={subject.id}
        name={subject.name}
        description={subject.description}
        instructors={subject.instructors}
        students={subject.students} />
      <Exams subjectId={subject.id} />
      <ExamModal show={examModalShow} onHide={() => setExamModalShow(false)} subjectId={subject.id} />
    </Container>
  );
};

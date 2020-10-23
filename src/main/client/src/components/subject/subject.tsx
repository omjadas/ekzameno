import { unwrapResult } from "@reduxjs/toolkit";
import React, { useEffect, useState } from "react";
import { Alert, Button, Container, Jumbotron } from "react-bootstrap";
import { useSelector } from "react-redux";
import { useParams } from "react-router-dom";
import { fetchSubject, selectSubjectBySlug } from "../../redux/slices/subjectsSlice";
import { fetchInstructorsForSubject, fetchStudentsForSubject, selectMe } from "../../redux/slices/usersSlice";
import { useAppDispatch } from "../../redux/store";
import { ExamModal } from "../exam/examModal";
import { Exams } from "../exam/exams";
import { Loader } from "../loader/loader";
import { SubjectModal } from "../subject/subjectModal";
import styles from "./subject.module.scss";

export const Subject = (): JSX.Element => {
  const { slug } = useParams<{slug: string}>();
  const dispatch = useAppDispatch();
  const subject = useSelector(selectSubjectBySlug(slug));
  const [examModalShow, setExamModalShow] = useState(false);
  const [subjectModalShow, setSubjectModalShow] = useState(false);
  const meType = useSelector(selectMe)?.type;
  const subjectId = subject?.id;
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  useEffect(() => {
    dispatch(fetchSubject(slug))
      .then(unwrapResult)
      .catch((e: Error) => {
        setErrorMessage("Failed to retrieve subject");
        console.error(e);
      });
  }, [dispatch, slug]);

  useEffect(() => {
    if (subjectId !== undefined && meType === "INSTRUCTOR") {
      dispatch(fetchInstructorsForSubject(subjectId))
        .then(unwrapResult)
        .catch((e: Error) => {
          setErrorMessage("Failed to retrieve instructors");
          console.error(e);
        });
      dispatch(fetchStudentsForSubject(subjectId))
        .then(unwrapResult)
        .catch((e: Error) => {
          setErrorMessage("Failed to retrieve students");
          console.error(e);
        });
    }
  }, [dispatch, subjectId, meType]);

  if (errorMessage !== null) {
    return (
      <Container className={styles.margin}>
        <Alert variant="danger">
          {errorMessage}
        </Alert>
      </Container>
    );
  }

  if (subject === undefined) {
    return <Loader />;
  }

  return (
    <Container className={styles.margin}>
      <Jumbotron>
        <h1>{subject.name}</h1>
        <p>{subject.description}</p>
        {
          meType === "INSTRUCTOR" &&
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
        students={subject.students}
        eTag={subject.meta.eTag} />
      <Exams subjectId={subject.id} />
      <ExamModal show={examModalShow} onHide={() => setExamModalShow(false)} subjectId={subject.id} />
    </Container>
  );
};

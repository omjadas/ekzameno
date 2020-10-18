import { unwrapResult } from "@reduxjs/toolkit";
import React, { useEffect, useState } from "react";
import { Alert, Button, Container, Jumbotron } from "react-bootstrap";
import { useSelector } from "react-redux";
import { useParams } from "react-router-dom";
import { fetchSubject, selectSubjectBySlug, selectSubjectsStatus } from "../../redux/slices/subjectsSlice";
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
  const subjectsStatus = useSelector(selectSubjectsStatus);
  const subject = useSelector(selectSubjectBySlug(slug));
  const [examModalShow, setExamModalShow] = useState(false);
  const [subjectModalShow, setSubjectModalShow] = useState(false);
  const me = useSelector(selectMe);
  const subjectId = subject?.id;
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  useEffect(() => {
    dispatch(fetchSubject(slug))
      .then(unwrapResult)
      .catch((e: Error) => {
        if (e.message === "400") {
          setErrorMessage("Bad Request");
        } else if (e.message === "401") {
          setErrorMessage("Unauthorized Request");
        } else if (e.message === "404") {
          setErrorMessage("Failed to fetch Subject");
        } else if (e.message === "412") {
          setErrorMessage("Client Error");
        } else if (e.message === "500") {
          setErrorMessage("Internal Server Error");
        }
        console.error(e);
      });
  }, [dispatch, slug]);

  useEffect(() => {
    if (subjectId !== undefined) {
      dispatch(fetchInstructorsForSubject(subjectId))
        .then(unwrapResult)
        .catch((e: Error) => {
          if (e.message === "400") {
            setErrorMessage("Bad Request");
          } else if (e.message === "401") {
            setErrorMessage("Unauthorized Request");
          } else if (e.message === "404") {
            setErrorMessage("Failed to fetch Instructors");
          } else if (e.message === "412") {
            setErrorMessage("Client Error");
          } else if (e.message === "500") {
            setErrorMessage("Internal Server Error");
          }
          console.error(e);
        });
      dispatch(fetchStudentsForSubject(subjectId))
        .then(unwrapResult)
        .catch((e: Error) => {
          if (e.message === "400") {
            setErrorMessage("Bad Request");
          } else if (e.message === "401") {
            setErrorMessage("Unauthorized Request");
          } else if (e.message === "404") {
            setErrorMessage("Failed to fetch Students");
          } else if (e.message === "412") {
            setErrorMessage("Client Error");
          } else if (e.message === "500") {
            setErrorMessage("Internal Server Error");
          }
          console.error(e);
        });
    }
  }, [dispatch, subjectId, subjectsStatus]);

  if (subject === undefined) {
    return <Loader />;
  }

  if (errorMessage  != null) {
    return (
      <Alert variant="danger">
        {errorMessage}
      </Alert>
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
        students={subject.students}
        eTag={subject.meta.eTag} />
      <Exams subjectId={subject.id} />
      <ExamModal show={examModalShow} onHide={() => setExamModalShow(false)} subjectId={subject.id} />
    </Container>
  );
};

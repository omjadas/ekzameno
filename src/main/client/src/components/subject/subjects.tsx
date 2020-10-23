import { unwrapResult } from "@reduxjs/toolkit";
import React, { useEffect, useState } from "react";
import { Alert, Card, Container } from "react-bootstrap";
import { useSelector } from "react-redux";
import { Link } from "react-router-dom";
import { fetchSubjects, selectAllSubjects, selectSubjectsStatus } from "../../redux/slices/subjectsSlice";
import { useAppDispatch } from "../../redux/store";
import { Loader } from "../loader/loader";
import styles from "./subjects.module.scss";

export const Subjects = (): JSX.Element => {
  const dispatch = useAppDispatch();
  const subjectsStatus = useSelector(selectSubjectsStatus);
  const subjects = useSelector(selectAllSubjects);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  useEffect(() => {
    dispatch(fetchSubjects())
      .then(unwrapResult)
      .catch((e: Error) => {
        setErrorMessage("Failed to retrieve subjects");
        console.error(e);
      });
  }, [dispatch]);

  if (subjectsStatus !== "finished") {
    return <Loader />;
  }

  if (errorMessage !== null) {
    return (
      <Container className={styles.wrapper}>
        <Alert variant="danger">
          {errorMessage}
        </Alert>
      </Container>
    );
  }

  return (
    <Container className={styles.wrapper}>
      {
        subjects.map(subject => {
          return (
            <Card key={subject.id}>
              <Card.Body>
                <Card.Title>
                  <Link to={`/subjects/${subject.slug}`}>
                    {subject.name}
                  </Link>
                </Card.Title>
                <Card.Text>{subject.description}</Card.Text>
              </Card.Body>
            </Card>
          );
        })
      }
    </Container>
  );
};

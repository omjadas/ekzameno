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
    if (subjectsStatus === "idle") {
      dispatch(fetchSubjects())
        .then(unwrapResult)
        .catch((e: Error) => {
          if (e.message === "400") {
            setErrorMessage("Bad Request");
          } else if (e.message === "401") {
            setErrorMessage("Unauthorized Request");
          } else if (e.message === "404") {
            setErrorMessage("Failed to fetch Subjects");
          } else if (e.message === "412") {
            setErrorMessage("Client Error");
          } else if (e.message === "500") {
            setErrorMessage("Internal Server Error");
          }
          console.error(e);
        });
    }
  }, [dispatch, subjectsStatus]);

  if (subjectsStatus !== "finished") {
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

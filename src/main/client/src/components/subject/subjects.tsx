import { unwrapResult } from "@reduxjs/toolkit";
import React, { useEffect } from "react";
import { Card, Container, Spinner } from "react-bootstrap";
import { useSelector } from "react-redux";
import { Link } from "react-router-dom";
import { fetchSubjects, selectAllSubjects, selectSubjectsStatus } from "../../redux/slices/subjectsSlice";
import { useAppDispatch } from "../../redux/store";
import styles from "./subjects.module.scss";

export const Subjects = (): JSX.Element => {
  const dispatch = useAppDispatch();
  const subjectsStatus = useSelector(selectSubjectsStatus);
  const subjects = useSelector(selectAllSubjects);

  useEffect(() => {
    if (subjectsStatus === "idle") {
      dispatch(fetchSubjects())
        .then(unwrapResult)
        .catch(e => {
          console.error(e);
        });
    }
  }, [dispatch, subjectsStatus]);

  if (subjectsStatus === "loading") {
    return (
      <Spinner animation="border" role="status">
        <span className="sr-only">Loading...</span>
      </Spinner>
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

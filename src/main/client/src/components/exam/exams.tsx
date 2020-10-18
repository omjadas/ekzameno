import { unwrapResult } from "@reduxjs/toolkit";
import React, { useEffect, useState } from "react";
import { Alert, Card } from "react-bootstrap";
import { useSelector } from "react-redux";
import { Link } from "react-router-dom";
import { fetchExams, selectExamsForSubject } from "../../redux/slices/examsSlice";
import { useAppDispatch } from "../../redux/store";
import styles from "./exams.module.scss";

interface ExamsProps {
  subjectId: string,
}

export const Exams = (props: ExamsProps): JSX.Element => {
  const dispatch = useAppDispatch();
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const exams = useSelector(selectExamsForSubject(props.subjectId));

  useEffect(() => {
    dispatch(fetchExams(props.subjectId))
      .then(unwrapResult)
      .catch(e => {
        if (e.message === "400") {
          setErrorMessage("Bad Request");
        } else if (e.message === "401") {
          setErrorMessage("Unauthorized Request");
        } else if (e.message === "404") {
          setErrorMessage("Failed to fetch Exams");
        } else if (e.message === "412") {
          setErrorMessage("Client Error");
        } else if (e.message === "500") {
          setErrorMessage("Internal Server Error");
        }
        console.error(e);
      });
  }, [props.subjectId, dispatch]);

  if (errorMessage  != null) {
    return (
      <Alert variant="danger">
        {errorMessage}
      </Alert>
    );
  }

  return (
    <div className={styles.wrapper}>
      {
        exams.map(exam => {
          return (
            <Card key={exam.id}>
              <Card.Body>
                <Card.Title>
                  <Link to={`/exams/${exam.slug}`}>
                    {exam.name}
                  </Link>
                </Card.Title>
                <Card.Text>{exam.description}</Card.Text>
              </Card.Body>
            </Card>
          );
        })
      }
    </div>
  );
};

import { unwrapResult } from "@reduxjs/toolkit";
import React, { useEffect, useState } from "react";
import { Alert, Card } from "react-bootstrap";
import { useSelector } from "react-redux";
import { Link } from "react-router-dom";
import { fetchExams, selectExamsForSubject } from "../../redux/slices/examsSlice";
import { selectMe } from "../../redux/slices/usersSlice";
import { useAppDispatch } from "../../redux/store";
import { Loader } from "../loader/loader";
import styles from "./exams.module.scss";

interface ExamsProps {
  subjectId: string,
}

export const Exams = (props: ExamsProps): JSX.Element => {
  const dispatch = useAppDispatch();
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [loadingExams, setLoadingExams] = useState(true);
  const exams = useSelector(selectExamsForSubject(props.subjectId));
  const me = useSelector(selectMe);

  useEffect(() => {
    dispatch(fetchExams(props.subjectId))
      .then(unwrapResult)
      .then(() => {
        setLoadingExams(false);
      })
      .catch(e => {
        setErrorMessage("Failed to retrieve exams");
        console.error(e);
      });
  }, [props.subjectId, dispatch]);

  if (errorMessage !== null) {
    return (
      <Alert variant="danger">
        {errorMessage}
      </Alert>
    );
  }

  if (me === undefined || loadingExams) {
    return <Loader />;
  }

  return (
    <div className={styles.wrapper}>
      {
        exams.map(exam => {
          return (
            <Card key={exam.id}>
              <Card.Body>
                <Card.Title>
                  {
                    me.type === "ADMINISTRATOR"
                      ? exam.name
                      : (
                        <Link to={`/exams/${exam.slug}`}>
                          {exam.name}
                        </Link>
                      )
                  }
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

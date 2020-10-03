import { unwrapResult } from "@reduxjs/toolkit";
import React, { useEffect } from "react";
import { Card } from "react-bootstrap";
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
  const exams = useSelector(selectExamsForSubject(props.subjectId));

  useEffect(() => {
    dispatch(fetchExams(props.subjectId))
      .then(unwrapResult)
      .catch(e => {
        console.error(e);
      });
  }, [props.subjectId, dispatch]);

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

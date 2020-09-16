import React, { useEffect } from "react";
import { useSelector } from "react-redux";
import { Card } from "react-bootstrap";
import { Link } from "react-router-dom";
import { fetchExams, selectAllExams } from "../../redux/slices/examsSlice";
import { useAppDispatch } from "../../redux/store";
import styles from "./exams.module.scss";

interface ExamsProps {
  subjectId: string,
}

export const Exams = (props: ExamsProps): JSX.Element => {
  const dispatch = useAppDispatch();
  const exams = useSelector(selectAllExams);

  useEffect(() => {
    dispatch(fetchExams(props.subjectId));
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

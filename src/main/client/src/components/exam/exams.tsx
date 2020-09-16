import React, { useEffect } from "react";
import { useSelector } from "react-redux";
import { Container, Card } from "react-bootstrap";
import { useParams } from "react-router-dom";
import { Link } from "react-router-dom";
import { fetchExams, selectExamsStatus, selectAllExams } from "../../redux/slices/examsSlice";
import { useAppDispatch } from "../../redux/store";
import styles from "./exams.module.scss";

export const Exams = (): JSX.Element => {
  const { slug } = useParams<{ slug: string }>();
  const dispatch = useAppDispatch();
  const examsStatus = useSelector(selectExamsStatus);
  const exams = useSelector(selectAllExams);

  useEffect(() => {
    if (examsStatus === "idle") {
      dispatch(fetchExams(slug));
    }
  }, [slug, dispatch, examsStatus]);

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

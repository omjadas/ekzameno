import React, { useEffect } from "react";
import { Card } from "react-bootstrap";
import { useSelector } from "react-redux";
import { Link } from "react-router-dom";
import { fetchExams, selectExamsByIds } from "../../redux/slices/examsSlice";
import { selectSubjectById, SubjectState } from "../../redux/slices/subjectsSlice";
import { RootState, useAppDispatch } from "../../redux/store";
import styles from "./exams.module.scss";

interface ExamsProps {
  subjectId: string,
}

export const Exams = (props: ExamsProps): JSX.Element => {
  const dispatch = useAppDispatch();
  const subject = useSelector<RootState, SubjectState | undefined>(
    state => selectSubjectById(state, props.subjectId)
  );
  const exams = useSelector(selectExamsByIds(subject?.examIds ?? []));

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

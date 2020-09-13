import React, { useEffect } from "react";
import { Card, Container } from "react-bootstrap";
import { useSelector } from "react-redux";
import { Link } from "react-router-dom";
import { fetchSubjects, selectAllSubjects, selectSubjectsStatus } from "../../redux/slices/subjectsSlice";
import { useAppDispatch } from "../../redux/store";

export const Subjects = (): JSX.Element => {
  const dispatch = useAppDispatch();
  const subjectsStatus = useSelector(selectSubjectsStatus);
  const subjects = useSelector(selectAllSubjects);

  useEffect(() => {
    if (subjectsStatus === "idle") {
      dispatch(fetchSubjects());
    }
  }, [dispatch, subjectsStatus]);

  return (
    <Container>
      {
        subjects.map(subject => {
          return (
            <Card key={subject.id}>
              <Card.Title>
                <Link to={`/subjects/${subject.slug}`}>
                  {subject.name}
                </Link>
              </Card.Title>
              <Card.Body>{subject.description}</Card.Body>
            </Card>
          );
        })
      }
    </Container>
  );
};

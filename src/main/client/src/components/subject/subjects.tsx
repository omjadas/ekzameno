import React, { useEffect } from "react";
import { Container, Card, CardColumns } from "react-bootstrap";
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
    <Container style={{ marginTop: "4rem" }}>
      <CardColumns>
        {
          subjects.map(subject => {
            return (
              <Card key={subject.id} style={{ width: "18rem", height: "16rem", margin: "4rem" }}>
                <Card.Title style={{ textAlign: "center" }}>
                  <Link to={`/subjects/${subject.slug}`}>
                    {subject.name}
                  </Link>
                </Card.Title>
                <Card.Body>{subject.description}</Card.Body>
              </Card>
            );
          })
        }
      </CardColumns>
    </Container>
  );
};

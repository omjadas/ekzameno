import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Card, Container } from "react-bootstrap";
import { fetchSubjects, selectSubjectsStatus } from "../../redux/slices/subjectsSlice";

export const Subjects = (): JSX.Element => {
  const dispatch = useDispatch();
  const subjectsStatus = useSelector(selectSubjectsStatus);

  useEffect(() => {
    if (subjectsStatus === "idle") {
      dispatch(fetchSubjects());
    }
  }, [dispatch, subjectsStatus]);

  return (
    <Container>
      <Card border="info" style={{ width: "18rem", margin: "6rem" }}>
        <Card.Title style={{ textAlign: "center" }}>New Subject</Card.Title>
        <Card.Text>
          Write something here.
        </Card.Text>
      </Card>
    </Container>
  );
};

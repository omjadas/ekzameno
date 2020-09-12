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
    </Container>
  );
};

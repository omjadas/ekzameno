import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Card, Container } from "react-bootstrap";
import { fetchSubjects, selectSubjectsStatus } from "../../redux/slices/subjectsSlice";
import { CreateSubjectModal } from "./subjectModal";

export const Subjects = (): JSX.Element => {
  const dispatch = useDispatch();
  const subjectsStatus = useSelector(selectSubjectsStatus);

  useEffect(() => {
    if (subjectsStatus === "idle") {
      dispatch(fetchSubjects());
    }
  }, [dispatch, subjectsStatus]);

  const [createSubjectshow, setcreateSubjectShow] = useState(false);

  return (
    <Container>
      <Card onClick={() => setcreateSubjectShow(true)} border="info" style={{ width: '18rem', margin: '6rem' }}>
        <Card.Img src="plusIcon.png"/>
        <Card.ImgOverlay>
          <Card.Title style={{ textAlign: 'center' }}>New Subject</Card.Title>
          <Card.Text>
            Write something here.
          </Card.Text>
        </Card.ImgOverlay>
      </Card>
      <CreateSubjectModal show={createSubjectshow} onHide={() => setcreateSubjectShow(false)} />
    </Container>
  );
};

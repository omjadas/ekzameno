import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router-dom";
import { fetchExams, selectExamsStatus } from "../../redux/slices/examsSlice";
import { Button, Form } from "react-bootstrap";
import { ExamModal } from "../exam/examModal";

export const Exam = (): JSX.Element => {
  const { slug } = useParams<{ slug: string }>();
  const dispatch = useDispatch();
  const examsStatus = useSelector(selectExamsStatus);

  useEffect(() => {
    if (examsStatus === "idle") {
      dispatch(fetchExams(slug));
    }
  }, [slug, dispatch, examsStatus]);

  const [createExamShow, setCreateExamShow] = useState(false);

  return (
    <Form className="ml-auto" inline>
      <Button
        variant="outline-info"
        onClick={() => setCreateExamShow(true)}>
        Create Exam
      </Button>
      <ExamModal show={createExamShow} onHide={() => setCreateExamShow(false)} />
    </Form>
  );
};

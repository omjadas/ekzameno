import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router-dom";
import { fetchExams, selectExamsStatus } from "../../redux/slices/examsSlice";
import { Button, Container, Form } from "react-bootstrap";
import { CreateExamModel } from "../exam/createExam";
import { PublishExamModel } from "./publishExam";
import { ViewPublishedExamModal } from "./viewPublishedExams";
import { ViewAllExamModal } from "./viewAllExams";

export const Exam = (): JSX.Element => {
  const { slug } = useParams<{ slug: string }>();
  const dispatch = useDispatch();
  const examsStatus = useSelector(selectExamsStatus);

  useEffect(() => {
    if (examsStatus === "idle") {
      dispatch(fetchExams(slug));
    }
  }, [slug, dispatch, examsStatus]);

  const [createExamshow, setcreateExamShow] = useState(false);
  const [publishExamshow, setpublishExamShow] = useState(false);
  const [viewPublishedExamshow, setviewPublishedExamShow] = useState(false);
  const [viewAllExamshow, setviewAllExamShow] = useState(false);

  return (

        <Form className="ml-auto" inline>
          <Button
            variant="outline-info"
            onClick={() => setcreateExamShow(true)}>
            Create Exam
          </Button>
          <Button
            variant="outline-info"
            onClick={() => setpublishExamShow(true)}>
            Publish Exam
          </Button>
          <Button
            variant="outline-info"
            onClick={() => setviewPublishedExamShow(true)}>
            View Published
          </Button>
          <Button
            variant="outline-info"
            onClick={() => setviewAllExamShow(true)}>
            View All The Exams
          </Button>
          <CreateExamModel show={createExamshow} onHide={() => setcreateExamShow(false)} />
          <PublishExamModel show={publishExamshow} onHide={() => setpublishExamShow(false)} />
          <ViewPublishedExamModal show={viewPublishedExamshow} onHide={() => setviewPublishedExamShow(false)} />
          <ViewAllExamModal show={viewAllExamshow} onHide={() => setviewAllExamShow(false)} />
        </Form>
    );
};
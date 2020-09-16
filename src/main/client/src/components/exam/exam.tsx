import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useParams } from "react-router-dom";
import { Button, Container, Jumbotron } from "react-bootstrap";
import { fetchExams, selectExamsStatus, selectExamBySlug } from "../../redux/slices/examsSlice";
import { useAppDispatch } from "../../redux/store";
import styles from "../subject/subject.module.scss";
import { ExamModal } from "../exam/examModal";
//import { unwrapResult } from "@reduxjs/toolkit";

// export interface ExamDeleteProps {
//   examId: string
// }

export const Exam = (): JSX.Element => {
  const { slug } = useParams<{ slug: string }>();
  const dispatch = useAppDispatch();
  const examsStatus = useSelector(selectExamsStatus);
  const exam = useSelector(selectExamBySlug(slug));
  const [examModalShow, setExamModalShow] = useState(false);

  useEffect(() => {
    if (examsStatus === "idle") {
      dispatch(fetchExams(slug));
    }
  }, [slug, dispatch, examsStatus]);

  if (exam === undefined) {
    return (
      <div>No exams created</div>
    );
  }
  // const onSubmit = (): void => {
  //   dispatch(deleteExam({
  //     examId: props.examId,
  //   }))
  //     .then(unwrapResult)
  //     .then(() => {
  //       props.onHide();
  //     })
  //     .catch(e => {
  //       console.error(e);
  //     });
  // };
  //
  //
  return (
    <Container className={styles.margin}>
      <Jumbotron>
        <h1>{exam.name}</h1>
        <p>{exam.description}</p>
        <Button onClick={() => setExamModalShow(true)}>
          Edit Exam
        </Button>
        <Button>
          Delete Exam
        </Button>
      </Jumbotron>
      <ExamModal show={examModalShow} onHide={() => setExamModalShow(false)}
        examId={exam.id}
        name={exam.name}
        description={exam.description}
        startTime={exam.startTime}
        finishTime={exam.finishTime}/>
    </Container>
  );
};

import { unwrapResult } from "@reduxjs/toolkit";
import React, { useEffect, useState } from "react";
import { Alert, Button, Container, Jumbotron } from "react-bootstrap";
import { useSelector } from "react-redux";
import { useHistory, useParams } from "react-router-dom";
import { deleteExam, fetchExam, selectExamBySlug, updateExam } from "../../redux/slices/examsSlice";
import { selectMe } from "../../redux/slices/usersSlice";
import { useAppDispatch } from "../../redux/store";
import { ExamModal } from "../exam/examModal";
import { QuestionModal } from "../question/questionModal";
import { Questions } from "../question/questions";
import styles from "../subject/subject.module.scss";
import { Submissions } from "../submission/submissions";

export const Exam = (): JSX.Element => {
  const { slug } = useParams<{ slug: string }>();
  const dispatch = useAppDispatch();
  const exam = useSelector(selectExamBySlug(slug));
  const [examModalShow, setExamModalShow] = useState(false);
  const [questionModalShow, setQuestionModalShow] = useState(false);
  const history = useHistory();
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const me = useSelector(selectMe);

  useEffect(() => {
    dispatch(fetchExam(slug))
      .then(unwrapResult)
      .catch((e: Error) => {
        if (e.message === "400") {
          setErrorMessage("Bad Request");
        } else if (e.message === "401") {
          setErrorMessage("Unauthorized Request");
        } else if (e.message === "404") {
          setErrorMessage("Failed to fetch Exams");
        } else if (e.message === "412") {
          setErrorMessage("Client Error");
        } else if (e.message === "500") {
          setErrorMessage("Internal Server Error");
        }
        console.error(e);
      });
  }, [dispatch, slug]);

  if (exam === undefined) {
    return (
      <div>Unable to find exam</div>
    );
  }

  const onClick = (): void => {
    dispatch(deleteExam(exam.id))
      .then(unwrapResult)
      .then(() => {
        history.goBack();
      })
      .catch((e: Error) => {
        if (e.message === "400") {
          setErrorMessage("Bad Request");
        } else if (e.message === "401") {
          setErrorMessage("Unauthorized Request");
        } else if (e.message === "404") {
          setErrorMessage("The exam not found");
        } else if (e.message === "412") {
          setErrorMessage("Client Error");
        } else if (e.message === "500") {
          setErrorMessage("Internal Server Error");
        }
        console.error(e);
      });
  };

  const currentTime = new Date();
  const startTime = new Date(exam.startTime);
  const finishTime = new Date(exam.finishTime);

  let state: "new" | "published" | "closed";

  if (startTime > currentTime) {
    state = "new";
  } else if (finishTime < currentTime) {
    state = "closed";
  } else {
    state = "published";
  }

  const publishNow = (): void => {
    dispatch(updateExam({
      id: exam.id,
      exam: {
        name: exam.name,
        description: exam.description,
        startTime: new Date().toISOString(),
        finishTime: exam.finishTime,
      },
      eTag: exam.meta.eTag,
    }))
      .then(unwrapResult)
      .catch((e: Error) => {
        if (e.message === "400") {
          setErrorMessage("Bad Request");
        } else if (e.message === "401") {
          setErrorMessage("Unauthorized Request");
        } else if (e.message === "404") {
          setErrorMessage("The exam not found");
        } else if (e.message === "412") {
          setErrorMessage("Client Error");
        } else if (e.message === "500") {
          setErrorMessage("Internal Server Error");
        }
        console.error(e);
      });
  };

  const closeNow = (): void => {
    dispatch(updateExam({
      id: exam.id,
      exam: {
        name: exam.name,
        description: exam.description,
        startTime: exam.startTime,
        finishTime: new Date().toISOString(),
      },
      eTag: exam.meta.eTag,
    }))
      .then(unwrapResult)
      .catch((e: Error) => {
        if (e.message === "400") {
          setErrorMessage("Bad Request");
        } else if (e.message === "401") {
          setErrorMessage("Unauthorized Request");
        } else if (e.message === "404") {
          setErrorMessage("The exam not found");
        } else if (e.message === "412") {
          setErrorMessage("Client Error");
        } else if (e.message === "500") {
          setErrorMessage("Internal Server Error");
        }
        console.error(e);
      });
  };

  if (errorMessage  !== null) {
    return (
      <Alert variant="danger">
        {errorMessage}
      </Alert>
    );
  }

  return (
    <Container className={styles.margin}>
      <Jumbotron>
        <h1>{exam.name}</h1>
        <p>{exam.description}</p>
        {
          me?.type === "INSTRUCTOR" &&
            <>
              <Button className="mr-2" onClick={() => setExamModalShow(true)}>
                Edit Exam
              </Button>
              <Button className="mr-2" onClick={onClick}>
                Delete Exam
              </Button>
              {
                state === "new" &&
                  <>
                    <Button className="mr-2" onClick={() => setQuestionModalShow(true)}>
                      Add Question
                    </Button>
                    <Button className="mr-2" onClick={publishNow}>
                      Publish Exam
                    </Button>
                  </>
              }
              {
                state === "published" &&
                  <Button onClick={closeNow}>
                    Close Exam
                  </Button>
              }
            </>
        }
      </Jumbotron>
      <ExamModal
        show={examModalShow}
        onHide={() => setExamModalShow(false)}
        id={exam.id}
        name={exam.name}
        description={exam.description}
        startTime={exam.startTime}
        finishTime={exam.finishTime}
        eTag={exam.meta.eTag} />
      <QuestionModal
        show={questionModalShow}
        onHide={() => setQuestionModalShow(false)}
        examId={exam.id} />
      {
        state !== "closed"
          ? <Questions examId={exam.id} />
          : <Submissions examId={exam.id} />
      }
    </Container>
  );
};

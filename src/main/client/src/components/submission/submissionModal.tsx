import React from "react";
import { Modal } from "react-bootstrap";
import { useSelector } from "react-redux";
import { ExamState, QuestionSubmission, selectExamById } from "../../redux/slices/examsSlice";
import { QuestionState, selectQuestionsForExam } from "../../redux/slices/questionsSlice";
import { selectUserById, UserState } from "../../redux/slices/usersSlice";
import { RootState } from "../../redux/store";

interface SubmissionModalProps {
  show: boolean,
  onHide: () => any,
  examId: string,
  studentId: string,
}

export const SubmissionModal = (props: SubmissionModalProps): JSX.Element => {
  const student = useSelector<RootState, UserState | undefined>(
    state => selectUserById(state, props.studentId)
  );

  const questionSubmissions: Record<string, QuestionSubmission | undefined> = {};

  useSelector<RootState, ExamState | undefined>(
    state => selectExamById(state, props.examId)
  )
    ?.submissions
    ?.find(submission => submission.studentId === props.studentId)
    ?.questionSubmissions
    .forEach(questionSubmission => {
      questionSubmissions[questionSubmission.questionId] = questionSubmission;
    });

  const questionMap: Record<string, QuestionState> = {};
  const questions = useSelector(selectQuestionsForExam(props.examId));

  questions
    .forEach(question => {
      questionMap[question.id] = question;
    });

  if (student === undefined) {
    return <></>;
  }

  return (
    <Modal show={props.show} onHide={props.onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>
          Mark {student.name}&apos;s Exam
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {
          questions.map(question => (
            <div key={question.id}>
              question: {question.question}
              <br />
              answer: {questionSubmissions[question.id]?.answer ?? ""}
            </div>
          ))
        }
      </Modal.Body>
    </Modal>
  );
};

package com.ekzameno.ekzameno.services;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;

import com.ekzameno.ekzameno.dtos.CreateOptionDTO;
import com.ekzameno.ekzameno.exceptions.PreconditionFailedException;
import com.ekzameno.ekzameno.mappers.QuestionMapper;
import com.ekzameno.ekzameno.mappers.QuestionSubmissionMapper;
import com.ekzameno.ekzameno.models.MultipleChoiceQuestion;
import com.ekzameno.ekzameno.models.Option;
import com.ekzameno.ekzameno.models.Question;
import com.ekzameno.ekzameno.models.QuestionSubmission;
import com.ekzameno.ekzameno.models.ShortAnswerQuestion;
import com.ekzameno.ekzameno.shared.DBConnection;
import com.ekzameno.ekzameno.shared.IdentityMap;
import com.ekzameno.ekzameno.shared.UnitOfWork;

/**
 * Service to handle questions.
 */
public class QuestionService {
    private QuestionMapper questionMapper = new QuestionMapper();
    private QuestionSubmissionMapper questionSubmissionMapper =
        new QuestionSubmissionMapper();

    /**
     * Create a question for a given exam.
     *
     * @param examId   ID of the exam to create the question for
     * @param question text of the question
     * @param marks    number of marks allocated to the question
     * @param type     type of the question
     * @param options  options for the question
     * @return created Question
     */
    public Question createQuestion(
        UUID examId,
        String question,
        int marks,
        String type,
        List<CreateOptionDTO> options
    ) {
        try (DBConnection connection = DBConnection.getCurrent()) {
            Question q;

            if (type.toUpperCase().equals(MultipleChoiceQuestion.TYPE)) {
                q = new MultipleChoiceQuestion(question, marks, examId);

                for (CreateOptionDTO a : options) {
                    Option option = new Option(a.answer, a.correct, q.getId());
                    ((MultipleChoiceQuestion) q).getOptions().add(option);
                }

            } else if (type.toUpperCase().equals(ShortAnswerQuestion.TYPE)) {
                q = new ShortAnswerQuestion(question, marks, examId);
            } else {
                throw new BadRequestException();
            }

            UnitOfWork.getCurrent().commit();
            return q;
        } catch (SQLException e) {
            try {
                UnitOfWork.getCurrent().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            throw new InternalServerErrorException();
        }
    }

    /**
     * Update a question for a given exam.
     *
     * @param questionId ID of the question
     * @param question   text of the question
     * @param marks      number of marks allocated to the question
     * @param eTag       entity tag
     * @return created Question
     */
    public Question updateQuestion(
        String question,
        int marks,
        UUID questionId,
        String eTag
    ) {
        try (DBConnection connection = DBConnection.getCurrent()) {
            Question questionModel = questionMapper.findById(questionId, true);

            if (!String.valueOf(questionModel.hashCode()).equals(eTag)) {
                throw new PreconditionFailedException();
            }

            questionModel.setQuestion(question);
            questionModel.setMarks(marks);
            UnitOfWork.getCurrent().commit();
            return questionModel;
        } catch (SQLException e) {
            try {
                UnitOfWork.getCurrent().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            throw new InternalServerErrorException();
        }
    }

    /**
     * Delete a question.
     *
     * @param questionId id of the question
     */
    public void deleteQuestion(
        UUID questionId
    ) {
        try (DBConnection connection = DBConnection.getCurrent()) {
            questionMapper.deleteById(questionId);
            UnitOfWork.getCurrent().commit();
        } catch (SQLException e) {
            try {
                UnitOfWork.getCurrent().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            throw new InternalServerErrorException();
        }
    }

    /**
     * Retrieve questions for a given exam.
     *
     * @param examId ID of the exam to retrieve questions for
     * @return questions for the given exam
     */
    public List<Question> getQuestionsForExam(UUID examId) {
        try {
            return questionMapper.findAllForExam(examId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServerErrorException();
        } finally {
            IdentityMap.reset();
        }
    }

    /**
     * Create a question submission.
     *
     * @param questionId       ID of the question the submission belongs to
     * @param examSubmissionId ID of the exam submission the submission belongs
     *                         to
     * @param answer           answer for the submission
     * @param marks            number of marks for the submission
     * @return the created question submission
     */
    public QuestionSubmission createSubmission(
        UUID questionId,
        UUID examSubmissionId,
        String answer,
        Integer marks
    ) {
        try (DBConnection connection = DBConnection.getCurrent()) {
            QuestionSubmission questionSubmission = new QuestionSubmission(
                answer,
                questionId,
                examSubmissionId,
                marks
            );

            UnitOfWork.getCurrent().commit();
            return questionSubmission;
        } catch (SQLException e) {
            try {
                UnitOfWork.getCurrent().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            throw new InternalServerErrorException();
        }
    }

    /**
     * Update a question submission.
     *
     * @param questionId       ID of the question the submission belongs to
     * @param examSubmissionId ID of the exam submission the submission belongs
     *                         to
     * @param marks            number of marks for the submission
     * @param eTag             entity tag
     * @return the updated question submission
     */
    public QuestionSubmission updateSubmission(
        UUID questionId,
        UUID examSubmissionId,
        Integer marks,
        String eTag
    ) {
        try (DBConnection connection = DBConnection.getCurrent()) {
            QuestionSubmission questionSubmission = questionSubmissionMapper
                .findByRelationIds(questionId, examSubmissionId);

            if (!String.valueOf(questionSubmission.hashCode()).equals(eTag)) {
                throw new PreconditionFailedException();
            }

            questionSubmission.setMarks(marks);

            UnitOfWork.getCurrent().commit();
            return questionSubmission;
        } catch (SQLException e) {
            try {
                UnitOfWork.getCurrent().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            throw new InternalServerErrorException();
        }
    }
}

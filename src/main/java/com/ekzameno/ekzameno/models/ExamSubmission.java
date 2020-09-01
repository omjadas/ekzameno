package com.ekzameno.ekzameno.models;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.ekzameno.ekzameno.mappers.ExamMapper;
import com.ekzameno.ekzameno.mappers.QuestionSubmissionMapper;
import com.ekzameno.ekzameno.mappers.StudentMapper;
import com.ekzameno.ekzameno.shared.UnitOfWork;

/**
 * Submission for an Exam.
 */
public class ExamSubmission extends Model {
    private int marks;
    private List<QuestionSubmission> questionSubmissions = null;
    private UUID studentId;
    private UUID examId;
    private Student student = null;
    private Exam exam = null;

    /**
     * Create an ExamSubmission with an ID.
     *
     * @param id    ID of the ExamSubmission
     * @param marks total number of marks for the ExamSubmission
     */
    public ExamSubmission(UUID id, int marks, UUID studentId, UUID examId) {
        super(id);
        this.marks = marks;
        this.studentId = studentId;
        this.examId = examId;
    }

    /**
     * Create an ExamSubmission without an ID (registers as new).
     *
     * @param marks total number of marks for the ExamSubmission.
     */
    public ExamSubmission(int marks, UUID studentId, UUID examId) {
        this.marks = marks;
        this.studentId = studentId;
        this.examId = examId;
    }

    public int getMarks() {
        return marks;
    }

    /**
     * Retrieve QuestionSubmissions for the ExamSubmission.
     *
     * @return QuestionSubmissions for the ExamSubmission
     * @throws SQLException if unable to retrieve the QuestionSubmissions
     */
    public List<QuestionSubmission> getQuestionSubmissions()
            throws SQLException {
        if (questionSubmissions == null) {
            return new QuestionSubmissionMapper()
                .findAllForExamSubmission(getId());
        } else {
            return questionSubmissions;
        }
    }

    /**
     * Set the number of marks earned by the ExamSubmission (marks the
     * ExamSubmission as dirty).
     *
     * @param marks number of marks earned
     */
    public void setMarks(int marks) {
        this.marks = marks;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    public UUID getStudentId() {
        return studentId;
    }

    public UUID getExamId() {
        return examId;
    }

    public Student getStudent() throws SQLException {
        if (student == null) {
            student = new StudentMapper().find(studentId);
        }
        return student;
    }

    public Exam getExam() throws SQLException {
        if (exam == null) {
            exam = new ExamMapper().find(examId);
        }
        return exam;
    }

    public void setStudent(Student student) {
        this.student = student;
        this.studentId = student.getId();
        UnitOfWork.getCurrent().registerDirty(this);
    }

    public void setExam(Exam exam) {
        this.exam = exam;
        this.examId = student.getId();
        UnitOfWork.getCurrent().registerDirty(this);
    }
}

package com.ekzameno.ekzameno.shared;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ekzameno.ekzameno.dtos.CreateOptionDTO;
import com.ekzameno.ekzameno.exceptions.ConflictException;
import com.ekzameno.ekzameno.models.Exam;
import com.ekzameno.ekzameno.models.Subject;
import com.ekzameno.ekzameno.models.User;
import com.ekzameno.ekzameno.services.ExamService;
import com.ekzameno.ekzameno.services.QuestionService;
import com.ekzameno.ekzameno.services.SubjectService;
import com.ekzameno.ekzameno.services.UserService;

/**
 * Seed the database.
 */
public class Seeds {
    private static final UserService userService = new UserService();
    private static final SubjectService subjectService = new SubjectService();
    private static final ExamService examService = new ExamService();
    private static final QuestionService questionService =
        new QuestionService();
    private static final String connectionUrl = System.getenv(
        "JDBC_DATABASE_URL"
    );
    private static final String ddl = "./scripts/ekzameno.sql";

    /**
     * Create DB tables and add default admin user.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try (
            Connection connection = DriverManager.getConnection(connectionUrl);
            Statement statement = connection.createStatement();
        ) {
            statement.execute(
                Files.lines(Paths.get(ddl)).collect(Collectors.joining())
            );
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            userService.registerUser(
                "Admin",
                "admin@ekzame.no",
                "password",
                "administrator"
            );

            User eduardo = userService.registerUser(
                "Eduardo",
                "eduardo@ekzame.no",
                "password",
                "instructor"
            );

            User maria = userService.registerUser(
                "Maria",
                "maria@ekzame.no",
                "password",
                "instructor"
            );

            User omja = userService.registerUser(
                "Omja",
                "omja@ekzame.no",
                "password",
                "student"
            );

            User joao = userService.registerUser(
                "Joao",
                "joao@ekzame.no",
                "password",
                "student"
            );

            User muzamil = userService.registerUser(
                "Muzamil",
                "muzamil@ekzame.no",
                "password",
                "student"
            );

            Subject sda = subjectService.createSubject(
                "SDA",
                "Software Design and Architecture",
                new UUID[] {
                    eduardo.getId(),
                    maria.getId()
                },
                new UUID[] {
                    omja.getId(),
                    joao.getId(),
                    muzamil.getId()
                }
            );

            Exam sdaMidSemExam = examService.createExam(
                "Mid-Sem (35 Marks)",
                "This exam contains multiple choice and short answer" +
                "questions. The exam is for 35 marks. The exam will " +
                "count towards final marks for the subject (10%). " +
                "There is a hurdle for the exam and student should " +
                "score more than 50%.",
                new Date(),
                new Date(new Date().getTime() + 604800000),
                sda.getId()
            );

            CreateOptionDTO msQ1A = new CreateOptionDTO();
            msQ1A.answer = "Identity Map";
            msQ1A.correct = true;
            CreateOptionDTO msQ1B = new CreateOptionDTO();
            msQ1B.answer = "Unit of Work";
            msQ1B.correct = false;

            questionService.createQuestion(
                sdaMidSemExam.getId(),
                "____ is a simple pattern that helps to maintain data " +
                "integrity",
                3,
                "MULTIPLE_CHOICE",
                Arrays.asList(msQ1A, msQ1B)
            );

            CreateOptionDTO msQ2A = new CreateOptionDTO();
            msQ2A.answer = "Identity Map";
            msQ2A.correct = false;
            CreateOptionDTO msQ2B = new CreateOptionDTO();
            msQ2B.answer = "Unit of Work";
            msQ2B.correct = true;
            CreateOptionDTO msQ2C = new CreateOptionDTO();
            msQ2C.answer = "Lazy Laod";
            msQ2C.correct = false;
            CreateOptionDTO msQ2D = new CreateOptionDTO();
            msQ2D.answer = "Data Mapper";
            msQ2D.correct = false;

            questionService.createQuestion(
                sdaMidSemExam.getId(),
                "The _____ pattern describes a way to keep track of " +
                "which domain objects have changed (or new object created)",
                3,
                "MULTIPLE_CHOICE",
                Arrays.asList(msQ2A, msQ2B, msQ2C, msQ2D)
            );

            CreateOptionDTO msQ3A = new CreateOptionDTO();
            msQ3A.answer = "True";
            msQ3A.correct = true;
            CreateOptionDTO msQ3B = new CreateOptionDTO();
            msQ3B.answer = "False";
            msQ3B.correct = false;

            questionService.createQuestion(
                sdaMidSemExam.getId(),
                "Transaction Script pattern follows a procedural" +
                "style of development: True or False",
                2,
                "MULTIPLE_CHOICE",
                Arrays.asList(msQ3A, msQ3B)
            );

            CreateOptionDTO msQ4A = new CreateOptionDTO();
            msQ4A.answer = "Change and Performance";
            msQ4A.correct = true;
            CreateOptionDTO msQ4B = new CreateOptionDTO();
            msQ4B.answer = "Understandability";
            msQ4B.correct = false;
            CreateOptionDTO msq4C = new CreateOptionDTO();
            msq4C.answer = "Substitution";
            msq4C.correct = false;

            questionService.createQuestion(
                sdaMidSemExam.getId(),
                "Layering is an important technique," +
                " but there are downsides:",
                3,
                "MULTIPLE_CHOICE",
                Arrays.asList(msQ4A, msQ4B, msq4C)
            );

            questionService.createQuestion(
                sdaMidSemExam.getId(),
                "What are patterns ? Explain any two patterns with examples",
                8,
                "SHORT_ANSWER",
                null
            );

            questionService.createQuestion(
                sdaMidSemExam.getId(),
                "In regards to symptoms of bad desgin, " +
                "immobility means ? Explain ?",
                4,
                "SHORT_ANSWER",
                null
            );

            questionService.createQuestion(
                sdaMidSemExam.getId(),
                "Explain the main three types of design " +
                "pattern with suitable example.",
                6,
                "SHORT_ANSWER",
                null
            );

            questionService.createQuestion(
                sdaMidSemExam.getId(),
                "What is the difference between " +
                "identity map and identity field?",
                4,
                "SHORT_ANSWER",
                null
            );

            CreateOptionDTO msQ5A = new CreateOptionDTO();
            msQ5A.answer = "Lazy Load";
            msQ5A.correct = true;
            CreateOptionDTO msQ5B = new CreateOptionDTO();
            msQ5B.answer = "Unit of Work";
            msQ5B.correct = false;

            questionService.createQuestion(
                sdaMidSemExam.getId(),
                "The pattern aims to reduce the amount of" +
                "data read from database by only reading " +
                "what it needs",
                2,
                "MULTIPLE_CHOICE",
                Arrays.asList(msQ5A, msQ5B)
            );

            Exam sdaFinalExam = examService.createExam(
                "Final Exam (30 Marks)",
                "The exam contains 4 questions with 3 short" +
                "answers and 1 multiple choice question. This" +
                " exam contributes 50% of the final marks.",
                new Date(),
                new Date(new Date().getTime() + 604800000),
                sda.getId()
            );

            questionService.createQuestion(
                sdaFinalExam.getId(),
                "Explain the benefits of using MVC architecture",
                10,
                "SHORT_ANSWER",
                null
            );

            CreateOptionDTO fQ1A = new CreateOptionDTO();
            fQ1A.answer = "False";
            fQ1A.correct = true;
            CreateOptionDTO fQ1B = new CreateOptionDTO();
            fQ1B.answer = "True";
            fQ1B.correct = false;

            questionService.createQuestion(
                sdaFinalExam.getId(),
                "Idenitty map and Identity field are" +
                "almost similar ? True or False",
                3,
                "MULTIPLE_CHOICE",
                Arrays.asList(fQ1A, fQ1B)
            );

            questionService.createQuestion(
                sdaFinalExam.getId(),
                "Briefly explain about authorization and authentication",
                7,
                "SHORT_ANSWER",
                null
            );

            questionService.createQuestion(
                sdaFinalExam.getId(),
                "Why do we use unit of work ?",
                10,
                "SHORT_ANSWER",
                null
            );

            Subject dbms = subjectService.createSubject(
                "DBMS",
                "Database Management System",
                new UUID[] {
                    eduardo.getId(),
                    maria.getId()
                },
                new UUID[] {
                    omja.getId(),
                    joao.getId(),
                    muzamil.getId()
                }
            );

            Exam dbmsQuiz = examService.createExam(
                "Quiz",
                "The Quiz contains 5 questions of 2 marks each",
                new Date(),
                new Date(new Date().getTime() + 604800000),
                dbms.getId()
            );

            CreateOptionDTO qQ1A = new CreateOptionDTO();
            qQ1A.answer = "One class may have many teachers";
            qQ1A.correct = false;
            CreateOptionDTO qQ1B = new CreateOptionDTO();
            qQ1B.answer = "Many classes may have many teachers";
            qQ1B.correct = false;
            CreateOptionDTO qQ1C = new CreateOptionDTO();
            qQ1C.answer = "One teacher can have many classes";
            qQ1C.correct = true;
            CreateOptionDTO qQ1D = new CreateOptionDTO();
            qQ1D.answer = "Many teachers may have many classes ";
            qQ1D.correct = false;

            questionService.createQuestion(
                dbmsQuiz.getId(),
                "What do you mean by one to many relationship" +
                "between Teacher and Class table?",
                2,
                "MULTIPLE_CHOICE",
                Arrays.asList(qQ1A, qQ1B, qQ1C, qQ1D)
            );

            CreateOptionDTO qQ2A = new CreateOptionDTO();
            qQ2A.answer = "Datasheet View ";
            qQ2A.correct = false;
            CreateOptionDTO qQ2B = new CreateOptionDTO();
            qQ2B.answer = "Design View";
            qQ2B.correct = false;
            CreateOptionDTO qQ2C = new CreateOptionDTO();
            qQ2C.answer = "Pivote TableView";
            qQ2C.correct = false;
            CreateOptionDTO qQ2D = new CreateOptionDTO();
            qQ2D.answer = "All Of Above";
            qQ2D.correct = true;

            questionService.createQuestion(
                dbmsQuiz.getId(),
                "What are the different view to present a Table ?",
                2,
                "MULTIPLE_CHOICE",
                Arrays.asList(qQ2A, qQ2B, qQ2C, qQ2D)
            );

            CreateOptionDTO qQ3A = new CreateOptionDTO();
            qQ3A.answer = "Child";
            qQ3A.correct = true;
            CreateOptionDTO qQ3B = new CreateOptionDTO();
            qQ3B.answer = "Parent";
            qQ3B.correct = false;
            CreateOptionDTO qQ3C = new CreateOptionDTO();
            qQ3C.answer = "Sister";
            qQ3C.correct = false;
            CreateOptionDTO qQ3D = new CreateOptionDTO();
            qQ3D.answer = "Master";
            qQ3D.correct = false;

            questionService.createQuestion(
                dbmsQuiz.getId(),
                "In one-to-many relationship the table on 'many'" +
                " side is called _______",
                2,
                "MULTIPLE_CHOICE",
                Arrays.asList(qQ3A, qQ3B, qQ3C, qQ3D)
            );

            CreateOptionDTO qQ4A = new CreateOptionDTO();
            qQ4A.answer = "Data Refinement ";
            qQ4A.correct = false;
            CreateOptionDTO qQ4B = new CreateOptionDTO();
            qQ4B.answer = "Establishing Relationship ";
            qQ4B.correct = false;
            CreateOptionDTO qQ4C = new CreateOptionDTO();
            qQ4C.answer = "Data Definition ";
            qQ4C.correct = true;
            CreateOptionDTO qQ4D = new CreateOptionDTO();
            qQ4D.answer = "None Of The Above";
            qQ4D.correct = false;

            questionService.createQuestion(
                dbmsQuiz.getId(),
                "In which state one gathers and list all the" +
                " necessary fields for the database design project",
                2,
                "MULTIPLE_CHOICE",
                Arrays.asList(qQ4A, qQ4B, qQ4C, qQ4D)
            );

            CreateOptionDTO qQ5A = new CreateOptionDTO();
            qQ5A.answer = "Form";
            qQ5A.correct = false;
            CreateOptionDTO qQ5B = new CreateOptionDTO();
            qQ5B.answer = "Macro";
            qQ5B.correct = false;
            CreateOptionDTO qQ5C = new CreateOptionDTO();
            qQ5C.answer = "Report";
            qQ5C.correct = false;
            CreateOptionDTO qQ5D = new CreateOptionDTO();
            qQ5D.answer = "Query";
            qQ5D.correct = true;

            questionService.createQuestion(
                dbmsQuiz.getId(),
                "Which of the following enables us to view " +
                "data from a table based on a specific criterion",
                2,
                "MULTIPLE_CHOICE",
                Arrays.asList(qQ5A, qQ5B, qQ5C, qQ5D)
            );

            Exam dbmsFinalExam = examService.createExam(
                "Final Exam (25 Marks)",
                "Final Exam:\n" +
                "Hurdle requirement: 50%",
                new Date(),
                new Date(new Date().getTime() + 604800000),
                dbms.getId()
            );

            CreateOptionDTO fsQ1A = new CreateOptionDTO();
            fsQ1A.answer = "Auto number";
            fsQ1A.correct = false;
            CreateOptionDTO fsQ1B = new CreateOptionDTO();
            fsQ1B.answer = "Memo";
            fsQ1B.correct = false;
            CreateOptionDTO fsQ1C = new CreateOptionDTO();
            fsQ1C.answer = "Mixed";
            fsQ1C.correct = false;
            CreateOptionDTO fsQ1D = new CreateOptionDTO();
            fsQ1D.answer = "Text";
            fsQ1D.correct = true;

            questionService.createQuestion(
                dbmsFinalExam.getId(),
                "Which data type allows alphanumeric characters " +
                "and special symbols to be entered?",
                3,
                "MULTIPLE_CHOICE",
                Arrays.asList(fsQ1A, fsQ1B, fsQ1C, fsQ1D)
            );

            questionService.createQuestion(
                sdaFinalExam.getId(),
                "What are Advantages and Disadvantages of DBMS?",
                10,
                "SHORT_ANSWER",
                null
            );

            questionService.createQuestion(
                sdaFinalExam.getId(),
                "What is Data Redundancy ?",
                10,
                "SHORT_ANSWER",
                null
            );

            CreateOptionDTO fsQ2A = new CreateOptionDTO();
            fsQ2A.answer = "Line";
            fsQ2A.correct = false;
            CreateOptionDTO fsQ2B = new CreateOptionDTO();
            fsQ2B.answer = "Relationship";
            fsQ2B.correct = true;
            CreateOptionDTO fsQ2C = new CreateOptionDTO();
            fsQ2C.answer = "Primary Key";
            fsQ2C.correct = false;
            CreateOptionDTO fsQ2D = new CreateOptionDTO();
            fsQ2D.answer = "Records";
            fsQ2D.correct = false;

            questionService.createQuestion(
                dbmsFinalExam.getId(),
                "It is used to establish an association" +
                "between related tables.",
                3,
                "MULTIPLE_CHOICE",
                Arrays.asList(fsQ2A, fsQ2B, fsQ2C, fsQ2D)
            );

            questionService.createQuestion(
                sdaFinalExam.getId(),
                "What is DBMS (Database Management System) ?",
                6,
                "SHORT_ANSWER",
                null
            );

            questionService.createQuestion(
                sdaFinalExam.getId(),
                "What is SQL (Structured Query Language)?",
                5,
                "SHORT_ANSWER",
                null
            );

            CreateOptionDTO fsQ3A = new CreateOptionDTO();
            fsQ3A.answer = "Unique Key";
            fsQ3A.correct = false;
            CreateOptionDTO fsQ3B = new CreateOptionDTO();
            fsQ3B.answer = "Field Name";
            fsQ3B.correct = false;
            CreateOptionDTO fsQ3C = new CreateOptionDTO();
            fsQ3C.answer = "Key Record";
            fsQ3C.correct = false;
            CreateOptionDTO fsQ3D = new CreateOptionDTO();
            fsQ3D.answer = "Primary Key";
            fsQ3D.correct = true;

            questionService.createQuestion(
                dbmsFinalExam.getId(),
                "This key that uniquely identifies each" +
                "record is called : ",
                3,
                "MULTIPLE_CHOICE",
                Arrays.asList(fsQ3A, fsQ3B, fsQ3C, fsQ3D)
            );

            questionService.createQuestion(
                sdaFinalExam.getId(),
                "What is the need of DBMS ?",
                10,
                "SHORT_ANSWER",
                null
            );
        } catch (ConflictException e) {
            System.out.println("Users already exist");
        }
    }
}

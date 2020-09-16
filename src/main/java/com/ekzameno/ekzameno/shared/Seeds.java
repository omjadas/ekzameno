package com.ekzameno.ekzameno.shared;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ekzameno.ekzameno.exceptions.UnknownUserTypeException;
import com.ekzameno.ekzameno.exceptions.UserAlreadyExistsException;
import com.ekzameno.ekzameno.models.Subject;
import com.ekzameno.ekzameno.models.User;
import com.ekzameno.ekzameno.services.ExamService;
import com.ekzameno.ekzameno.services.SubjectService;
import com.ekzameno.ekzameno.services.UserService;

/**
 * Seed the database.
 */
public class Seeds {
    private static final UserService userService = new UserService();
    private static final SubjectService subjectService = new SubjectService();
    private static final ExamService examService = new ExamService();
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
                "odas@ekzame.no",
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

            examService.createExam(
                "Mid-Sem",
                "Mid-Semester Exam",
                new Date(),
                new Date(new Date().getTime() + 604800000),
                sda.getId()
            );

            examService.createExam(
                "Final",
                "Final Exam",
                new Date(),
                new Date(new Date().getTime() + 604800000),
                sda.getId()
            );
        } catch (UnknownUserTypeException e) {
            // this should only occur if the above user types are modified
            e.printStackTrace();
        } catch (UserAlreadyExistsException e) {
            System.out.println("Users already exist");
        }
    }
}

package com.ekzameno.ekzameno.shared;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

import com.ekzameno.ekzameno.exceptions.UnknownUserTypeException;
import com.ekzameno.ekzameno.exceptions.UserAlreadyExistsException;
import com.ekzameno.ekzameno.services.UserService;

/**
 * Seed the database.
 */
public class Seeds {
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
        }

        try (DBConnection connection = DBConnection.getInstance()) {
            try {
				new UserService().registerUser(
				    "Admin",
				    "admin@ekzame.no",
				    "password",
				    "administrator"
				);
			} catch (UnknownUserTypeException e) {
                // this should only occur if the above user type is modified
				e.printStackTrace();
            } catch (UserAlreadyExistsException e) {
                System.out.println("Admin already exists");
			}
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

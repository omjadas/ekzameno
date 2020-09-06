package com.ekzameno.ekzameno.shared;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

import com.ekzameno.ekzameno.services.AuthService;

public class Seeds {
    private static final String connectionUrl = System.getenv(
        "JDBC_DATABASE_URL"
    );
    private static final String ddl = "./scripts/ekzameno.sql";

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
            new AuthService().registerUser(
                "Admin",
                "admin@ekzame.no",
                "password",
                "administrator"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

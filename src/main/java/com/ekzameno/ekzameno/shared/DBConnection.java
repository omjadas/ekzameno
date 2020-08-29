package com.ekzameno.ekzameno.shared;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String connectionUrl = System.getenv("JDBC_DATABASE_URL");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionUrl);
    }
}

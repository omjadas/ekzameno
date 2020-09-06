package com.ekzameno.ekzameno.shared;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Wrapper class to handle connections to the database.
 */
public class DBConnection implements AutoCloseable {
    private static ThreadLocal<DBConnection> dbConnection = new ThreadLocal<>();
    private Connection connection = null;
    private static final String connectionUrl = System.getenv(
        "JDBC_DATABASE_URL"
    );

    private DBConnection() throws SQLException {
        connection = DriverManager.getConnection(connectionUrl);
        connection.setAutoCommit(false);
    }

    /**
     * Retrieve a thread local singleton DBConnection.
     *
     * @return thread local singleton DBConnection
     * @throws SQLException if unable to connect to the data base
     */
    public static DBConnection getInstance() throws SQLException {
        DBConnection dbc = dbConnection.get();

        if (dbc == null)  {
            dbc = new DBConnection();
            dbConnection.set(dbc);
        }

        return dbConnection.get();
    }

    /**
     * Retrieve connection to the Database.
     *
     * @return connection to the database
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Close the DBConnection.
     */
    public void close() throws SQLException {
        connection.close();
        dbConnection.set(null);
    }
}

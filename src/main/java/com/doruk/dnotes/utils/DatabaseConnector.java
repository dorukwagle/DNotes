package com.doruk.dnotes.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnector {
    private static DatabaseConnector instance;
    private Connection connection;

    private DatabaseConnector() {
        try {
            Class.forName("org.sqlite.JDBC");

            String dbDir = PathUtils.getDatabaseDir();
            String dbPath = dbDir + File.separator + "dnotes.db";
            String dbUrl = "jdbc:sqlite:" + dbPath;

            connection = DriverManager.getConnection(dbUrl);
            connection.setAutoCommit(true);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC driver not found", e);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to SQLite database", e);
        }
    }

    private static DatabaseConnector getInstance() throws RuntimeException {
        if (instance == null) 
            instance = new DatabaseConnector();
        return instance;
    }

    public static Connection getConnection() throws RuntimeException {
        return getInstance().connection;
    }
}

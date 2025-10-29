package com.doruk.dnotes.utils;

import com.doruk.dnotes.store.GlobalConstants;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnector {
    private static DatabaseConnector instance;
    private static String dbPath;
    private Connection connection;

    private DatabaseConnector() {
        try {
            Class.forName("org.sqlite.JDBC");

            String dbDir = PathUtils.getDatabaseDir();
            dbPath = dbDir + File.separator + GlobalConstants.DATABASE_NAME;
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

    public static String getDbPath() {
        return dbPath;
    }
}

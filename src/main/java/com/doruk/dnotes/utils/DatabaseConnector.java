package com.doruk.dnotes.utils;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.interfaces.IEventManager;
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

            connection.createStatement().executeUpdate("PRAGMA busy_timeout = 3000;");

            DIFactory.createEventManager().register(IEventManager.InternalEvent.SHUTDOWN, DatabaseConnector::closeConnection);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC driver not found", e);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to SQLite database", e);
        }
    }

    private static synchronized DatabaseConnector getInstance() throws RuntimeException {
        try {
            if (instance == null)
                instance = new DatabaseConnector();
            else if (instance.connection == null || instance.connection.isClosed())
                instance = new DatabaseConnector();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return instance;
    }

    public static synchronized void closeConnection() {
        if (instance != null && instance.connection != null) {
            try {
                instance.connection.close();
            } catch (SQLException ignored) {
            }
            instance.connection = null;
        }
    }

    public static synchronized Connection getConnection() throws RuntimeException {
        return getInstance().connection;
    }

    public static String getDbPath() {
        return dbPath;
    }
}

package com.doruk.dnotes.utils;

import java.sql.Connection;
import java.sql.SQLException;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.enums.Preference;

public class DatabaseInitializer {
    private static Connection connection;
    
    public static void initialize() throws RuntimeException, SQLException {
        connection = DatabaseConnector.getConnection();
        var preferences = DIFactory.createGlobalPreference();
        
        // only run once
        if (preferences.loadBoolean(Preference.DBInitialized, false))
            return;


        var collectionTable = """
                CREATE TABLE IF NOT EXISTS collections (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    deletedAt TIMESTAMP
                );
                """;
        
        var bookTable = """
                CREATE TABLE IF NOT EXISTS books (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    collectionId INTEGER NOT NULL,
                    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    deletedAt TIMESTAMP,
                    FOREIGN KEY (collectionId) REFERENCES collections (id)
                );
                """;
        
        var bookPageTable = """
                CREATE TABLE IF NOT EXISTS bookPages (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    bookId INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    content TEXT NOT NULL,
                    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    deletedAt TIMESTAMP,
                    FOREIGN KEY (bookId) REFERENCES books (id)
                );
                """;

        var statement = connection.createStatement();
        statement.execute(collectionTable);
        statement.execute(bookTable);
        statement.execute(bookPageTable);

        // create index
        createIndex();

        // create views
        createViews();

        preferences.saveBoolean(Preference.DBInitialized, true);
    }

    private static void createIndex() throws SQLException {
        String[] indexStatements = {
            "CREATE INDEX IF NOT EXISTS idx_coll_deleted_at ON collections (deletedAt);",
            "CREATE INDEX IF NOT EXISTS idx_book_deleted_at ON books (deletedAt);",
            "CREATE INDEX IF NOT EXISTS idx_book_page_deleted_at ON bookPages (deletedAt);",

            "CREATE INDEX IF NOT EXISTS idx_book_collection_id ON books (collectionId);",
            "CREATE INDEX IF NOT EXISTS idx_book_page_book_id ON bookPages (bookId);",

            "CREATE INDEX IF NOT EXISTS idx_book_page_name ON bookPages (name);",
        };

        var statement = connection.createStatement();
        for (String indexStatement : indexStatements) {
            statement.execute(indexStatement);
        }
    }

    private static void createViews() throws SQLException {
        var collectionView = """
            CREATE VIEW IF NOT EXISTS collectionView AS
            SELECT 
                id,
                name,
                updatedAt,
                createdAt
            FROM collections
            WHERE deletedAt IS NULL;
            """;

        var bookView = """
            CREATE VIEW IF NOT EXISTS bookView AS
            SELECT 
                b.id,
                b.title,
                b.collectionId,
                b.updatedAt,
                b.createdAt,
                (SELECT COUNT(*) FROM bookPages WHERE bookId = b.id and deletedAt is null) as pages,
                (SELECT GROUP_CONCAT(name, ', ') FROM (SELECT name FROM bookPages WHERE bookId = b.id and deletedAt is null order by updatedAt desc limit 4)) as preview
            FROM books b
            WHERE b.deletedAt IS NULL;""";

        var bookPageView = """
            CREATE VIEW IF NOT EXISTS bookPageView AS
            SELECT 
                id,
                bookId,
                content,
                updatedAt,
                createdAt
            FROM bookPages
            WHERE deletedAt IS NULL;
            """;

        var statement = connection.createStatement();
        statement.execute(collectionView);
        statement.execute(bookView);
        statement.execute(bookPageView);
    }
}

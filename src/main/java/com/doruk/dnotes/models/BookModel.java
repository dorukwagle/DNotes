package com.doruk.dnotes.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.doruk.dnotes.dto.BookDto;
import com.doruk.dnotes.dto.PaginationParams;
import com.doruk.dnotes.exceptions.DataAccessException;
import com.doruk.dnotes.interfaces.IModel;
import com.doruk.dnotes.utils.DatabaseConnector;
import com.doruk.dnotes.utils.PaginateQuery;

public class BookModel implements IModel<BookDto> {
    private Connection connection;
    
    public BookModel() {
        this.connection = DatabaseConnector.getConnection();
    }

    @Override
    public BookDto add(BookDto book) {
        try {
            var stmt = connection.prepareStatement("INSERT INTO books (title, collectionId) VALUES (?, ?) RETURNING id, updatedAt");
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getCollectionId());
            
            try (var rs = stmt.executeQuery()) {
                // since only one row returned
                rs.next();

                return new BookDto(
                    String.valueOf(rs.getInt("id")),
                    book.getCollectionId(),
                    book.getTitle(),
                    rs.getDate("updatedAt").toString()
                );
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create new book", e);
        }
    }

    @Override
    public BookDto update(BookDto book) {
        try {
            var stmt = connection.prepareStatement("UPDATE books SET title = ?, updatedAt = CURRENT_TIMESTAMP WHERE id = ? RETURNING updatedAt");
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getId());
            
            try (var rs = stmt.executeQuery()) {
                // since only one row returned
                rs.next();

                return new BookDto(
                    book.getId(),
                    book.getCollectionId(),
                    book.getTitle(),
                    rs.getDate("updatedAt").toString()
                );
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update book", e);
        }
    }

    @Override
    public void delete(String id) {
        try {
            // also delete the child tables data i.e. bookPages
            var bookPages = connection.prepareStatement("DELETE FROM bookPages WHERE bookId = ?");
            bookPages.setString(1, id);
            bookPages.executeUpdate();

            var stmt = connection.prepareStatement("DELETE FROM books WHERE id = ?");
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete book", e);
        }
    }

    @Override
    public void softDelete(String id) {
        try {
            var stmt = connection.prepareStatement("UPDATE books SET deletedAt = CURRENT_TIMESTAMP WHERE id = ?");
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete book", e);
        }
    }

    @Override
    public List<BookDto> getAll(PaginationParams paginationParams) {
        try {
            var rs = new PaginateQuery("bookView", paginationParams)
                .select("id, collectionId, title, pages, preview, updatedAt")
                .prepareStatement()
                .executeQuery();

            // add to list
            List<BookDto> books = new ArrayList<>();
            while (rs.next()) {
                books.add(new BookDto(
                    String.valueOf(rs.getInt("id")),
                    rs.getString("collectionId"),
                    rs.getString("title"),
                    rs.getDate("updatedAt").toString(),
                    rs.getString("preview"),
                    rs.getInt("pages")
                ));
            }
            return books;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to get all books from database", e);
        }
    }

    @Override
    public List<BookDto> getAllDeleted(PaginationParams paginationParams) {
        try {
            var rs = new PaginateQuery("books", paginationParams)
                .where("deletedAt IS NOT NULL")
                .select("id, collectionId, title, updatedAt")
                .prepareStatement()
                .executeQuery();

            // add to list
            List<BookDto> books = new ArrayList<>();
            while (rs.next()) {
                books.add(new BookDto(
                    String.valueOf(rs.getInt("id")),
                    rs.getString("collectionId"),
                    rs.getString("title"),
                    rs.getDate("updatedAt").toString()
                ));
            }

            return books;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to get all deleted books from database", e);
        }
    }

    @Override
    public BookDto restore(String id) {
        try {
            var stmt = connection.prepareStatement("UPDATE books SET deletedAt = NULL WHERE id = ? RETURNING collectionId, title, updatedAt");
            stmt.setString(1, id);
            
            try (var rs = stmt.executeQuery()) {
                // since only one row returned
                rs.next();

                return new BookDto(
                    id,
                    rs.getString("collectionId"),
                    rs.getString("title"),
                    rs.getDate("updatedAt").toString()
                );
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to restore book", e);
        }
    } 
}

package com.doruk.dnotes.models;

import com.doruk.dnotes.dto.BookPageDto;
import com.doruk.dnotes.dto.PaginationParams;
import com.doruk.dnotes.enums.NoteType;
import com.doruk.dnotes.exceptions.DataAccessException;
import com.doruk.dnotes.interfaces.IModel;
import com.doruk.dnotes.utils.DatabaseConnector;
import com.doruk.dnotes.utils.PaginateQuery;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class NoteModel implements IModel<BookPageDto> {
    private String parentId;
    private Connection connection;

    public NoteModel() {
        this.connection = DatabaseConnector.getConnection();
    }

    protected abstract String getViewName();

    protected abstract NoteType getNoteType();

    @Override
    public IModel<BookPageDto> ofParentId(String parentId) {
        this.parentId = parentId;
        return this;
    }

    @Override
    public BookPageDto add(BookPageDto bookPage) {
        var query = "INSERT INTO bookPages (name, bookId, content, noteType, sharedBy) VALUES (?, ?, ?, ?, ?) RETURNING id, updatedAt";
        try (var stmt = connection.prepareStatement(query)) {
            stmt.setString(1, bookPage.getName());
            stmt.setString(2, bookPage.getBookId());
            stmt.setString(3, bookPage.getContentId());
            stmt.setString(4, this.getNoteType().name());
            stmt.setString(5, bookPage.getSharedBy()); // can be null


            try (var rs = stmt.executeQuery()) {
                // since only one row returned
                rs.next();

                return new BookPageDto(
                        String.valueOf(rs.getInt("id")),
                        bookPage.getBookId(),
                        bookPage.getName(),
                        bookPage.getContentId(),
                        rs.getDate("updatedAt").toString()
                );
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create new note", e);
        }
    }

    @Override
    public BookPageDto update(BookPageDto bookPage) {
        var query = "UPDATE bookPages SET name = ?, updatedAt = CURRENT_TIMESTAMP WHERE id = ? RETURNING updatedAt";
        try (var stmt = connection.prepareStatement(query)) {
            stmt.setString(1, bookPage.getName());
            stmt.setString(2, bookPage.getId());

            try (var rs = stmt.executeQuery()) {
                // since only one row returned
                rs.next();

                return new BookPageDto(
                        bookPage.getId(),
                        bookPage.getBookId(),
                        bookPage.getName(),
                        bookPage.getContentId(),
                        rs.getDate("updatedAt").toString()
                );
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update note", e);
        }
    }

    @Override
    public void softDelete(String id) {
        try {
            var stmt = connection.prepareStatement("UPDATE bookPages SET deletedAt = CURRENT_TIMESTAMP WHERE id = ?");
            stmt.setString(1, id);
            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete note", e);
        }
    }

    @Override
    public BookPageDto get(String id) {
        try {
            var stmt = connection.prepareStatement("SELECT * FROM bookPages WHERE id = ?");
            stmt.setString(1, id);
            try (var rs = stmt.executeQuery()) {
                // since only one row returned
                rs.next();

                return new BookPageDto(
                        String.valueOf(rs.getInt("id")),
                        rs.getString("bookId"),
                        rs.getString("name"),
                        rs.getString("content"),
                        rs.getDate("updatedAt").toString()
                );
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to get note", e);
        }
    }

    @Override
    public List<BookPageDto> getAll(PaginationParams paginationParams) {
        try {
            var stmt = new PaginateQuery(this.getViewName(), paginationParams)
                    .where(this.parentId != null ? "bookId = " + this.parentId : "")
                    .select("id, bookId, name, content, updatedAt")
                    .searchBy("name")
                    .sortBy("name")
                    .prepareStatement();

            var rs = stmt.executeQuery();

            // add to list
            List<BookPageDto> bookPages = new ArrayList<>();
            while (rs.next()) {
                bookPages.add(new BookPageDto(
                        String.valueOf(rs.getInt("id")),
                        rs.getString("bookId"),
                        rs.getString("name"),
                        rs.getString("content"),
                        rs.getDate("updatedAt").toString()
                ));
            }

            stmt.close();
            return bookPages;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to get all book pages from database", e);
        }
    }
}

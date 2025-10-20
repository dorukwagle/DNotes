package com.doruk.dnotes.models;

import com.doruk.dnotes.dto.BrowserDto;
import com.doruk.dnotes.enums.BrowserElement;
import com.doruk.dnotes.enums.NoteType;
import com.doruk.dnotes.exceptions.DataAccessException;
import com.doruk.dnotes.interfaces.IManagementModel;
import com.doruk.dnotes.utils.DatabaseConnector;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ManagementModel implements IManagementModel {
    private Connection connection;

    public ManagementModel() {
        this.connection = DatabaseConnector.getConnection();
    }

    @Override
    public List<BrowserDto> getCollections() throws DataAccessException {
        List<BrowserDto> collections = new LinkedList<>();
        try (var stmt = connection.prepareStatement("select id, name from collectionView;")) {
            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    collections.add(new BrowserDto(
                            rs.getString("id"),
                            rs.getString("name"),
                            BrowserElement.Collection
                    ));
                }
            }
            return collections;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch collections", e);
        }
    }

    @Override
    public List<BrowserDto> getBooks(BrowserDto collection) throws DataAccessException {
        var books = new LinkedList<BrowserDto>();
        try (var stmt = connection.prepareStatement("select id, title from books where deleted is null and collectionId = ?;")) {
            stmt.setString(1, collection.getId());
            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(new BrowserDto(
                            rs.getString("id"),
                            rs.getString("title"),
                            BrowserElement.Book
                    ));
                }
            }
            return books;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch books", e);
        }
    }

    @Override
    public List<BrowserDto> getNotes(BrowserDto book) throws DataAccessException {
        var notes = new LinkedList<BrowserDto>();
        try (var stmt = connection.prepareStatement("select id, name from bookPageView where bookId = ?;")) {
            stmt.setString(1, book.getId());
            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notes.add(new BrowserDto(
                            rs.getString("id"),
                            rs.getString("name"),
                            BrowserElement.Note
                    ));
                }
            }
            return notes;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch notes", e);
        }
    }

    @Override
    public List<BrowserDto> getNotesByType(NoteType type) throws DataAccessException {
        if (type == NoteType.NORMAL)
            throw new IllegalArgumentException("Normal is not a valid note type");

        var notes = new LinkedList<BrowserDto>();

        var viewType = type == NoteType.QUICK ? "quickNoteView" : "sharedNoteView";
        try (var stmt = connection.prepareStatement("select id, name from " + viewType)) {
            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notes.add(new BrowserDto(
                            rs.getString("id"),
                            rs.getString("name"),
                            BrowserElement.Note
                    ));
                }
                return notes;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch notes", e);
        }
    }

    @Override
    public void moveNotesToBook(List<BrowserDto> notes, BrowserDto book) throws DataAccessException {
        var noteIds = notes.stream().map(BrowserDto::getId).toArray();
        try (var stmt = connection.prepareStatement("update bookPage set bookId = ? where id in ?;")) {
            var sqlArray = connection.createArrayOf("INTEGER", noteIds);
            stmt.setString(1, book.getId());
            stmt.setArray(2, sqlArray);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to move notes to the given book", e);
        }
    }

    @Override
    public void moveBooksToCollection(List<BrowserDto> books, BrowserDto collection) throws DataAccessException {
        var bookIds = books.stream().map(BrowserDto::getId).toArray();
        try (var stmt = connection.prepareStatement("update books set collectionId = ? where id in ?;")) {
            var sqlArray = connection.createArrayOf("INTEGER", bookIds);
            stmt.setString(1, collection.getId());
            stmt.setArray(2, sqlArray);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to move books to the given collection", e);
        }
    }
}

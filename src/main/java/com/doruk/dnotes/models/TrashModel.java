package com.doruk.dnotes.models;

import com.doruk.dnotes.dto.BrowserDto;
import com.doruk.dnotes.enums.BrowserElement;
import com.doruk.dnotes.exceptions.DataAccessException;
import com.doruk.dnotes.interfaces.ITrashModel;
import com.doruk.dnotes.utils.DatabaseConnector;
import com.doruk.dnotes.utils.PathUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TrashModel implements ITrashModel {
    private final Connection connection;

    public TrashModel() {
        this.connection = DatabaseConnector.getConnection();
    }

    private List<BrowserDto> getNotes(String query, String searchStr) throws DataAccessException {
        var list = new LinkedList<BrowserDto>();
        try (var stmt = connection.prepareStatement(query)) {
            if (searchStr != null)
                stmt.setString(1, "%" + searchStr + "%");
            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new BrowserDto(
                            rs.getString("id"),
                            rs.getString("name"),
                            BrowserElement.Note
                    ));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch deleted notes", e);
        }
    }

    private void cleanupCollections() throws DataAccessException {
        try (var stmt = connection.prepareStatement("delete from collections where deletedAt is not null and id in " +
                "(select c.id as id from collections c left join books b on c.id = b.collectionId where b.id is null);")) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to cleanup deleted collections", e);
        }
    }

    private void cleanupBooks() throws DataAccessException {
        try (var stmt = connection.prepareStatement("delete from books where deletedAt is not null and id in " +
                "(select b.id as id from books b left join bookPages bp on b.id = bp.bookId where bp.id is null);")) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to cleanup deleted books", e);
        }
    }

    private void deleteContentFile(String fileId) throws DataAccessException {
        var fullPath = PathUtils.getNoteFilename(fileId);
        try {
            Files.delete(Path.of(fullPath));
        } catch (IOException e) {
            throw new DataAccessException("Failed to delete content file", e);
        }
    }

    @Override
    public List<BrowserDto> searchNotes(String searchStr) throws DataAccessException {
        return this.getNotes("select id, name from bookPages where deletedAt is not null " +
                "and name like ? order by updatedAt desc;", searchStr);
    }

    @Override
    public List<BrowserDto> getDeletedNotes() throws DataAccessException {
        return this.getNotes("select id, name from bookPages where deletedAt is not null order by updatedAt desc;", null);
    }

    @Override
    public void deleteNotes(List<BrowserDto> notes) throws DataAccessException {
        var noteIds = notes.stream().map(BrowserDto::getId).collect(Collectors.joining(","));
        try (var stmt = connection.prepareStatement("delete from bookPages where id in (" + noteIds + ") returning content;");
            var rs = stmt.executeQuery();
        ) {
            while (rs.next()) {
                this.deleteContentFile(rs.getString("content"));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete note", e);
        }
    }

    @Override
    public void restoreNotes(List<BrowserDto> notes) throws DataAccessException {
        Set<String> distinct = new HashSet<>();

        var noteIds = notes.stream().map(BrowserDto::getId).collect(Collectors.joining(","));
        try (var stmt = connection.prepareStatement("update bookPages set deletedAt = null where id in (" + noteIds + ") returning bookId;");
            var rs = stmt.executeQuery();
        ) {
            while (rs.next())
                distinct.add(rs.getString("bookId"));

            // now update the books
            var bookIds = String.join(",", distinct);
            var bstmt = connection.prepareStatement("update books set deletedAt = null where id in (" + bookIds + ") returning collectionId;");

            var brs = bstmt.executeQuery();
            // clear the distinct
            distinct.clear();
            while (brs.next())
                distinct.add(brs.getString("collectionId"));

            // now update the collections
            var collectionIds = String.join(",", distinct);
            var cstmt = connection.prepareStatement("update collections set deletedAt = null where id in (" + collectionIds + ");");
            cstmt.executeUpdate();

            bstmt.close();
            brs.close();
            cstmt.close();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to restore notes", e);
        }
    }

    @Override
    public void cleanup() throws DataAccessException {
        this.cleanupBooks();
        this.cleanupCollections();
    }
}

package com.doruk.dnotes.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.doruk.dnotes.dto.CollectionDto;
import com.doruk.dnotes.dto.PaginationParams;
import com.doruk.dnotes.enums.SortBy;
import com.doruk.dnotes.enums.SortOrder;
import com.doruk.dnotes.exceptions.DataAccessException;
import com.doruk.dnotes.interfaces.IModel;
import com.doruk.dnotes.utils.DatabaseConnector;

public class CollectionModel implements IModel<CollectionDto> {
    private Connection connection;
    
    public CollectionModel() {
        this.connection = DatabaseConnector.getConnection();
    }

    private List<CollectionDto> getCollections(boolean deleted, PaginationParams params) {
        var sortBy = params.getSortBy().orElse(SortBy.Date);
        var sortOrder = params.getSortOrder().orElse(SortOrder.Descending);
        var search = params.getSearch().orElse("");

        // for deleted
        // SELECT * FROM collections WHERE deletedAt IS NOT NULL
        StringBuilder query = new StringBuilder("SELECT * FROM" + (deleted ? " collections" : " collectionView") + " where");

        if (!search.isEmpty())
            query.append(" name LIKE ?");

        if (deleted)
            query.append(search.isEmpty() ? "" : " and" + " deletedAt IS NOT NULL");
            
        
        query.append(" ORDER BY");
        query.append(sortBy == SortBy.Date ? " updatedAt" : " name");
        query.append(sortOrder == SortOrder.Descending ? " DESC" : " ASC");
                
        try {
            var stmt = connection.prepareStatement(query.toString());
            if (!search.isEmpty())
                stmt.setString(1, "%" + search + "%");
            var rs = stmt.executeQuery();

            // add to list
            List<CollectionDto> collections = new ArrayList<>();
            while (rs.next()) {
                collections.add(new CollectionDto(
                    String.valueOf(rs.getInt("id")),
                    rs.getString("name"),
                    rs.getDate("updatedAt").toString()
                ));
            }

            return collections;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to get all collections from database", e);
        }
    }
    
    @Override
    public CollectionDto add(CollectionDto collectionDto) {
        try {
            var stmt = connection.prepareStatement("INSERT INTO collections (name) VALUES (?) RETURNING id, updatedAt");
            stmt.setString(1, collectionDto.getName());
            var rs = stmt.executeQuery();

            // since only one row returned
            rs.next();

            return new CollectionDto(
                String.valueOf(rs.getInt("id")),
                collectionDto.getName(),
                rs.getDate("updatedAt").toString()
            );
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create new collection", e);
        }
    }

    @Override
    public CollectionDto update(CollectionDto collectionDto) {
        try {
            var stmt = connection.prepareStatement("UPDATE collections SET name = ?, updatedAt = CURRENT_TIMESTAMP WHERE id = ? RETURNING updatedAt");
            stmt.setString(1, collectionDto.getName());
            stmt.setString(2, collectionDto.getId());
            var rs = stmt.executeQuery();

            // since only one row returned
            rs.next();

            return new CollectionDto(
                collectionDto.getId(),
                collectionDto.getName(),
                rs.getDate("updatedAt").toString()
            );
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update collection", e);
        }
    }

    @Override
    public void delete(String id) {
        try {
            // also delete the child tables data i.e. books and bookPages
            var bookPages = connection.prepareStatement("DELETE FROM bookPages WHERE bookId IN (SELECT id FROM books WHERE collectionId = ?)");
            bookPages.setString(1, id);
            bookPages.executeUpdate();

            var books = connection.prepareStatement("DELETE FROM books WHERE collectionId = ?");
            books.setString(1, id);
            books.executeUpdate();

            var stmt = connection.prepareStatement("DELETE FROM collections WHERE id = ?");
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete collection", e);
        }
    }

    @Override
    public void softDelete(String id) {
        try {
            var stmt = connection.prepareStatement("UPDATE collections SET deletedAt = CURRENT_TIMESTAMP WHERE id = ?");
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete collection", e);
        }
    }

    @Override
    public List<CollectionDto> getAll(PaginationParams params) {
        return getCollections(false, params);
    }

    @Override
    public List<CollectionDto> getAllDeleted(PaginationParams params) {
        return getCollections(true, params);
    }

    @Override
    public CollectionDto restore(String id) {
        try {
            var stmt = connection.prepareStatement("UPDATE collections SET deletedAt = NULL WHERE id = ? RETURNING name, updatedAt");
            stmt.setString(1, id);
            var rs = stmt.executeQuery();

            // since only one row returned
            rs.next();

            return new CollectionDto(
                id,
                rs.getString("name"),
                rs.getDate("updatedAt").toString()
            );
        } catch (SQLException e) {
            throw new DataAccessException("Failed to restore collection", e);
        }
    }
}
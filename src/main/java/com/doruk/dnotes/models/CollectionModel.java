package com.doruk.dnotes.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.doruk.dnotes.dto.CollectionDto;
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
    
    @Override
    public CollectionDto add(CollectionDto collectionDto) {
        try {
            var stmt = connection.prepareStatement("INSERT INTO collections (name) VALUES (?)");
            stmt.setString(1, collectionDto.getName());
            stmt.executeUpdate();

            return collectionDto;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create new collection", e);
        }
    }

    @Override
    public CollectionDto update(CollectionDto collectionDto) {
        return null;
    }

    @Override
    public void delete(String id) {
    }

    @Override
    public List<CollectionDto> getAll(Optional<SortBy> sortBy, Optional<SortOrder> sortOrder) {
        return null;
    }
}
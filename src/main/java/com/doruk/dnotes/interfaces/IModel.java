package com.doruk.dnotes.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.doruk.dnotes.enums.SortBy;
import com.doruk.dnotes.enums.SortOrder;
import com.doruk.dnotes.exceptions.DataAccessException;

public interface IModel<T> {
    T add(T t) throws DataAccessException;
    T update(T t) throws DataAccessException;
    void delete(String id) throws DataAccessException;
    List<T> getAll(Optional<SortBy> sortBy, Optional<SortOrder> sortOrder) throws DataAccessException;
}

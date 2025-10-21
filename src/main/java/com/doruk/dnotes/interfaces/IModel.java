package com.doruk.dnotes.interfaces;

import java.util.List;

import com.doruk.dnotes.dto.PaginationParams;
import com.doruk.dnotes.exceptions.DataAccessException;

public interface IModel<T> {
    T add(T t) throws DataAccessException;
    T update(T t) throws DataAccessException;
    void softDelete(String id) throws DataAccessException;
    List<T> getAll(PaginationParams paginationParams) throws DataAccessException;
    IModel<T> ofParentId(String parentId);

    default T get(String id) throws DataAccessException {
        return null;
    }
}

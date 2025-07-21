package com.doruk.dnotes.interfaces;

import java.util.List;
import java.util.Optional;

import com.doruk.dnotes.enums.SortBy;
import com.doruk.dnotes.enums.SortOrder;

public interface IModel<T> {
    T add(T t);
    T update(T t);
    void delete(String id);
    List<T> getAll(Optional<SortBy> sortBy, Optional<SortOrder> sortOrder);
}

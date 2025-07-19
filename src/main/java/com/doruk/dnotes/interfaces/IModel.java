package com.doruk.dnotes.interfaces;

import java.util.List;

public interface IModel<T> {
    T add(T t);
    T update(T t);
    void delete(String id);
    List<T> getAll();
}

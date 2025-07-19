package com.doruk.dnotes.models;

import java.util.List;

import com.doruk.dnotes.dto.CollectionDto;
import com.doruk.dnotes.interfaces.IModel;

public class CollectionModel implements IModel<CollectionDto> {
    
    @Override
    public CollectionDto add(CollectionDto collectionDto) {
        return null;
    }

    @Override
    public CollectionDto update(CollectionDto collectionDto) {
        return null;
    }

    @Override
    public void delete(String id) {
    }

    @Override
    public List<CollectionDto> getAll() {
        return null;
    }
}

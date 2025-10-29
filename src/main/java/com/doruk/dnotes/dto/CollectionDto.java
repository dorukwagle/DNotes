package com.doruk.dnotes.dto;

import com.doruk.dnotes.interfaces.ISidebarItem;

public class CollectionDto implements ISidebarItem {
    private String name;
    private String id;
    private String updatedAt;

    public CollectionDto(String id, String name, String updatedAt) {
        this.name = name;
        this.id = id;
        this.updatedAt = updatedAt;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public Type getType() {
        return Type.COLLECTION;
    }
}

package com.doruk.dnotes.dto;

public class CollectionDto {
    private String name;
    private String id;
    private String updatedAt;

    public CollectionDto(String id, String name, String updatedAt) {
        this.name = name;
        this.id = id;
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}

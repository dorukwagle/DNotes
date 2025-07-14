package com.doruk.dnotes.dto;

public class CollectionDto {
    private String name;
    private String id;
    private String updatedDate;

    public CollectionDto(String id, String name, String updatedDate) {
        this.name = name;
        this.id = id;
        this.updatedDate = updatedDate;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }
}

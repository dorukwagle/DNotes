package com.doruk.dnotes.dto;

public class CollectionDto {
    private String name;
    private String collectionId;
    private String updatedDate;

    public CollectionDto(String collectionId, String name, String updatedDate) {
        this.name = name;
        this.collectionId = collectionId;
        this.updatedDate = updatedDate;
    }

    public String getName() {
        return name;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }
}

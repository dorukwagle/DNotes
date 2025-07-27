package com.doruk.dnotes.dto;

public class BookDto {
    private String title;
    private String preview;
    private String id;
    private String updatedAt;
    private String collectionId;
    private int pages;

    public BookDto(String id, String collectionId, String title, String updatedAt, String preview, int pages) {
        this.id = id;
        this.title = title;
        this.preview = preview;
        this.updatedAt = updatedAt;
        this.collectionId = collectionId;
        this.pages = pages;
    }

    public BookDto(String id, String collectionId, String title, String updatedAt) {
        this.id = id;
        this.title = title;
        this.updatedAt = updatedAt;
        this.collectionId = collectionId;
    }

    public String getTitle() {
        return title;
    }

    public String getPreview() {
        return preview;
    }

    public String getId() {
        return id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public int getPages() {
        return pages;
    }
}

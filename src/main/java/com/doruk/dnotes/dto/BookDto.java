package com.doruk.dnotes.dto;

public class BookDto {
    private String title;
    private String preview;
    private String id;
    private String updatedAt;

    public BookDto(String id, String title, String preview, String updatedAt) {
        this.id = id;
        this.title = title;
        this.preview = preview;
        this.updatedAt = updatedAt;
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
}

package com.doruk.dnotes.dto;

public class BookPageDto {
    private String id;
    private String bookId;
    private String name;
    private String content;
    private String updatedAt;

    public BookPageDto() {
    }

    public BookPageDto(String id, String bookId, String name, String content, String updatedAt) {
        this.id = id;
        this.bookId = bookId;
        this.name = name;
        this.content = content;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public String getBookId() {
        return bookId;
    }

    public String getContent() {
        return content;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getName() {
        return name;
    }

    public BookPageDto setId(String id) {
        this.id = id;
        return this;
    }
}

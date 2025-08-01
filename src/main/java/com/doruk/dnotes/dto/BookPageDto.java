package com.doruk.dnotes.dto;

import com.doruk.dnotes.interfaces.ISidebarItem;

public class BookPageDto implements ISidebarItem {
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

    @Override
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

    @Override
    public String getName() {
        return name;
    }

    public BookPageDto setId(String id) {
        this.id = id;
        return this;
    }
}

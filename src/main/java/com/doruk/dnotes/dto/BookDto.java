package com.doruk.dnotes.dto;

public class BookDto {
    private String title;
    private String preview;
    private String bookId;
    private String updatedDate;

    public BookDto(String bookId, String title, String preview, String updatedDate) {
        this.bookId = bookId;
        this.title = title;
        this.preview = preview;
        this.updatedDate = updatedDate;
    }

    public String getTitle() {
        return title;
    }

    public String getPreview() {
        return preview;
    }

    public String getBookId() {
        return bookId;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }
}

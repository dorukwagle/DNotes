package com.doruk.dnotes.store;

import java.util.Optional;

import com.doruk.dnotes.dto.BookDto;

public class BookStore {
    private static Optional<BookDto> selectedBook = Optional.empty();

    public static Optional<BookDto> getSelectedBook() {
        return selectedBook;
    }

    public static void setSelectedBook(BookDto selectedBook) {
        BookStore.selectedBook = Optional.ofNullable(selectedBook);
    }
}

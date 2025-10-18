package com.doruk.dnotes.store;

import java.util.Optional;

import com.doruk.dnotes.dto.BookDto;
import com.doruk.dnotes.enums.NoteType;

public class BookStore {
    private static Optional<BookDto> selectedBook = Optional.empty();
    private static boolean addNewPage = false;
    private static NoteType noteType = NoteType.NORMAL;

    public static Optional<BookDto> getSelectedBook() {
        return selectedBook;
    }

    public static void setSelectedBook(BookDto selectedBook) {
        BookStore.selectedBook = Optional.ofNullable(selectedBook);
    }

    public static NoteType getNoteType() {
        var tmp = noteType;
        // reset value
        noteType = NoteType.NORMAL;
        return tmp;
    }

    public static void setNoteType(NoteType noteType) {
        BookStore.noteType = noteType;
    }

    public static boolean isAddNewPage() {
        var tmp = addNewPage;
        // reset value then return
        addNewPage = false;
        return tmp;
    }

    public static void setAddNewPage(boolean addNewPage) {
        BookStore.addNewPage = addNewPage;
    }
}

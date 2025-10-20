package com.doruk.dnotes.interfaces;

import com.doruk.dnotes.dto.BrowserDto;
import com.doruk.dnotes.enums.NoteType;

import java.util.List;

public interface IManagementModel {
    List<BrowserDto> getCollections();

    List<BrowserDto> getBooks(BrowserDto collection);

    List<BrowserDto> getNotes(BrowserDto book);

    List<BrowserDto> getNotesByType(NoteType type);

    void moveNotesToBook(List<BrowserDto> notes, BrowserDto book);

    void moveBooksToCollection(List<BrowserDto> books, BrowserDto collection);
}

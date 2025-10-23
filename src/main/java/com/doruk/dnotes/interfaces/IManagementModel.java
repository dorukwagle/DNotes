package com.doruk.dnotes.interfaces;

import com.doruk.dnotes.dto.BrowserDto;
import com.doruk.dnotes.enums.NoteType;
import com.doruk.dnotes.exceptions.DataAccessException;

import java.util.List;

public interface IManagementModel {
    List<BrowserDto> getCollections() throws DataAccessException;

    List<BrowserDto> getBooks(BrowserDto collection) throws DataAccessException;

    List<BrowserDto> getNotes(BrowserDto book) throws DataAccessException;

    List<BrowserDto> getNotesByType(NoteType type) throws DataAccessException;

    void moveNotesToBook(List<BrowserDto> notes, BrowserDto book) throws DataAccessException;

    void moveBooksToCollection(List<BrowserDto> books, BrowserDto collection) throws DataAccessException;

    void passwordProtectNote(String noteId, String password) throws DataAccessException;

    void removePasswordProtection(String noteId) throws DataAccessException;
}

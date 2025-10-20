package com.doruk.dnotes.models;

import com.doruk.dnotes.dto.BrowserDto;
import com.doruk.dnotes.enums.NoteType;
import com.doruk.dnotes.interfaces.IManagementModel;
import com.doruk.dnotes.utils.DatabaseConnector;

import java.sql.Connection;
import java.util.List;

public class ManagementModel implements IManagementModel {
    private Connection connection;

    public ManagementModel() {
        this.connection = DatabaseConnector.getConnection();
    }

    @Override
    public List<BrowserDto> getCollections() {
        return null;
    }

    @Override
    public List<BrowserDto> getBooks(BrowserDto collection) {
        return null;
    }

    @Override
    public List<BrowserDto> getNotes(BrowserDto book) {
        return null;
    }

    @Override
    public List<BrowserDto> getNotesByType(NoteType type) {
        return null;
    }

    @Override
    public void moveNotesToBook(List<BrowserDto> notes, BrowserDto book) {

    }

    @Override
    public void moveBooksToCollection(List<BrowserDto> books, BrowserDto collection) {

    }
}

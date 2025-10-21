package com.doruk.dnotes.interfaces;

import com.doruk.dnotes.dto.BrowserDto;
import com.doruk.dnotes.exceptions.DataAccessException;

import java.util.List;

public interface ITrashModel {
    List<BrowserDto> searchNotes(String searchStr) throws DataAccessException;

    List<BrowserDto> getDeletedNotes() throws DataAccessException;

    void deleteNotes(List<BrowserDto> notes) throws DataAccessException;

    void restoreNotes(List<BrowserDto> notes) throws DataAccessException;

    void cleanup() throws DataAccessException;
}

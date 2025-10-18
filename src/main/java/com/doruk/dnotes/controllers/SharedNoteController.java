package com.doruk.dnotes.controllers;

import com.doruk.dnotes.enums.NoteType;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.store.BookStore;

public class SharedNoteController {
    public SharedNoteController(INavigationController nav) {
        BookStore.setNoteType(NoteType.SHARED);

        // navigate to books page
        nav.goToSharedNotePage();
    }
}

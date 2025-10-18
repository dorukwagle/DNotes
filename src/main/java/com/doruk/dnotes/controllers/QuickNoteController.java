package com.doruk.dnotes.controllers;

import com.doruk.dnotes.enums.NoteType;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.interfaces.IQuickNoteController;
import com.doruk.dnotes.store.BookStore;
import javafx.scene.Parent;

public class QuickNoteController implements IQuickNoteController {
    private final INavigationController nav;
    public QuickNoteController(INavigationController nav) {
        this.nav = nav;

        BookStore.setNoteType(NoteType.QUICK);
    }

    @Override
    public void addNew() {
        BookStore.setAddNewPage(true);
        nav.goToQuickNotePage();
    }

    @Override
    public void open() {
        BookStore.setAddNewPage(false);
        nav.goToQuickNotePage();
    }

    @Override
    public Parent getView() {
        return null;
    }
}

package com.doruk.dnotes.models;

import com.doruk.dnotes.enums.NoteType;

public class QuickBook extends NoteModel {

    @Override
    protected String getViewName() {
        return "quickNoteView";
    }

    @Override
    protected NoteType getNoteType() {
        return NoteType.QUICK;
    }
}

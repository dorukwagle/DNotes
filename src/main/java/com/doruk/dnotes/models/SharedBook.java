package com.doruk.dnotes.models;

import com.doruk.dnotes.enums.NoteType;

public class SharedBook extends NoteModel {
    @Override
    protected String getViewName() {
        return "sharedNoteView";
    }

    @Override
    protected NoteType getNoteType() {
        return NoteType.SHARED;
    }
}

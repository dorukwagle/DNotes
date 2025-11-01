package com.doruk.dnotes.store;

public class NoteStore {
    private static String noteContent;

    public synchronized static String getOpenedNote() {
        return noteContent;
    }

    public synchronized static void setOpenedNote(String noteContent) {
        NoteStore.noteContent = noteContent;
    }
}

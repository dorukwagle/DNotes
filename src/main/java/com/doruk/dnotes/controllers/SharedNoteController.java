package com.doruk.dnotes.controllers;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.dataUtils.readWrite.SharedReader;
import com.doruk.dnotes.dataUtils.readWrite.SharedWriter;
import com.doruk.dnotes.dto.BookPageDto;
import com.doruk.dnotes.enums.NoteType;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.store.BookStore;
import com.doruk.dnotes.store.GlobalConstants;
import com.doruk.dnotes.utils.PathUtils;
import javafx.stage.FileChooser;

public class SharedNoteController {
    public SharedNoteController(INavigationController nav) {
        BookStore.setNoteType(NoteType.SHARED);

        // update app title
        nav.updateAppTitle("Shared With Me");

        // navigate to books page
        nav.goToSharedNotePage();
    }

    // share the note
    public SharedNoteController(BookPageDto note) {
        var model = DIFactory.createPromptModal("Note Sharing", "Note: " + note.getName(), "Your Name:");
        var res = model.showAndWait();

        var sharedBy = (res.isEmpty() || res.get().trim().isEmpty()) ?
                GlobalConstants.APP_NAME :
                res.get();

        SharedWriter.write(note, sharedBy);

        DIFactory.createConfirmationModal("Note Shared", "File is written to: " + PathUtils.getShareDir())
                .showAndWait();
    }

    // import shared note, importNote is always true
    public SharedNoteController(INavigationController nav, boolean importNote) {
        var chooser = new FileChooser();

        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter( GlobalConstants.APP_NAME + " Shared Note", "*" + GlobalConstants.APP_FORMAT));

        var file = chooser.showOpenDialog(nav.getStage());
        if (file == null)
            return;

        var note = SharedReader.read(file);
        DIFactory.createSharedNoteModel().add(note);

        new SharedNoteController(nav);
    }
}

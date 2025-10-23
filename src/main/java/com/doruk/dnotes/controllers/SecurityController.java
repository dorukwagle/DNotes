package com.doruk.dnotes.controllers;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.dataUtils.SecurityWriter;
import com.doruk.dnotes.dto.BookPageDto;
import com.doruk.dnotes.exceptions.ProcessingStageException;
import com.doruk.dnotes.interfaces.IManagementModel;
import com.doruk.dnotes.utils.HashUtil;
import org.kordamp.ikonli.materialdesign2.MaterialDesignL;

import java.io.IOException;

public class SecurityController {
    private final IManagementModel model;

    public SecurityController(BookPageDto note) {
        this.model = DIFactory.createManagementModel();

//        var note = DIFactory.createNoteModel().get(dto.getId());

        var model = DIFactory.createPasswordPrompt(note.getIsLocked() ? "Password Protected!" : "Note Protection");
        model.setCancelButtonVisible(true);
        model.setConfirmationCheckboxVisible(false);

        model.setConfirmBtnText(note.getIsLocked() ? "Unlock" : "Protect");
        model.setConfirmBtnGraphics(note.getIsLocked() ? MaterialDesignL.LOCK_OPEN_ALERT : MaterialDesignL.LOCK_CHECK);

        if (!note.getIsLocked())
            model.enablePasswordConfirmation();

        model.showAndWait();

        model.setOnSubmitAction(() -> {
            var password = model.getPassword();
            if (password == null || password.isBlank()) {
                this.displayWarning("Invalid Input", "Password cannot be empty.");
                return;
            }

            // if it's locked, verify password
            if (note.getIsLocked()) {
                if (!HashUtil.compareHash(password, note.getPassword())) {
                    this.displayWarning("Failed", "Incorrect password.");
                    return;
                }
                this.removePasswordProtection(note, password);
            }
            // if not locked, protect it
            else this.passwordProtectNote(note, password);
        });
    }

    private void displayWarning(String title, String body) {
        var model = DIFactory.createConfirmationModal(title, body);
        model.showAndWait();
    }

    private void passwordProtectNote(BookPageDto note, String password) {
        var hashedPass = HashUtil.hash(password);
        this.model.passwordProtectNote(note.getId(), hashedPass);

        try {
            // call the security writer
            SecurityWriter.applyPasswordProtection(note.getContentId(), password);
        } catch (IOException e) {
            throw new ProcessingStageException("Unable to apply encryption to the file", e);
        }
    }

    private void removePasswordProtection(BookPageDto note, String curPassword) {
        this.model.removePasswordProtection(note.getId());

        try {
            SecurityWriter.removePasswordProtection(note.getContentId(), curPassword);
        } catch (IOException e) {
            throw new ProcessingStageException("Unable to remove encryption from the file", e);
        }
    }
}

package com.doruk.dnotes.views.components;

import com.doruk.dnotes.DIFactory;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;

public class LoadingSpinner {
    private final GenericModal modal;

    public LoadingSpinner() {
        var spinner = new ProgressIndicator();
        spinner.setMinHeight(200);
        spinner.setMinWidth(200);
        var parent = new HBox(spinner);
        parent.setAlignment(Pos.BASELINE_CENTER);
        this.modal = DIFactory.createGenericModal(parent, false, 200, 200);
    }

    public void show() {
        this.modal.show();
    }

    public void hide() {
        this.modal.close();
    }
}

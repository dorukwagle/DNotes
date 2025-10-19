package com.doruk.dnotes.views;

import com.doruk.dnotes.interfaces.IView;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class TrashView implements IView {
    private Parent view;

    public TrashView() {
        this.view = new Label("hello trash...");
    }

    @Override
    public Parent getView() {
        return this.view;
    }

    @Override
    public void setPlaceholder(String txt) {
        // no placeholder needed here
    }
}

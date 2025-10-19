package com.doruk.dnotes.views;

import com.doruk.dnotes.interfaces.IView;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class ManagementView implements IView {
    private Parent view;

    public ManagementView() {
        this.view = new Label("hello management...");
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

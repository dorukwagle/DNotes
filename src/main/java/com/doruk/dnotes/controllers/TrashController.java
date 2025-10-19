package com.doruk.dnotes.controllers;

import com.doruk.dnotes.interfaces.IController;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.interfaces.IView;
import com.doruk.dnotes.views.TrashView;
import javafx.scene.Parent;

public class TrashController implements IController {
    private INavigationController nav;
    private TrashView view;

    public TrashController(INavigationController nav, IView view) {
        this.nav = nav;
        this.view = (TrashView)view;

        nav.updateAppTitle("Trash");
    }

    @Override
    public Parent getView() {
        return this.view.getView();
    }
}

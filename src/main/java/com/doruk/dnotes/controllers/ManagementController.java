package com.doruk.dnotes.controllers;

import com.doruk.dnotes.interfaces.IController;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.interfaces.IView;
import com.doruk.dnotes.views.ManagementView;
import javafx.scene.Parent;

public class ManagementController implements IController {
    private INavigationController nav;
    private ManagementView view;

    public ManagementController(INavigationController nav, IView view) {
        this.nav = nav;
        this.view = (ManagementView)view;

        nav.updateAppTitle("Notes Management");
    }

    @Override
    public Parent getView() {
        return this.view.getView();
    }
}

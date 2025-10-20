package com.doruk.dnotes.controllers;

import com.doruk.dnotes.dto.BrowserDto;
import com.doruk.dnotes.enums.NoteType;
import com.doruk.dnotes.interfaces.IController;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.interfaces.IView;
import com.doruk.dnotes.views.ManagementView;
import javafx.scene.Parent;

import java.util.Stack;

public class ManagementController implements IController {
    private INavigationController nav;
    private ManagementView view;
    private final Stack<BrowserDto> sourceStack;
    private final Stack<BrowserDto> destinationStack;

    public ManagementController(INavigationController nav, IView view) {
        this.nav = nav;
        this.view = (ManagementView)view;
        this.sourceStack = new Stack<>();
        this.destinationStack = new Stack<>();

        nav.updateAppTitle("Notes Management");

        setupActions();
    }

    private void setupActions() {
        this.view.getCloseButton().setOnAction(_ -> this.nav.goToHomePage());
    }

    private void populateSource() {

    }

    private void populateDestination() {

    }

    private void populateSourceWith(NoteType type) {

    }

    @Override
    public Parent getView() {
        return this.view.getView();
    }
}

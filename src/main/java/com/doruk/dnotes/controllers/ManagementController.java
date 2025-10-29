package com.doruk.dnotes.controllers;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.dataUtils.SecurityWriter;
import com.doruk.dnotes.dto.BookPageDto;
import com.doruk.dnotes.dto.BrowserDto;
import com.doruk.dnotes.enums.BrowserElement;
import com.doruk.dnotes.enums.NoteType;
import com.doruk.dnotes.exceptions.ProcessingStageException;
import com.doruk.dnotes.interfaces.IController;
import com.doruk.dnotes.interfaces.IManagementModel;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.interfaces.IView;
import com.doruk.dnotes.utils.HashUtil;
import com.doruk.dnotes.views.ManagementView;
import com.doruk.dnotes.views.components.BrowserTable;
import javafx.scene.Parent;
import org.kordamp.ikonli.materialdesign2.MaterialDesignL;

import java.io.IOException;
import java.util.Stack;

public class ManagementController implements IController {
    private INavigationController nav;
    private ManagementView view;
    private final Stack<BrowserDto> sourceStack;
    private final Stack<BrowserDto> destinationStack;
    private final BrowserTable destinationTable;
    private final BrowserTable sourceTable;
    private final IManagementModel model;

    private enum Action {
        MoveBook,
        MoveNote,
        Invalid
    }

    public ManagementController(INavigationController nav, IView view) {
        this.nav = nav;
        this.view = (ManagementView) view;
        this.sourceStack = new Stack<>();
        this.destinationStack = new Stack<>();
        this.model = DIFactory.createManagementModel();

        this.sourceTable = this.view.getSourceTable();
        this.destinationTable = this.view.getDestinationTable();

        nav.updateAppTitle("Notes Management");

        setupActions();

        populateSource();
        populateDestination();
    }

    private void setupActions() {
        this.view.getCloseButton().setOnAction(_ -> this.nav.goToHomePage());

        this.view.getDestinationBackButton().setOnAction(_ -> {
            if (this.destinationStack.isEmpty())
                return;

            this.destinationStack.pop();
            this.populateDestination();
        });

        this.view.getSourceBackButton().setOnAction(_ -> {
            if (this.sourceStack.isEmpty())
                return;

            if (!this.view.getFilterComboBox().getValue().equals("None"))
                return;

            this.sourceStack.pop();
            this.populateSource();
        });

        this.view.getFilterComboBox().valueProperty().subscribe(value -> {
            if (value.equals("None")) {
                this.populateSource();
                return;
            }
            var noteType = NoteType.valueOf(value.toUpperCase());
            this.populateSourceWith(noteType);
        });

        this.sourceTable.setOnItemDoubleClick(item -> {
            if (item.getType() == BrowserElement.Note)
                return;

            this.sourceStack.push(item);
            this.populateSource();
        });

        this.destinationTable.setOnItemDoubleClick(item -> {
            if (item.getType() == BrowserElement.Note)
                return;

            this.destinationStack.push(item);
            this.populateDestination();
        });

        this.view.getMoveButton().setOnAction(_ -> this.move());
    }

    private Action getAction(BrowserElement source, BrowserElement destination) {
        var moveBookAction = source == BrowserElement.Book && destination == BrowserElement.Collection;
        var moveNoteAction = source == BrowserElement.Note && destination == BrowserElement.Book;

        return moveBookAction ? Action.MoveBook : moveNoteAction ? Action.MoveNote : Action.Invalid;
    }

    private void reloadState() {
        if (this.view.getFilterComboBox().getValue().equals("None")) {
            this.populateSource();
            return;
        }
        // populate specific type
        var noteType = NoteType.valueOf(this.view.getFilterComboBox().getValue().toUpperCase());
        this.populateSourceWith(noteType);

    }

    private void move() {
        var source = this.sourceTable.getSelectedItems();
        var destination = this.destinationTable.getSelectedItem();

        if (source.isEmpty() || destination == null) {
            this.view.displayError("Please select at least a source and a destination.");
            return;
        }

        var sourceType = source.getFirst().getType();
        var destinationType = destination.getType();

        var action = this.getAction(sourceType, destinationType);
        if (action == Action.Invalid) {
            this.view.displayError("Cannot perform this action. " +
                    "Source cannot be moved to the destination. Incompatible types. " +
                    "Please carefully read the manual.");
            return;
        }

        switch (action) {
            case MoveBook -> this.model.moveBooksToCollection(source, destination);
            case MoveNote -> this.model.moveNotesToBook(source, destination);
        }

        // reload the state
        this.reloadState();
    }

    private void populateContents(Stack<BrowserDto> stack, BrowserTable table) {
        if (stack.isEmpty()) {
            table.updateContents(this.model.getCollections());
            return;
        }

        var parent = stack.peek();
        if (parent.getType() == BrowserElement.Note)
            return;

        table.updateContents(
                parent.getType() == BrowserElement.Collection
                        ? this.model.getBooks(parent)
                        : this.model.getNotes(parent)
        );
    }

    private void populateSource() {
        this.populateContents(this.sourceStack, this.sourceTable);
    }

    private void populateDestination() {
        this.populateContents(this.destinationStack, this.destinationTable);
    }

    private void populateSourceWith(NoteType type) {
        this.sourceTable.updateContents(this.model.getNotesByType(type));
    }

    @Override
    public Parent getView() {
        return this.view.getView();
    }
}

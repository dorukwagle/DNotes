package com.doruk.dnotes.views;

import atlantafx.base.theme.Styles;
import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.interfaces.IView;
import com.doruk.dnotes.views.components.BackButton;
import com.doruk.dnotes.views.components.BrowserTable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignB;
import org.kordamp.ikonli.materialdesign2.MaterialDesignD;

public class TrashView implements IView {
    private VBox view;
    private FontIcon icon;
    private TextField searchBar;
    private Button closeButton;
    private BrowserTable notesTable;
    private Button restoreButton;
    private Button deleteButton;

    public TrashView() {
        this.view = new VBox(10);
        this.view.setStyle("-fx-padding: 10px;");
        this.view.setAlignment(Pos.TOP_CENTER);

        // create a nav bar
        var navBar = new BorderPane();
        navBar.getStyleClass().add("top-bar");
        navBar.setStyle(
                "-fx-background-color: -color-bg-subtle;" +
                        "-fx-padding: 10 20;" +
                        "-fx-border-color: -color-border-default;" +
                        "-fx-border-width: 0 0 1 0;"
        );
        this.view.getChildren().add(navBar);

        // create a search bar
        searchBar = new TextField();
        searchBar.setPromptText("Search...");
        searchBar.setMaxWidth(500);
        searchBar.setMinHeight(50);
        this.view.getChildren().add(searchBar);

        // create cross button
        closeButton = new BackButton();
        closeButton.setMinWidth(50);
        closeButton.setMinHeight(40);
        navBar.setRight(closeButton);

        // table body
        notesTable = new BrowserTable(true);
        notesTable.setMaxWidth(500);
        notesTable.setMinHeight(550);
        this.view.getChildren().add(notesTable);


        // buttons container
        var buttonsContainer = new HBox(10);
        buttonsContainer.setAlignment(Pos.CENTER);
        this.view.getChildren().add(buttonsContainer);

        // create a restore button
        restoreButton = new Button("Restore");
        restoreButton.getStyleClass().addAll(Styles.ROUNDED, Styles.LARGE, Styles.TEXT_BOLD, Styles.SUCCESS);
        restoreButton.setStyle(restoreButton.getStyle() + "-fx-font-size: 25px;");
        restoreButton.setPrefWidth(400);
        icon = new FontIcon(MaterialDesignB.BACKUP_RESTORE);
        icon.setScaleX(3);
        icon.setScaleY(3);
        restoreButton.setGraphic(icon);
        restoreButton.setContentDisplay(ContentDisplay.RIGHT);
        restoreButton.setGraphicTextGap(30);

        // create a delete button
        deleteButton = new Button();
        deleteButton.setPrefWidth(100);
        icon = new FontIcon(MaterialDesignD.DELETE_ALERT);
        icon.setScaleX(3);
        icon.setScaleY(3);
        deleteButton.setGraphic(icon);
        deleteButton.getStyleClass().addAll(Styles.ROUNDED, Styles.BUTTON_OUTLINED, Styles.LARGE, Styles.TEXT_BOLD, Styles.DANGER);
        deleteButton.setStyle(deleteButton.getStyle() + "-fx-font-size: 25px;");

        buttonsContainer.getChildren().addAll(restoreButton, deleteButton);
    }

    public TextField getSearchBar() {
        return searchBar;
    }

    public Button getCloseButton() {
        return closeButton;
    }

    public BrowserTable getNotesTable() {
        return notesTable;
    }

    public Button getRestoreButton() {
        return restoreButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void displayWarningAndRun(String title, String txt, Runnable action) {
        var model = DIFactory.createConfirmationModal(title, txt);
        model.setOnOk(action);
        model.setOnCancel(null);
        model.showAndWait();
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

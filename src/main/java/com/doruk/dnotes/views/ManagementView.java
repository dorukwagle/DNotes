package com.doruk.dnotes.views;

import atlantafx.base.theme.Styles;
import com.doruk.dnotes.interfaces.IView;
import com.doruk.dnotes.views.components.BackButton;
import com.doruk.dnotes.views.components.BrowserTable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;

public class ManagementView implements IView {
    private VBox view;
    private Button closeButton;
    private CheckBox selectionModeToggle;
    private BrowserTable source;
    private BrowserTable destination;
    private Button moveButton;
    private Button sourceBack;
    private Button destinationBack;
    private ComboBox<String> filterComboBox;
    private FontIcon icon;

    public ManagementView() {
        this.view = new VBox();
        this.view.setSpacing(10);

        var navBar = new BorderPane();
        navBar.getStyleClass().add("top-bar");
        navBar.setStyle(
                "-fx-background-color: -color-bg-subtle;" +
                        "-fx-padding: 10 20;" +
                        "-fx-border-color: -color-border-default;" +
                        "-fx-border-width: 0 0 1 0;"
        );
        this.view.getChildren().add(navBar);

        Label sourceLabel = new Label("Source");
        sourceLabel.getStyleClass().add("info-label");
        sourceLabel.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");
        sourceLabel.setPadding(new Insets(0, 0, 0, 100));
        navBar.setLeft(sourceLabel);

        var rightContainer = new HBox(100);

        Label destinationLabel = new Label("Destination");
        destinationLabel.getStyleClass().add("info-label");
        destinationLabel.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");
        rightContainer.getChildren().add(destinationLabel);

        // add cross button
        closeButton = new BackButton();
        closeButton.setMinWidth(50);
        rightContainer.getChildren().add(closeButton);

        // Add button to top right
        navBar.setRight(rightContainer);
        BorderPane.setAlignment(closeButton, Pos.CENTER_RIGHT);
        BorderPane.setMargin(closeButton, new Insets(0, 0, 0, 10));

        // selection mode toggle checkbox
        selectionModeToggle = new CheckBox("Enable Selection Mode");
        selectionModeToggle.setSelected(false);
        selectionModeToggle.setMinWidth(250);
        selectionModeToggle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        selectionModeToggle.setPadding(new Insets(0, 0, 0, 50));
        this.view.getChildren().add(selectionModeToggle);

        // main container
        var mainContainer = new BorderPane();
        mainContainer.setPadding(new Insets(10, 30, 30, 30));
        this.view.getChildren().add(mainContainer);

        // source container
        var sourceContainer = new VBox(10);
        mainContainer.setLeft(sourceContainer);

        source = new BrowserTable();
        source.setMinWidth(400);

        sourceBack = new Button();
        sourceBack.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.LARGE, Styles.ROUNDED, Styles.ACCENT);
        icon = new FontIcon(MaterialDesignA.ARROW_LEFT_CIRCLE_OUTLINE);
        icon.setScaleX(2);
        icon.setScaleY(2);
        sourceBack.setGraphic(icon);
        sourceBack.setGraphicTextGap(10);
        sourceBack.setContentDisplay(ContentDisplay.RIGHT);

        // back, label and combo container
        var subContainenr = new HBox(20);

        // label for combobox
        Label filterLabel = new Label("More:");
        filterLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // combobox for None/Quick/Shared filter
        filterComboBox = new ComboBox<String>();
        filterComboBox.getItems().addAll("None", "Quick", "Shared");
        filterComboBox.setValue("None");
        filterComboBox.setMinWidth(150);

        subContainenr.getChildren().addAll(sourceBack, filterLabel, filterComboBox);
        sourceContainer.getChildren().addAll(source, subContainenr);

        // center container for button and info
        var centerContainer = new VBox();
        centerContainer.setSpacing(10);
        centerContainer.setAlignment(Pos.CENTER);
        centerContainer.setMaxWidth(450);
        mainContainer.setCenter(centerContainer);

        var infoContainer = new VBox();
        infoContainer.setSpacing(10);
        infoContainer.setAlignment(Pos.CENTER_LEFT);
        infoContainer.setMaxWidth(450);
        centerContainer.getChildren().add(infoContainer);

        // info labels
        Label sourceInfo = new Label("1. Select one or more items from source (either books or notes at a time)");
        sourceInfo.setAlignment(Pos.CENTER_LEFT);
        sourceInfo.setWrapText(true);
        sourceInfo.setStyle("-fx-font-size: 16px;");
        infoContainer.getChildren().add(sourceInfo);

        Label destinationInfo = new Label("2. Open destination item (either collection or book)");
        destinationInfo.getStyleClass().add("info-label");
        destinationInfo.setStyle("-fx-font-size: 16px;");
        destinationInfo.setTextAlignment(TextAlignment.LEFT);
        destinationInfo.setWrapText(true);
        infoContainer.getChildren().add(destinationInfo);

        Label moveInfo = new Label("3. Click move button");
        moveInfo.getStyleClass().add("info-label");
        moveInfo.setStyle("-fx-font-size: 16px;");
        infoContainer.getChildren().add(moveInfo);

        Label moveInfo2 = new Label("Note: Collections cant be moved. Can only move Notes to Books, Books to Collections.");
        moveInfo2.getStyleClass().add("info-label");
        // set word wrap
        moveInfo2.setWrapText(true);
        moveInfo2.setStyle("-fx-font-size: 16px;");
        infoContainer.getChildren().add(moveInfo2);

        // move button
        moveButton = new Button("Move");
        moveButton.getStyleClass().addAll(Styles.BUTTON_CIRCLE, Styles.LARGE, Styles.SUCCESS, Styles.TEXT_BOLDER);
        moveButton.setPrefWidth(300);
        moveButton.setPrefHeight(60);
        moveButton.setStyle(moveButton.getStyle() + "-fx-font-size: 22px;");
        icon = new FontIcon(MaterialDesignA.ARROW_RIGHT_BOLD_BOX);
        icon.setScaleX(3);
        icon.setScaleY(3);
        moveButton.setGraphic(icon);
        moveButton.setGraphicTextGap(25);
        moveButton.setContentDisplay(ContentDisplay.RIGHT);
        centerContainer.getChildren().add(moveButton);

        // destination container
        var destinationContainer = new VBox(10);
        mainContainer.setRight(destinationContainer);

        destination = new BrowserTable();
        destination.setMinWidth(400);

        destinationBack = new Button();
        destinationBack.getStyleClass().addAll(Styles.BUTTON_CIRCLE, Styles.LARGE, Styles.ACCENT);
        icon = new FontIcon(MaterialDesignA.ARROW_LEFT_CIRCLE_OUTLINE);
        icon.setScaleX(2);
        icon.setScaleY(2);
        destinationBack.setGraphic(icon);
        destinationBack.setGraphicTextGap(10);
        destinationBack.setContentDisplay(ContentDisplay.RIGHT);

        destinationContainer.getChildren().addAll(destination, destinationBack);
    }

    @Override
    public Parent getView() {
        return this.view;
    }

    public BrowserTable getSourceTable() {
        return this.source;
    }

    public BrowserTable getDestinationTable() {
        return this.destination;
    }

    public Button getMoveButton() {
        return this.moveButton;
    }

    public Button getCloseButton() {
        return closeButton;
    }

    public CheckBox getSelectionModeToggle() {
        return selectionModeToggle;
    }

    public Button getSourceBackButton() {
        return sourceBack;
    }

    public Button getDestinationBackButton() {
        return destinationBack;
    }

    public ComboBox<String> getFilterComboBox() {
        return filterComboBox;
    }

    @Override
    public void setPlaceholder(String txt) {
        // no placeholder needed here
    }
}

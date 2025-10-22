package com.doruk.dnotes.views.components;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignL;
import atlantafx.base.theme.Styles;

public class PasswordPrompt {
    private final Stage dialog;
    private final PasswordField passwordField;
    private final CheckBox rememberCheckbox;
    private final Button submitButton;
    private Runnable onSubmitAction;

    public PasswordPrompt(String title) {
        // Create the dialog
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setTitle(title);

        // Create lock icon
        var lockIcon = new FontIcon(MaterialDesignL.LOCK_OUTLINE);
        lockIcon.setIconSize(48);
        lockIcon.getStyleClass().addAll(Styles.ACCENT);
        
        var iconContainer = new HBox(lockIcon);
        iconContainer.setAlignment(Pos.CENTER);
        
        // Create title label
        var titleLabel = new Label(title);
        titleLabel.getStyleClass().addAll(Styles.TITLE_2);
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setAlignment(Pos.CENTER);
        
        // Create password field
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.getStyleClass().addAll(Styles.TEXT, Styles.LARGE);
        passwordField.setMaxWidth(Double.MAX_VALUE);
        
        // Create remember me checkbox
        rememberCheckbox = new CheckBox("Remember password ?");
        rememberCheckbox.getStyleClass().add(Styles.TEXT);
        
        // Create submit button
        submitButton = new Button("Submit");
        submitButton.setDefaultButton(true);
        submitButton.getStyleClass().addAll(Styles.SUCCESS, Styles.MEDIUM);
        submitButton.setMaxWidth(Double.MAX_VALUE);
        
        // Set up button action
        submitButton.setOnAction(_ -> {
            if (onSubmitAction != null) {
                onSubmitAction.run();
            }
            close();
        });
        
        // Button container
        HBox buttonBox = new HBox(10, submitButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        
        // Main container
        VBox container = getContainer(iconContainer, titleLabel, buttonBox);

        // Create scene
        Scene scene = new Scene(container);
        dialog.setScene(scene);
        
        // Make sure the dialog can't be closed without submitting
        dialog.setOnCloseRequest(Event::consume);
    }

    private VBox getContainer(HBox iconContainer, Label titleLabel, HBox buttonBox) {
        VBox container = new VBox(15, iconContainer, titleLabel, passwordField, rememberCheckbox, buttonBox);
        container.setPadding(new Insets(25));
        container.setMinWidth(350);
        container.setMaxWidth(450);
        container.setStyle("""
            -fx-background-color: -color-bg-default;
            -fx-background-radius: 8px;
            -fx-border-color: -color-border-muted;
            -fx-border-radius: 8px;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);
            """);
        return container;
    }

    public void setOnSubmitAction(Runnable action) {
        this.onSubmitAction = action;
    }
    
    public String getPassword() {
        return passwordField.getText();
    }
    
    public boolean isRememberPassword() {
        return rememberCheckbox.isSelected();
    }
    
    public void showAndWait() {
        // Clear previous state
        passwordField.clear();
        rememberCheckbox.setSelected(false);
        
        // Show the dialog
        dialog.showAndWait();
        dialog.centerOnScreen();
    }
    
    private void close() {
        dialog.close();
    }
}

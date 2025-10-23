package com.doruk.dnotes.views.components;

import atlantafx.base.theme.Styles;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignE;
import org.kordamp.ikonli.materialdesign2.MaterialDesignS;

public class PasswordPrompt {
    private final Stage dialog;
    private final PasswordField passwordField;
    private final PasswordField confirmPasswordField;
    private final CheckBox rememberCheckbox;
    private final Button submitButton;
    private Runnable onSubmitAction;
    private final Button cancelButton;

    public PasswordPrompt(String title) {
        // Create the dialog
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setTitle(title);

        // Create lock icon
        var lockIcon = new FontIcon(MaterialDesignS.SECURITY);
        lockIcon.setScaleX(3);
        lockIcon.setScaleY(3);
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

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.getStyleClass().addAll(Styles.TEXT, Styles.LARGE);
        confirmPasswordField.setMaxWidth(Double.MAX_VALUE);
        confirmPasswordField.setVisible(false);
        confirmPasswordField.setManaged(false);
        
        // Create remember me checkbox
        rememberCheckbox = new CheckBox("Remember password ?");
        rememberCheckbox.getStyleClass().add(Styles.TEXT);
        
        // Create submit button
        submitButton = new Button("Submit");
        submitButton.setDefaultButton(true);
        submitButton.setStyle("-fx-font-size: 20px;");
        submitButton.getStyleClass().addAll(Styles.SUCCESS, Styles.LARGE, Styles.TEXT_BOLDER);
        submitButton.setMaxWidth(Double.MAX_VALUE);
        submitButton.setGraphicTextGap(15);
        submitButton.setContentDisplay(ContentDisplay.RIGHT);
        
        // Set up button action
        submitButton.setOnAction(_ -> {
            if (onSubmitAction != null) {
                onSubmitAction.run();
            }
            close();
        });

        // Create cancel button
        cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-font-size: 20px;");
        cancelButton.getStyleClass().addAll(Styles.DANGER, Styles.LARGE, Styles.TEXT_BOLDER);
        var icon = new FontIcon(MaterialDesignE.EXIT_TO_APP);
        icon.setScaleX(2);
        icon.setScaleY(2);
        cancelButton.setGraphic(icon);
        cancelButton.setGraphicTextGap(15);
        cancelButton.setContentDisplay(ContentDisplay.RIGHT);
        cancelButton.setMaxWidth(Double.MAX_VALUE);
        cancelButton.setVisible(false);
        cancelButton.setOnAction(_ -> close());
        
        // Button container
        HBox buttonBox = new HBox(10, submitButton, cancelButton);
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
        VBox container = new VBox(15, iconContainer, titleLabel, passwordField, confirmPasswordField, rememberCheckbox, buttonBox);
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

    public void setCancelButtonVisible(boolean enable) {
        cancelButton.setVisible(enable);
    }

    public void setConfirmationCheckboxVisible(boolean enable) {
        rememberCheckbox.setVisible(enable);
    }

    public void setConfirmBtnText(String text) {
        submitButton.setText(text);
    }

    public void setConfirmBtnGraphics(Ikon icon) {
        FontIcon fontIcon = new FontIcon(icon);
        fontIcon.setScaleY(2);
        fontIcon.setScaleX(2);
        submitButton.setGraphic(fontIcon);
    }

    public void enablePasswordConfirmation() {
        confirmPasswordField.setVisible(true);
        confirmPasswordField.setManaged(true);
        submitButton.setDisable(true);

        confirmPasswordField.textProperty().addListener((obs, oldVal, newVal) -> {
            var isTextEqual = newVal.equals(passwordField.getText());
            submitButton.setDisable(!isTextEqual);
        });
    }
    
    private void close() {
        dialog.close();
    }
}

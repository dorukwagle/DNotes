package com.doruk.dnotes.views.components;

import java.util.stream.Stream;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignD;
import org.kordamp.ikonli.materialdesign2.MaterialDesignI;

import com.doruk.dnotes.interfaces.IOptionsModal;

import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class OptionsModal implements IOptionsModal {
    private final Stage dialog;
    private final TextField inputField;
    private final CheckBox confirmCheckbox;
    private final Button deleteButton;
    private final Button updateButton;
    private final Button cancelButton;
    private Runnable onDeleteAction;
    private Runnable onUpdateAction;

    public OptionsModal() {
        // Create the dialog
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);

        // create alert section with icon (info icon)
        var alertIcon = new FontIcon(MaterialDesignI.INFORMATION_OUTLINE);
        alertIcon.setScaleX(5);
        alertIcon.setScaleY(5);
        HBox.setMargin(alertIcon, new Insets(0, 10, 0, 0));

        var alertLabel = new Label();
        alertLabel.setGraphic(alertIcon);
        alertLabel.setPrefHeight(64);
        alertLabel.setPrefWidth(64);
        alertLabel.getStyleClass().addAll(Styles.WARNING);

        var alertText = new Text("Are you sure ?");
        alertText.getStyleClass().add(Styles.TITLE_2);
        
        // Create a region to push the icon to the right
        var spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        var alertBox = new HBox(alertText, spacer, alertLabel);
        alertBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(alertBox, Priority.ALWAYS);
        
        // Create input field with AtlantaFX styling
        inputField = new TextField();
        inputField.setMaxWidth(Double.MAX_VALUE);
        inputField.getStyleClass().add(Styles.TITLE_4);
        
        // Create checkbox with AtlantaFX styling
        confirmCheckbox = new CheckBox("I confirm to delete this item");
        confirmCheckbox.getStyleClass().add(Styles.TEXT);
        
        // Create buttons with AtlantaFX styling
        deleteButton = new Button();
        var deleteIcon = new FontIcon(MaterialDesignD.DELETE);
        deleteIcon.setIconSize(24);
        deleteButton.setGraphic(deleteIcon);
        deleteButton.getStyleClass().addAll(Styles.DANGER, Styles.MEDIUM);
        
        updateButton = new Button("Update");
        updateButton.setDefaultButton(true);
        updateButton.getStyleClass().addAll(Styles.SUCCESS, Styles.MEDIUM);
        
        cancelButton = new Button("Cancel");
        cancelButton.setCancelButton(true);
        cancelButton.getStyleClass().addAll(Styles.ACCENT, Styles.MEDIUM);

        Stream.of(cancelButton, deleteButton, updateButton)
            .forEach(button -> button.setCursor(Cursor.HAND));
        
        deleteButton.setOnAction(_ -> {
            if (onDeleteAction != null) {
                onDeleteAction.run();
            }
            System.out.println("Delete button clicked");
            close();
        });
        
        updateButton.setOnAction(_ -> {
            if (onUpdateAction != null) {
                onUpdateAction.run();
            }
            System.out.println("Update button clicked");
            close();
        });
        
        cancelButton.setOnAction(_ -> close());
        
        // Button container
        HBox buttonBox = new HBox(10, deleteButton, new Region(), updateButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(buttonBox.getChildren().get(1), Priority.ALWAYS);
        
        // Main container with AtlantaFX styling
        VBox container = new VBox(15, alertBox, inputField, confirmCheckbox, buttonBox);
        container.setPadding(new Insets(20));
        container.setMinWidth(400);
        container.setMaxWidth(600);
        container.getStyleClass().add("modal-container");
        container.setStyle("""
            -fx-background-color: -color-bg-default;
            -fx-background-radius: 8px;
            -fx-border-color: -color-border-muted;
            -fx-border-radius: 8px;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);
            """);
        
        // Create a new scene
        Scene scene = new Scene(container);
        dialog.setScene(scene);
    }
    
    @Override
    public void setOnDeleteAction(Runnable action) {
        this.onDeleteAction = action;
    }
    
    @Override
    public void setOnUpdateAction(Runnable action) {
        this.onUpdateAction = action;
    }
    
    @Override
    public String getInputText() {
        return inputField.getText();
    }
    
    @Override
    public void setInputText(String text) {
        inputField.setText(text);
    }
    
    @Override
    public void showAndWait() {
        // Clear previous state
        inputField.clear();
        confirmCheckbox.setSelected(false);
        
        // Show the dialog
        dialog.showAndWait();
        dialog.centerOnScreen();
    }

    @Override
    public boolean isConfirmationChecked() {
        return confirmCheckbox.isSelected();
    }
    
    private void close() {
        dialog.close();
    }
}

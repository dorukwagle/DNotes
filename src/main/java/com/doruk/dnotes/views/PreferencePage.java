package com.doruk.dnotes.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignB;

import com.doruk.dnotes.interfaces.IPreferenceView;
import atlantafx.base.theme.Styles;

public class PreferencePage implements IPreferenceView {
    private BorderPane root;
    private Button backButton;
    private VBox contentBox;
    
    public PreferencePage() {
        // Main layout
        this.root = new BorderPane();
        
        // Top bar with back button
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: -color-bg-default;");
        
        this.backButton = new Button();
        var icon = new FontIcon(MaterialDesignB.BACKSPACE);
        icon.setIconSize(24);
        icon.setScaleX(1.2);
        backButton.setGraphic(icon);
        backButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_ICON, Styles.MEDIUM);
        backButton.setStyle(backButton.getStyle() + "-fx-padding: 8 12; -fx-cursor: hand;");
        
        topBar.getChildren().add(backButton);
        
        // Scrollable content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        BorderPane.setMargin(scrollPane, new Insets(15));
        
        contentBox = new VBox(30);
        contentBox.setPadding(new Insets(30, 50, 50, 50));
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setMaxWidth(800);
        contentBox.getStyleClass().add(Styles.ACCENT);
        
        // Section 1: Global Themes
        VBox themeSection = createSection("Global Themes", 
            "Cupertino Dark", "Cupertino Light", "Primer Dark", 
            "Primer Light", "Nord Dark", "Nord Light", "Dracula");
        
        // Section 2: Editor Color
        VBox editorSection = createSection("Editor Color", "Subtle", "Muted");
        
        VBox centerContainer = new VBox(30, themeSection, editorSection);
        centerContainer.setAlignment(Pos.TOP_CENTER);
        centerContainer.setMaxWidth(600);
        
        contentBox.getChildren().add(centerContainer);
        scrollPane.setContent(contentBox);
        
        // Center the content in the ScrollPane
        StackPane scrollContent = new StackPane(contentBox);
        scrollContent.setAlignment(Pos.TOP_CENTER);
        scrollPane.setContent(scrollContent);
        
        // Add components to root
        root.setTop(topBar);
        root.setCenter(scrollPane);
        root.setStyle(
            "-fx-background-color: -color-bg-subtle;" +
            "-fx-padding: 0;"
        );
    }
    
    private VBox createSection(String title, String... options) {
        VBox section = new VBox(15);
        section.setAlignment(Pos.TOP_LEFT);
        section.setMaxWidth(600);
        section.setStyle(
            "-fx-background-color: -color-bg-default;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        
        // Section header with background
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle(
            "-fx-background-color: -color-bg-subtle;" +
            "-fx-background-radius: 4;" +
            "-fx-padding: 10 15;" +
            "-fx-border-radius: 4;" +
            "-fx-border-color: -color-border-muted;" +
            "-fx-border-width: 1;"
        );
        
        Label titleLabel = new Label(title.toUpperCase());
        titleLabel.setStyle(
            "-fx-font-size: 1.5em;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: -color-fg-default;"
        );
        header.getChildren().add(titleLabel);
        
        // Options container with radio buttons
        VBox optionsBox = new VBox(10);
        optionsBox.setPadding(new Insets(10, 0, 0, 0));
        
        ToggleGroup toggleGroup = new ToggleGroup();
        
        for (String option : options) {
            RadioButton radioButton = new RadioButton(option);
            radioButton.setToggleGroup(toggleGroup);
            radioButton.setStyle(
                "-fx-font-size: 1.2em;" +
                "-fx-text-fill: -color-fg-default;"
            );
            optionsBox.getChildren().add(radioButton);
        }
        
        section.getChildren().addAll(header, optionsBox);
        return section;
    }

    @Override
    public Parent getView() {
        // Apply theme styles
        return this.root;
    }

    @Override
    public Button getBackButton() {
        return this.backButton;
    }
}

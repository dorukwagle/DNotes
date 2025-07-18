package com.doruk.dnotes.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import java.util.function.Consumer;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignB;

import com.doruk.dnotes.enums.EditorColor;
import com.doruk.dnotes.enums.Themes;
import com.doruk.dnotes.interfaces.IPreferenceView;
import atlantafx.base.theme.Styles;

public class PreferencePage implements IPreferenceView {
    private BorderPane root;
    private Button backButton;
    private VBox contentBox;
    private ToggleGroup themeToggleGroup;
    private ToggleGroup editorToggleGroup;
    private Consumer<Integer> themeOnSelect;
    private Consumer<Integer> editorColorOnSelect;
    
    
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
        
        // center container for all sections
        VBox centerContainer = new VBox(30);
        centerContainer.setAlignment(Pos.TOP_CENTER);
        centerContainer.setMaxWidth(600);

        // Section 1: Global Themes
        var themeSection = createSection(centerContainer, "Global Themes");
        themeToggleGroup = new ToggleGroup();
        
        for (var theme : Themes.values()) {
            RadioButton radioButton = new RadioButton(theme.name());
            radioButton.setToggleGroup(themeToggleGroup);
            radioButton.setUserData(theme.getId());
            radioButton.setStyle(
                "-fx-font-size: 1.2em;" +
                "-fx-text-fill: -color-fg-default;"
            );
            radioButton.setOnMouseClicked(_ -> themeOnSelect.accept(theme.getId()));
            themeSection.getChildren().add(radioButton);
        }
        
        // Section 2: Editor Color
        var editorSection = createSection(centerContainer, "Editor Color");
        editorToggleGroup = new ToggleGroup();
        
        for (var color : EditorColor.values()) {
            RadioButton radioButton = new RadioButton(color.name());
            radioButton.setToggleGroup(editorToggleGroup);
            radioButton.setUserData(color.getId());
            radioButton.setStyle(
                "-fx-font-size: 1.2em;" +
                "-fx-text-fill: -color-fg-default;"
            );
            radioButton.setOnMouseClicked(_ -> editorColorOnSelect.accept(color.getId()));
            editorSection.getChildren().add(radioButton);
        }
        
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
    
    private VBox createSection(VBox parent, String title) {
        parent.setSpacing(15);
        parent.setAlignment(Pos.TOP_LEFT);
        parent.setMaxWidth(600);
        parent.setStyle(
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
        
        parent.getChildren().addAll(header, optionsBox);
        return parent;
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

    @Override
    public void setThemeOnSelect(Consumer<Integer> themeOnSelect) {
        this.themeOnSelect = themeOnSelect;
    }

    @Override
    public void setEditorColorOnSelect(Consumer<Integer> editorColorOnSelect) {
        this.editorColorOnSelect = editorColorOnSelect;
    }

    @Override
    public void setSelectedTheme(Themes theme) {
        var toggle = this.themeToggleGroup.getToggles()
            .filtered(t -> t.getUserData().equals(theme.getId()))
            .get(0);
        this.themeToggleGroup.selectToggle(toggle);
    }

    @Override
    public void setSelectedEditorColor(EditorColor color) {
        var toggle = this.editorToggleGroup.getToggles()
            .filtered(t -> t.getUserData().equals(color.getId()))
            .get(0);
        this.editorToggleGroup.selectToggle(toggle);
    }

    @Override
    public void setPlaceholder(String txt) {
        // no placeholder neede here
    }
}

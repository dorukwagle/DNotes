package com.doruk.dnotes.views;

import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;
import org.kordamp.ikonli.materialdesign2.MaterialDesignB;
import org.kordamp.ikonli.materialdesign2.MaterialDesignN;


public class ContextView {
    private final VBox parent;

    public ContextView() {
        this.parent = new VBox(20);
        this.parent.setAlignment(Pos.CENTER);
        this.parent.setMaxWidth(800);
        this.parent.setPadding(new Insets(30));

        var infoLabel = new Label("Context View (Ctrl + K)");
        infoLabel.setStyle("-fx-font-size: 2em; -fx-font-weight: bold;");
        this.parent.getChildren().add(infoLabel);

        // First row of buttons
        HBox firstRow = createButtonRow(
                createActionButton("Collections", MaterialDesignB.BOOKSHELF, "Browse your saved notes and collections"),
                createActionButton("Shared With Me", MaterialDesignA.ACCOUNT_GROUP, "View notes shared with you")
        );

        // Second row of buttons
        HBox secondRow = createButtonRow(
                createActionButton("Add quick note", MaterialDesignN.NOTE_PLUS, "Create a new quick note", "Ctrl+Q"),
                createActionButton("View quick notes", MaterialDesignN.NOTEBOOK_CHECK, "View all your quick notes")
        );

        this.parent.getChildren().addAll(firstRow, secondRow);
    }

    private HBox createButtonRow(VBox... buttonContainers) {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER);

        for (VBox container : buttonContainers) {
            HBox.setHgrow(container, Priority.ALWAYS);
            container.setMaxWidth(Double.MAX_VALUE);
            row.getChildren().add(container);
        }

        return row;
    }

    private VBox createActionButton(String title, Ikon icon, String tooltipText) {
        return createActionButton(title, icon, tooltipText, null);
    }

    private VBox createActionButton(String title, Ikon icon, String tooltipText, String shortcut) {
        // Create the button
        Button button = new Button();
        FontIcon fontIcon = new FontIcon(icon);
        fontIcon.setScaleY(5);
        fontIcon.setScaleX(5);
        button.setGraphic(fontIcon);
        button.getStyleClass().addAll(Styles.BUTTON_OUTLINED, Styles.LARGE);

        // Create the label for the title
        VBox container = getContainer(title, button);

        // Set button styling
        String buttonStyle = """
            -fx-padding: 20;
            -fx-min-width: 190px;
            -fx-min-height: 95px;
            -fx-pref-width: 120px;
            -fx-pref-height: 120px;
            -fx-background-radius: 12;
            -fx-border-radius: 12;
            -fx-content-display: center;
            -fx-cursor: hand;
            -fx-alignment: center;
        """;

        button.setStyle(buttonStyle);

        // Add hover effect
        button.hoverProperty().addListener((_, _, isHovering) -> {
            if (isHovering) {
                button.setStyle(buttonStyle +
                        "-fx-background-color: -color-accent-subtle; " +
                        "-fx-border-color: -color-accent-emphasis;");
            } else {
                button.setStyle(buttonStyle +
                        "-fx-background-color: -color-bg-default; " +
                        "-fx-border-color: -color-border-muted;");
            }
        });

        // Add tooltip with description and shortcut
        String tooltipContent = tooltipText;
        if (shortcut != null) {
            tooltipContent += "\n\nShortcut: " + shortcut;
        }
        Tooltip tooltip = new Tooltip(tooltipContent);
        tooltip.setWrapText(true);
        tooltip.setMaxWidth(300);
        button.setTooltip(tooltip);

        // Set content display to graphic only
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        return container;
    }

    private static VBox getContainer(String title, Button button) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("""
            -fx-font-size: 1.2em;
            -fx-text-fill: -color-fg-default;
            -fx-padding: 5 0 0 0;
            -fx-alignment: center;
            -fx-text-alignment: center;
        """);

        // Create container for button and label
        VBox container = new VBox(5, button, titleLabel);
        container.setAlignment(Pos.CENTER);
        container.setStyle("-fx-padding: 5;");
        return container;
    }

    public Parent getView() {
        return parent;
    }
}

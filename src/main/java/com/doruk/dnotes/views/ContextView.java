package com.doruk.dnotes.views;

import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
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

    private HBox createButtonRow(Button... buttons) {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER);

        for (Button button : buttons) {
            HBox.setHgrow(button, Priority.ALWAYS);
            button.setMaxWidth(Double.MAX_VALUE);
            row.getChildren().add(button);
        }

        return row;
    }

    private Button createActionButton(String title, Ikon icon, String tooltipText) {
        return createActionButton(title, icon, tooltipText, null);
    }

    private Button createActionButton(String title, Ikon icon, String tooltipText, String shortcut) {
        Button button = new Button(title.toUpperCase());
        button.setGraphic(new FontIcon(icon));
        button.getStyleClass().addAll(Styles.BUTTON_OUTLINED, Styles.LARGE);
        button.setStyle("-fx-padding: 20 15; -fx-font-size: 1.1em; -fx-alignment: center-left;");

        // Add hover effect
        button.hoverProperty().addListener((obs, oldVal, isHovering) -> {
            if (isHovering) {
                button.setStyle(button.getStyle() +
                        "-fx-background-color: -color-accent-subtle; " +
                        "-fx-border-color: -color-accent-emphasis;");
            } else {
                button.setStyle(button.getStyle() +
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

        return button;
    }

    public Parent getView() {
        return parent;
    }
}

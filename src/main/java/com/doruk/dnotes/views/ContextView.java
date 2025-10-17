package com.doruk.dnotes.views;

import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
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
        Button button = new Button();
        FontIcon fontIcon = new FontIcon(icon);
        fontIcon.setScaleY(5);
        fontIcon.setScaleX(5);
        button.setGraphic(fontIcon);
        button.getStyleClass().addAll(Styles.BUTTON_OUTLINED, Styles.LARGE);

        // Set button styling
        String buttonStyle = """
            -fx-padding: 20;
            -fx-min-width: 120px;
            -fx-min-height: 120px;
            -fx-pref-width: 120px;
            -fx-pref-height: 120px;
            -fx-background-radius: 12;
            -fx-border-radius: 12;
            -fx-content-display: top;
            -fx-graphic-text-gap: 10;
            -fx-alignment: center;
            -fx-text-alignment: center;
            -fx-wrap-text: true;
            -fx-font-size: 0.9em;
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

        // Set content display to top to place icon above text
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        return button;
    }

    public Parent getView() {
        return parent;
    }
}

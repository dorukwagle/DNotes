package com.doruk.dnotes.MarkdownEditor;

import atlantafx.base.theme.Styles;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignF;
import org.kordamp.ikonli.materialdesign2.MaterialDesignL;

/**
 * Bold, Italic, Underline, Strikethrough
 * Headings H1 - H4
 * Lists Bullets, Number
 * Font Size, Color, Highlight ( font bg color )
 * Blockquotes
 * Links (clickable, styled )
 * Checkboxes (turn dim, when checked)
 */

public class ControlPanelView {
    private final HBox root;
    private ToggleButton boldButton;
    private ToggleButton italicButton;
    private ToggleButton underlineButton;
    private ToggleButton strikethroughButton;
    private ToggleButton h1Button;
    private ToggleButton h2Button;
    private ToggleButton h3Button;
    private ToggleButton h4Button;
    private ToggleButton bulletListButton;
    private ToggleButton numberListButton;
    private ToggleButton blockquoteButton;
    private ToggleButton linkButton;
    private ToggleButton checkboxButton;
    private ComboBox<String> fontSizeCombo;
    
    public ControlPanelView() {
        root = new HBox(8);
        root.setAlignment(Pos.CENTER_LEFT);
        root.setPadding(new Insets(4));
        root.getStyleClass().add("tool-bar");
        HBox.setHgrow(root, Priority.ALWAYS);
        
        // Text Formatting Group
        boldButton = createIconToggleButton(MaterialDesignF.FORMAT_BOLD, "Bold");
        italicButton = createIconToggleButton(MaterialDesignF.FORMAT_ITALIC, "Italic");
        underlineButton = createIconToggleButton(MaterialDesignF.FORMAT_UNDERLINE, "Underline");
        strikethroughButton = createIconToggleButton(MaterialDesignF.FORMAT_STRIKETHROUGH, "Strikethrough");
        
        // Headings Group
        h1Button = createIconToggleButton(MaterialDesignF.FORMAT_HEADER_1, "Heading 1");
        h2Button = createIconToggleButton(MaterialDesignF.FORMAT_HEADER_2, "Heading 2");
        h3Button = createIconToggleButton(MaterialDesignF.FORMAT_HEADER_3, "Heading 3");
        h4Button = createIconToggleButton(MaterialDesignF.FORMAT_HEADER_4, "Heading 4");
        
        // Lists Group
        bulletListButton = createIconToggleButton(MaterialDesignF.FORMAT_LIST_BULLETED, "Bullet List");
        numberListButton = createIconToggleButton(MaterialDesignF.FORMAT_LIST_NUMBERED, "Numbered List");
        
        // Other Formatting
        blockquoteButton = createIconToggleButton(MaterialDesignF.FORMAT_QUOTE_CLOSE, "Blockquote");
        linkButton = createIconToggleButton(MaterialDesignL.LINK, "Insert Link");
        checkboxButton = createIconToggleButton(MaterialDesignF.FORMAT_LIST_CHECKBOX, "Checkbox");
        
        // Font Size Dropdown
        fontSizeCombo = new ComboBox<>();
        fontSizeCombo.getItems().addAll("8", "10", "12", "14", "16", "18", "20", "24", "28", "32", "36", "48");
        fontSizeCombo.setValue("14");
        fontSizeCombo.getStyleClass().addAll(Styles.SMALL);
        fontSizeCombo.setPrefWidth(80);
        
        // Add button groups with separators
        addButtonGroup(
            boldButton, italicButton, underlineButton, strikethroughButton, blockquoteButton
        );
        
        addButtonGroup(
            h1Button, h2Button, h3Button, h4Button
        );
        
        addButtonGroup(
            bulletListButton, numberListButton, checkboxButton
        );
        
        addButtonGroup(
            linkButton
        );
        
        // Add font size dropdown with separator
        addSeparator();
        root.getChildren().add(fontSizeCombo);
    }
    
    private ToggleButton createIconToggleButton(Ikon icon, String tooltip) {
        ToggleButton button = new ToggleButton();
        FontIcon fontIcon = new FontIcon(icon);
        fontIcon.setIconSize(16);
        button.setGraphic(fontIcon);
        
        button.getStyleClass().addAll("tool-button");
        button.setTooltip(new Tooltip(tooltip));
        
        // Add hover and selected states using AtlantFX styles
        button.hoverProperty().addListener((_, _, isHovering) -> {
            if (isHovering) {
                button.pseudoClassStateChanged(javafx.css.PseudoClass.getPseudoClass("hover"), true);
            } else {
                button.pseudoClassStateChanged(javafx.css.PseudoClass.getPseudoClass("hover"), false);
            }
        });
        
        button.selectedProperty().addListener((_, _, isSelected) -> {
            button.pseudoClassStateChanged(javafx.css.PseudoClass.getPseudoClass("selected"), isSelected);
        });
        
        return button;
    }
    
    private void addButtonGroup(Node... nodes) {
        if (root.getChildren().size() > 0) {
            addSeparator();
        }
        
        HBox group = new HBox(4);
        group.setAlignment(Pos.CENTER);
        group.getChildren().addAll(nodes);
        root.getChildren().add(group);
    }
    
    private void addSeparator() {
        Separator separator = new Separator(javafx.geometry.Orientation.VERTICAL);
        separator.setPadding(new Insets(0, 4, 0, 4));
        root.getChildren().add(separator);
    }

    public Parent getView() {
        return root;
    }
}

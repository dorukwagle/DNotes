package com.doruk.dnotes.MarkdownEditor;

import atlantafx.base.theme.Styles;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.StackPane;

import java.util.List;

import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignB;
import org.kordamp.ikonli.materialdesign2.MaterialDesignF;

import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.store.GlobalConstants;

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
    private final FlowPane root;
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
    private ToggleButton checkboxButton;
    private ToggleButton alignCenterButton;
    private ComboBox<String> fontSizeCombo;
    private ColorPicker textColorPicker;
    private ColorPicker highlightColorPicker;
    private Button backButton;
    private ToggleButton textColorBtn;
    private ToggleButton highlightColorBtn;

    public ControlPanelView() {
        root = new FlowPane();
        root.setHgap(8);
        root.setVgap(8);
        root.setAlignment(Pos.CENTER_LEFT);
        root.setPadding(new Insets(4));
        root.getStyleClass().add("tool-bar");
        root.setStyle("-fx-background-color: -color-bg-subtle; -fx-background-radius: 10;");
        root.setPadding(new Insets(10));
        
        HBox.setHgrow(root, Priority.ALWAYS);
        
        // Text Formatting Group
        boldButton = createIconToggleButton(MaterialDesignF.FORMAT_BOLD, "Bold");
        boldButton.setId(ToolName.Bold.name());
        italicButton = createIconToggleButton(MaterialDesignF.FORMAT_ITALIC, "Italic");
        italicButton.setId(ToolName.Italic.name());
        underlineButton = createIconToggleButton(MaterialDesignF.FORMAT_UNDERLINE, "Underline");
        underlineButton.setId(ToolName.Underline.name());
        strikethroughButton = createIconToggleButton(MaterialDesignF.FORMAT_STRIKETHROUGH, "Strikethrough");
        strikethroughButton.setId(ToolName.Strikethrough.name());
        
        // Headings Group
        h1Button = createIconToggleButton(MaterialDesignF.FORMAT_HEADER_1, "Heading 1");
        h1Button.setId(ToolName.H1.name());
        h2Button = createIconToggleButton(MaterialDesignF.FORMAT_HEADER_2, "Heading 2");
        h2Button.setId(ToolName.H2.name());
        h3Button = createIconToggleButton(MaterialDesignF.FORMAT_HEADER_3, "Heading 3");
        h3Button.setId(ToolName.H3.name());
        h4Button = createIconToggleButton(MaterialDesignF.FORMAT_HEADER_4, "Heading 4");
        h4Button.setId(ToolName.H4.name());
        
        // Lists Group
        bulletListButton = createIconToggleButton(MaterialDesignF.FORMAT_LIST_BULLETED, "Bullet List");
        bulletListButton.setId(ToolName.BulletList.name());
        numberListButton = createIconToggleButton(MaterialDesignF.FORMAT_LIST_NUMBERED, "Numbered List");
        numberListButton.setId(ToolName.NumberList.name());

        // paragraph alignment
        alignCenterButton = createIconToggleButton(MaterialDesignF.FORMAT_ALIGN_CENTER, "Align Center");
        alignCenterButton.setId(ToolName.AlignCenter.name());
        
        // Other Formatting
        blockquoteButton = createIconToggleButton(MaterialDesignF.FORMAT_QUOTE_CLOSE, "Blockquote");
        blockquoteButton.setId(ToolName.Blockquote.name());
        checkboxButton = createIconToggleButton(MaterialDesignF.FORMAT_LIST_CHECKS, "Checkbox");
        checkboxButton.setId(ToolName.CheckList.name());
        
        // Font Size Dropdown
        fontSizeCombo = new ComboBox<>();
        fontSizeCombo.getItems().addAll("12", "14", "16", "18", "20", "24", "28", "32", "36");
        fontSizeCombo.setValue(String.valueOf(GlobalConstants.DEFAULT_FONT_SIZE));
        fontSizeCombo.getStyleClass().addAll(Styles.SMALL);
        fontSizeCombo.setPrefWidth(70);

        // CREATE COLOR PICKERS AND TOGGLES
        // Create a toggle button for text color
        textColorBtn = createIconToggleButton(MaterialDesignF.FORMAT_COLOR_TEXT, "Text Color");
        textColorBtn.setId(ToolName.FontColor.name());
        
        // Text Color Picker
        textColorPicker = new ColorPicker(Color.BLACK);
        textColorPicker.setScaleX(1.6);
        textColorPicker.setStyle("-fx-background-color: transparent; -fx-background-radius: 0; -fx-padding: 0; -fx-cursor: hand;");
        textColorPicker.setScaleY(1.6);
        textColorPicker.setPrefWidth(40);
        
        // Create a toggle button for highlight color
        highlightColorBtn = createIconToggleButton(MaterialDesignF.FORMAT_COLOR_FILL, "Highlight Color");
        highlightColorBtn.setId(ToolName.FontBG.name());   
             
        // Highlight Color Picker
        highlightColorPicker = new ColorPicker(Color.CYAN);
        highlightColorPicker.setStyle("-fx-background-color: transparent; -fx-background-radius: 0; -fx-padding: 0; -fx-cursor: hand;");
        highlightColorPicker.setScaleX(1.6);
        highlightColorPicker.setScaleY(1.6);
        highlightColorPicker.setPrefWidth(40);
        
        // Add button groups with separators
        addButtonGroup(
            boldButton, italicButton, underlineButton, strikethroughButton
        );
        
        addButtonGroup(
            h1Button, h2Button, h3Button, h4Button
        );

        addButtonGroup(
            blockquoteButton
        );
        
        // add align center button
        addButtonGroup(
            alignCenterButton
        );
            
        addButtonGroup(
            bulletListButton, numberListButton, checkboxButton
        );
            
        // Add color pickers
        addButtonGroup(
            textColorBtn, 
            createColorPicker(textColorPicker),
            highlightColorBtn, 
            createColorPicker(highlightColorPicker)
        );
        
        // Add font size dropdown with icon
        addSeparator();
        HBox fontSizeGroup = new HBox(4);
        fontSizeGroup.setAlignment(Pos.CENTER);
        
        FontIcon fontSizeIcon = new FontIcon(MaterialDesignF.FORMAT_SIZE);
        fontSizeIcon.setIconSize(16);
        fontSizeIcon.setScaleX(1.5);
        fontSizeIcon.setScaleY(1.5);
        Label fontSizeLabel = new Label();
        fontSizeLabel.setGraphic(fontSizeIcon);
        fontSizeLabel.setTooltip(new Tooltip("Font Size"));
        
        fontSizeCombo.getStyleClass().add("font-size-combo");
        
        fontSizeGroup.getChildren().addAll(fontSizeLabel, fontSizeCombo);
        root.getChildren().add(fontSizeGroup);

        // add back button
        backButton = new Button();
        backButton.setTooltip(new Tooltip("Back"));
        backButton.getStyleClass().addAll(Styles.MEDIUM, Styles.DANGER);
        backButton.setGraphic(new FontIcon(MaterialDesignB.BACKSPACE));
        backButton.setStyle(backButton.getStyle() + "-fx-cursor: hand;");
        backButton.setPrefWidth(40);
         
        addButtonGroup(backButton);
    }
    
    private ToggleButton createIconToggleButton(Ikon icon, String tooltip) {
        ToggleButton button = new ToggleButton();
        FontIcon fontIcon = new FontIcon(icon);
        fontIcon.setIconSize(16);
        fontIcon.setScaleX(1.3);
        fontIcon.setScaleY(1.3);
        button.setGraphic(fontIcon);
        
        button.getStyleClass().addAll("tool-button");
        button.setStyle(button.getStyle() + "-fx-cursor: hand;");
        button.setTooltip(new Tooltip(tooltip));
        
        return button;
    }

    private StackPane createColorPicker(ColorPicker picker) {
        picker.setStyle("-fx-opacity: 0;");
        Rectangle colorBox = new Rectangle(24, 24);
        colorBox.setArcWidth(4);
        colorBox.setArcHeight(4);
        colorBox.setStroke(Color.GRAY);
        colorBox.setFill(picker.getValue());

        // Bind fill to the color picker's value
        picker.valueProperty().addListener((_, _, newColor) -> {
            colorBox.setFill(newColor);
        });

        StackPane wrapper = new StackPane(colorBox, picker);
        wrapper.setStyle("-fx-cursor: hand;");
        return wrapper;
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

    public Button getBackButton() {
        return backButton;
    }

    public ColorPicker getHighColorPicker() {
        return this.highlightColorPicker;
    }

    public ColorPicker getTextColorPicker() {
        return this.textColorPicker;
    }

    public ComboBox<String> getFontSizeCombo() {
        return this.fontSizeCombo;
    }

    public List<ToggleButton> getStyleButtons() {
        return List.of(
            boldButton, 
            italicButton, 
            underlineButton, 
            strikethroughButton, 
            blockquoteButton,
            h1Button, 
            h2Button, 
            h3Button, 
            h4Button,
            bulletListButton, 
            numberListButton, 
            checkboxButton,
            alignCenterButton,
            textColorBtn,
            highlightColorBtn
        );
    }
}

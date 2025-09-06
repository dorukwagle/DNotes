package com.doruk.dnotes.MarkdownEditor;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.doruk.dnotes.MarkdownEditor.changeHandlers.CaretSelectionHandler;
import com.doruk.dnotes.MarkdownEditor.changeHandlers.CheckboxClickHandler;
import com.doruk.dnotes.MarkdownEditor.changeHandlers.FontBGColorHandler;
import com.doruk.dnotes.MarkdownEditor.changeHandlers.FontColorHandler;
import com.doruk.dnotes.MarkdownEditor.changeHandlers.FontSizeHandler;
import com.doruk.dnotes.MarkdownEditor.enums.EditorColor;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.IMarkdownEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.View;
import com.doruk.dnotes.MarkdownEditor.keyActionHandlers.BulletListKeyHandler;
import com.doruk.dnotes.MarkdownEditor.keyActionHandlers.KeyEventDispatcher;
import com.doruk.dnotes.MarkdownEditor.keyActionHandlers.NumberListKeyHandler;
import com.doruk.dnotes.MarkdownEditor.utils.StyleGroupRegistry;
import com.doruk.dnotes.store.GlobalConstants;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

public class MarkdownEditor implements IMarkdownEditor {

    private StringBuilder editorText;
    private View editorView;
    private static final Set<KeyCode> keyActions = Set.of(
        KeyCode.ENTER, 
        KeyCode.TAB, 
        KeyCode.BACK_SPACE
    );
    private static final Set<KeyCode> modifierKeyActions = Set.of(
        KeyCode.X,
        KeyCode.V
    );

    public MarkdownEditor() {
        editorText = new StringBuilder();
        editorView = new EditorWrapper();

        // load the fonts
        loadFonts();

        // initial setup
        initialSetup();

        initializeChangeHandlers();

        initializeKeyEventHandlers();
    }

    private boolean shouldHandleKeyAction(KeyEvent event, KeyCode action) {
        if (keyActions.contains(action))
            return true;
        
        if ((event.isControlDown() || event.isMetaDown()) && 
            modifierKeyActions.contains(action))
                return true;
        
        return false;
    }

    private void loadFonts() {
        Font.loadFont(getClass().getResourceAsStream("/fonts/magnolia_script_regular.otf"), 0);
    }

    private void initialSetup() {
        // reset colors to default
        this.editorView.getControlPanel()
                .getTextColorPicker()
                .setValue(GlobalConstants.DEFAULT_FONT_COLOR);
        this.editorView.getControlPanel()
                .getHighColorPicker()
                .setValue(GlobalConstants.DEFAULT_FONT_BG_COLOR);

        // loop over each buttons, then apply each tools
        this.editorView.getControlPanel()
                .getStyleButtons()
                .stream()
                .forEach(btn -> {
                    var tool = Factory.createTool(ToolName.fromName(btn.getId()), editorView.getEditor());
                    btn.setOnAction(_ -> {
                        var area = editorView.getEditor().getArea();
                        area.requestFocus();

                        if (btn.isSelected())
                            tool.apply(editorView.getEditor());
                        else
                            tool.unapply(editorView.getEditor());
                    });
                });
        
        // add event filter to resolve and unselect conflicting tools
        this.editorView.getControlPanel()
                .getStyleButtons()
                .stream()
                .forEach(btn -> {
                    btn.addEventFilter(MouseEvent.MOUSE_CLICKED, _ -> {
                        var toolName = ToolName.fromName(btn.getId());
                        var conflictingTools = StyleGroupRegistry.getConflictingTools(toolName);
                        conflictingTools.remove(toolName);
                        this.editorView.getControlPanel()
                            .getStyleButtons()
                            .stream()
                            .filter(toggle -> {
                                return conflictingTools.contains(ToolName.fromName(toggle.getId())) 
                                && toggle.isSelected();
                            })
                            .forEach(toggle -> toggle.setSelected(false));
                    });
                });

        editorView.getEditor().getArea().insertText(0, "hello ⚾world \n hi world{\u2028} 😄testing world {\r}brave world");
    }

    private void initializeChangeHandlers() {
        var panel = editorView.getControlPanel();

        // initialize the handlers
        new FontSizeHandler(editorView.getEditor(), panel);
        new FontColorHandler(editorView.getEditor(), panel);
        new FontBGColorHandler(editorView.getEditor(), panel);
        new CaretSelectionHandler(editorView.getEditor(), panel);
        new CheckboxClickHandler(editorView.getEditor());
    }

    private void initializeKeyEventHandlers() {
        KeyEventDispatcher.addHandler(new BulletListKeyHandler());
        KeyEventDispatcher.addHandler(new NumberListKeyHandler());

        this.editorView.getEditor().getArea()
            .addEventFilter(KeyEvent.KEY_PRESSED, event -> {                
                if (!this.shouldHandleKeyAction(event, event.getCode()))
                    return;
                
                KeyEventDispatcher.dispatch(editorView.getEditor(), event.getCode(), event);
            });
    }

    @Override
    public void setEditorBackground(EditorColor color) {
        editorView.setEditorBackground(color);
    }

    @Override
    public void setEditorText(StringBuilder builder) {
        editorText = builder;
    }

    @Override
    public StringBuilder getEditorText() {
        return editorText;
    }

    @Override
    public Parent getView() {
        return this.editorView.getView();
    }

    @Override
    public void setOnClose(Runnable onClose) {
        this.editorView.getCloseButton()
                .setOnAction(_ -> onClose.run());
    }
}

/**
 * MEDIATOR:
 * -> to update tools state with UI state change
 * -> to listen caret pos, selection, then check tools and update UI toggles
 * 
 * VISITOR PATTERN:
 * -> to handle keyboard events (Enter, Tab, Backspace)
 * and perform specific tasks for each active tools
 * 
 */

/**
 * FOR EMOJI SUPPORT IN LINUX
 * 
 * If you want, I can outline a dynamic FreeType rendering pipeline for RichTextFX that:
 * Takes Noto Color Emoji TTF
 * Rasterizes any requested emoji glyph on-demand
 * Inserts it as a NodeSegment into your editor
 * This way, you never have to ship thousands of static images, and it will work on Linux.
 */
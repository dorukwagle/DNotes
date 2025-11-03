package com.doruk.dnotes.MarkdownEditor;

import com.doruk.dnotes.MarkdownEditor.changeHandlers.*;
import com.doruk.dnotes.MarkdownEditor.codecs.dto.ParagraphNode;
import com.doruk.dnotes.MarkdownEditor.enums.EditorColor;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.IMarkdownEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.View;
import com.doruk.dnotes.MarkdownEditor.keyActionHandlers.BulletListKeyHandler;
import com.doruk.dnotes.MarkdownEditor.keyActionHandlers.CheckListKeyHandler;
import com.doruk.dnotes.MarkdownEditor.keyActionHandlers.KeyEventDispatcher;
import com.doruk.dnotes.MarkdownEditor.keyActionHandlers.NumberListKeyHandler;
import com.doruk.dnotes.MarkdownEditor.lists.ListManager;
import com.doruk.dnotes.MarkdownEditor.utils.StyleGroupRegistry;
import com.doruk.dnotes.MarkdownEditor.utils.StyleHelper;
import com.doruk.dnotes.store.GlobalConstants;
import javafx.application.Platform;
import javafx.collections.ObservableMap;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.*;
import javafx.scene.text.Font;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class MarkdownEditor implements IMarkdownEditor {

    private StringBuilder editorText;
    private View editorView;
    private final Set<KeyCode> keyActions = Set.of(
            KeyCode.ENTER,
            KeyCode.TAB,
            KeyCode.BACK_SPACE
    );
    private final Set<KeyCode> modifierKeyActions = Set.of(
            KeyCode.X,
            KeyCode.V
    );
    private CaretSelectionHandler caretSelectionHandler; // store for cleanup

    public MarkdownEditor() {
        editorText = new StringBuilder();
        editorView = new EditorWrapper();

        // load the fonts
        loadFonts();

        // initial setup
        initialSetup();

        initializeChangeHandlers();

        initializeKeyEventHandlers();

        // set default style
        this.editorView.getEditor().getArea()
                .setTextInsertionStyle(StyleHelper.defaultStyle());

        // bind shortcut keys to style buttons
        this.editorView.getControlPanel().getView()
                .sceneProperty()
                .subscribe(scene -> {
                    if (scene == null)
                        return;
                    this.bindShortcutKeys();
                });
    }

    private boolean shouldHandleKeyAction(KeyEvent event, KeyCode action) {
        if (keyActions.contains(action))
            return true;

        return (event.isControlDown() || event.isMetaDown()) &&
                modifierKeyActions.contains(action);
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
                .forEach(btn -> {
                    var tool = Factory.createTool(ToolName.fromName(btn.getId()), editorView.getEditor());
                    btn.addEventHandler(MouseEvent.MOUSE_CLICKED, _ -> {
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
                .forEach(btn ->
                        btn.addEventFilter(MouseEvent.MOUSE_CLICKED, _ -> {
                            var toolName = ToolName.fromName(btn.getId());
                            var conflictingTools = StyleGroupRegistry.getConflictingTools(toolName);
                            conflictingTools.remove(toolName);
                            this.editorView.getControlPanel()
                                    .getStyleButtons()
                                    .stream()
                                    .filter(toggle -> conflictingTools.contains(ToolName.fromName(toggle.getId()))
                                            && toggle.isSelected())
                                    .forEach(toggle -> toggle.setSelected(false));
                        }));
    }

    private void initializeChangeHandlers() {
        var panel = editorView.getControlPanel();

        // initialize the handlers
        new FontSizeHandler(editorView.getEditor(), panel);
        new FontColorHandler(editorView.getEditor(), panel);
        new FontBGColorHandler(editorView.getEditor(), panel);
        caretSelectionHandler = new CaretSelectionHandler(editorView.getEditor(), panel);
        new CheckboxClickHandler(editorView.getEditor());
    }

    private void initializeKeyEventHandlers() {
        KeyEventDispatcher.addHandler(new BulletListKeyHandler());
        KeyEventDispatcher.addHandler(new NumberListKeyHandler());
        KeyEventDispatcher.addHandler(new CheckListKeyHandler());

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

    // set, what to do when the red cleanup button is clicked in control panel
    @Override
    public void setOnClose(Runnable onClose) {
        this.editorView.getCloseButton()
                .setOnAction(_ -> onClose.run());
    }

    @Override
    public void close() {
        // cleanup caret selection handler
        caretSelectionHandler.cleanup();
        caretSelectionHandler = null;

        // clear the key handlers
        KeyEventDispatcher.clearHandlers();

        // clean up list manager
        ListManager.getInstance().cleanup();

        // also cleanup tools mediator
        ToolsMediator.cleanup();

        // cleanup the factory
        Factory.cleanup();

        this.editorView = null;
    }

    @Override
    public Enum<?>[] getCodecsValues() {
        return Factory.createCodecManager()
                .getCodecsValues();
    }

    @Override
    public Stream<ParagraphNode> encodeAndDump() {
        return Factory.createCodecManager()
                .dumpEditorDocument(editorView.getEditor());
    }

    @Override
    public void decodeAndLoad(ParagraphNode node) {
        Factory.createCodecManager()
                .loadEditorDocument(editorView.getEditor(), node);

        // since, cursor goes to the end, remove the focus, let user click and replace the cursor
        // take away the focus
        Platform.runLater(() -> this.editorView.getCloseButton().requestFocus());
    }

    @Override
    public void setDisabled(boolean disabled) {
        this.editorView.getEditor().getArea().setDisable(disabled);
        // also disable all the editor button panels, except the close button
        this.editorView.getControlPanel().getStyleButtons().forEach(btn -> btn.setDisable(disabled));

        // also disable color pickers, and combo boxes
        this.editorView.getControlPanel().getTextColorPicker().setDisable(disabled);
        this.editorView.getControlPanel().getHighColorPicker().setDisable(disabled);
        this.editorView.getControlPanel().getFontSizeCombo().setDisable(disabled);

        // enable the close button
        this.editorView.getControlPanel().getBackButton().setDisable(false);
    }

    private void bindToKeys(ObservableMap<KeyCombination, Runnable> map, KeyCode key, ToggleButton btn) {
        map.put(new KeyCodeCombination(key, KeyCombination.CONTROL_DOWN), () -> {
            btn.fire();
            btn.fireEvent(new MouseEvent(
                    MouseEvent.MOUSE_CLICKED,
                    0, 0, 0, 0,
                    MouseButton.PRIMARY, 1,
                    false, false, false, false,
                    true, false, false, false,
                    false, false, null
            ));
        });
    }

    private void bindShortcutKeys() {
        Map<ToolName, ToggleButton> btns = new EnumMap<>(ToolName.class);
        var btnSet = Set.of(
                ToolName.Bold.name(),
                ToolName.Italic.name(),
                ToolName.Underline.name(),
                ToolName.H1.name(),
                ToolName.H2.name(),
                ToolName.H3.name(),
                ToolName.H4.name()
        );

        this.editorView.getControlPanel().getStyleButtons().forEach(btn -> {
            if (btnSet.contains(btn.getId()))
                btns.put(ToolName.fromName(btn.getId()), btn);
        });

        var binding = this.editorView.getView().getScene()
                .getAccelerators();

        bindToKeys(binding, KeyCode.B, btns.get(ToolName.Bold));
        bindToKeys(binding, KeyCode.I, btns.get(ToolName.Italic));
        bindToKeys(binding, KeyCode.U, btns.get(ToolName.Underline));
        bindToKeys(binding, KeyCode.DIGIT1, btns.get(ToolName.H1));
        bindToKeys(binding, KeyCode.DIGIT2, btns.get(ToolName.H2));
        bindToKeys(binding, KeyCode.DIGIT3, btns.get(ToolName.H3));
        bindToKeys(binding, KeyCode.DIGIT4, btns.get(ToolName.H4));
    }
}
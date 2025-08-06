package com.doruk.dnotes.MarkdownEditor;

import java.util.EnumMap;
import java.util.Map;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.MarkdownEditor.enums.BtnFunction;
import com.doruk.dnotes.MarkdownEditor.interfaces.View;
import com.doruk.dnotes.enums.EditorColor;
import com.doruk.dnotes.enums.Preference;
import com.doruk.dnotes.interfaces.IMarkdownEditor;
import com.doruk.dnotes.interfaces.IPreference;

import javafx.scene.Parent;

public class MarkdownEditor implements IMarkdownEditor {
    
    private StringBuilder editorText;
    private View editorView;
    private IPreference preference;
    private Map<BtnFunction, Boolean> strategyState = new EnumMap<>(BtnFunction.class);

    public MarkdownEditor() {
        this.preference = DIFactory.createGlobalPreference();
        editorText = new StringBuilder();
        editorView = new EditorWrapper();

        strategyState.putAll(Map.of(
            BtnFunction.Bold, false,
            BtnFunction.Italic, false,
            BtnFunction.Underline, false,
            BtnFunction.H1, false,
            BtnFunction.H2, false,
            BtnFunction.H3, false,
            BtnFunction.H4, false,
            BtnFunction.Strikethrough, false,
            BtnFunction.AlignCenter, false,
            BtnFunction.Blockquote, false
        ));
        strategyState.putAll(Map.of(
            BtnFunction.Checkbox, false,
            BtnFunction.BulletList, false,
            BtnFunction.NumberList, false
        ));

        var selectedColor = preference.loadLong(Preference.EditorColor, 0);
        editorView.setEditorBackground(EditorColor.fromId((int) selectedColor));
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

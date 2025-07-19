package com.doruk.dnotes.MarkdownEditor;

import com.doruk.dnotes.DIFactory;
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

    public MarkdownEditor() {
        this.preference = DIFactory.createGlobalPreference();
        editorText = new StringBuilder();
        editorView = new EditorView();

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

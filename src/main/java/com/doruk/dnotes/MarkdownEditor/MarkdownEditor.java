package com.doruk.dnotes.MarkdownEditor;

import com.doruk.dnotes.interfaces.IMarkdownEditor;
import javafx.scene.Parent;

public class MarkdownEditor implements IMarkdownEditor {
    
    private StringBuilder editorText;
    private IEditorView editorView;

    public MarkdownEditor() {
        editorText = new StringBuilder();
        editorView = new EditorView();
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
            .setOnAction(event -> onClose.run());
    }
}

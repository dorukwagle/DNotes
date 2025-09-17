package com.doruk.dnotes.MarkdownEditor.interfaces;

public interface ICodecManager {
    void dumpEditorDocument(FXTextEditor editor);
    void loadEditorDocument(FXTextEditor editor);
    String[] getCodecsValues();
}

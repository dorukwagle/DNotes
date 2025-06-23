package com.doruk.dnotes.interfaces;

public interface IMarkdownEditor extends IController {
    void setEditorText(StringBuilder builder);
    StringBuilder getEditorText();
    void setOnClose(Runnable onClose);
}

package com.doruk.dnotes.MarkdownEditor.interfaces;

public abstract class StatefulParagraphStyleTool<T> extends ParagraphStyleTool {
    private T state;

    public StatefulParagraphStyleTool(FXTextEditor editor) {
        super(editor);
    }

    public void setState(T state) {
        this.state = state;
    }

    public T getState() {
        return state;
    }

}

package com.doruk.dnotes.MarkdownEditor.interfaces;

import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;

public abstract class StatefulTextStyleTool<T> extends TextStyleTool {
    
    public StatefulTextStyleTool(FXTextEditor editor) {
        super(editor);
    }

    private T state;
    
    public void setState(T state) {
        this.state = state;
    }
    
    public T getState() {
        return state;
    }

    @Override
    protected <U> boolean hasStyle(U style) {
        // stateful widgets are always applied with default value from the UI
        // however load the currently applied style to the state
        this.loadState((TextStyle)style);
        return true;
    }

    protected abstract void loadState(TextStyle style);
}

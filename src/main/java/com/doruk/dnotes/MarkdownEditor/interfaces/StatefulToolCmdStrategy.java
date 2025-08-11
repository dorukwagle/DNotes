package com.doruk.dnotes.MarkdownEditor.interfaces;

public abstract class StatefulToolCmdStrategy<T> extends ToolCmdStrategy {
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
        this.loadState(style);
        return true;
    }

    protected abstract <U> void loadState(U style);
}

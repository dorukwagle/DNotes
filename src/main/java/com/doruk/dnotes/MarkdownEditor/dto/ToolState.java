package com.doruk.dnotes.MarkdownEditor.dto;

public class ToolState {
    private int paragraphIndex;
    private int level;
    private int lineCount;

    public ToolState(int paragraphIndex, int level, int lineCount) {
        this.paragraphIndex = paragraphIndex;
        this.level = level;
        this.lineCount = lineCount;
    }

    public int getParagraphIndex() {
        return paragraphIndex;
    }

    public int getLevel() {
        return level;
    }

    public int getLineCount() {
        return lineCount;
    }
}

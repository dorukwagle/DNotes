package com.doruk.dnotes.MarkdownEditor.dto;

import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;

public class ParagraphListItemInfo {
    public final int paragraphIndex;
    public final int level;
    public final int lineCount;
    public final boolean isChecked;
    public final ParagraphType listType;
    private boolean apply;
    public final ParagraphStyle oldStyle;

    public ParagraphListItemInfo(ParagraphStyle oldStyle, int paragraphIndex, int level, int lineCount, boolean isChecked, ParagraphType listType) {
        this.paragraphIndex = paragraphIndex;
        this.level = level;
        this.lineCount = lineCount;
        this.isChecked = isChecked;
        this.listType = listType;
        this.oldStyle = oldStyle;
        this.apply = true;
    }

    public ParagraphListItemInfo(ParagraphStyle oldStyle, int paragraphIndex, ParagraphType listType) {
        this.paragraphIndex = paragraphIndex;
        this.level = 1;
        this.lineCount = 1;
        this.isChecked = false;
        this.listType = listType;
        this.oldStyle = oldStyle;
        this.apply = true;
    }

    public ParagraphListItemInfo(ParagraphStyle oldStyle, int paragraphIndex, boolean isChecked, ParagraphType listType) {
        this.paragraphIndex = paragraphIndex;
        this.level = 1;
        this.lineCount = 1;
        this.isChecked = isChecked;
        this.listType = listType;
        this.oldStyle = oldStyle;
        this.apply = true;
    }

    public ParagraphListItemInfo(ParagraphStyle oldStyle, int paragraphIndex, int level, int lineCount) {
        this.paragraphIndex = paragraphIndex;
        this.level = level;
        this.lineCount = lineCount;
        this.isChecked = false;
        this.listType = null;
        this.oldStyle = oldStyle;
        this.apply = true;
    }

    public ParagraphListItemInfo setApply(boolean apply) {
        this.apply = apply;
        return this;
    }

    public boolean getApply() {
        return this.apply;
    }
}

package com.doruk.dnotes.MarkdownEditor.docstyle;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.enums.StyleGroup;

public class ParagraphStyle {
    // hold only one paragraph style per group
    private Map<StyleGroup, ParagraphType> groupMap = new EnumMap<>(StyleGroup.class);

    // for list items
    public final int level; // indent level
    public final int lineCount; // number of lines in the list current indent
    public final String numberListId; // for number list items

    // for check lists
    public final boolean isItemChecked;

    // for lists, to add extra paragraph paddings
    public final int offset;

    public static final ParagraphStyle EMPTY = new ParagraphStyle();

    public ParagraphStyle() {
        this.level = 1;
        this.lineCount = 1;
        this.numberListId = null;
        this.isItemChecked = false;
        this.offset = 0;
    }

    public ParagraphStyle(int level, int lineCount, boolean isItemChecked) {
        this.level = level;
        this.lineCount = lineCount;
        this.isItemChecked = isItemChecked;
        this.numberListId = null;
        this.offset = 0;
    }

    public ParagraphStyle(int level, int lineCount, boolean isItemChecked, int offset) {
        this.level = level;
        this.lineCount = lineCount;
        this.isItemChecked = isItemChecked;
        this.numberListId = null;
        this.offset = offset;
    }
    
    public ParagraphStyle(int level, int lineCount, boolean isItemChecked, String numberListId, int offset) {
        this.level = level;
        this.lineCount = lineCount;
        this.isItemChecked = isItemChecked;
        this.numberListId = numberListId;
        this.offset = offset;
    }

    public Optional<ParagraphType> getStyle(StyleGroup group) {
        return Optional.ofNullable(groupMap.get(group));
    }

    public static ParagraphStyle newWithStyle(ParagraphStyle oldStyle, StyleGroup group, ParagraphType style) {
        var newStyle = new ParagraphStyle(oldStyle.level, oldStyle.lineCount, oldStyle.isItemChecked, oldStyle.numberListId, oldStyle.offset);
        newStyle.groupMap.putAll(oldStyle.groupMap);
        newStyle.groupMap.put(group, style);
        return newStyle;
    }

    public ParagraphStyle withStyle(ParagraphStyle oldStyle, StyleGroup group, ParagraphType style) {
        this.groupMap.putAll(oldStyle.groupMap);
        this.groupMap.put(group, style);
        return this;
    }

    public ParagraphStyle withStyles(EnumMap<StyleGroup, ParagraphType> groupMap) {
        this.groupMap = groupMap;
        return this;
    }
}


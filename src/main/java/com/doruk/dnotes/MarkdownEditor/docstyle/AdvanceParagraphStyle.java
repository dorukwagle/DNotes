package com.doruk.dnotes.MarkdownEditor.docstyle;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import com.doruk.dnotes.MarkdownEditor.enums.ListLabelType;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.enums.StyleGroup;

public class AdvanceParagraphStyle {
    // hold only one paragraph style per group
    private final Map<StyleGroup, ParagraphType> groupMap = new EnumMap<>(StyleGroup.class);

    // for list items
    public final int level; // indent level
    public final int lineCount; // number of lines in the list current indent

    // for check lists
    public final boolean isItemChecked;

    public static final AdvanceParagraphStyle EMPTY = new AdvanceParagraphStyle();

    public AdvanceParagraphStyle() {
        this.level = 0;
        this.lineCount = 0;
        this.isItemChecked = false;
    }

    public AdvanceParagraphStyle(int level, int lineCount, boolean isItemChecked) {
        this.level = level;
        this.lineCount = lineCount;
        this.isItemChecked = isItemChecked;
    }

    public Optional<ParagraphType> getStyle(StyleGroup group) {
        return Optional.ofNullable(groupMap.get(group));
    }

    public static AdvanceParagraphStyle newWithStyle(AdvanceParagraphStyle oldStyle, StyleGroup group, ParagraphType style) {
        var newStyle = new AdvanceParagraphStyle(oldStyle.level, oldStyle.lineCount, oldStyle.isItemChecked);
        newStyle.groupMap.putAll(oldStyle.groupMap);
        newStyle.groupMap.put(group, style);
        return newStyle;
    }

    public AdvanceParagraphStyle withStyle(AdvanceParagraphStyle oldStyle, StyleGroup group, ParagraphType style) {
        this.groupMap.putAll(oldStyle.groupMap);
        this.groupMap.put(group, style);
        return this;
    }
}


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
    public final ListLabelType labelType;
    public final int lineCount; // number of lines in the list current indent

    // for check lists
    public final boolean isItemChecked;

    public static final AdvanceParagraphStyle EMPTY = new AdvanceParagraphStyle();

    public AdvanceParagraphStyle() {
        this.groupMap.put(StyleGroup.Alignment, ParagraphType.ALIGN_LEFT);
        this.level = 0;
        this.labelType = ListLabelType.NULL;
        this.lineCount = 0;
        this.isItemChecked = false;
    }

    public Optional<ParagraphType> getStyle(StyleGroup group) {
        return Optional.ofNullable(groupMap.get(group));
    }
}

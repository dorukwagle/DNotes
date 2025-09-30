package com.doruk.dnotes.MarkdownEditor.codecs.dto;

import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.enums.StyleGroup;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class MutableParagraphStyle {
    // hold only one paragraph style per group
    private List<ParagraphType> paragraphTypes;

    // for list items
    public int level; // indent level
    public int lineCount; // number of lines in the list current indent
    public String numberListId; // for number list items

    // for check lists
    public boolean isItemChecked;

    // for lists, to add extra paragraph paddings
    public int offset;

    public static final ParagraphStyle EMPTY = new ParagraphStyle();

    public MutableParagraphStyle() {
        this.paragraphTypes = new ArrayList<>(20);
        this.level = 0;
        this.lineCount = 0;
        this.numberListId = null;
        this.isItemChecked = false;
        this.offset = 0;
    }

    public void addStyle(ParagraphType style) {
        this.paragraphTypes.add(style);
    }

    public List<ParagraphType> getStyles() {
        return this.paragraphTypes;
    }
}

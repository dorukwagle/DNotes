package com.doruk.dnotes.MarkdownEditor.docstyle;

import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;

public class ParagraphStyle {
    public final ParagraphType type;
    public final boolean checked;

    public static final ParagraphStyle EMPTY = new ParagraphStyle(ParagraphType.ALIGN_LEFT, false);

    public ParagraphStyle(ParagraphType type, boolean checked) {
        this.type = type;
        this.checked = checked;
    }

    public ParagraphType getType() {
        return type;
    }

    public boolean isChecked() {
        return checked;
    }
}

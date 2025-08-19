package com.doruk.dnotes.MarkdownEditor.utils;

import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;

public class ParagraphStyleHelper {
    public static ParagraphStyle withHeading1(ParagraphStyle oldStyle) {
        return ParagraphStyle.newWithStyle(oldStyle,
                StyleGroupRegistry.getGroup(ParagraphType.H1), ParagraphType.H1);
    }

    public static ParagraphStyle withHeading2(ParagraphStyle oldStyle) {
        return ParagraphStyle.newWithStyle(oldStyle,
                StyleGroupRegistry.getGroup(ParagraphType.H2), ParagraphType.H2);
    }

    public static ParagraphStyle withHeading3(ParagraphStyle oldStyle) {
        return ParagraphStyle.newWithStyle(oldStyle,
                StyleGroupRegistry.getGroup(ParagraphType.H3), ParagraphType.H3);
    }

    public static ParagraphStyle withHeading4(ParagraphStyle oldStyle) {
        return ParagraphStyle.newWithStyle(oldStyle,
                StyleGroupRegistry.getGroup(ParagraphType.H4), ParagraphType.H4);
    }

    public static ParagraphStyle withBlockquote(ParagraphStyle oldStyle) {
        return ParagraphStyle.newWithStyle(oldStyle,
                StyleGroupRegistry.getGroup(ParagraphType.BLOCKQUOTE), ParagraphType.BLOCKQUOTE);
    }

    public static ParagraphStyle withAlignCenter(ParagraphStyle oldStyle) {
        return ParagraphStyle.newWithStyle(oldStyle,
                StyleGroupRegistry.getGroup(ParagraphType.ALIGN_CENTER), ParagraphType.ALIGN_CENTER);
    }

    public static ParagraphStyle withAlignLeft(ParagraphStyle oldStyle) {
        return ParagraphStyle.newWithStyle(oldStyle,
                StyleGroupRegistry.getGroup(ParagraphType.ALIGN_LEFT), ParagraphType.ALIGN_LEFT);
    }

    public static ParagraphStyle withBulletList(ParagraphStyle oldStyle, int level, int lineCount) {
        return new ParagraphStyle(level, lineCount, oldStyle.isItemChecked)
                .withStyle(oldStyle, StyleGroupRegistry.getGroup(ParagraphType.BULLET_LIST_ITEM),
                        ParagraphType.BULLET_LIST_ITEM);
    }

    public static ParagraphStyle withNumberList(ParagraphStyle oldStyle, int level, int lineCount) {
        return new ParagraphStyle(level, lineCount, oldStyle.isItemChecked)
                .withStyle(oldStyle,
                        StyleGroupRegistry.getGroup(ParagraphType.NUMBER_LIST_ITEM), ParagraphType.NUMBER_LIST_ITEM);
    }

    public static ParagraphStyle withCheckList(ParagraphStyle oldStyle, boolean checked) {
        return new ParagraphStyle(oldStyle.level, oldStyle.lineCount, checked)
                .withStyle(oldStyle,
                        StyleGroupRegistry.getGroup(ParagraphType.CHECK_LIST_ITEM), ParagraphType.CHECK_LIST_ITEM);
    }
}

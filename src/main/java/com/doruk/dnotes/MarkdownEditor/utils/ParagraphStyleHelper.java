package com.doruk.dnotes.MarkdownEditor.utils;

import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;

public class ParagraphStyleHelper {
    public static ParagraphStyle withHeading1(ParagraphStyle oldStyle, boolean apply) {
        return ParagraphStyle.newWithStyle(oldStyle,
                StyleGroupRegistry.getGroup(ParagraphType.H1), apply ? ParagraphType.H1 : null);
    }

    public static ParagraphStyle withHeading2(ParagraphStyle oldStyle, boolean apply) {
        return ParagraphStyle.newWithStyle(oldStyle,
                StyleGroupRegistry.getGroup(ParagraphType.H2), apply ? ParagraphType.H2 : null);
    }

    public static ParagraphStyle withHeading3(ParagraphStyle oldStyle, boolean apply) {
        return ParagraphStyle.newWithStyle(oldStyle,
                StyleGroupRegistry.getGroup(ParagraphType.H3), apply ? ParagraphType.H3 : null);
    }

    public static ParagraphStyle withHeading4(ParagraphStyle oldStyle, boolean apply) {
        return ParagraphStyle.newWithStyle(oldStyle,
                StyleGroupRegistry.getGroup(ParagraphType.H4), apply ? ParagraphType.H4 : null);
    }

    public static ParagraphStyle withBlockquote(ParagraphStyle oldStyle, boolean apply) {
        return ParagraphStyle.newWithStyle(oldStyle,
                StyleGroupRegistry.getGroup(ParagraphType.BLOCKQUOTE), apply ? ParagraphType.BLOCKQUOTE : null);
    }

    public static ParagraphStyle withAlignCenter(ParagraphStyle oldStyle, boolean apply) {
        return ParagraphStyle.newWithStyle(oldStyle,
                StyleGroupRegistry.getGroup(ParagraphType.ALIGN_CENTER), apply ? ParagraphType.ALIGN_CENTER : null);
    }

    public static ParagraphStyle withAlignLeft(ParagraphStyle oldStyle, boolean apply) {
        return ParagraphStyle.newWithStyle(oldStyle,
                StyleGroupRegistry.getGroup(ParagraphType.ALIGN_LEFT), apply ? ParagraphType.ALIGN_LEFT : null);
    }

    public static ParagraphStyle withBulletList(ParagraphStyle oldStyle, int level, int lineCount, boolean apply) {
        return new ParagraphStyle(level, lineCount, oldStyle.isItemChecked)
                .withStyle(oldStyle, StyleGroupRegistry.getGroup(ParagraphType.BULLET_LIST_ITEM),
                        apply ? ParagraphType.BULLET_LIST_ITEM : null);
    }

    public static ParagraphStyle withNumberList(ParagraphStyle oldStyle, int level, int lineCount, boolean apply) {
        return new ParagraphStyle(level, lineCount, oldStyle.isItemChecked)
                .withStyle(oldStyle,
                        StyleGroupRegistry.getGroup(ParagraphType.NUMBER_LIST_ITEM), apply ? ParagraphType.NUMBER_LIST_ITEM : null);
    }

    public static ParagraphStyle withCheckList(ParagraphStyle oldStyle, boolean checked, boolean apply) {
        return new ParagraphStyle(oldStyle.level, oldStyle.lineCount, checked)
                .withStyle(oldStyle,
                        StyleGroupRegistry.getGroup(ParagraphType.CHECK_LIST_ITEM), apply ? ParagraphType.CHECK_LIST_ITEM : null);
    }
    
    public static ParagraphStyle withBulletList(ParagraphStyle oldStyle, int level, int lineCount, int offset, boolean apply) {
        return new ParagraphStyle(level, lineCount, oldStyle.isItemChecked, offset)
                .withStyle(oldStyle, StyleGroupRegistry.getGroup(ParagraphType.BULLET_LIST_ITEM),
                        apply ? ParagraphType.BULLET_LIST_ITEM : null);
    }

    public static ParagraphStyle withNumberList(ParagraphStyle oldStyle, int level, int lineCount, int offset, boolean apply) {
        return new ParagraphStyle(level, lineCount, oldStyle.isItemChecked, offset)
                .withStyle(oldStyle,
                        StyleGroupRegistry.getGroup(ParagraphType.NUMBER_LIST_ITEM), apply ? ParagraphType.NUMBER_LIST_ITEM : null);
    }

    public static ParagraphStyle withNumberList(ParagraphStyle oldStyle, int level, int lineCount, String numberListId, int offset, boolean apply) {
        return new ParagraphStyle(level, lineCount, oldStyle.isItemChecked, numberListId, offset)
                .withStyle(oldStyle,
                        StyleGroupRegistry.getGroup(ParagraphType.NUMBER_LIST_ITEM), apply ? ParagraphType.NUMBER_LIST_ITEM : null);
    }

    public static ParagraphStyle withCheckList(ParagraphStyle oldStyle, boolean checked, int offset, boolean apply) {
        return new ParagraphStyle(oldStyle.level, oldStyle.lineCount, checked, offset)
                .withStyle(oldStyle,
                        StyleGroupRegistry.getGroup(ParagraphType.CHECK_LIST_ITEM), apply ? ParagraphType.CHECK_LIST_ITEM : null);
    }
}

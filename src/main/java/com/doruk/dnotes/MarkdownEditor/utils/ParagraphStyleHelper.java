package com.doruk.dnotes.MarkdownEditor.utils;

import com.doruk.dnotes.MarkdownEditor.docstyle.AdvanceParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;

public class ParagraphStyleHelper {
    public static AdvanceParagraphStyle withDefault() {
        return AdvanceParagraphStyle.EMPTY;
    }

    public static AdvanceParagraphStyle withHeading1(AdvanceParagraphStyle oldStyle) {
        return AdvanceParagraphStyle.newWithStyle(oldStyle,
                StyleGroupRegistry.getGroup(ParagraphType.H1), ParagraphType.H1);
    }

    public static AdvanceParagraphStyle withHeading2(AdvanceParagraphStyle oldStyle) {
        return AdvanceParagraphStyle.newWithStyle(oldStyle,
                StyleGroupRegistry.getGroup(ParagraphType.H2), ParagraphType.H2);
    }

    public static AdvanceParagraphStyle withHeading3(AdvanceParagraphStyle oldStyle) {
        return AdvanceParagraphStyle.newWithStyle(oldStyle,
                StyleGroupRegistry.getGroup(ParagraphType.H3), ParagraphType.H3);
    }

    public static AdvanceParagraphStyle withHeading4(AdvanceParagraphStyle oldStyle) {
        return AdvanceParagraphStyle.newWithStyle(oldStyle,
                StyleGroupRegistry.getGroup(ParagraphType.H4), ParagraphType.H4);
    }

    public static AdvanceParagraphStyle withBlockquote(AdvanceParagraphStyle oldStyle) {
        return AdvanceParagraphStyle.newWithStyle(oldStyle,
                StyleGroupRegistry.getGroup(ParagraphType.BLOCKQUOTE), ParagraphType.BLOCKQUOTE);
    }

    public static AdvanceParagraphStyle withAlignCenter(AdvanceParagraphStyle oldStyle) {
        return AdvanceParagraphStyle.newWithStyle(oldStyle,
                StyleGroupRegistry.getGroup(ParagraphType.ALIGN_CENTER), ParagraphType.ALIGN_CENTER);
    }

    public static AdvanceParagraphStyle withBulletList(AdvanceParagraphStyle oldStyle, int level, int lineCount) {
        return new AdvanceParagraphStyle(level, lineCount, oldStyle.isItemChecked)
                .withStyle(oldStyle, StyleGroupRegistry.getGroup(ParagraphType.BULLET_LIST_ITEM),
                        ParagraphType.BULLET_LIST_ITEM);
    }

    public static AdvanceParagraphStyle withNumberList(AdvanceParagraphStyle oldStyle, int level, int lineCount) {
        return new AdvanceParagraphStyle(level, lineCount, oldStyle.isItemChecked)
                .withStyle(oldStyle,
                        StyleGroupRegistry.getGroup(ParagraphType.NUMBER_LIST_ITEM), ParagraphType.NUMBER_LIST_ITEM);
    }

    public static AdvanceParagraphStyle withCheckList(AdvanceParagraphStyle oldStyle, boolean checked) {
        return new AdvanceParagraphStyle(oldStyle.level, oldStyle.lineCount, checked)
                .withStyle(oldStyle,
                        StyleGroupRegistry.getGroup(ParagraphType.CHECK_LIST_ITEM), ParagraphType.CHECK_LIST_ITEM);
    }
}

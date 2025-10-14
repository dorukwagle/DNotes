package com.doruk.dnotes.MarkdownEditor.utils;

import com.doruk.dnotes.MarkdownEditor.codecs.dto.MutableParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.dto.ParagraphListItemInfo;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.enums.StyleGroup;

import java.util.EnumMap;
import java.util.Map;

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

    public static ParagraphStyle withListNode(ParagraphListItemInfo itemInfo, String listId, boolean apply) {
        return new ParagraphStyle(itemInfo.level, itemInfo.lineCount, itemInfo.isChecked, listId, itemInfo.oldStyle.offset)
                .withStyle(itemInfo.oldStyle,
                        StyleGroupRegistry.getGroup(itemInfo.listType), apply ? itemInfo.listType : null);
    }
    
    public static ParagraphStyle withListNode(ParagraphListItemInfo itemInfo, String listId, int offset, boolean apply) {
        return new ParagraphStyle(itemInfo.level, itemInfo.lineCount, itemInfo.isChecked, listId, offset)
                .withStyle(itemInfo.oldStyle,
                        StyleGroupRegistry.getGroup(itemInfo.listType), apply ? itemInfo.listType : null);
    }

    public static ParagraphStyle convertToParagraphStyle(MutableParagraphStyle mutableStyle) {
        EnumMap<StyleGroup, ParagraphType> groupMap = new EnumMap<>(StyleGroup.class);
        mutableStyle.getStyles().forEach(style -> groupMap.put(StyleGroupRegistry.getGroup(style), style));

        return new ParagraphStyle(mutableStyle.level, mutableStyle.lineCount, mutableStyle.isItemChecked, mutableStyle.numberListId, mutableStyle.offset)
                .withStyles(groupMap);
    }
}

package com.doruk.dnotes.MarkdownEditor.codecs.codec;

import com.doruk.dnotes.MarkdownEditor.codecs.dto.MutableParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.codecs.dto.ParagraphNode;
import com.doruk.dnotes.MarkdownEditor.codecs.enums.ParagraphModifiers;
import com.doruk.dnotes.MarkdownEditor.codecs.interfaces.Codec;
import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;

public class CheckListCodec extends Codec<ParagraphNode, ParagraphStyle, MutableParagraphStyle> {
    @Override
    public CodecType getCodecType() {
        return CodecType.ParagraphCodec;
    }

    @Override
    public void encode(ParagraphNode node, ParagraphStyle style) {
        if (!this.isApplied(style, ParagraphType.CHECK_LIST_ITEM))
            return;

        node.addGlobalStyle(ToolName.CheckList);

        // numberListId = list_(and numbers)
        int listId = Integer.parseInt(style.numberListId.split("_")[1]);

        // same list modifiers
        node.addModifier(ParagraphModifiers.NumberListId, listId);
        node.addModifier(ParagraphModifiers.Level, style.level);
        node.addModifier(ParagraphModifiers.LineCount, style.lineCount);
        node.addModifier(ParagraphModifiers.Offset, style.offset);

        // also save checked state
        node.addModifier(ParagraphModifiers.IsItemChecked, style.isItemChecked ? 1 : 0);
    }

    @Override
    public void decode(ParagraphNode node, MutableParagraphStyle style) {
        if (!node.getGlobalStyles().contains(ToolName.CheckList))
            return;

        style.addStyle(ParagraphType.CHECK_LIST_ITEM);

        // load the modifiers
        var modifiers = node.getModifiers();

        style.numberListId = "list_" + modifiers.get(ParagraphModifiers.NumberListId);
        style.level = modifiers.get(ParagraphModifiers.Level);
        style.lineCount = modifiers.get(ParagraphModifiers.LineCount);
        style.offset = modifiers.get(ParagraphModifiers.Offset);
        style.isItemChecked = modifiers.get(ParagraphModifiers.IsItemChecked) == 1;
    }
}

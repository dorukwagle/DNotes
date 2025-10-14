package com.doruk.dnotes.MarkdownEditor.codecs.codec;

import com.doruk.dnotes.MarkdownEditor.codecs.dto.MutableParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.codecs.dto.ParagraphNode;
import com.doruk.dnotes.MarkdownEditor.codecs.enums.ParagraphModifiers;
import com.doruk.dnotes.MarkdownEditor.codecs.interfaces.Codec;
import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;

public class NumberListCodec extends Codec<ParagraphNode, ParagraphStyle, MutableParagraphStyle> {
    @Override
    public CodecType getCodecType() {
        return CodecType.ParagraphCodec;
    }

    @Override
    public void encode(ParagraphNode node, ParagraphStyle style) {
        if (!this.isApplied(style, ParagraphType.NUMBER_LIST_ITEM))
            return;

        node.addGlobalStyle(ToolName.NumberList);

        // numberListId = list_(and numbers)
        int listId = Integer.parseInt(style.numberListId.split("_")[1]);

        // list modifiers
        node.addModifier(ParagraphModifiers.NumberListId, listId);
        node.addModifier(ParagraphModifiers.LineCount, style.lineCount);
        node.addModifier(ParagraphModifiers.Level, style.level);
        node.addModifier(ParagraphModifiers.Offset, style.offset);
    }

    @Override
    public void decode(ParagraphNode node, MutableParagraphStyle style) {
        if (!node.getGlobalStyles().contains(ToolName.NumberList))
            return;

        style.addStyle(ParagraphType.NUMBER_LIST_ITEM);

        // load modifiers
        var modifiers = node.getModifiers();

        // numberListId = list_(and numbers)
        style.numberListId = "list_" + modifiers.get(ParagraphModifiers.NumberListId);
        style.lineCount = modifiers.get(ParagraphModifiers.LineCount);
        style.level = modifiers.get(ParagraphModifiers.Level);
        style.offset = modifiers.get(ParagraphModifiers.Offset);
    }
}

package com.doruk.dnotes.MarkdownEditor.codecs.codec;

import com.doruk.dnotes.MarkdownEditor.codecs.dto.ParagraphNode;
import com.doruk.dnotes.MarkdownEditor.codecs.enums.ParagraphModifiers;
import com.doruk.dnotes.MarkdownEditor.codecs.interfaces.Codec;
import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;

public class BulletListCodec extends Codec<ParagraphNode, ParagraphStyle> {
    @Override
    public CodecType getCodecType() {
        return CodecType.ParagraphCodec;
    }

    @Override
    public void encode(ParagraphNode node, ParagraphStyle style) {
        if (!this.isApplied(style, ParagraphType.BULLET_LIST_ITEM))
            return;

        node.addGlobalStyle(ToolName.BulletList);

        // numberListId = list_(and numbers)
        long listId = Long.parseLong(style.numberListId.split("_")[1]);

        // same list modifiers
        node.addModifier(ParagraphModifiers.NumberListId, listId);
        node.addModifier(ParagraphModifiers.LineCount, style.lineCount);
        node.addModifier(ParagraphModifiers.Level, style.level);
        node.addModifier(ParagraphModifiers.Offset, style.offset);
    }
}

package com.doruk.dnotes.MarkdownEditor.codecs.codec;

import com.doruk.dnotes.MarkdownEditor.codecs.dto.MutableParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.codecs.dto.ParagraphNode;
import com.doruk.dnotes.MarkdownEditor.codecs.interfaces.Codec;
import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;

public class BlockquoteCodec extends Codec<ParagraphNode, ParagraphStyle, MutableParagraphStyle> {
    @Override
    public CodecType getCodecType() {
        return CodecType.ParagraphCodec;
    }

    @Override
    public void encode(ParagraphNode node, ParagraphStyle style) {
        if (!this.isApplied(style, ParagraphType.BLOCKQUOTE))
            return;

        node.addGlobalStyle(ToolName.Blockquote);
    }

    @Override
    public void decode(ParagraphNode node, MutableParagraphStyle style) {
        if (node.getGlobalStyles().contains(ToolName.Blockquote))
            style.addStyle(ParagraphType.BLOCKQUOTE);
    }
}

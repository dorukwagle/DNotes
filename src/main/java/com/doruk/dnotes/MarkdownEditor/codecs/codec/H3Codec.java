package com.doruk.dnotes.MarkdownEditor.codecs.codec;

import com.doruk.dnotes.MarkdownEditor.codecs.dto.MutableParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.codecs.dto.ParagraphNode;
import com.doruk.dnotes.MarkdownEditor.codecs.interfaces.Codec;
import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;

public class H3Codec extends Codec<ParagraphNode, ParagraphStyle, MutableParagraphStyle> {
    @Override
    public CodecType getCodecType() {
        return CodecType.ParagraphCodec;
    }

    @Override
    public void encode(ParagraphNode node, ParagraphStyle style) {
        if (!this.isApplied(style, ParagraphType.H3))
            return;

        node.addGlobalStyle(ToolName.H3);
    }

    @Override
    public void decode(ParagraphNode node, MutableParagraphStyle style) {
        if (!node.getGlobalStyles().contains(ToolName.H3))
            return;

        style.addStyle(ParagraphType.H3);
    }
}

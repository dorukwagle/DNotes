package com.doruk.dnotes.MarkdownEditor.codecs.codec;

import com.doruk.dnotes.MarkdownEditor.codecs.dto.MutableTextStyle;
import com.doruk.dnotes.MarkdownEditor.codecs.dto.SegmentNode;
import com.doruk.dnotes.MarkdownEditor.codecs.interfaces.Codec;
import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;

public class UnderlineCodec extends Codec<SegmentNode, TextStyle, MutableTextStyle> {
    @Override
    public CodecType getCodecType() {
        return CodecType.TextCodec;
    }

    @Override
    public void encode(SegmentNode node, TextStyle style) {
        if (!style.underline)
            return;

        node.addStyle(ToolName.Underline);
    }

    @Override
    public void decode(SegmentNode node, MutableTextStyle style) {
        if (!node.getStyles().contains(ToolName.Underline))
            return;

        style.underline = true;
    }
}

package com.doruk.dnotes.MarkdownEditor.codecs.codec;

import com.doruk.dnotes.MarkdownEditor.codecs.dto.SegmentNode;
import com.doruk.dnotes.MarkdownEditor.codecs.interfaces.Codec;
import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.utils.StyleHelper;
import com.doruk.dnotes.store.GlobalConstants;

public class FontColorCodec extends Codec<SegmentNode, TextStyle> {
    @Override
    public CodecType getCodecType() {
        return CodecType.TextCodec;
    }

    @Override
    public void encode(SegmentNode node, TextStyle style) {
        node.addStyle(ToolName.FontColor);
        var textColor = style.textColor == null ? GlobalConstants.DEFAULT_FONT_COLOR : style.textColor;
        node.addStateValue(ToolName.FontColor, StyleHelper.colorToInteger(textColor));
    }
}

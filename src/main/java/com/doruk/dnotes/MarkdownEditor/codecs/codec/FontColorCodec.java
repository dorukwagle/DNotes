package com.doruk.dnotes.MarkdownEditor.codecs.codec;

import com.doruk.dnotes.MarkdownEditor.codecs.dto.MutableTextStyle;
import com.doruk.dnotes.MarkdownEditor.codecs.dto.SegmentNode;
import com.doruk.dnotes.MarkdownEditor.codecs.interfaces.Codec;
import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.utils.StyleHelper;
import com.doruk.dnotes.store.GlobalConstants;

public class FontColorCodec extends Codec<SegmentNode, TextStyle, MutableTextStyle> {
    @Override
    public CodecType getCodecType() {
        return CodecType.TextCodec;
    }

    @Override
    public void encode(SegmentNode node, TextStyle style) {
        var textColor = style.textColor == null ? GlobalConstants.DEFAULT_FONT_COLOR : style.textColor;
        node.addStateValue(ToolName.FontColor, StyleHelper.colorToInteger(textColor));
    }

    @Override
    public void decode(SegmentNode node, MutableTextStyle style) {
        style.textColor = StyleHelper.colorFromInteger(node.getStateValues().get(ToolName.FontColor));
    }
}

package com.doruk.dnotes.MarkdownEditor.codecs.codec;

import com.doruk.dnotes.MarkdownEditor.codecs.dto.SegmentNode;
import com.doruk.dnotes.MarkdownEditor.codecs.interfaces.Codec;
import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.utils.StyleHelper;
import com.doruk.dnotes.store.GlobalConstants;


public class FontBGCodec extends Codec<SegmentNode, TextStyle> {
    @Override
    public CodecType getCodecType() {
        return CodecType.TextCodec;
    }

    @Override
    public void encode(SegmentNode node, TextStyle style) {
        node.addStyle(ToolName.FontBG);
        var bgColor = style.backgroundColor == null ? GlobalConstants.DEFAULT_FONT_BG_COLOR : style.backgroundColor;
        node.addStateValue(ToolName.FontBG, StyleHelper.colorToInteger(bgColor));
    }
}

package com.doruk.dnotes.MarkdownEditor.codecs.interfaces;

import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.utils.StyleGroupRegistry;

// N: Node type
// S: Style type
public abstract class Codec<N, S> {
    public enum CodecType {
        ParagraphCodec,
        TextCodec;
    }

    public abstract CodecType getCodecType();

    // only for paragraph codecs, text codecs may override it if needed
    protected boolean isApplied(S style, ParagraphType paragraphType) {
        if (!(style instanceof ParagraphStyle stl))
            return false;

        var appliedStyle = stl.getStyle(StyleGroupRegistry.getGroup(paragraphType));
        
        return appliedStyle.isPresent() && appliedStyle.get() == paragraphType;
    }

    public abstract void encode(N node, S style);
}

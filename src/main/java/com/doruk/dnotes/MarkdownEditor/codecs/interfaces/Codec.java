package com.doruk.dnotes.MarkdownEditor.codecs.interfaces;

// N: Node type
// S: Style type
public interface Codec<N, S> {
    public enum CodecType {
        ParagraphCodec,
        TextCodec;
    }

    public CodecType getCodecType();

    public void encode(N node, S style);
}

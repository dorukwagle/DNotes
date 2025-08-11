package com.doruk.dnotes.MarkdownEditor.interfaces;

public interface Renderer<N, S> {
    void render(N node, S style);
}

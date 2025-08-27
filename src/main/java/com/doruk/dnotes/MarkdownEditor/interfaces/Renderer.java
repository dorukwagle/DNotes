package com.doruk.dnotes.MarkdownEditor.interfaces;

import javafx.scene.Node;

public interface Renderer<N, S> {
    void render(N node, S style);

    default Node renderParagraphGraphic(S style, int index) {
        return null;
    }
}

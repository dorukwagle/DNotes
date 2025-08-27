package com.doruk.dnotes.MarkdownEditor;

import java.lang.classfile.Label;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.TextExt;
import org.fxmisc.richtext.model.SegmentOps;

import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextFlow;

public class EditorFX implements FXTextEditor {
    private final GenericStyledArea<ParagraphStyle, String, TextStyle> area;
    private final Map<ToolName, Renderer<TextExt, TextStyle>> textRenderers = new EnumMap<>(ToolName.class);
    private final Map<ToolName, Renderer<TextFlow, ParagraphStyle>> paragraphRenderers = new EnumMap<>(ToolName.class);

    public EditorFX() {
        area = new GenericStyledArea<>(
                ParagraphStyle.EMPTY,
                (flow, style) -> {
                    // apply all renderers
                    this.paragraphRenderers.forEach((_, renderer) -> renderer.render(flow, style));
                },
                TextStyle.EMPTY,
                SegmentOps.styledTextOps(),
                segment -> {
                    var text = new TextExt(segment.getSegment());
                    // apply all renderers
                    this.textRenderers.forEach((_, renderer) -> renderer.render(text, segment.getStyle()));
                    return text;
                });

        area.setParagraphGraphicFactory(index -> {
            var paragraph = area.getParagraph(index);
            var style = paragraph.getParagraphStyle();

            // Call all renderers that can render graphics
            Node graphic = this.paragraphRenderers.values().stream()
                .map(renderer -> renderer.renderParagraphGraphic(style))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

            // if (graphic != null) {
            //     StackPane wrapper = new StackPane(graphic);
            //     wrapper.setAlignment(Pos.BASELINE_CENTER); // align with baseline of text
            //     wrapper.setPadding(new Insets(0, 20, 5, 5));
            //     return wrapper;
            // }

            return graphic;
        });

        area.setWrapText(true);
        area.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        area.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        area.setStyle(area.getStyle() + "-fx-padding: 10px;");

        area.getStylesheets().add(getClass().getResource("/styles.scss").toExternalForm());
    }

    @Override
    public GenericStyledArea<ParagraphStyle, String, TextStyle> getArea() {
        return area;
    }

    @Override
    public void addTextRenderer(ToolName tool, Renderer<TextExt, TextStyle> renderer) {
        this.textRenderers.put(tool, renderer);
    }

    @Override
    public void addParagraphRenderer(ToolName tool, Renderer<TextFlow, ParagraphStyle> renderer) {
        this.paragraphRenderers.put(tool, renderer);
    }

    @Override
    public void removeRenderer(ToolName tool) {
        var map = this.textRenderers.containsKey(tool) ? this.textRenderers : this.paragraphRenderers;
        map.remove(tool);
    }
}

package com.doruk.dnotes.MarkdownEditor;

import java.util.EnumMap;
import java.util.Map;

import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.TextExt;
import org.fxmisc.richtext.model.SegmentOps;

import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;

import javafx.scene.text.TextFlow;

public class EditorFX implements FXTextEditor {
    private final GenericStyledArea<ParagraphStyle, String, TextStyle> area;
    private final Map<ToolName, Renderer<TextExt>> textRenderers = new EnumMap<>(ToolName.class);
    private final Map<ToolName, Renderer<TextFlow>> paragraphRenderers = new EnumMap<>(ToolName.class);

    public EditorFX() {       
        area = new GenericStyledArea<>(
                ParagraphStyle.EMPTY,
                (flow, _) -> {
                    // set global styles
                    flow.setStyle("-fx-padding: 8px;");
                    // apply all renderers
                    this.paragraphRenderers.forEach((_, renderer) -> renderer.render(flow));
                },
                TextStyle.EMPTY,
                SegmentOps.styledTextOps(),
                segment -> {
                    var text = new TextExt(segment.getSegment());
                    // apply all renderers
                    this.textRenderers.forEach((_, renderer) -> renderer.render(text));
                    return text;
                });

        area.setWrapText(true);
        area.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        area.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        // area.getStyleClass().add(Styles.ACCENT);
        area.getStylesheets().add(getClass().getResource("/styles.scss").toExternalForm());
    }

    @Override
    public GenericStyledArea<ParagraphStyle, String, TextStyle> getArea() {
        return area;
    }

    @Override
    public void addTextRenderer(ToolName tool, Renderer<TextExt> renderer) {
        this.textRenderers.put(tool, renderer);
    }

    @Override
    public void addParagraphRenderer(ToolName tool, Renderer<TextFlow> renderer) {
        this.paragraphRenderers.put(tool, renderer);
    }

    @Override
    public void removeRenderer(ToolName tool) {
        var map = this.textRenderers.containsKey(tool) ?
            this.textRenderers :
            this.paragraphRenderers;
        map.remove(tool);
    }
}

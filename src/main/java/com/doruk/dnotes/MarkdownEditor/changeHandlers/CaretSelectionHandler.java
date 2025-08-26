package com.doruk.dnotes.MarkdownEditor.changeHandlers;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.fxmisc.richtext.GenericStyledArea;
import org.reactfx.Change;
import org.reactfx.EventStreams;
import org.reactfx.util.Tuples;

import com.doruk.dnotes.MarkdownEditor.ControlPanelView;
import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.StatefulTextStyleTool;
import com.doruk.dnotes.MarkdownEditor.interfaces.ToolCmdStrategy;
import com.doruk.dnotes.store.GlobalConstants;

import javafx.application.Platform;
import javafx.scene.control.IndexRange;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class CaretSelectionHandler {
    private GenericStyledArea<ParagraphStyle, String, TextStyle> area;
    private ControlPanelView controlPanel;
    private FXTextEditor editor;
    private AtomicInteger caretChangeCount = new AtomicInteger(0);
    private static final Duration DELAY = Duration.ofMillis(300);

    public CaretSelectionHandler(FXTextEditor editor, ControlPanelView controlPanel) {
        this.area = editor.getArea();
        this.controlPanel = controlPanel;
        this.editor = editor;

        var caretChange = EventStreams.changesOf(area.caretPositionProperty());
        var selectionChange = EventStreams.changesOf(area.selectionProperty());

        // detect text changes
        area.plainTextChanges()
                .subscribe(change -> {
                    var insertion = change.getInserted().length() > 0 && change.getRemoved().isEmpty();
                    if (insertion)
                        caretChangeCount.getAndDecrement();
                });

        // also detect caret changes
        caretChange.subscribe(_ -> {
            caretChangeCount.getAndIncrement();
        });

        // wait for 600ms before calling
        caretChange.successionEnds(DELAY)
                .subscribe(this::onCaretPosChange);

        selectionChange.successionEnds(DELAY)
                .subscribe(this::onSelectionChange);
    }

    private void onCaretPosChange(Change<Integer> caretPos) {
        // check total caret changes count
        // also check if it's selection,
        if (caretChangeCount.getAndSet(0) < 1 ||
                area.getSelection().getLength() > 0)
            return;

        var btnMap = controlPanel.getStyleButtons()
                .stream()
                .collect(Collectors.toMap(
                        btn -> btn.getId(),
                        btn -> btn));

        List<Pair<String, ToolCmdStrategy>> allTools = btnMap.values()
                .stream()
                .map(btn -> new Pair<>(btn.getId(), Factory.createTool(
                        ToolName.fromName(btn.getId()), null)))
                .toList();

        allTools.forEach(tuple -> {
            var isApplied = tuple.getValue().isApplied(editor);
            btnMap.get(tuple.getKey()).setSelected(isApplied);
            if (!isApplied)
                return;

            // also check if it's stateful tool
            var tool = tuple.getValue();
            if (!(tool instanceof StatefulTextStyleTool stateTool))
                return;

            switch (ToolName.fromName(tuple.getKey())) {
                case FontColor -> {
                    var color = (Color) stateTool.getState();
                    Platform.runLater(() -> controlPanel.getTextColorPicker().setValue(color));

                    if (GlobalConstants.DEFAULT_FONT_COLOR.equals(color))
                        btnMap.get(tuple.getKey()).setSelected(false);
                }
                case FontBG -> {
                    var color = (Color) stateTool.getState();
                    Platform.runLater(() -> controlPanel.getHighColorPicker().setValue(color));

                    if (GlobalConstants.DEFAULT_FONT_BG_COLOR.equals(color))
                        btnMap.get(tuple.getKey()).setSelected(false);
                }
                default -> {
                    return;
                }
            }
        });

        // also process font tool, it's not in the allTools list
        var fontTool = Factory.createTool(ToolName.Font, null);
        if (!(fontTool.isApplied(editor) && fontTool instanceof StatefulTextStyleTool fontStateTool))
            return;
        
        var size = (Integer) fontStateTool.getState();
        Platform.runLater(() -> controlPanel.getFontSizeCombo()
            .setValue(size.toString()));
    }

    private void onSelectionChange(Change<IndexRange> change) {
        if (change.getNewValue().getLength() == 0)
            return;

        System.out.println("Selection changed" + change.getNewValue());
    }
}

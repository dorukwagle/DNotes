package com.doruk.dnotes.MarkdownEditor.changeHandlers;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javafx.scene.control.ToggleButton;
import org.fxmisc.richtext.GenericStyledArea;
import org.reactfx.Change;
import org.reactfx.EventStreams;

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
    private final GenericStyledArea<ParagraphStyle, String, TextStyle> area;
    private final ControlPanelView controlPanel;
    private final FXTextEditor editor;
    private final AtomicInteger caretChangeCount = new AtomicInteger(0);
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
                    var insertion = !change.getInserted().isEmpty() && change.getRemoved().isEmpty();
                    if (insertion)
                        caretChangeCount.getAndDecrement();
                });

        // also detect caret changes
        caretChange.subscribe(_ -> {
            caretChangeCount.getAndIncrement();
        });

        // check of new caret position every delay, then update the tools
        caretChange.successionEnds(DELAY)
                .subscribe(this::onCaretPosChange);

        // track what style is applied at each caret pos, and change the insertion style
        // do this every 20ms
        caretChange.successionEnds(Duration.ofMillis(20))
                .subscribe((pos) -> this.updateInsertionStyle(pos.getNewValue()));

        selectionChange.successionEnds(DELAY)
                .subscribe(this::onSelectionChange);

        // also monitor the focus received
        area.focusedProperty()
                .subscribe(focused -> {
                    System.out.println("received focus: " + focused);
                    if (!focused) return;
                    var caretPos = area.getCaretPosition();
                    this.updateInsertionStyle(caretPos);
                    // increase the caret change count, to simulate change
                    caretChangeCount.set(1);
                    this.onCaretPosChange(new Change<>(caretPos - 1, caretPos));
                });
    }

    private void updateInsertionStyle(int pos) {
        var style = area.getStyleAtPosition(pos);
        editor.getArea().setTextInsertionStyle(style);
    }

    private void onCaretPosChange(Change<Integer> caretPos) {
        // check total caret changes count
        // also check if it's selection,
        if (caretChangeCount.getAndSet(0) < 1 ||
                area.getSelection().getLength() > 0)
            return;

        this.updateUiStates(tool -> tool.isApplied(editor));
    }

    private void onSelectionChange(Change<IndexRange> change) {
        if (change.getNewValue().getLength() == 0)
            return;

        this.updateUiStates(tool -> tool.isAppliedOnSelection(editor));
    }

    private void updateUiStates(Predicate<ToolCmdStrategy> checkApplied) {
        var btnMap = controlPanel.getStyleButtons()
                .stream()
                .collect(Collectors.toMap(
                        ToggleButton::getId,
                        btn -> btn));

        List<Pair<String, ToolCmdStrategy>> allTools = btnMap.values()
                .stream()
                .map(btn -> new Pair<>(btn.getId(), Factory.createTool(
                        ToolName.fromName(btn.getId()), null)))
                .toList();

        allTools.forEach(tuple -> {
            var isApplied = checkApplied.test(tuple.getValue());
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
                default -> {}
            }
        });

        // also process font tool, it's not in the allTools list
        var fontTool = Factory.createTool(ToolName.Font, null);
        var isApplied = checkApplied.test(fontTool);

        if (!(isApplied && fontTool instanceof StatefulTextStyleTool fontStateTool))
            return;

        var size = (Integer) fontStateTool.getState();
        Platform.runLater(() -> controlPanel.getFontSizeCombo()
                .setValue(size.toString()));
    }
}

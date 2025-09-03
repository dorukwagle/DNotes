package com.doruk.dnotes.MarkdownEditor.keyActionHandlers;

import java.util.Set;

import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.dto.ParagraphListItemInfo;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.KeyEventHandler;
import com.doruk.dnotes.MarkdownEditor.interfaces.ListStyleTool;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class NumberListKeyHandler implements KeyEventHandler {
    @Override
    public void handle(
        FXTextEditor editor,
        Set<ToolName> enabledTools, 
        KeyCode action, 
        KeyEvent event
    ) {
        if (!enabledTools.contains(ToolName.NumberList))
            return;
        
        switch (action) {
            case ENTER -> handleEnter(editor, event);
            case TAB -> handleTab(editor, event);
            case BACK_SPACE -> handleBackspace(editor, event);
            default -> {}
        }
    }

    private boolean isCaretAtStart(FXTextEditor editor) {
        var area = editor.getArea();
        return area.getCaretColumn() == 0;
    }

    // check if it's the last line in the document
    private boolean isLastDocumentItem(FXTextEditor editor, int paragraphIndex) {
        var area = editor.getArea();
        return area.getParagraphs().size() - 1 == paragraphIndex;
    }

    private void incrementDownwardsLineCounts(
        FXTextEditor editor, 
        int paragraphIndex, 
        Set<Integer> scopeLevels) {
            var area = editor.getArea();
            var referencePar = area.getParagraph(paragraphIndex);
            var referenceStyle = referencePar.getParagraphStyle();
            
            
            // if the reference line is last line, just return
            if (isLastDocumentItem(editor, paragraphIndex))
                return;

            var tool = Factory.createTool(ToolName.NumberList, editor);     
            if (!(tool instanceof ListStyleTool numberTool))
                return;

            var parIndex = paragraphIndex;
            while (true) {
                ++parIndex;
                
                var curPar = area.getParagraph(parIndex);
                var curStyle = curPar.getParagraphStyle();
                
                if (!scopeLevels.contains(curStyle.level)){
                    if (curStyle.level < referenceStyle.level)
                        break;
                    if (curStyle.level > referenceStyle.level)
                        continue;
                }

                if (isLastDocumentItem(editor, parIndex))
                    break;
            }
        }

    private void handleEnter(FXTextEditor editor, KeyEvent event) {
        var area = editor.getArea();
        var pos = area.getCaretPosition();

        var style = area.getParagraph(editor.getParagraphIndexAtPos(pos)).getParagraphStyle();
        
        var tool = Factory.createTool(ToolName.NumberList, editor);     
        if (!(tool instanceof ListStyleTool numberTool))
            return;

        
        // consume the event and insert new paragraph to the editor, before applying
        event.consume();
        editor.getArea().insertText(pos, "\n");

        // get the current paragraph index after insert
        var currentParagraph = editor.getParagraphIndexAtPos(pos) + 1;
        // var newState = new ParagraphListItemInfo(
        //     currentParagraph,
        //     style.level, 
        //     style.lineCount + 1
        // );

        // numberTool.applyWithUpdatedState(editor, newState);

        // // if it's not the last line, increment downwards line counts.
        // if (isLastDocumentItem(editor, currentParagraph))
        //     return;

        incrementDownwardsLineCounts(editor, currentParagraph, Set.of(style.level));
    }

    private void handleTab(FXTextEditor editor, KeyEvent event) {
        var area = editor.getArea();
        // check if at middle of paragraph
        var pos = area.getCaretPosition();
        if (!isCaretAtStart(editor))
            return;
        
        var paragraph = area.getParagraph(editor.getParagraphIndexAtPos(pos));
        var style = paragraph.getParagraphStyle();

        // check if it's first item
        if (style.lineCount == 1)
            return;
        
        event.consume();

        var tool = Factory.createTool(ToolName.NumberList, editor);     
        if (!(tool instanceof ListStyleTool numberTool))
            return;

        // numberTool.applyWithUpdatedState(editor, new ParagraphListItemInfo(
        //     editor.getParagraphIndexAtPos(pos), 
        //     style.level + 1, 
        //     1
        // ));
    }

    private void handleBackspace(FXTextEditor editor, KeyEvent event) {
        var area = editor.getArea();
        // check if at middle of paragraph
        var pos = area.getCaretPosition();
        if (!isCaretAtStart(editor))
            return;
        
        var paragraph = area.getParagraph(editor.getParagraphIndexAtPos(pos));
        var style = paragraph.getParagraphStyle();
        // also check if it's outermost level
        if (style.level == 1)
            return;
        
        event.consume();

        var tool = Factory.createTool(ToolName.NumberList, editor);     
        if (!(tool instanceof ListStyleTool numberTool))
            return;

        // numberTool.applyWithUpdatedState(editor, new ParagraphListItemInfo(
        //     editor.getParagraphIndexAtPos(pos), 
        //     style.level - 1, 
        //     style.lineCount
        // ));
    }
}

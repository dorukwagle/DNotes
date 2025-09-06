package com.doruk.dnotes.MarkdownEditor.keyActionHandlers;

import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.KeyEventHandler;
import com.doruk.dnotes.MarkdownEditor.lists.ListManager;
import com.doruk.dnotes.MarkdownEditor.utils.StyleGroupRegistry;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class NumberListKeyHandler implements KeyEventHandler {
    @Override
    public void handle(
        FXTextEditor editor,
        KeyCode action, 
        KeyEvent event
    ) {
        var currentParagraphStyle = editor.getArea().getParagraph(editor.getParagraphIndexAtPos(editor.getArea().getCaretPosition())).getParagraphStyle();
        var group = StyleGroupRegistry.getGroup(ParagraphType.NUMBER_LIST_ITEM);
        // if it's not a list item
        if (currentParagraphStyle.numberListId == null)
            return;
        // now check if number list
        if (currentParagraphStyle.getStyle(group).get() != ParagraphType.NUMBER_LIST_ITEM)
            return;
        
        switch (action) {
            case ENTER -> handleEnter(editor, event);
            case TAB -> handleTab(editor, event);
            case BACK_SPACE -> {
                if (event.isShiftDown())
                    handleShiftBackspace(editor, event);
                else
                    handleBackspace(editor, event);
            }
            default -> {}
        }
    }

    private boolean isCaretAtStart(FXTextEditor editor) {
        var area = editor.getArea();
        return area.getCaretColumn() == 0;
    }

    private void handleEnter(FXTextEditor editor, KeyEvent event) {
        var area = editor.getArea();
        var pos = area.getCaretPosition();

        var style = area.getParagraph(editor.getParagraphIndexAtPos(pos)).getParagraphStyle();
        
        // consume the event and insert new paragraph to the editor, before applying
        event.consume();
        editor.getArea().insertText(pos, "\n");

        // get the current paragraph index after insert
        var currentParagraph = editor.getParagraphIndexAtPos(pos) + 1;
        ListManager.getInstance().computeListNumbering(ParagraphType.NUMBER_LIST_ITEM, style.numberListId, currentParagraph);
    }

    private void handleTab(FXTextEditor editor, KeyEvent event) {
        var area = editor.getArea();
        var pos = area.getCaretPosition();
        // check if it's first item or caret is at column start
        if (!isCaretAtStart(editor))
            return;
            
        event.consume();

        var paragraph = area.getParagraph(editor.getParagraphIndexAtPos(pos));
        var style = paragraph.getParagraphStyle();
        
        // if first item, then increase the offset
        if (style.level == 1 && style.lineCount == 1) {
            ListManager.getInstance()
                .increaseListOffset(ParagraphType.NUMBER_LIST_ITEM, 
                    style.numberListId, editor.getParagraphIndexAtPos(pos));
            return;
        }
        if (style.lineCount == 1)
            return;// don't futher increaes the level, let editor handle tab
        
        ListManager.getInstance()
            .increaseItemLevel(ParagraphType.NUMBER_LIST_ITEM, style.numberListId, editor.getParagraphIndexAtPos(pos), style);
    }

    private void handleBackspace(FXTextEditor editor, KeyEvent event) {
        var area = editor.getArea();
        // check if at middle of paragraph
        var pos = area.getCaretPosition();
        if (!isCaretAtStart(editor))
            return;
        
        var paragraph = area.getParagraph(editor.getParagraphIndexAtPos(pos));
        var style = paragraph.getParagraphStyle();

        ListManager.getInstance()
            .computeListNumbering(
                ParagraphType.NUMBER_LIST_ITEM, 
                style.numberListId, 
                editor.getParagraphIndexAtPos(pos)
            );
    }

    private void handleShiftBackspace(FXTextEditor editor, KeyEvent event) {
        var area = editor.getArea();
        // check if at middle of paragraph
        var pos = area.getCaretPosition();

        var paragraph = area.getParagraph(editor.getParagraphIndexAtPos(pos));
        var style = paragraph.getParagraphStyle();

        if (style.level == 1)
            return;
        
        event.consume();

        ListManager.getInstance()
            .decreaseItemLevel(
                ParagraphType.NUMBER_LIST_ITEM, 
                style.numberListId, 
                editor.getParagraphIndexAtPos(pos),
                style
            );
    }
}

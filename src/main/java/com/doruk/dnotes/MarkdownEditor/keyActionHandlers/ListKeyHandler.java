package com.doruk.dnotes.MarkdownEditor.keyActionHandlers;

import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.KeyEventHandler;
import com.doruk.dnotes.MarkdownEditor.lists.ListManager;
import com.doruk.dnotes.MarkdownEditor.utils.StyleGroupRegistry;
import com.doruk.dnotes.store.GlobalConstants;

import javafx.application.Platform;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public abstract class ListKeyHandler implements KeyEventHandler {
    protected abstract ParagraphType getParagraphListType();

    protected boolean isThisToolActive(FXTextEditor editor) {
        var currentParagraphStyle = editor.getArea().getParagraph(editor.getParagraphIndexAtPos(editor.getArea().getCaretPosition())).getParagraphStyle();
        var group = StyleGroupRegistry.getGroup(this.getParagraphListType());
        // if it's not a list item
        if (currentParagraphStyle.numberListId == null)
            return false;
        // now check if number list
        if (currentParagraphStyle.getStyle(group).get() != this.getParagraphListType())
            return false;

        return true;
    }

    @Override
    public void handle(
        FXTextEditor editor,
        KeyCode action, 
        KeyEvent event
    ) {
        if (!this.isThisToolActive(editor))
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
            case V -> handlePaste(editor, event);
            case X -> handleCut(editor, event);
            default -> {}
        }
    }

    protected boolean isCaretAtStart(FXTextEditor editor) {
        var area = editor.getArea();
        return area.getCaretColumn() == 0;
    }

    protected void reCalculateListNumbering(FXTextEditor editor) {
        var area = editor.getArea();
        
        Platform.runLater(() -> {
            var parIndex = editor.getParagraphIndexAtPos(area.getCaretPosition());
            var style = area.getParagraph(parIndex).getParagraphStyle();
            
            ListManager.getInstance()
                .computeListNumbering(
                    this.getParagraphListType(), 
                    style.numberListId,
                    parIndex
                );
        });
    }

    private void handleEnter(FXTextEditor editor, KeyEvent event) {
        var area = editor.getArea();
        var pos = area.getCaretPosition();

        var style = area.getParagraph(editor.getParagraphIndexAtPos(pos)).getParagraphStyle();
        
        // consume the event and insert new paragraph to the editor, before applying
        event.consume();
        editor.getArea().insertText(pos, "\n");
        // follow the caret, scroll into view
        editor.getArea().requestFollowCaret();

        // get the current paragraph index after insert
        var currentParagraph = editor.getParagraphIndexAtPos(pos) + 1;
        ListManager.getInstance().computeListNumbering(
            this.getParagraphListType(), 
            style.numberListId, 
            currentParagraph
        );
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
            if (style.offset < GlobalConstants.MAX_LIST_ITEM_OFFSET)
                ListManager.getInstance()
                    .increaseListOffset(this.getParagraphListType(), 
                        style.numberListId, editor.getParagraphIndexAtPos(pos));
            return;
        }
        if (style.lineCount == 1)
            return;// don't futher increaes the level, let editor handle tab
        
        ListManager.getInstance()
            .increaseItemLevel(
                this.getParagraphListType(), 
                style.numberListId, 
                editor.getParagraphIndexAtPos(pos), 
                style
            );
    }

    private void handleBackspace(FXTextEditor editor, KeyEvent event) {
        var area = editor.getArea();
        var hasSelection = area.getSelection().getLength() > 0;
        // if has selection and no multi lines
        if (hasSelection && !area.getSelectedText().contains("\n"))
            return;
        
        if (!hasSelection && !isCaretAtStart(editor))
            return;

        var pos = area.getCaretPosition();
        var curParIndex = editor.getParagraphIndexAtPos(pos);
        var style = area.getParagraph(curParIndex).getParagraphStyle();
        boolean firstListItem = style.lineCount == 1 && style.level == 1;
            
        // if it's first list item, need to handle the backspace manually, as it puts cursor
        // outside of the list.
        if (firstListItem) {
            // check if next item exist
            var nextStyle = area.getParagraph(curParIndex + 1).getParagraphStyle();
            if (!style.numberListId.equals(nextStyle.numberListId))
                return;

            // le the event run, and in the next pulse, when cursor resets, re-calculate the list
            // now the index of next item is the index of current item
            Platform.runLater(() -> ListManager.getInstance()
                .computeListNumbering(
                    this.getParagraphListType(), 
                    style.numberListId,
                    curParIndex
                ));
            return;
        }
        // if it's any other paragraph except the first one, just re-calculate the list
        this.reCalculateListNumbering(
            editor
        ); // executes in next javafx pulse
    }

    private void handleCut(FXTextEditor editor, KeyEvent event) {
        var area = editor.getArea();
        var selection = area.getSelection();

        if (selection.getLength() == 0 || !area.getSelectedText().contains("\n"))
            return;
        
        this.reCalculateListNumbering(editor);
    }

    private void handlePaste(FXTextEditor editor, KeyEvent event) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        var content = clipboard.getString();
        if (!clipboard.hasString() || 
            content.isEmpty() || 
            !content.contains("\n"))
                return;
        
        this.reCalculateListNumbering(editor);
    }

    private void handleShiftBackspace(FXTextEditor editor, KeyEvent event) {
        var area = editor.getArea();
        // check if at middle of paragraph
        var pos = area.getCaretPosition();

        var paragraph = area.getParagraph(editor.getParagraphIndexAtPos(pos));
        var style = paragraph.getParagraphStyle();

        
        if (style.level == 1 && style.lineCount > 1)
        return;
        
        event.consume();
        
        // if first item, then decrease the offset
        if (style.level == 1 && style.lineCount == 1) {
            if (style.offset > 0)
                ListManager.getInstance()
                    .decreaseListOffset(this.getParagraphListType(), 
                        style.numberListId, editor.getParagraphIndexAtPos(pos));
            return;
        }

        ListManager.getInstance()
            .decreaseItemLevel(
                this.getParagraphListType(), 
                style.numberListId, 
                editor.getParagraphIndexAtPos(pos),
                style
            );
    }
}

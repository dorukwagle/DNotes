package com.doruk.dnotes.MarkdownEditor.keyActionHandlers;

import java.util.Set;

import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.dto.ParagraphListItemInfo;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.KeyEventHandler;
import com.doruk.dnotes.MarkdownEditor.interfaces.ListStyleTool;
import com.doruk.dnotes.MarkdownEditor.utils.StyleGroupRegistry;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class BulletListKeyHandler implements KeyEventHandler {
    @Override
    public void handle(
        FXTextEditor editor,
        KeyCode action, 
        KeyEvent event
    ) {
        var currentParagraphStyle = editor.getArea().getParagraph(editor.getParagraphIndexAtPos(editor.getArea().getCaretPosition())).getParagraphStyle();
        var group = StyleGroupRegistry.getGroup(ParagraphType.BULLET_LIST_ITEM);
        if (currentParagraphStyle.numberListId == null)
            return; // it's not a list item
        // now check if bullet list
        if (currentParagraphStyle.getStyle(group).get() != ParagraphType.BULLET_LIST_ITEM)
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

    private void handleEnter(FXTextEditor editor, KeyEvent event) {
        var area = editor.getArea();
        var pos = area.getCaretPosition();

        var style = area.getParagraph(editor.getParagraphIndexAtPos(pos)).getParagraphStyle();
        
        var tool = Factory.createTool(ToolName.BulletList, editor);     
        if (!(tool instanceof ListStyleTool bulletTool))
            return;

        // bulletTool.applyWithUpdatedState(editor, new ParagraphListItemInfo(
        //     editor.getParagraphIndexAtPos(pos), 
        //     style.level, 
        //     style.lineCount + 1
        // ));
    }

    private void handleTab(FXTextEditor editor, KeyEvent event) {
        var area = editor.getArea();
        // check if at middle of paragraph
        var pos = area.getCaretPosition();
        if (!isCaretAtStart(editor))
            return;
        
        var paragraph = area.getParagraph(editor.getParagraphIndexAtPos(pos));
        var style = paragraph.getParagraphStyle();
        // System.out.println("paragraph: " + paragraph.getText() + "\n" + "count: " + style.lineCount);
        // check if it's first item
        if (style.lineCount == 1)
            return;
        
        event.consume();

        var tool = Factory.createTool(ToolName.BulletList, editor);     
        if (!(tool instanceof ListStyleTool bulletTool))
            return;

        // bulletTool.applyWithUpdatedState(editor, new ParagraphListItemInfo(
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

        var style = area.getParagraph(editor.getParagraphIndexAtPos(pos)).getParagraphStyle();
        // also check if it's outermost level
        if (style.level == 1)
            return;

        event.consume();

        var tool = Factory.createTool(ToolName.BulletList, editor);     
        if (!(tool instanceof ListStyleTool bulletTool))
            return;

        // bulletTool.applyWithUpdatedState(editor, new ParagraphListItemInfo(
        //     editor.getParagraphIndexAtPos(pos), 
        //     style.level - 1, 
        //     style.lineCount
        // ));
    }
}

package com.doruk.dnotes.MarkdownEditor.tools;

import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.ParagraphStyleTool;
import com.doruk.dnotes.MarkdownEditor.utils.ParagraphStyleHelper;
import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.Factory;

public class Blockquote extends ParagraphStyleTool {
    
    public Blockquote(FXTextEditor editor) {
        super(editor);
    }

    @Override
    protected ParagraphType getParagraphType() {
        return ParagraphType.BLOCKQUOTE;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T getStyle(T currentStyle, boolean apply) {
        return (T) ParagraphStyleHelper.withBlockquote((ParagraphStyle)currentStyle);
    }

    @Override
    protected void addRenderer(FXTextEditor editor) {
        editor.addParagraphRenderer(ToolName.Blockquote,
                Factory.createParagraphRenderer(ToolName.Blockquote));
    }    
}

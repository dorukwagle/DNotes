package com.doruk.dnotes.MarkdownEditor.tools;

import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.ParagraphStyleTool;
import com.doruk.dnotes.MarkdownEditor.utils.ParagraphStyleHelper;

public class H2 extends ParagraphStyleTool {
    
    public H2(FXTextEditor editor) {
        super(editor);
    }

    @Override
    protected ParagraphType getParagraphType() {
        return ParagraphType.H2;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T getStyle(T currentStyle, boolean apply) {
        return (T) ParagraphStyleHelper.withHeading2((ParagraphStyle)currentStyle);
    }

    @Override
    protected void addRenderer(FXTextEditor editor) {
        editor.addParagraphRenderer(ToolName.H2,
                Factory.createParagraphRenderer(ToolName.H2));
    }    
}

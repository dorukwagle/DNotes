package com.doruk.dnotes.MarkdownEditor.codecs;

import org.fxmisc.richtext.model.Paragraph;

import com.doruk.dnotes.MarkdownEditor.codecs.enums.ParagraphModifiers;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.ICodecManager;

public class CodecManager implements ICodecManager {
    @Override
    public void dumpEditorDocument(FXTextEditor editor) {
        
    }

    @Override
    public void loadEditorDocument(FXTextEditor editor) {
        
    }

    @Override
    public String[] getCodecsValues() {
        // manually add codecs avoiding loops, to maintain consistency and same order always
        return new String[] {
            ParagraphModifiers.IsItemChecked.name(),
            ParagraphModifiers.Level.name(),
            ParagraphModifiers.LineCount.name(),
            ParagraphModifiers.NumberListId.name(),
            ParagraphModifiers.Offset.name(),
            ToolName.Underline.name(),
            ToolName.AlignLeft.name(),
            ToolName.CheckList.name(),
            ToolName.H1.name(),
            ToolName.H2.name(),
            ToolName.H3.name(),
            ToolName.BulletList.name(),
            ToolName.NumberList.name(),
            ToolName.H4.name(),
            ToolName.Bold.name(),
            ToolName.Italic.name(),
            ToolName.Blockquote.name(),
            ToolName.AlignCenter.name(),
            ToolName.Strikethrough.name(),
            ToolName.FontColor.name(),
            ToolName.FontBG.name(),
            ToolName.Font.name()
        };
    }
}

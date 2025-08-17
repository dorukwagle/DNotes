package com.doruk.dnotes.MarkdownEditor;

import org.fxmisc.richtext.TextExt;

import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.Renderer;
import com.doruk.dnotes.MarkdownEditor.interfaces.ToolCmdStrategy;
import com.doruk.dnotes.MarkdownEditor.renderers.AlignCenterRenderer;
import com.doruk.dnotes.MarkdownEditor.renderers.AlignLeftRenderer;
import com.doruk.dnotes.MarkdownEditor.renderers.BlockquoteRenderer;
import com.doruk.dnotes.MarkdownEditor.renderers.BoldRenderer;
import com.doruk.dnotes.MarkdownEditor.renderers.CheckboxRenderer;
import com.doruk.dnotes.MarkdownEditor.renderers.FontBGRenderer;
import com.doruk.dnotes.MarkdownEditor.renderers.FontColorRenderer;
import com.doruk.dnotes.MarkdownEditor.renderers.FontRenderer;
import com.doruk.dnotes.MarkdownEditor.renderers.H1Renderer;
import com.doruk.dnotes.MarkdownEditor.renderers.H2Renderer;
import com.doruk.dnotes.MarkdownEditor.renderers.H3Renderer;
import com.doruk.dnotes.MarkdownEditor.renderers.H4Renderer;
import com.doruk.dnotes.MarkdownEditor.renderers.ItalicRenderer;
import com.doruk.dnotes.MarkdownEditor.renderers.OLItemRenderer;
import com.doruk.dnotes.MarkdownEditor.renderers.StrikethroughRenderer;
import com.doruk.dnotes.MarkdownEditor.renderers.ULItemRenderer;
import com.doruk.dnotes.MarkdownEditor.renderers.UnderlineRenderer;
import com.doruk.dnotes.MarkdownEditor.tools.AlignCenter;
import com.doruk.dnotes.MarkdownEditor.tools.AlignLeft;
import com.doruk.dnotes.MarkdownEditor.tools.Blockquote;
import com.doruk.dnotes.MarkdownEditor.tools.Bold;
import com.doruk.dnotes.MarkdownEditor.tools.BulletList;
import com.doruk.dnotes.MarkdownEditor.tools.CheckList;
import com.doruk.dnotes.MarkdownEditor.tools.Font;
import com.doruk.dnotes.MarkdownEditor.tools.FontBG;
import com.doruk.dnotes.MarkdownEditor.tools.FontColor;
import com.doruk.dnotes.MarkdownEditor.tools.H1;
import com.doruk.dnotes.MarkdownEditor.tools.H2;
import com.doruk.dnotes.MarkdownEditor.tools.H3;
import com.doruk.dnotes.MarkdownEditor.tools.H4;
import com.doruk.dnotes.MarkdownEditor.tools.Italic;
import com.doruk.dnotes.MarkdownEditor.tools.NumberList;
import com.doruk.dnotes.MarkdownEditor.tools.Strikethrough;
import com.doruk.dnotes.MarkdownEditor.tools.Underlin;

import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;

public class Factory {
    public static ToolCmdStrategy createTool(ToolName toolName, FXTextEditor editor) {
        return switch (toolName) {
            case Bold -> new Bold(editor);
            case Italic -> new Italic(editor);
            case Underline -> new Underlin(editor);
            case Strikethrough -> new Strikethrough(editor);
            case FontColor -> new FontColor(editor);
            case FontBG -> new FontBG(editor);
            case Font -> new Font(editor);
            case H1 -> new H1(editor);
            case H2 -> new H2(editor);
            case H3 -> new H3(editor);
            case H4 -> new H4(editor);
            case Blockquote -> new Blockquote(editor);
            case AlignCenter -> new AlignCenter(editor);
            case AlignLeft -> new AlignLeft(editor);
            case BulletList -> new BulletList(editor);
            case CheckList -> new CheckList(editor);
            case NumberList -> new NumberList(editor);
            default -> null;
        };
    }

    public static FXTextEditor getFXTextEditor() {
        return new EditorFX();
    }

    public static Renderer<TextExt, TextStyle> createTextRenderer(ToolName toolName) {
        return switch (toolName) {
            case Bold -> new BoldRenderer();
            case Italic -> new ItalicRenderer();
            case Strikethrough -> new StrikethroughRenderer();
            case Underline -> new UnderlineRenderer();
            default -> null;
        };
    }

    public static Renderer<TextExt, TextStyle> createStatefulTextRenderer(ToolName toolName, Object param) {
        return switch (toolName) {
            case FontColor -> new FontColorRenderer((Color)param);
            case FontBG -> new FontBGRenderer((Color)param);
            case Font -> new FontRenderer((Integer)param);
            default -> null;
        };
    }

    public static Renderer<TextFlow, ParagraphStyle> createParagraphRenderer(ToolName toolName) {
        return switch (toolName) {
            case AlignCenter -> new AlignCenterRenderer();
            case AlignLeft -> new AlignLeftRenderer();
            case H1 -> new H1Renderer();
            case H2 -> new H2Renderer();
            case H3 -> new H3Renderer();
            case H4 -> new H4Renderer();
            case Blockquote -> new BlockquoteRenderer();
            case NumberList -> new OLItemRenderer();
            case BulletList -> new ULItemRenderer();
            case CheckList -> new CheckboxRenderer();
            default -> null;
        };
    }
}

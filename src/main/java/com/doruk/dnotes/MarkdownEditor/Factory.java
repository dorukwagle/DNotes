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
import com.doruk.dnotes.MarkdownEditor.tools.Bold;
import com.doruk.dnotes.MarkdownEditor.tools.Italic;

import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;

public class Factory {
    public static ToolCmdStrategy createTool(ToolName toolName, FXTextEditor editor) {
        return switch (toolName) {
            case BulletList -> null;
            case Underline -> null;
            case Checkbox -> null;
            case H1 -> null;
            case H2 -> null;
            case H3 -> null;
            case OrderedList -> null;
            case H4 -> null;
            case Bold -> new Bold(editor);
            case Italic -> new Italic(editor);
            case Blockquote -> null;
            case AlignCenter -> null;
            case Strikethrough -> null;
            case FontColor -> null;
            case FontBG -> null;
            case Font -> null;
            case AlignLeft -> null;
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

    public static Renderer<TextExt, TextStyle> createStatefulRenderer(ToolName toolName, Object param) {
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
            case OrderedList -> new OLItemRenderer();
            case UnorderedList -> new ULItemRenderer();
            case Checkbox -> new CheckboxRenderer();
            default -> null;
        };
    }
}

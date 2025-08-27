package com.doruk.dnotes.MarkdownEditor;

import java.util.EnumMap;
import java.util.Map;

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
import com.doruk.dnotes.MarkdownEditor.renderers.BulletListRenderer;
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
import com.doruk.dnotes.MarkdownEditor.tools.Underline;

import javafx.scene.text.TextFlow;

public class Factory {
    private static Map<ToolName, ToolCmdStrategy> tools;

    private static void initializeTools(FXTextEditor editor) {
        tools = new EnumMap<>(ToolName.class);

        tools.put(ToolName.Bold, new Bold(editor));
        tools.put(ToolName.Italic, new Italic(editor));
        tools.put(ToolName.Underline, new Underline(editor));
        tools.put(ToolName.Strikethrough, new Strikethrough(editor));
        tools.put(ToolName.FontColor, new FontColor(editor));
        tools.put(ToolName.FontBG, new FontBG(editor));
        tools.put(ToolName.Font, new Font(editor));
        tools.put(ToolName.H1, new H1(editor));
        tools.put(ToolName.H2, new H2(editor));
        tools.put(ToolName.H3, new H3(editor));
        tools.put(ToolName.H4, new H4(editor));
        tools.put(ToolName.Blockquote, new Blockquote(editor));
        tools.put(ToolName.AlignCenter, new AlignCenter(editor));
        tools.put(ToolName.AlignLeft, new AlignLeft(editor));
        tools.put(ToolName.BulletList, new BulletList(editor));
        tools.put(ToolName.CheckList, new CheckList(editor));
        tools.put(ToolName.NumberList, new NumberList(editor));
    }

    public static ToolCmdStrategy createTool(ToolName toolName, FXTextEditor editor) {
        if (tools == null)
            initializeTools(editor);

        return tools.get(toolName);
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
            case FontColor -> new FontColorRenderer();
            case FontBG -> new FontBGRenderer();
            case Font -> new FontRenderer();
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
            case BulletList -> new BulletListRenderer();
            case CheckList -> new CheckboxRenderer();
            default -> null;
        };
    }
}

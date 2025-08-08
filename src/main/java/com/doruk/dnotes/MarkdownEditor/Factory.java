package com.doruk.dnotes.MarkdownEditor;

import com.doruk.dnotes.MarkdownEditor.enums.ToolName;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.interfaces.ToolCmdStrategy;

public class Factory {
    public static ToolCmdStrategy getToolCmdStrategy(ToolName toolName) {
        switch (toolName) {
            case BulletList -> {
                return null;
            }
            case Underline -> {
                return null;
            }
            case Checkbox -> {
                return null;
            }
            case H1 -> {
                return null;
            }
            case H2 -> {
                return null;
            }
            case H3 -> {
                return null;
            }
            case NumberList -> {
                return null;
            }
            case H4 -> {
                return null;
            }
            case Bold -> {
                return null;
            }
            case Italic -> {
                return null;
            }
            case Blockquote -> {
                return null;
            }
            case AlignCenter -> {
                return null;
            }
            case Strikethrough -> {
                return null;
            }
            default -> {
                return null;
            }
        }
    }

    public static FXTextEditor getFXTextEditor() {
        return new EditorFX();
    }
}

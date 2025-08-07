package com.doruk.dnotes.MarkdownEditor.interfaces;

import org.fxmisc.richtext.GenericStyledArea;

import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
import com.doruk.dnotes.MarkdownEditor.docstyle.TextStyle;

public interface FXTextEditor {
    GenericStyledArea<ParagraphStyle, String, TextStyle> getArea();
}

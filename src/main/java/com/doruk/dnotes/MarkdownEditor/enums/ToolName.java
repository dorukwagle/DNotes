package com.doruk.dnotes.MarkdownEditor.enums;

import java.util.Arrays;

public enum ToolName {
    Underline("Underline"),
    AlignLeft("AlignLeft"),
    CheckList("CheckList"),
    H1("H1"),
    H2("H2"),
    H3("H3"),
    H4("H4"),
    BulletList("BulletList"),
    NumberList("NumberList"),
    Bold("Bold"),
    Italic("Italic"),
    Blockquote("Blockquote"),
    AlignCenter("AlignCenter"),
    Strikethrough("Strikethrough"),
    FontColor("FontColor"),
    FontBG("FontBG"),
    Font("Font");
    
    private final String name;
    
    ToolName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ToolName fromName(String name) {
        return Arrays.stream(values())
            .filter(toolName -> toolName.getName().equals(name))
            .findFirst()
            .orElse(null);
    }
}

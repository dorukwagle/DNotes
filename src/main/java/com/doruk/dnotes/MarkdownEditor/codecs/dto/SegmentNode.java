package com.doruk.dnotes.MarkdownEditor.codecs.dto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.doruk.dnotes.MarkdownEditor.enums.ToolName;

public class SegmentNode {
    private final Set<ToolName> styles;
    private final String text;
    private final Map<ToolName, Integer> stateValues;

    public SegmentNode(String text) {
        this.styles = new HashSet<>();
        this.text = text;
        this.stateValues = new HashMap<>();
    }

    public Set<ToolName> getStyles() {
        return styles;
    }

    public String getText() {
        return text;
    }

    public Map<ToolName, Integer> getStateValues() {
        return stateValues;
    }

    public void addStyle(ToolName style) {
        this.styles.add(style);
    }

    public void addStateValue(ToolName style, int value) {
        this.stateValues.put(style, value);
    }
}

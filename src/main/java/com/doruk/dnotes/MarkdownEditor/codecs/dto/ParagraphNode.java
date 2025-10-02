package com.doruk.dnotes.MarkdownEditor.codecs.dto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.doruk.dnotes.MarkdownEditor.codecs.enums.ParagraphModifiers;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;

public class ParagraphNode {
    private final Set<ToolName> globalStyles;
    private final Map<ParagraphModifiers, Integer> modifiers;
    private final List<SegmentNode> segments;

    public ParagraphNode() {
        this.globalStyles = new HashSet<>();
        this.modifiers = new HashMap<>();
        this.segments = new LinkedList<>();
    }

    public Set<ToolName> getGlobalStyles() {
        return globalStyles;
    }

    public List<SegmentNode> getSegments() {
        return segments;
    }

    public Map<ParagraphModifiers, Integer> getModifiers() {
        return modifiers;
    }

    public void addGlobalStyle(ToolName style) {
        this.globalStyles.add(style);
    }

    public void addModifier(ParagraphModifiers modifier, int value) {
        this.modifiers.put(modifier, value);
    }

    public void addSegment(SegmentNode segment) {
        this.segments.add(segment);
    }
}

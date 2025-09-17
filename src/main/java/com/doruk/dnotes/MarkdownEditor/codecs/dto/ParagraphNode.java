package com.doruk.dnotes.MarkdownEditor.codecs.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.doruk.dnotes.MarkdownEditor.codecs.enums.ParagraphModifiers;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;

public class ParagraphNode {
    private final Set<ToolName> globalStyles;
    private final Map<ParagraphModifiers, Long> modifiers;
    private final List<SegmentNode> segments;

    public ParagraphNode() {
        this.globalStyles = new HashSet<>();
        this.modifiers = new HashMap<>();
        this.segments = new ArrayList<>();
    }

    public Set<ToolName> getGlobalStyles() {
        return globalStyles;
    }

    public List<SegmentNode> getSegments() {
        return segments;
    }

    public Map<ParagraphModifiers, Long> getModifiers() {
        return modifiers;
    }

    public void addGlobalStyle(ToolName style) {
        this.globalStyles.add(style);
    }

    public void addModifier(ParagraphModifiers modifier, long value) {
        this.modifiers.put(modifier, value);
    }

    public void addSegment(SegmentNode segment) {
        this.segments.add(segment);
    }
}

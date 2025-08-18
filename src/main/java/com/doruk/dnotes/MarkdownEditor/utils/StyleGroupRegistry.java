package com.doruk.dnotes.MarkdownEditor.utils;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
import com.doruk.dnotes.MarkdownEditor.enums.StyleGroup;
import com.doruk.dnotes.MarkdownEditor.enums.ToolName;

public class StyleGroupRegistry {
    private static final Map<StyleGroup, Set<ParagraphType>> styleMap = Map.of(
            StyleGroup.Heading, Set.of(ParagraphType.H1, ParagraphType.H2, ParagraphType.H3, ParagraphType.H4),
            StyleGroup.Blockquote, Set.of(ParagraphType.BLOCKQUOTE),
            StyleGroup.List,
            Set.of(ParagraphType.BULLET_LIST_ITEM, ParagraphType.NUMBER_LIST_ITEM, ParagraphType.CHECK_LIST_ITEM),
            StyleGroup.Alignment, Set.of(ParagraphType.ALIGN_LEFT, ParagraphType.ALIGN_CENTER));

    private static final Map<StyleGroup, Set<ToolName>> toolMap = Map.of(
            StyleGroup.Heading, Set.of(ToolName.H1, ToolName.H2, ToolName.H3, ToolName.H4),
            StyleGroup.Blockquote, Set.of(ToolName.Blockquote),
            StyleGroup.List, Set.of(ToolName.BulletList, ToolName.NumberList, ToolName.CheckList),
            StyleGroup.Alignment, Set.of(ToolName.AlignLeft, ToolName.AlignCenter));

    public static Set<ParagraphType> getConflictingStyles(ParagraphType type) {
        return styleMap.values()
                .stream()
                .filter(set -> set.contains(type))
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    public static Set<ToolName> getConflictingTools(ToolName tool) {
        return toolMap.values()
                .stream()
                .filter(set -> set.contains(tool))
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    public static StyleGroup getGroup(ParagraphType type) {
        return styleMap.entrySet()
                .stream()
                .filter(e -> e.getValue().contains(type))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}

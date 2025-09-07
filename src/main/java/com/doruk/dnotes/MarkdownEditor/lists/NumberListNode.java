package com.doruk.dnotes.MarkdownEditor.lists;

import java.util.HashMap;
import java.util.Map;

import javafx.util.Pair;

public class NumberListNode {
    private static Map<Integer, Integer> calculateSingleLevelNumbering(int fromParIndex, int toParIndex) {
        Map<Integer, Integer> indexNumberMap = new HashMap<>();

        var cursor = 1;
        for (int i = fromParIndex; i <= toParIndex; i++) 
            indexNumberMap.put(i, cursor++);

        return indexNumberMap;
    }

    private static Map<Integer, Integer> calculateMultiLevelNumbering(int fromParIndex, int toParIndex, Map<Integer, Integer> indexAndLevel) {
        Map<Integer, Integer> indexNumberMap = new HashMap<>();

        Map<Integer, Integer> levelLineCount = new HashMap<>();

        int referenceLevel = 0;
        for (int i = fromParIndex; i <= toParIndex; i++) {
           int level = indexAndLevel.get(i);
           if (level < referenceLevel)
                levelLineCount.put(level + 1, 1); // reset the inner level

            referenceLevel = level;

           int count = levelLineCount.computeIfAbsent(level, _ -> 1);

           indexNumberMap.put(i, count);
           levelLineCount.put(level, count + 1);
        }

        return indexNumberMap;
    }
    
    /**
     * This method calculates the numbering for the given list items. 
     * The returned pair's first element is a map of paragraph index to numbering,
     * and the second element is a boolean indicating whether the level is preserved.
     * 
     * @param fromParIndex the starting paragraph index
     * @param toParIndex the ending paragraph index
     * @param indexAndLevel a map of paragraph index to level
     * @return a pair of map and boolean
     */
    public static Pair<Map<Integer, Integer>, Boolean> calculateItemsNumbering(int fromParIndex, int toParIndex, 
        Map<Integer, Integer> indexAndLevel) 
    {
        // if the first list item contains level > 1, don't preserve the levels
        // if first item has level 1, then preserve the default levels.
        var preserve = indexAndLevel.get(fromParIndex) == 1;
        return new Pair<>(
            preserve ? calculateMultiLevelNumbering(fromParIndex, toParIndex, indexAndLevel) : 
                calculateSingleLevelNumbering(fromParIndex, toParIndex),
            preserve
        );
    }
}

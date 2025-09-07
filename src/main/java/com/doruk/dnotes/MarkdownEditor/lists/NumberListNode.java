package com.doruk.dnotes.MarkdownEditor.lists;

import java.util.HashMap;
import java.util.Map;

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

        // if any level is less than the pivot, then start the numbering from 2
        int pivot = indexAndLevel.get(fromParIndex);
        int referenceLevel = 0;
        for (int i = fromParIndex; i <= toParIndex; i++) {
            int level = indexAndLevel.get(i);
            if (level < referenceLevel)
                levelLineCount.put(level + 1, 1); // reset the inner level

            referenceLevel = level;

            int count = levelLineCount.computeIfAbsent(level, _ -> level < pivot ? 2 : 1);

            indexNumberMap.put(i, count);
            levelLineCount.put(level, count + 1);
        }

        return indexNumberMap;
    }

    /**
     * This method calculates the numbering for the given list items.
     * Put -1 as key with any value, to remove levels, only apply single level numbering.
     * 
     * @param fromParIndex  the starting paragraph index
     * @param toParIndex    the ending paragraph index
     * @param indexAndLevel a map of paragraph index to level
     * @return a map of paragraph index to numbering
     */
    public static Map<Integer, Integer> calculateItemsNumbering(int fromParIndex, int toParIndex,
            Map<Integer, Integer> indexAndLevel) {
        if (indexAndLevel.containsKey(-1))
            return calculateSingleLevelNumbering(fromParIndex, toParIndex);
        
        return calculateMultiLevelNumbering(fromParIndex, toParIndex, indexAndLevel);
    }
}

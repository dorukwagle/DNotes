package com.doruk.dnotes.MarkdownEditor.lists;

import java.util.HashMap;
import java.util.Map;

import javafx.util.Pair;

public class NumberListNode {
    private static Map<Integer, Integer> calculateSingleLevelNumbering(int fromParIndex, int toParIndex) {
        Map<Integer, Integer> indexAndLevel = new HashMap<>();

        var cursor = 1;
        for (int i = fromParIndex; i <= toParIndex; i++) 
            indexAndLevel.put(i, cursor++);

        return indexAndLevel;
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
        if (indexAndLevel.get(fromParIndex) > 1)
            return new Pair<>(calculateSingleLevelNumbering(fromParIndex, toParIndex), false);
        
        // if first item has level 1, then preserve the default levels.
        var indexNumberMap = new HashMap<Integer, Integer>();
        
        return new Pair<>(indexNumberMap, true);
    }
}

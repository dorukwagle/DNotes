package com.doruk.dnotes.MarkdownEditor.utils;

public class NumeralUtility {
    public static String toAlphabeticUpper(int number) {
        if (number < 0)
            return "";

        // treating from 0 index
        --number;

        var start = 'A';
        var end = 'Z';
        var totalChar = end - start + 1; // 26

        
        var cyclicValue = number % totalChar;
        int offsetValue = number / totalChar;

        StringBuilder offsets = new StringBuilder();
        while (--offsetValue >= 0)
            offsets.append("A");
        
        return offsets + String.valueOf((char) (cyclicValue + start));
    }

    public static String toAlphabeticLower(int number) {
        if (number < 0)
            return "";

        // treating from 0 index
        --number;

        var start = 'a';
        var end = 'z';
        var totalChar = end - start + 1; // 26

        
        var cyclicValue = number % totalChar;
        int offsetValue = number / totalChar;

        StringBuilder offsets = new StringBuilder();
        while (--offsetValue >= 0)
            offsets.append("a");
        
        return offsets + String.valueOf((char) (cyclicValue + start));
    }

    public static String toRoman(int number) {
        return "";
    }
}

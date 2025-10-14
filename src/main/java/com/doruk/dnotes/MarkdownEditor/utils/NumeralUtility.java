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

    private static String toRoman(int number) {
        var thousandsR = "M";
        String[] hundredsR = {"C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String[] tensR = {"X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] onesR = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};

        
        StringBuilder result = new StringBuilder();
        
        // process thousands
        int thousandsPlace = number / 1000;
        if (thousandsPlace > 0) {
            number -= thousandsPlace * 1000; // update the number
            while (--thousandsPlace >= 0)
                result.append(thousandsR);
        }

        // process hundreds
        int hundredsPlace = number / 100;
        if (hundredsPlace > 0) {
            number -= hundredsPlace * 100; // update the number
            result.append(hundredsR[hundredsPlace - 1]);
        }

        // process tens
        int tensPlace = number / 10;
        if (tensPlace > 0) {
            number -= tensPlace * 10; // update the number
            result.append(tensR[tensPlace - 1]);
        }

        // process ones
        if (number > 0)
            result.append(onesR[number - 1]);

        return result.toString();
    }

    public static String toRomanUpper(int number) {
        return toRoman(number).toUpperCase();
    }

    public static String toRomanLower(int number) {
        return toRoman(number).toLowerCase();
    }
}

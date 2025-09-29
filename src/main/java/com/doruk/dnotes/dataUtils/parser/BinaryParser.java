package com.doruk.dnotes.dataUtils.parser;

import java.util.HashMap;
import java.util.Map;

public abstract class BinaryParser {
    protected static final class Markers {
        public static final byte PARAGRAPH_START = 101;
        public static final byte GLOBALS_START = 102;
        public static final byte GLOBALS_STATE_VALUES = 103;
        public static final byte SEGMENT_START = 104;
        public static final byte SEGMENT_STYLES = 105;
        public static final byte SEGMENT_STATE_VALUES = 106;
        public static final byte SEGMENT_TEXT = 107;
        public static final byte PARAGRAPH_END = 108;
    }

    protected final static Map<String, Byte> codecsByteMap = new HashMap<>();
    protected final static Map<Byte, String> bytesCodecMap = new HashMap<>();

    private static byte totalCodecs = 104; // from 151 to 254 
    private static byte codecByteStart = (byte)151;

    protected BinaryParser(String[] codecsName) {
        if (codecsName.length > totalCodecs)
            throw new IllegalArgumentException("Too many codecs: Max -> " +
                totalCodecs + "; Given -> " + codecsName.length);

        byte cursor = codecByteStart;
        for (String codecName : codecsName) {
            codecsByteMap.put(codecName, cursor);
            bytesCodecMap.put(cursor, codecName);
            cursor++;
        }
    }
}

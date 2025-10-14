package com.doruk.dnotes.dataUtils.parser;

import java.util.HashMap;
import java.util.Map;


/**
 * ORDER OF ENCODING: SKELETON FORMAT OF MARKDOWN
 * 1. PARAGRAPH START MARKER
 * 2. GLOBALS START MARKER
 * 3. GLOBALS LENGTH
 * 4. GLOBALS
 * 5. MODIFIERS START MARKER
 * 6. MODIFIERS LENGTH
 * 7. MODIFIERS
 * 8. SEGMENT START MARKER
 * 9. SEGMENT STYLES MARKER
 * 10. SEGMENT STYLES LENGTH
 * 11. SEGMENT STYLES
 * 12. SEGMENT STATE VALUE MARKERS
 * 13. SEGMENT STATE VALUES LENGTH
 * 14. SEGMENT STATE VALUES
 * 15. SEGMENT TEXT START MARKER
 * 16. SEGMENT TEXT LENGTH
 * 17. SEGMENT TEXT
 * 18. PARAGRAPH END MARKER

 * # ALL LENGTH ARE REPRESENTED IN 7 BITs, WHERE 1 EXTRA BIT IS CONTINUATION BIT
 * # EVERY KEY VALUES LIKE: MODIFIERS, SEGMENT STATE VALUES, HAVE FOLLOWING LENGTH BYTES.
 *
 */

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

    protected final static Map<Enum<?>, Byte> codecsByteMap = new HashMap<>();
    protected final static Map<Byte, Enum<?>> bytesCodecMap = new HashMap<>();

    private final static byte totalCodecs = 104; // from 151 to 254
    private final static byte codecByteStart = (byte)151;

    protected BinaryParser(Enum<?>[] codecsName) {
        if (codecsName.length > totalCodecs)
            throw new IllegalArgumentException("Too many codecs: Max -> " +
                totalCodecs + "; Given -> " + codecsName.length);

        byte cursor = codecByteStart;
        for (Enum<?> codecName : codecsName) {
            codecsByteMap.put(codecName, cursor);
            bytesCodecMap.put(cursor, codecName);
            cursor++;
        }
    }
}

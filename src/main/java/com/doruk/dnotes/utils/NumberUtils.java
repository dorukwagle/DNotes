package com.doruk.dnotes.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class NumberUtils {
    public static long continuousBytesToLong(InputStream stream) throws IOException {
        byte[] ref = new byte[1];
        int value = 0;
        int shift = 0;

        while (stream.read(ref) != -1) {
            value |= (ref[0] & 0x7F) << shift;

            if ((ref[0] & 0x80) == 0)
                break;

            shift += 7;
        }

        return value;
    }

    public static List<Byte> toContinuationBytes(long value) {
        var bytes = new ArrayList<Byte>(10);

        while (true) {
            byte chunk = (byte) (value & 0x7F); // extract 7 bits
            // shift the value right by 7 bits
            value >>>= 7;

            if (value == 0) { // check if it's the last chunk
                bytes.add(chunk);
                break;
            }

            bytes.add((byte) (chunk | 0x80));  // add continuation bit
        }
        return bytes;
    }
}

package com.doruk.dnotes.dataUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataReader {
    public static void readFully(InputStream stream, byte[] bytes) throws IOException {
        var byteStream = new DataInputStream(stream);
        byteStream.readFully(bytes);
    }

    public static void readFully(InputStream stream, byte[] bytes, int length) throws IOException {
        var byteStream = new DataInputStream(stream);
        byteStream.readFully(bytes, 0, length);
    }
}

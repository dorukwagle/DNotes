package com.doruk.dnotes.dataUtils;

import com.doruk.dnotes.exceptions.ProcessingStageException;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamDataUtil {
    public static void readFully(InputStream stream, byte[] bytes) throws IOException {
        var byteStream = new DataInputStream(stream);
        byteStream.readFully(bytes);
    }

    public static void readFully(InputStream stream, byte[] bytes, int length) throws IOException {
        var byteStream = new DataInputStream(stream);
        byteStream.readFully(bytes, 0, length);
    }

    public static void writeFileData(InputStream in, OutputStream out, long fileLength) throws IOException {
        var remainingBytes = fileLength;
        var chunkSize = 2048; // 2kb

        byte[] chunk = new byte[chunkSize];
        while (true) {
            int expectedBytes = (int) Math.min(remainingBytes, chunkSize);
            StreamDataUtil.readFully(in, chunk, expectedBytes);
            out.write(chunk, 0, expectedBytes);

            remainingBytes -= expectedBytes;

            if (remainingBytes == 0)
                break;
            if (remainingBytes < 0)
                throw new ProcessingStageException("File length, and expected length mismatch");
        }
        out.flush();
    }
}

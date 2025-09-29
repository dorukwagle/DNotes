package com.doruk.dnotes.dataUtils.obfuscator;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ObfuscatorStream extends FilterOutputStream {
    private final byte key;

    public ObfuscatorStream(OutputStream out, byte key) {
        super(out);
        this.key = key;
    }
    
    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        out.write(b);
    }
}

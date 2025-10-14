package com.doruk.dnotes.dataUtils.obfuscator;

import com.doruk.dnotes.utils.KeyUtil;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

public class ObfuscatorOutputStream extends FilterOutputStream {
    private final RandomGenerator prng;
    private final byte[] ksBuf;

    private static final int DEFAULT_BUF = 4 * 1024; // chunk 8 kb
    private int cursor = 0;

    public ObfuscatorOutputStream(OutputStream out, byte[] seed) {
        super(Objects.requireNonNull(out));
        Objects.requireNonNull(seed, "seed required");

        byte[] key = KeyUtil.derivePrngKey(seed);
        this.prng = RandomGeneratorFactory.of("Xoroshiro128PlusPlus").create(key);

        this.ksBuf = new byte[DEFAULT_BUF];

        // fill the array for the first time
        prng.nextBytes(ksBuf);
    }

    private byte getNextByte() {
        if (cursor >= ksBuf.length) {
            prng.nextBytes(ksBuf);
            cursor = 0;
        }

        return ksBuf[cursor++];
    }

    @Override
    public void write(int b) throws IOException {
        out.write((b & 0xFF) ^ (getNextByte() & 0xFF));
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        Objects.checkFromIndexSize(off, len, b.length);

        for (int i = off; i < (off + len); i++)
            b[i] ^= (byte)(getNextByte() & 0xFF);

        out.write(b, off, len);
    }

    @Override
    public void write(byte[] b) throws IOException {
        // delegate to canonical method
        write(b, 0, b.length);
    }
}
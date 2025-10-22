package com.doruk.dnotes.dataUtils.obfuscator;

import com.doruk.dnotes.utils.KeyUtil;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

public class ObfuscatorInputStream extends FilterInputStream {
    private final RandomGenerator prng;
    private final byte[] ksBuf;

    private static final int DEFAULT_BUF = 4 * 1024; // chunk 4 kb
    private int cursor = 0;

    public ObfuscatorInputStream(InputStream in, byte[] seed) {
        super(Objects.requireNonNull(in));
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
    public int read() throws IOException {
        return (in.read() & 0xFF) ^ (getNextByte() & 0xFF);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        Objects.checkFromIndexSize(off, len, b.length);
        int l = in.read(b, off, len);

        for (int i = off; i < (off + l); i++)
            b[i] ^= (byte)(getNextByte() & 0xFF);

        return l;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }
}

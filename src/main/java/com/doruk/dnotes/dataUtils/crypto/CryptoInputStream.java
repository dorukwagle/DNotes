package com.doruk.dnotes.dataUtils.crypto;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Objects;

public final class CryptoInputStream extends FilterInputStream {
    private static final byte[] MAGIC = new byte[]{'A','E','S','G'}; // must match output stream
    private static final byte VERSION = 1;
    private static final int SALT_LEN = 16;
    private static final int IV_LEN = 12;
    private static final int ITERATIONS = 100_000;
    private static final int KEY_LEN_BITS = 256;
    private static final int GCM_TAG_BITS = 128;

    private final CipherInputStream cipherIn;

    public CryptoInputStream(InputStream in, String password) throws IOException {
        super(Objects.requireNonNull(in));
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("password required");
        }

        try {
            // --- 1️⃣ Read header ---
            byte[] hdr = new byte[4 + 1 + SALT_LEN + IV_LEN];
            int read = in.readNBytes(hdr, 0, hdr.length);
            if (read != hdr.length)
                throw new IOException("invalid or incomplete header");

            // parse header
            for (int i = 0; i < MAGIC.length; i++) {
                if (hdr[i] != MAGIC[i])
                    throw new IOException("bad magic header");
            }
            byte version = hdr[4];
            if (version != VERSION)
                throw new IOException("unsupported version");

            byte[] salt = Arrays.copyOfRange(hdr, 5, 5 + SALT_LEN);
            byte[] iv = Arrays.copyOfRange(hdr, 5 + SALT_LEN, hdr.length);

            // --- 2️⃣ Derive key ---
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LEN_BITS);
            byte[] keyBytes = skf.generateSecret(spec).getEncoded();
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

            // --- 3️⃣ Init cipher (decrypt) ---
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_BITS, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);

            // --- 4️⃣ Create CipherInputStream ---
            this.cipherIn = new CipherInputStream(in, cipher);

            // wipe sensitive key material
            Arrays.fill(keyBytes, (byte) 0);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException("crypto init failed", e);
        }
    }

    @Override
    public int read() throws IOException {
        return cipherIn.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return cipherIn.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return cipherIn.read(b, off, len);
    }

    @Override
    public void close() throws IOException {
        cipherIn.close();
        super.close();
    }
}

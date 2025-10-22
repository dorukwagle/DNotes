package com.doruk.dnotes.dataUtils.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;

public final class CryptoOutputStream extends FilterOutputStream {
    private static final byte[] MAGIC = new byte[]{'A','E','S','G'}; // 4 bytes
    private static final byte VERSION = 1;
    private static final int SALT_LEN = 16;
    private static final int IV_LEN = 12; // 96-bit recommended for GCM
    private static final int ITERATIONS = 100_000; // adjust per policy
    private static final int KEY_LEN_BITS = 256;
    private static final int GCM_TAG_BITS = 128;

    private final Cipher cipher;
    private final byte[] outBuf = new byte[64 * 1024]; // chunk buffer for update()
    private boolean closed = false;

    /**
     * Create an AES-GCM encrypting OutputStream that derives a key from the provided password.
     * The constructor writes a header to the wrapped stream (magic|ver|salt|iv).
     *
     * @param out the underlying stream to write ciphertext to
     * @param password user-entered password (String). Keep it secret outside.
     * @throws IOException on IO or crypto initialization failure
     */
    public CryptoOutputStream(OutputStream out, String password) throws IOException {
        super(out);
        if (password == null || password.isEmpty()) throw new IllegalArgumentException("password required");

        try {
            SecureRandom rnd = new SecureRandom();
            byte[] salt = new byte[SALT_LEN];
            byte[] iv = new byte[IV_LEN];
            rnd.nextBytes(salt);
            rnd.nextBytes(iv);

            // Derive key: PBKDF2WithHmacSHA256
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LEN_BITS);
            byte[] keyBytes = skf.generateSecret(spec).getEncoded();
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

            // Initialize cipher
            cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_BITS, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);

            // Write header: MAGIC | VERSION | salt | iv
            out.write(MAGIC);
            out.write(VERSION);
            out.write(salt);
            out.write(iv);
            out.flush();

            // wipe keyBytes if possible
            Arrays.fill(keyBytes, (byte)0);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException("crypto init failed", e);
        }
    }

    @Override
    public void write(int b) throws IOException {
        byte[] single = new byte[]{(byte) b};
        write(single, 0, 1);
    }

    @Override
    public void write(byte[] src, int off, int len) throws IOException {
        if (closed) throw new IOException("stream closed");
        int processed = 0;
        try{
            while (processed < len) {
                int chunk = Math.min(len - processed, outBuf.length);
                int outLen = cipher.update(src, off + processed, chunk, outBuf, 0);
                if (outLen > 0) out.write(outBuf, 0, outLen);
                processed += chunk;
            }
        } catch (GeneralSecurityException e) {
            throw new IOException("CryptoOutputStream write failed", e);
        }
    }

    @Override
    public void flush() throws IOException {
        // flush underlying stream (cipher maintains internal buffering)
        out.flush();
    }

    @Override
    public void close() throws IOException {
        if (closed) return;
        closed = true;
        try {
            // get final bytes (including GCM tag) and write them
            byte[] finalBytes = cipher.doFinal();
            if (finalBytes != null && finalBytes.length > 0) out.write(finalBytes);
            out.flush();
        } catch (Exception e) {
            throw new IOException("cipher finalization failed", e);
        } finally {
            try { super.close(); } catch (IOException ignored) {}
        }
    }
}

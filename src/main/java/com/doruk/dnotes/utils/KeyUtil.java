package com.doruk.dnotes.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class KeyUtil {
    public static byte[] generateSeed() {
        byte[] seed = new byte[32];
        new SecureRandom().nextBytes(seed);
        return seed;
    }

    private static void shiftSwap(byte[] key) {
        int pivot = (int) (key.length / 2);

        for (int i = 0; i < pivot; i++) {
            byte temp = key[i];
            key[i] = key[i + pivot];
            key[i + pivot] = temp;
        }
    }

    private static void reverse(byte[] key) {
        int left = 0;
        int right = key.length - 1;

        while (left < right) {
            byte temp = key[left];
            key[left] = key[right];
            key[right] = temp;
            left++;
            right--;
        }
    }

    private static byte[] secretFromSeed(byte[] seed) {
        byte[] key = seed.clone();
        reverse(key);
        shiftSwap(key);
        return key;
    }

    private static byte[] infoSeedFromSeed(byte[] seed) {
        byte[] key = seed.clone();
        shiftSwap(key);
        return key;
    }

    public static byte[] derivePrngKey(byte[] seed) throws RuntimeException {
            try {
                if (seed == null || seed.length == 0)
                    throw new IllegalArgumentException("seed required");

                // generate master key from seed
                var masterKey = secretFromSeed(seed);
                // recreate seed
                seed = infoSeedFromSeed(seed);

                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(masterKey, "HmacSHA256"));
                mac.update(seed);

                var key = mac.doFinal(); // 32 bytes
                // extract random 16 bytes from key
                var prngKey = new byte[16];
                System.arraycopy(key, 0, prngKey, 0, 4);
                System.arraycopy(key, 8, prngKey, 4, 4);
                System.arraycopy(key, 16, prngKey, 8, 4);
                System.arraycopy(key, 26, prngKey, 12, 4);

                return prngKey;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }
}


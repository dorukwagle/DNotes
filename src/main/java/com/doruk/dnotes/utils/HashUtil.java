package com.doruk.dnotes.utils;

import org.mindrot.jbcrypt.BCrypt;
import java.util.Objects;

public final class HashUtil {

    // work factor (cost)
    private static final int COST = 12;

    // Produce the bcrypt hash to store (includes salt & cost)
    public static String hash(String password) {
        Objects.requireNonNull(password);
        return BCrypt.hashpw(password, BCrypt.gensalt(COST));
    }

    // Verify password against stored bcrypt hash (safe)
    public static boolean compareHash(String password, String storedHash) {
        Objects.requireNonNull(password);
        Objects.requireNonNull(storedHash);
        try {
            return BCrypt.checkpw(password, storedHash);
        } catch (Exception e) {
            return false;
        }
    }
}

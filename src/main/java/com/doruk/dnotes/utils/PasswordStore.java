package com.doruk.dnotes.utils;

import java.util.HashMap;
import java.util.Map;

public class PasswordStore {
    private static final Map<String, String> store = new HashMap<>();

    public static void addPassword(String fileId, String password) {
        store.put(fileId, password);
    }

    public static String getPassword(String fileId) {
        return store.get(fileId);
    }

    public static boolean contains(String fileId) {
        return store.containsKey(fileId);
    }
}

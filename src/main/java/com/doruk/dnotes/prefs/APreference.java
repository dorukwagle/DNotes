package com.doruk.dnotes.prefs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.prefs.Preferences;
import com.doruk.dnotes.enums.Preference;
import com.doruk.dnotes.interfaces.IPreference;

public abstract class APreference implements IPreference {
    protected static Preferences prefs;
    private static final Map<Preference, Set<Consumer<Object>>> listeners = new HashMap<>();

    @Override
    public <T> void save(String key, T value) {
        prefs.put(key, value.toString());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void addListener(Preference prefKey, Consumer<T> listener) {
        if (!listeners.containsKey(prefKey))
            listeners.put(prefKey, new HashSet<>());
        
        // Cast to Consumer<Object> is safe because we'll ensure type safety when notifying
        listeners.get(prefKey).add((Consumer<Object>) listener);
    }

    @Override
    public <T> void notifyListeners(Preference prefKey, T value) {
        var listeners = APreference.listeners.get(prefKey);
        if (listeners == null || listeners.isEmpty())
            return;
        
        CompletableFuture.runAsync(() -> {
            for (Consumer<Object> listener : listeners) {
                try {
                    listener.accept(value);
                } catch (ClassCastException e) {
                    // This can happen if the listener was added with a different type
                    System.err.println("Warning: Type mismatch in preference listener for " + prefKey);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T load(String key, T defaultValue) {
        return (T) prefs.get(key, defaultValue.toString());
    }

    @Override
    public void saveString(Preference prefKey, String value) {
        prefs.put(prefKey.name(), value);
    }

    @Override
    public void saveLong(Preference prefKey, long value) {
        prefs.putLong(prefKey.name(), value);
    }

    @Override
    public void saveBoolean(Preference prefKey, boolean value) {
        prefs.putBoolean(prefKey.name(), value);
    }

    @Override
    public void saveDouble(Preference prefKey, double value) {
        prefs.putDouble(prefKey.name(), value);
    }

    @Override
    public String loadString(Preference prefKey, String defaultValue) {
        return prefs.get(prefKey.name(), defaultValue);
    }

    @Override
    public long loadLong(Preference prefKey, long defaultValue) {
        return prefs.getLong(prefKey.name(), defaultValue);
    }

    @Override
    public boolean loadBoolean(Preference prefKey, boolean defaultValue) {
        return prefs.getBoolean(prefKey.name(), defaultValue);
    }

    @Override
    public double loadDouble(Preference prefKey, double defaultValue) {
        return prefs.getDouble(prefKey.name(), defaultValue);
    } 
}

package com.doruk.dnotes.prefs;

import java.util.prefs.Preferences;
import com.doruk.dnotes.enums.Preference;
import com.doruk.dnotes.interfaces.IPreference;

public abstract class APreference implements IPreference {
    protected static Preferences prefs;

    @Override
    public <T> void save(String key, T value) {
        prefs.put(key, value.toString());
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

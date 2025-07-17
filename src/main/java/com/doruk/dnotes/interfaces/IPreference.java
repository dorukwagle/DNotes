package com.doruk.dnotes.interfaces;

import com.doruk.dnotes.enums.Preference;

public interface IPreference {
    <T> void save(String key, T value);
    <T> T load(String key, T defaultValue);

    void saveString(Preference prefKey, String value);
    void saveLong(Preference prefKey, long value);
    void saveBoolean(Preference prefKey, boolean value);
    void saveDouble(Preference prefKey, double value);

    String loadString(Preference prefKey, String defaultValue);
    long loadLong(Preference prefKey, long defaultValue);
    boolean loadBoolean(Preference prefKey, boolean defaultValue);
    double loadDouble(Preference prefKey, double defaultValue);
}

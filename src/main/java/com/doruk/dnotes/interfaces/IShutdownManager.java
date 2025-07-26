package com.doruk.dnotes.interfaces;

public interface IShutdownManager {
    void register(IShutdownListener listener);
    void unregister(IShutdownListener listener);
    void executeListeners();
}

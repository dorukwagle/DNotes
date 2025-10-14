package com.doruk.dnotes.utils;

import java.util.HashSet;
import java.util.Set;

import com.doruk.dnotes.interfaces.IShutdownListener;
import com.doruk.dnotes.interfaces.IShutdownManager;

public class ShutdownManager implements IShutdownManager {
    private static final ShutdownManager instance = new ShutdownManager();
    private final Set<IShutdownListener> listeners = new HashSet<>();

    private ShutdownManager() {}

    public static ShutdownManager getInstance() {
        return instance;
    }

    @Override
    public void register(IShutdownListener listener) {
        listeners.add(listener);
    }

    @Override
    public void unregister(IShutdownListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void executeListeners() {
        listeners.forEach(listener -> listener.onShutdown());
    }
}

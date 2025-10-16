package com.doruk.dnotes.utils;

import java.util.*;

import com.doruk.dnotes.interfaces.IEventManager;

public class EventManager implements IEventManager {
    private static final EventManager instance = new EventManager();
    private final Map<InternalEvent, Set<Runnable>> listeners = new EnumMap<>(InternalEvent.class);

    private EventManager() {}

    public static EventManager getInstance() {
        return instance;
    }

    @Override
    public void register(InternalEvent event, Runnable listener) {
        if (!listeners.containsKey(event))
            listeners.put(event, new HashSet<>());
        listeners.get(event).add(listener);
    }

    @Override
    public void unregister(InternalEvent event, Runnable listener) {
        listeners.get(event).remove(listener);
    }

    @Override
    public void publishEvent(InternalEvent event) {
        listeners.get(event).forEach(Runnable::run);
    }
}

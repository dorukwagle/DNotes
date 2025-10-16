package com.doruk.dnotes.interfaces;

public interface IEventManager {
    enum InternalEvent {
        SHUTDOWN,
        CONTEXT_SWITCH
    }

    void register(InternalEvent event, Runnable listener);
    void unregister(InternalEvent event, Runnable listener);
    void publishEvent(InternalEvent event);
}

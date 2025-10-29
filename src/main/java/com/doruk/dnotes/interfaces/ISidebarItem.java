package com.doruk.dnotes.interfaces;

public interface ISidebarItem {
    enum Type {
        COLLECTION,
        LOCKED_NOTE,
        NOTE
    }

    String getId();
    String getName();

    Type getType();
}

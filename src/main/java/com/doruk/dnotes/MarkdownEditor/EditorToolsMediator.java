package com.doruk.dnotes.MarkdownEditor;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.doruk.dnotes.MarkdownEditor.enums.EditorToolsEvent;

public class EditorToolsMediator {
    private static final Map<EditorToolsEvent, List<Runnable>> handlers = new EnumMap<>(EditorToolsEvent.class);

    public static void subscribe(EditorToolsEvent event, Runnable handler) {
        handlers.computeIfAbsent(event, _ -> new ArrayList<>())
            .add(handler);
    }

    public static void publish(EditorToolsEvent event) {
        CompletableFuture.runAsync(() -> 
            handlers.get(event).forEach(handler -> handler.run()));
    }
}

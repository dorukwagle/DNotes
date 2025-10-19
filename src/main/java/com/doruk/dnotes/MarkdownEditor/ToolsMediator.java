package com.doruk.dnotes.MarkdownEditor;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.doruk.dnotes.MarkdownEditor.enums.ToolsEvent;

public class ToolsMediator {
    private static final Map<ToolsEvent, List<Consumer<Object[]>>> handlers = new EnumMap<>(ToolsEvent.class);

    public static void subscribe(ToolsEvent event, Consumer<Object[]> handler) {
        handlers.computeIfAbsent(event, _ -> new ArrayList<>())
            .add(handler);
    }

    public static void publish(ToolsEvent event, Object... args) {
        handlers.get(event).forEach(handler -> handler.accept(args));
    }

    public static void cleanup() {
        handlers.clear();
    }
}

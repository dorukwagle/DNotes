package com.doruk.dnotes.MarkdownEditor.lists;

public class ListManager {
    private static ListManager instance;

    private ListManager() {}

    public static ListManager getInstance() {
        if (instance == null)
            instance = new ListManager();
        return instance;
    }

    public String createListNodeAndGet(int paragraphIndex) {
        return null;        
    }

    public String createListNodeAndGet(int... paragraphIndexes) {
        return null;
    }

    public String addListItemAndGet(String listId, int level, int paragraphIndex) {
        return null;
    }

    public void reComputeNode(String nodeId) {

    }
}

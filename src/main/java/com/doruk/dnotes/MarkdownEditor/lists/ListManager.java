package com.doruk.dnotes.MarkdownEditor.lists;

import java.util.HashMap;
import java.util.Map;

import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.dto.ParagraphListItemInfo;
import com.doruk.dnotes.MarkdownEditor.interfaces.FXTextEditor;
import com.doruk.dnotes.MarkdownEditor.utils.ParagraphStyleHelper;

public class ListManager {
    private static ListManager instance;
    private static FXTextEditor editor;
    Map<String, NumberListNode> listMap;


    private ListManager() {
        ListManager.editor = Factory.getFXTextEditor();
        this.listMap = new HashMap<>();
    }

    public static ListManager getInstance() {
        if (instance == null)
            instance = new ListManager();

        return instance;
    }

    // check if it's the last line in the document
    private boolean isLastDocumentItem(int paragraphIndex) {
        var area = editor.getArea();
        return area.getParagraphs().size() - 1 == paragraphIndex;
    }

    private int findListStartAndFillLevel(int referenceParIndex, Map<Integer, Integer> holder) {
        // if 0, then terminate
        if (referenceParIndex == 0)
            return 0;
        
        var area = editor.getArea();
        var parIndex = referenceParIndex - 1; // exclude the reference
        var listId = area.getParagraph(parIndex).getParagraphStyle().numberListId;
        
        while (true) {
            var curPar = area.getParagraph(parIndex);
            var curStyle = curPar.getParagraphStyle();

            holder.put(parIndex, curStyle.level);
            
            if (!listId.equals(curStyle.numberListId) || isLastDocumentItem(parIndex))
                break;
            
            --parIndex;
        }
        
        return parIndex;
    }

    private int findListEndAndFillLevel(int referenceParIndex, Map<Integer, Integer> holder) {
        var area = editor.getArea();
        var parIndex = referenceParIndex;
        var listId = area.getParagraph(parIndex).getParagraphStyle().numberListId;

        while (true) {
            var curPar = area.getParagraph(parIndex);
            var curStyle = curPar.getParagraphStyle();

            holder.put(parIndex, curStyle.level);
            
            if (!listId.equals(curStyle.numberListId) || isLastDocumentItem(parIndex))
                break;
            
            ++parIndex;
        }
        
        return parIndex;
    }

    private int fillLevel(int fromParIndex, int toParIndex, Map<Integer, Integer> holder) {
        var area = editor.getArea();

        for (int i = fromParIndex; i <= toParIndex; i++) {
            var curPar = area.getParagraph(i);
            var curStyle = curPar.getParagraphStyle();
            holder.put(i, curStyle.level);
        }
        
        return toParIndex;
    }

    public void createOrRemoveListNode(ParagraphListItemInfo itemInfo) {
        var listState = new ParagraphListItemInfo(
            itemInfo.oldStyle,
            itemInfo.paragraphIndex,
            1,
            1,
            false,
            itemInfo.listType
        );

        var listId = this.generateListId();
        this.listMap.put(listId, new NumberListNode());

        // set id to null, if list type is null
        var style = ParagraphStyleHelper.withListNode(listState, 
            itemInfo.listType == null ? null : listId);
        editor.getArea().setParagraphStyle(itemInfo.paragraphIndex, style);
    }

    public void createListNode(int fromParIndex, int toParIndex) {
        // in a loop, add each paragraph style, level, number etc, and list id
    }

    public void addListItem(String listId, int level, int paragraphIndex) {
        
    }

    public void removeListItems(String listId, int paragraphIndex) {
        
    }

    // public void releaseItems() {}

    public void reComputeNode(String nodeId) {

    }

    private String generateListId() {
        return "list_" + System.currentTimeMillis();
    }
}

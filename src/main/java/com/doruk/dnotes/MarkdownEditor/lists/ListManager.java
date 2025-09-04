package com.doruk.dnotes.MarkdownEditor.lists;

import java.util.HashMap;
import java.util.Map;

import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.dto.ParagraphListItemInfo;
import com.doruk.dnotes.MarkdownEditor.enums.ParagraphType;
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

        // set id to null, if list type is null
        var apply = itemInfo.getApply();
        var style = ParagraphStyleHelper.withListNode(listState, apply ? listId : null, apply);
        
        editor.getArea().setParagraphStyle(itemInfo.paragraphIndex, style);
    }

    public void createListNode(ParagraphType listType, int fromParIndex, int toParIndex, boolean apply) {
        // in a loop, add each paragraph style, level, number etc, and list id
        var listId = this.generateListId();

        for (int i = fromParIndex; i <= toParIndex; i++) {
            var currentStyle = editor.getArea().getParagraph(i).getParagraphStyle();
            var state = new ParagraphListItemInfo(currentStyle, i, listType);
            var newStyle = ParagraphStyleHelper.withListNode(state, apply ? listId : null, apply);
            editor.getArea().setParagraphStyle(i, newStyle);
        }
    }

    public void computeListNumbering(String listId, int referenceParIndex) {
        var indexAndLevel = new HashMap<Integer, Integer>();
        
        var fromParIndex = this.findListStartAndFillLevel(referenceParIndex, indexAndLevel);
        var toParIndex = this.findListEndAndFillLevel(referenceParIndex, indexAndLevel);

        var calculation = NumberListNode.calculateItemsNumbering(fromParIndex, toParIndex, indexAndLevel);
        var isLevelPreserved = calculation.getValue();
        var indexNumberMap = calculation.getKey();

        for (int i = fromParIndex; i <= toParIndex; i++) {
            var currentStyle = editor.getArea().getParagraph(i).getParagraphStyle();
            // skip if already correct numbering
            if (currentStyle.lineCount == indexNumberMap.get(i) && 
                currentStyle.level == indexAndLevel.get(i))
                continue;
            
            var state = new ParagraphListItemInfo(
                currentStyle,
                i,
                isLevelPreserved ? indexAndLevel.get(i) : 1,
                indexNumberMap.get(i)
            );
            var newStyle = ParagraphStyleHelper.withListNode(state, listId, true);
            editor.getArea().setParagraphStyle(i, newStyle);
        }
    }

    private String generateListId() {
        return "list_" + System.currentTimeMillis();
    }
}

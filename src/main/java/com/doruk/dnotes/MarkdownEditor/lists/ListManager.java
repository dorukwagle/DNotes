package com.doruk.dnotes.MarkdownEditor.lists;

import java.util.HashMap;
import java.util.Map;

import com.doruk.dnotes.MarkdownEditor.Factory;
import com.doruk.dnotes.MarkdownEditor.docstyle.ParagraphStyle;
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

    private int findListStartAndFillLevel(int referenceParIndex, Map<Integer, Integer> holder) {
        // if 0, then terminate
        if (referenceParIndex == 0)
            return 0;
        
        var area = editor.getArea();
        var parIndex = referenceParIndex - 1; // exclude the reference
        var listId = area.getParagraph(parIndex).getParagraphStyle().numberListId;
        
        while (parIndex >= 0) {
            var curPar = area.getParagraph(parIndex);
            var curStyle = curPar.getParagraphStyle();
            
            if (!listId.equals(curStyle.numberListId))
                break;
            
            holder.put(parIndex, curStyle.level);
            
            --parIndex;
        }
        // end correction: +1
        return parIndex + 1;
    }

    private int findListEndAndFillLevel(int referenceParIndex, Map<Integer, Integer> holder) {
        var area = editor.getArea();
        var parIndex = referenceParIndex;
        var listId = area.getParagraph(parIndex).getParagraphStyle().numberListId;
        var totalParagraphs = area.getParagraphs().size();
        
        while (parIndex < totalParagraphs) {
            var curPar = area.getParagraph(parIndex);
            var curStyle = curPar.getParagraphStyle();
            
            if (!listId.equals(curStyle.numberListId))
                break;
            
            holder.put(parIndex, curStyle.level);
            
            ++parIndex; 
        }
        
        // end correction of 1
        return parIndex - 1;
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

        // if it's unapply, then remove all items below it
        var indexAndLevel = new HashMap<Integer, Integer>();
        var toParIndex = this.findListEndAndFillLevel(itemInfo.paragraphIndex, indexAndLevel);

        // if the reference/current paragraph index is the only index, just return
        if (indexAndLevel.size() == 1)
            return;
        
        for (int i = itemInfo.paragraphIndex + 1; i <= toParIndex; i++) 
            editor.getArea().setParagraphStyle(i, style);
    }

    public void createOrRemoveListNode(ParagraphType listType, int fromParIndex, int toParIndex, boolean apply) {
        // in a loop, add each paragraph style, level, number etc, and list id
        var listId = this.generateListId();

        for (int i = fromParIndex; i <= toParIndex; i++) {
            var currentStyle = editor.getArea().getParagraph(i).getParagraphStyle();
            var state = new ParagraphListItemInfo(currentStyle, i, listType);
            var newStyle = ParagraphStyleHelper.withListNode(state, apply ? listId : null, apply);
            editor.getArea().setParagraphStyle(i, newStyle);
        }
    }

    public void computeListNumbering(ParagraphType listType, String listId, int referenceParIndex) {
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
                indexNumberMap.get(i),
                false,
                listType
            );
            var newStyle = ParagraphStyleHelper.withListNode(state, listId, true);
            editor.getArea().setParagraphStyle(i, newStyle);
        }
    }

    public void increaseListOffset(ParagraphType listType, String listId, int listStartIndex) {
        
    }

    public void increaseItemLevel(ParagraphType listType, String listId, int itemParIndex, ParagraphStyle curStyle) {
        this.adjustItemLevel(listType, listId, itemParIndex, curStyle, 1);
    }
    
    public void decreaseItemLevel(ParagraphType listType, String listId, int itemParIndex, ParagraphStyle curStyle) {
        this.adjustItemLevel(listType, listId, itemParIndex, curStyle, -1);
    }

    private void adjustItemLevel(ParagraphType listType, String listId, int itemParIndex, ParagraphStyle curStyle, int adjustBy) {
        var newStyle = ParagraphStyleHelper.withListNode(
            new ParagraphListItemInfo(
                curStyle, 
                itemParIndex, 
                curStyle.level + adjustBy,
                curStyle.lineCount,
                false,
                listType
            ), 
            listId, 
            curStyle.offset, 
            true
        );
        editor.getArea().setParagraphStyle(itemParIndex, newStyle);
        this.computeListNumbering(listType, listId, itemParIndex);
    }

    private String generateListId() {
        return "list_" + System.currentTimeMillis();
    }
}

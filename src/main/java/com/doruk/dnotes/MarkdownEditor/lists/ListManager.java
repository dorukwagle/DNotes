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

    private void preventHistory(Runnable task) {
        editor.getSuspendableUndo().suspendWhile(task::run);
    }

    private int findListStartAndFillLevel(int referenceParIndex, Map<Integer, Integer> holder) {
        // if 0, then terminate
        if (referenceParIndex == 0)
            return 0;
        
        var area = editor.getArea();
        var parIndex = referenceParIndex - 1; // exclude the reference
        var listId = area.getParagraph(parIndex).getParagraphStyle().numberListId;

        // if list id is null, then probably the reference item is the first list item
        if (listId == null)
            return referenceParIndex;
        
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

    private void scanSelectedAndFillLevel(int fromParIndex, int toParIndex, Map<Integer, Integer> holder) {
        var area = editor.getArea();
        var areListItems = true;

        for (int i = fromParIndex; i <= toParIndex; i++) {
            var curPar = area.getParagraph(i);
            var curStyle = curPar.getParagraphStyle();
            
            areListItems = curStyle.numberListId != null;

            holder.put(i, curStyle.level);
        }

        // if list items if false, it means some items have stale list levels. 
        // so ignore leveling, just make first line level anything other than 1, 
        // rest will be handled by the NumberListNode
        if (!areListItems)
            holder.put(fromParIndex, Integer.MAX_VALUE);
    }

    private void removeListStyle(int parIndex, ParagraphStyle style) {
        var indexAndLevel = new HashMap<Integer, Integer>();
        var toParIndex = this.findListEndAndFillLevel(parIndex, indexAndLevel);
        
        // remove all list item numberings downwards, including reference
        for (int i = parIndex; i <= toParIndex; i++) {
            final int index = i;
            this.preventHistory(() -> editor.getArea().setParagraphStyle(index, style));
        }
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
        
        if (apply)
            this.preventHistory(() -> editor.getArea().setParagraphStyle(itemInfo.paragraphIndex, style));
        else
            this.removeListStyle(itemInfo.paragraphIndex, style);
    }

    private void applyOrRemoveListNumbering(
            ParagraphType listType, 
            String listId, 
            int fromParIndex, 
            int toParIndex, 
            Map<Integer, Integer>
            indexAndLevel, 
            boolean apply, 
            boolean skipCorrectNumbering) {
        var calculation = NumberListNode.calculateItemsNumbering(fromParIndex, toParIndex, indexAndLevel);
        var isLevelPreserved = calculation.getValue();
        var indexNumberMap = calculation.getKey();

        for (int i = fromParIndex; i <= toParIndex; i++) {
            var currentStyle = editor.getArea().getParagraph(i).getParagraphStyle();
            // skip if already correct numbering
            if (
                skipCorrectNumbering && 
                currentStyle.lineCount == indexNumberMap.get(i) && 
                currentStyle.level == indexAndLevel.get(i)
            )
                continue;
            
            var state = new ParagraphListItemInfo(
                currentStyle,
                i,
                isLevelPreserved ? indexAndLevel.get(i) : 1,
                indexNumberMap.get(i),
                false,
                listType
            );
            final int index = i; 
            var newStyle = ParagraphStyleHelper.withListNode(state, apply ? listId : null, apply);
            this.preventHistory(() -> editor.getArea().setParagraphStyle(index, newStyle));
        }
    }

    public void createOrRemoveListNode(ParagraphType listType, int fromParIndex, int toParIndex, boolean apply) {
        var indexAndLevel = new HashMap<Integer, Integer>();
        this.scanSelectedAndFillLevel(fromParIndex, toParIndex, indexAndLevel);

        this.applyOrRemoveListNumbering(
            listType, 
            this.generateListId(),
            fromParIndex, 
            toParIndex, 
            indexAndLevel, 
            apply, 
            false
        );
    }

    public void computeListNumbering(ParagraphType listType, String listId, int referenceParIndex) {
        var indexAndLevel = new HashMap<Integer, Integer>();
        
        var fromParIndex = this.findListStartAndFillLevel(referenceParIndex, indexAndLevel);
        var toParIndex = this.findListEndAndFillLevel(referenceParIndex, indexAndLevel);

        this.applyOrRemoveListNumbering(
            listType, 
            listId,
            fromParIndex, 
            toParIndex, 
            indexAndLevel, 
            true, 
            true
        );
    }

    public void adjustListOffset(ParagraphType listType, String listId, int listStartIndex, int adjustBy) {
        var area = editor.getArea();
        var parIndex = listStartIndex;
        var totalParagraphs = area.getParagraphs().size();
        var offset = area.getParagraph(parIndex).getParagraphStyle().offset + adjustBy;

        while (parIndex < totalParagraphs) {
            var curPar = area.getParagraph(parIndex);
            var curStyle = curPar.getParagraphStyle();
            
            if (!listId.equals(curStyle.numberListId))
                break;
            
            var state = new ParagraphListItemInfo(
                curStyle,
                parIndex,
                curStyle.level,
                curStyle.lineCount,
                false,
                listType
            );
            final int index = parIndex; 
            var newStyle = ParagraphStyleHelper.withListNode(state, listId, offset, true);
            this.preventHistory(() -> editor.getArea().setParagraphStyle(index, newStyle));
            
            ++parIndex; 
        }
    }

    public void increaseListOffset(ParagraphType listType, String listId, int listStartIndex) {
        this.adjustListOffset(listType, listId, listStartIndex, 1);
    }

    public void decreaseListOffset(ParagraphType listType, String listId, int listStartIndex) {
        this.adjustListOffset(listType, listId, listStartIndex, -1);
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
        this.preventHistory(() -> editor.getArea().setParagraphStyle(itemParIndex, newStyle));
        this.computeListNumbering(listType, listId, itemParIndex);
    }

    private String generateListId() {
        return "list_" + System.currentTimeMillis();
    }
}

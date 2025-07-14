package com.doruk.dnotes.interfaces;

import javafx.scene.control.Button;

import java.util.List;
import java.util.function.Function;

import com.doruk.dnotes.dto.CollectionDto;
import com.doruk.dnotes.dto.SearchControlsDto;

import javafx.scene.Parent;

public interface IBookView extends IView {
    Button getBackButton();
    Button getEditorButton();
    void displayEditor(Parent editorView);
    void setSidebarItems(List<CollectionDto> items);
    void setSidebarItemOnSelect(Function<CollectionDto, Void> onSelect);
    SearchControlsDto getSidebarSearchControls();
}

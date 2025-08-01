package com.doruk.dnotes.interfaces;

import javafx.scene.control.Button;

import java.util.List;
import java.util.function.Consumer;

import com.doruk.dnotes.dto.BookPageDto;
import com.doruk.dnotes.dto.SearchControlsDto;

import javafx.scene.Parent;

public interface IBookView extends IView {
    Button getBackButton();
    void displayEditor(Parent editorView);
    void setSidebarItems(List<BookPageDto> items);
    void setSidebarItemOnSelect(Consumer<BookPageDto> onSelect);
    SearchControlsDto getSidebarSearchControls();
    void setSelectedSidebarItem(BookPageDto item);
}

package com.doruk.dnotes.interfaces;

import com.doruk.dnotes.views.components.Sidebar;
import javafx.scene.control.Button;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.doruk.dnotes.dto.BookPageDto;
import com.doruk.dnotes.dto.SearchControlsDto;

import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;

public interface IBookView extends IView {
    Button getBackButton();
    void displayEditor(Parent editorView);
    void setSidebarItems(List<BookPageDto> items);
    void setSidebarItemOnSelect(Consumer<BookPageDto> onSelect);
    SearchControlsDto getSidebarSearchControls();
    void setSelectedSidebarItem(BookPageDto item);
    Button getNewNoteButton();
    void setSidebarItemOnRightClick(BiConsumer<MouseEvent, BookPageDto> onRightClick);
}

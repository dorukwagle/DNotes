package com.doruk.dnotes.interfaces;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.doruk.dnotes.dto.BookDto;
import com.doruk.dnotes.dto.CollectionDto;
import com.doruk.dnotes.dto.SearchControlsDto;
import com.doruk.dnotes.enums.MenuItems;

import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;

public interface IHomeView extends IView {
    void setSidebarItems(List<CollectionDto> items);
    void setSidebarItemOnSelect(Consumer<CollectionDto> onSelect);
    void setSidebarItemOnRightClick(BiConsumer<MouseEvent, CollectionDto> onRightClick);
    SearchControlsDto getSidebarSearchControls();
    SearchControlsDto getSearchControls();
    void setBooks(List<BookDto> books);
    void setBooksOnSelect(Consumer<BookDto> onSelect);
    void setOnCardOptionsClick(Consumer<BookDto> onDeleteBtnClick);
    void setSelectedSidebarItem(CollectionDto item);
    void setMenuItemsOnClick(Consumer<MenuItems> onClick);
    ObservableList<CollectionDto> getSidebarItems();
    ObservableList<BookDto> getBookItems();
    void setOnAddBook(Runnable onAddBook);
    void setOnAddCollection(Runnable onAddCollection);
}

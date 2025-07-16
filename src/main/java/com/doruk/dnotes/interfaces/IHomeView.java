package com.doruk.dnotes.interfaces;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.doruk.dnotes.dto.BookDto;
import com.doruk.dnotes.dto.CollectionDto;
import com.doruk.dnotes.dto.SearchControlsDto;
import com.doruk.dnotes.enums.MenuItems;

public interface IHomeView extends IView {
    void setSidebarItems(List<CollectionDto> items);
    void setSidebarItemOnSelect(Function<CollectionDto, Void> onSelect);
    SearchControlsDto getSidebarSearchControls();
    SearchControlsDto getSearchControls();
    void setBooks(List<BookDto> books);
    void setBooksOnSelect(Function<BookDto, Void> onSelect);
    void setOnCardsDeleteBtnClick(Function<BookDto, Void> onDeleteBtnClick);
    void setSelectedSidebarItem(CollectionDto item);
    void setMenuItemsOnClick(Consumer<MenuItems> onClick);
}

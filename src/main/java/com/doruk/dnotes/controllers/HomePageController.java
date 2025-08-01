package com.doruk.dnotes.controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.dto.BookDto;
import com.doruk.dnotes.dto.CollectionDto;
import com.doruk.dnotes.dto.PaginationParams;
import com.doruk.dnotes.dto.SearchControlsDto;
import com.doruk.dnotes.enums.MenuItems;
import com.doruk.dnotes.enums.Preference;
import com.doruk.dnotes.enums.SortBy;
import com.doruk.dnotes.enums.SortOrder;
import com.doruk.dnotes.interfaces.IController;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.interfaces.IPreference;
import com.doruk.dnotes.store.BookStore;
import com.doruk.dnotes.interfaces.IHomeView;
import com.doruk.dnotes.interfaces.IModel;

import javafx.application.Platform;
import javafx.scene.Parent;

public class HomePageController implements IController {

    private final IHomeView homePageView;
    private final INavigationController navigationController;
    private final IModel<CollectionDto> collectionModel;
    private final IModel<BookDto> bookModel;
    private List<CollectionDto> collections;
    private List<BookDto> books;
    private CollectionDto selectedCollection;
    private PaginationParams collectionParams;
    private PaginationParams bookParams;
    private boolean collectionLock = false;
    private boolean bookLock = false;

    private IPreference preference;

    private enum UpdateStateAction {
        Update,
        Delete
    }

    public HomePageController(IHomeView view, INavigationController navigationController) {
        this.homePageView = view;
        this.navigationController = navigationController;
        this.collectionModel = DIFactory.createCollectionModel();
        this.bookModel = DIFactory.createBookModel();
        this.collectionParams = new PaginationParams();
        this.bookParams = new PaginationParams();
        this.preference = DIFactory.createGlobalPreference();

        renderCollections();
        setupActions();

        // open last collection if remember state is enabled
        var rememberState = this.preference.loadBoolean(Preference.RememberAppState, false);
        if (!rememberState)
            return;

        var lastCollectionId = this.preference.loadString(Preference.LastOpenedCollectionId, "");
        if (lastCollectionId.isEmpty())
            return;
            
        var collection = new CollectionDto(lastCollectionId, "", "");
        this.clickOnCollection(collection);
    }

    private void renderCollections() {
        this.collections = this.collectionModel.getAll(this.collectionParams);

        if (this.collections.isEmpty())
            this.homePageView.setPlaceholder("No Collections found!, Create one to get started...");
        else
            this.homePageView.setPlaceholder("Select a collection to view it's books...!");

        // if searching, override the placeholder
        if (this.collectionLock)
            this.homePageView.setPlaceholder("Search in Progress!. Click on a result to view...");

        this.homePageView.setSidebarItems(this.collections);
    }

    private void addToCollectionState(CollectionDto collection) {
        // check if first entry, need to rerender the views
        if (this.collections.isEmpty()) {
            this.renderCollections();
            return;
        }

        this.collections.addFirst(collection);
        this.homePageView.setSidebarItems(this.collections);
        this.clickOnCollection(collection); // open the collection
    }

    private void addToBookState(BookDto book) {
        // check if first entry, need to rerender the views
        if (this.books.isEmpty()) {
            this.openCollection(this.selectedCollection);
            return;
        }

        this.books.addFirst(book);
        this.homePageView.setBooks(this.books);
    }

    private void updateCollectionState(CollectionDto collection, UpdateStateAction action) {
        // remove the duplicate collection
        this.collections = this.collections.stream()
                .filter(c -> !c.getId().equals(collection.getId()))
                .collect(Collectors.toList());

        // check if it was the last item, if so render collections
        if (action == UpdateStateAction.Delete && this.collections.isEmpty()) {
            this.renderCollections();
            return;
        }

        // add updated collection to first
        if (action == UpdateStateAction.Update)
            this.collections.addFirst(collection);

        this.homePageView.setSidebarItems(this.collections);
        Platform.runLater(() -> 
            this.homePageView.setSelectedSidebarItem(collection)
        );
    }

    private void updateBookState(BookDto book, UpdateStateAction action) {
        // remove the duplicate book
        this.books = this.books.stream()
                .filter(b -> !b.getId().equals(book.getId()))
                .collect(Collectors.toList());

        // add updated book to first
        if (action == UpdateStateAction.Update)
            this.books.addFirst(book);

        // check if it was the last item
        if (action == UpdateStateAction.Delete && this.books.isEmpty())
            this.openCollection(this.selectedCollection);

        this.homePageView.setBooks(this.books);
    }

    private void createCollection() {
        var model = DIFactory.createPromptModal("Create Collection", "Enter collection name", "Name: ");
        var res = model.showAndWait();

        if (!res.isPresent() || res.get().trim().isEmpty())
            return;

        var collection = this.collectionModel.add(new CollectionDto("", res.get(), ""));
        this.addToCollectionState(collection);
    }

    private void createBook() {
        if (this.selectedCollection == null) {
            DIFactory.createConfirmationModal("No Collection Selected", "Please select a collection to create a book")
                    .showAndWait();
            return;
        }

        var model = DIFactory.createPromptModal("Create Book", "Enter book name", "My Book");
        var res = model.showAndWait();

        if (!res.isPresent() || res.get().trim().isEmpty())
            return;

        var book = this.bookModel.add(new BookDto(
                "",
                this.selectedCollection.getId(),
                res.get(),
                ""));

        this.addToBookState(book);
    }

    private void handleCollectionRightClick(CollectionDto collectionDto) {
        var modal = DIFactory.createOptionsModal();
        modal.setInputText(collectionDto.getName());

        modal.setOnDeleteAction(() -> {
            if (!modal.isConfirmationChecked())
                return;

            this.collectionModel.softDelete(collectionDto.getId());
            this.updateCollectionState(collectionDto, UpdateStateAction.Delete);
        });

        modal.setOnUpdateAction(() -> {
            var collection = this.collectionModel.update(new CollectionDto(
                    collectionDto.getId(),
                    modal.getInputText(),
                    ""));
            this.updateCollectionState(collection, UpdateStateAction.Update);
        });

        modal.showAndWait();
    }

    private void handleCardsOptionsClick(BookDto bookDto) {
        var modal = DIFactory.createOptionsModal();
        modal.setInputText(bookDto.getTitle());

        modal.setOnDeleteAction(() -> {
            if (!modal.isConfirmationChecked())
                return;

            this.bookModel.softDelete(bookDto.getId());
            this.updateBookState(bookDto, UpdateStateAction.Delete);
        });

        modal.setOnUpdateAction(() -> {
            var book = this.bookModel.update(new BookDto(
                bookDto.getId(),
                this.selectedCollection.getId(),
                modal.getInputText(),
                ""
            ));
            this.updateBookState(book, UpdateStateAction.Update);
        });

        modal.showAndWait();
    }

    private void openCollection(CollectionDto collectionDto) {
        // if collection is empty, just return
        if (this.collections.isEmpty())
            return;

        var col = this.collections.stream()
            .filter(c -> c.getId().equals(collectionDto.getId()))
            .findFirst()
            .orElse(null);

        // check if the recently clicked collection is deleted
        if (col == null) {
            this.homePageView.setSelectedSidebarItem(null);
            this.homePageView.setPlaceholder("Opps!, The collection is deleted, please select a new one.");
            return;
        }

        this.books = this.bookModel.ofParentId(collectionDto.getId())
                .getAll(this.bookParams);

        if (this.books.isEmpty())
            this.homePageView.setPlaceholder("No books found!, Create one to get started...");
        else
            this.homePageView.setPlaceholder(null); // remove the placeholder
        
        // if searching, override the placeholder
        if (this.bookLock && this.books.isEmpty())
            this.homePageView.setPlaceholder("No Matching book found...!");

        this.homePageView.setBooks(this.books);
        this.selectedCollection = collectionDto;
        preference.saveString(Preference.LastOpenedCollectionId, collectionDto.getId());
    }

    private void searchBooks(SearchControlsDto controls) {
        var params = new PaginationParams(
            controls.getSearchField().getText(),
            controls.getSortByToggle().isSelected() ? SortBy.Name : SortBy.Date,
            controls.getSortOrderToggle().isSelected() ? SortOrder.Ascending : SortOrder.Descending
        );

        // if search field is empty, unlock the collection
        this.bookLock = !controls.getSearchField().getText().trim().isEmpty();
        this.bookParams = params;
        this.openCollection(this.selectedCollection);
    }

    private void searchCollections(SearchControlsDto controls) {
        var params = new PaginationParams(
            controls.getSearchField().getText(),
            controls.getSortByToggle().isSelected() ? SortBy.Name : SortBy.Date,
            controls.getSortOrderToggle().isSelected() ? SortOrder.Ascending : SortOrder.Descending
        );

        // if search field is empty, unlock the collection
        this.collectionLock = !controls.getSearchField().getText().trim().isEmpty();
        this.collectionParams = params;

        this.selectedCollection = null;
        this.renderCollections();
    }

    private void setupActions() {
        homePageView.setBooksOnSelect(book -> {
            BookStore.setSelectedBook(book);
            this.navigationController.goToBooksPage();
        });

        homePageView.setSidebarItemOnSelect(this::openCollection);
        homePageView.setSidebarItemOnRightClick(this::handleCollectionRightClick);
        homePageView.setOnAddBook(this::createBook);
        homePageView.setOnAddCollection(this::createCollection);
        homePageView.setOnCardOptionsClick(this::handleCardsOptionsClick);

        homePageView.setMenuItemsOnClick(menuItem -> {
            switch (menuItem) {
                case MenuItems.Backup -> System.out.println("navigating to backup page");
                case MenuItems.Restore -> System.out.println("navigating to restore page");
                case MenuItems.Trash -> System.out.println("navigating to trash page");
                case MenuItems.Preferences -> this.navigationController.goToPreferencePage();
            }
        });

        // set search bar controls actions
        var sidebarSearchControls = homePageView.getSidebarSearchControls();
        sidebarSearchControls.getSearchField()
            .setOnAction(_ -> this.searchCollections(sidebarSearchControls));
        sidebarSearchControls.getSortByToggle()
            .setOnAction(_ -> this.searchCollections(sidebarSearchControls));
        sidebarSearchControls.getSortOrderToggle()
            .setOnAction(_ -> this.searchCollections(sidebarSearchControls));

        var searchControls = homePageView.getSearchControls();
        searchControls.getSearchField()
            .setOnAction(_ -> this.searchBooks(searchControls));
        searchControls.getSortByToggle()
            .setOnAction(_ -> this.searchBooks(searchControls));
        searchControls.getSortOrderToggle()
            .setOnAction(_ -> this.searchBooks(searchControls));
    }

    private void clickOnCollection(CollectionDto collectionDto) {
        Platform.runLater(() -> {
            this.homePageView.setSelectedSidebarItem(collectionDto);
            this.openCollection(collectionDto);
        });
    }

    @Override
    public Parent getView() {
        return homePageView.getView();
    }
}

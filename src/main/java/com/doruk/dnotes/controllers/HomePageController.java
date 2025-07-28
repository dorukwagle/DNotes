package com.doruk.dnotes.controllers;


import java.util.Dictionary;
import java.util.List;
import java.util.stream.Collectors;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.dto.BookDto;
import com.doruk.dnotes.dto.CollectionDto;
import com.doruk.dnotes.dto.PaginationParams;
import com.doruk.dnotes.enums.MenuItems;
import com.doruk.dnotes.interfaces.IController;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.store.BookStore;
import com.doruk.dnotes.interfaces.IHomeView;
import com.doruk.dnotes.interfaces.IModel;

import javafx.scene.Parent;

public class HomePageController implements IController {

    private final IHomeView homePageView;
    private final INavigationController navigationController;
    private final IModel<CollectionDto> collectionModel;
    private final IModel<BookDto> bookModel;
    private List<CollectionDto> collections;
    private List<BookDto> books;
    private CollectionDto selectedCollection;

    private enum UpdateStateAction {
        Update,
        Delete
    }

    public HomePageController(IHomeView view, INavigationController navigationController) {
        this.homePageView = view;
        this.navigationController = navigationController;
        this.collectionModel = DIFactory.createCollectionModel();
        this.bookModel = DIFactory.createBookModel();

        renderCollections();
        setupActions();
    }
    
    private void renderCollections() {
        this.collections = this.collectionModel.getAll(new PaginationParams());

        if (this.collections.isEmpty())
            this.homePageView.setPlaceholder("No Collections found!, Create one to get started...");
        else
            this.homePageView.setPlaceholder("Select a collection to view it's books...!");
        
        this.homePageView.setSidebarItems(this.collections);
    }

    private void addToCollectionState(CollectionDto collection) {
        this.collections.addFirst(collection);
        this.homePageView.setSidebarItems(this.collections);
    }

    private void addToBookState(BookDto book) {
        this.books.addFirst(book);
        this.homePageView.setBooks(this.books);
    }

    private void updateCollectionState(CollectionDto collection, UpdateStateAction action) {
        // remove the duplicate collection
        this.collections = this.collections.stream()
        .filter(c -> !c.getId().equals(collection.getId()))
        .collect(Collectors.toList());
        
        // add updated collection to first
        if (action == UpdateStateAction.Update)
            this.collections.addFirst(collection);

        this.homePageView.setSidebarItems(this.collections);
    }

    private void updateBookState(BookDto book, UpdateStateAction action) {
        // remove the duplicate book
        this.books = this.books.stream()
            .filter(b -> !b.getId().equals(book.getId()))
            .collect(Collectors.toList());
        
        // add updated book to first
        if (action == UpdateStateAction.Update)
            this.books.addFirst(book);
        
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
            DIFactory.createConfirmationModal("No Collection Selected", "Please select a collection to create a book").showAndWait();
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
            ""
        ));

        this.addToBookState(book);
    }

    private void handleCollectionRightClick(CollectionDto collectionDto) {
        
    }

    private void handleCardsOptionsClick(BookDto bookDto) {
        
    }

    private void openCollection(CollectionDto collectionDto) {        
        this.books = this.bookModel.getAll(new PaginationParams());

        if (this.books.isEmpty())
            this.homePageView.setPlaceholder("No books found!, Create one to get started...");
        else
            this.homePageView.setPlaceholder(null); // remove the placeholder
        
        this.homePageView.setBooks(this.books);
        this.selectedCollection = collectionDto;
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
    }

    @Override
    public Parent getView() {
        return homePageView.getView();
    }
}

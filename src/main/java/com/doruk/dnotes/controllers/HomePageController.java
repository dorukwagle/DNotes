package com.doruk.dnotes.controllers;


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
import com.doruk.dnotes.views.components.OptionsModal;
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

    private void addToCollection(CollectionDto collection) {
        this.collections.addFirst(collection);
        this.homePageView.setSidebarItems(this.collections);
    }

    private void addToBooks(BookDto book) {
        this.books.addFirst(book);
        this.homePageView.setBooks(this.books);
    }

    private void updateCollection(CollectionDto collection) {
        // remove the duplicate collection
        this.collections = this.collections.stream()
        .filter(c -> !c.getId().equals(collection.getId()))
        .collect(Collectors.toList());
        
        // add updated collection to first
        this.collections.addFirst(collection);
        this.homePageView.setSidebarItems(this.collections);
    }

    private void updateBook(BookDto book) {
        // remove the duplicate book
        this.books = this.books.stream()
            .filter(b -> !b.getId().equals(book.getId()))
            .collect(Collectors.toList());
        
        // add updated book to first
        this.books.addFirst(book);
        this.homePageView.setBooks(this.books);
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
            System.out.println("Book selected: " + book.getTitle());
            BookStore.setSelectedBook(book);
            this.navigationController.goToBooksPage();
        });
        
        homePageView.setSidebarItemOnSelect(this::openCollection);
        
        homePageView.setOnCardOptionsClick(_ -> {
            
        });
        
        homePageView.setSidebarItemOnRightClick(collectionDto -> {
            OptionsModal optionsModal = new OptionsModal();
            optionsModal.showAndWait();
        });
        
        homePageView.setMenuItemsOnClick(menuItem -> {
            System.out.println("Menu item clicked: " + menuItem.name());
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

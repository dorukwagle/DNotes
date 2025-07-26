package com.doruk.dnotes.controllers;


import java.util.List;

import com.doruk.dnotes.DIFactory;
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
    private List<CollectionDto> collections;

    public HomePageController(IHomeView view, INavigationController navigationController) {
        this.homePageView = view;
        this.navigationController = navigationController;
        this.collectionModel = DIFactory.createCollectionModel();

        init();
        setupActions();
    }
    
    private void init() {
        this.collections = this.collectionModel.getAll(new PaginationParams());

        if (this.collections.isEmpty())
            this.homePageView.setPlaceholder("No Collections found!, Create one to get started...");
        else
            this.homePageView.setPlaceholder("Select a collection to view it's books...!");
        
        this.homePageView.setSidebarItems(this.collections);
    }

    private void openCollection(CollectionDto collectionDto) {
        // again if no books, show placeholder: No books found!, Create one to get started...
        // otherwise show books
    }
    
    private void setupActions() {
        homePageView.setBooksOnSelect(book -> {
            System.out.println("Book selected: " + book.getTitle());
            BookStore.setSelectedBook(book);
            this.navigationController.goToBooksPage();
        });
        
        homePageView.setSidebarItemOnSelect(collectionDto -> {
            System.out.println("Sidebar item selected: " + collectionDto.getName());
        });
        
        homePageView.setOnCardOptionsClick(_ -> {
            
        });
        
        homePageView.setSidebarItemOnRightClick(collectionDto -> {
            System.out.println("Sidebar item right clicked: " + collectionDto.getName());
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

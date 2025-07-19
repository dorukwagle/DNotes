package com.doruk.dnotes.controllers;


import com.doruk.dnotes.enums.MenuItems;
import com.doruk.dnotes.interfaces.IController;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.store.BookStore;
import com.doruk.dnotes.interfaces.IHomeView;
import javafx.scene.Parent;

public class HomePageController implements IController {

    private final IHomeView homePageView;
    private final INavigationController navigationController;

    public HomePageController(IHomeView view, INavigationController navigationController) {
        this.homePageView = view;
        this.navigationController = navigationController;
        setupActions();
    }

    @Override
    public Parent getView() {
        return homePageView.getView();
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
        
        homePageView.setOnCardsDeleteBtnClick(_ -> {
           
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
}

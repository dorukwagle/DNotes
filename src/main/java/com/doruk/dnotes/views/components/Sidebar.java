package com.doruk.dnotes.views.components;

import atlantafx.base.controls.CustomTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import org.kordamp.ikonli.javafx.FontIcon;

public class Sidebar {
    private final VBox root;

    public Sidebar() {
        this.root = new VBox();
        initializeUI();
    }

    private void initializeUI() {
        // Set style
        this.root.getStyleClass().add("sidebar");
        this.root.setStyle("-fx-background-color: -color-bg-default; -fx-border-color: -color-border-muted; -fx-border-width: 0 1 0 0;");
        
        // Create search bar
        Node searchBar = createSearchBar();
        
        // Create list view
        ListView<String> listView = createListView();
        
        // Add all components to the sidebar
        this.root.getChildren().addAll(searchBar, listView);
        VBox.setVgrow(listView, Priority.ALWAYS);
    }
    
    private Node createSearchBar() {
        CustomTextField searchField = new CustomTextField();
        searchField.setLeft(new FontIcon("mdi2m-magnify"));
        searchField.setPromptText("Search...");
        searchField.getStyleClass().add("search-field");
        searchField.focusTraversableProperty().set(false);
        searchField.setMaxWidth(Double.MAX_VALUE);
        searchField.setPadding(new Insets(10));
        return searchField;
    }
    
    private ListView<String> createListView() {
        ListView<String> listView = new ListView<>();
        
        // listView.getStyleClass().add("sidebar-list text danger");
        listView.setFocusTraversable(false);
        
        // Add some dummy data
        ObservableList<String> items = FXCollections.observableArrayList(
            "Meeting Notes",
            "Project Ideas",
            "Shopping List",
            "Book Summaries",
            "Work Tasks",
            "Personal Goals",
            "Recipes",
            "Travel Plans",
            "Learning Resources",
            "Daily Journal"
        );
        
        listView.setItems(items);
        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    return;
                }
                setText(item);
                setPadding(new Insets(10));
                setStyle("-fx-background-radius: 4; -fx-font-size: 16px;");
                setOnMouseEntered(e -> setStyle("-fx-background-color: -color-bg-subtle; -fx-background-radius: 4; -fx-font-size: 16px;"));
                setOnMouseExited(e -> setStyle("-fx-background-color: transparent; -fx-background-radius: 4; -fx-font-size: 16px;"));
            }
        });
        return listView;
    }
    
    public Parent getView() {
        return this.root;
    }
}

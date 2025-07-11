package com.doruk.dnotes.views.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.kordamp.ikonli.javafx.FontIcon;
import atlantafx.base.theme.Styles;

public class Sidebar {
    private final VBox root;

    public Sidebar() {
        this.root = new VBox();
        this.root.getStyleClass().add("sidebar");
        this.root.setStyle(
            "-fx-background-color: -color-bg-default; " +
            "-fx-border-color: -color-border-muted; " +
            "-fx-border-width: 0 1 0 0; " +
            "-fx-min-width: 320; " +
            "-fx-pref-width: 320; " +
            "-fx-max-width: 360;"
        );
        
        // Create search bar
        Node searchBar = createSearchBar();
        
        // Create list view
        ListView<String> listView = createListView();
        
        // Add all components to the sidebar
        this.root.getChildren().addAll(searchBar, listView);
        VBox.setVgrow(listView, Priority.ALWAYS);
    }
    
    private Node createSearchBar() {
        // Main container for search bar and controls
        HBox container = new HBox();
        container.setSpacing(12);
        container.setPadding(new Insets(20, 20, 12, 20));
        
        // Search field with icon
        HBox searchContainer = new HBox(10);
        searchContainer.setAlignment(Pos.CENTER_LEFT);
        searchContainer.setStyle("-fx-background-color: -color-bg-subtle; -fx-background-radius: 8;");
        searchContainer.setPadding(new Insets(0, 5, 0, 5));
        searchContainer.setMinHeight(50);
        searchContainer.setMaxHeight(50);
        
        // Search icon
        FontIcon searchIcon = new FontIcon("mdi2m-magnify");
        searchIcon.setIconSize(18);
        searchIcon.getStyleClass().add("muted");
        
        // Search field
        TextField searchField = new TextField();
        searchField.setPromptText("Search notes...");
        searchField.getStyleClass().add("search-field");
        searchField.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-border-width: 0; " +
            "-fx-padding: 0 0 0 8; " +
            "-fx-font-size: 15px;" +
            "-fx-text-fill: -color-fg-default;"
        );
        searchField.focusTraversableProperty().set(false);
        HBox.setHgrow(searchField, Priority.ALWAYS);
        
        searchContainer.getChildren().addAll(searchIcon, searchField);
        
        // Sort controls container aligned with search bar
        HBox sortControls = new HBox(2);
        sortControls.setAlignment(Pos.CENTER_RIGHT);
        sortControls.setPadding(new Insets(0, 0, 0, 5));
        
        // Sort by toggle (Date/Alphabetical)
        ToggleButton sortByToggle = new ToggleButton("Date");
        sortByToggle.setGraphic(new FontIcon("mdi2c-calendar-month-outline"));
        sortByToggle.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.BUTTON_OUTLINED, Styles.MEDIUM);
        sortByToggle.setTooltip(new Tooltip("Toggle sort by date & alphabetical"));
        sortByToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            sortByToggle.setGraphic(new FontIcon(newVal ? "mdi2a-alphabetical-variant" : "mdi2c-calendar-month-outline"));
        });
        
        // Sort order toggle (Ascending/Descending)
        ToggleButton sortOrderToggle = new ToggleButton("");
        sortOrderToggle.setGraphic(new FontIcon("mdi2s-sort-ascending"));
        sortOrderToggle.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.BUTTON_OUTLINED, Styles.MEDIUM);
        sortOrderToggle.setTooltip(new Tooltip("Toggle sort order"));
        sortOrderToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            sortOrderToggle.setGraphic(new FontIcon(newVal ? "mdi2s-sort-descending" : "mdi2s-sort-ascending"));
        });
        
        sortControls.getChildren().addAll(sortByToggle, sortOrderToggle);
        // searchContainer.getChildren().addAll(sortByToggle, sortOrderToggle);
        searchContainer.getChildren().add(sortControls);
        container.getChildren().addAll(searchContainer);
        return container;
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
                setOnMouseEntered(e -> setStyle("-fx-background-color: -color-bg-subtle; -fx-background-radius: 4; -fx-font-size: 16px; -fx-cursor: hand;"));
                setOnMouseExited(e -> setStyle("-fx-background-color: transparent; -fx-background-radius: 4; -fx-font-size: 16px;"));
            }
        });
        return listView;
    }
    
    public Parent getView() {
        return this.root;
    }
}

package com.doruk.dnotes.views.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.List;
import java.util.function.Consumer;

import org.kordamp.ikonli.javafx.FontIcon;

import com.doruk.dnotes.dto.SearchControlsDto;
import com.doruk.dnotes.interfaces.ISidebarItem;

import atlantafx.base.theme.Styles;

public class Sidebar <T extends ISidebarItem> {
    private final VBox root;
    ObservableList<T> items;
    private TextField searchField;
    private ToggleButton sortByToggle;
    private ToggleButton sortOrderToggle;
    private Consumer<T> onSelect;
    private Consumer<T> onRightClick;
    private ListView<T> listView;

    public Sidebar() {
        this.root = new VBox();
        this.root.getStyleClass().add("sidebar");
        this.root.setStyle(
                "-fx-background-color: -color-bg-default; " +
                        "-fx-border-color: -color-border-muted; " +
                        "-fx-border-width: 0 1 0 0; " +
                        "-fx-min-width: 320; " +
                        "-fx-pref-width: 320; " +
                        "-fx-max-width: 360;");

        // Create search bar
        Node searchBar = createSearchBar();

        // Create list view
        ListView<T> listView = createListView();

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
        searchField = new TextField();
        searchField.setPromptText("Search notes...");
        searchField.getStyleClass().add("search-field");
        searchField.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-width: 0; " +
                        "-fx-padding: 0 0 0 8; " +
                        "-fx-font-size: 15px;" +
                        "-fx-text-fill: -color-fg-default;");
        searchField.focusTraversableProperty().set(false);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        searchContainer.getChildren().addAll(searchIcon, searchField);

        // Sort controls container aligned with search bar
        HBox sortControls = new HBox(2);
        sortControls.setAlignment(Pos.CENTER_RIGHT);
        sortControls.setPadding(new Insets(0, 0, 0, 5));

        // Sort by toggle (Date/Alphabetical)
        sortByToggle = new ToggleButton("Date");
        sortByToggle.setGraphic(new FontIcon("mdi2c-calendar-month-outline"));
        sortByToggle.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.BUTTON_OUTLINED, Styles.MEDIUM);
        sortByToggle.setTooltip(new Tooltip("Toggle sort by date & alphabetical"));
        sortByToggle.selectedProperty().addListener((_, _, newVal) -> {
            sortByToggle
                    .setGraphic(new FontIcon(newVal ? "mdi2a-alphabetical-variant" : "mdi2c-calendar-month-outline"));
        });

        // Sort order toggle (Ascending/Descending)
        sortOrderToggle = new ToggleButton("");
        sortOrderToggle.setGraphic(new FontIcon("mdi2s-sort-descending"));
        sortOrderToggle.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.BUTTON_OUTLINED, Styles.MEDIUM);
        sortOrderToggle.setTooltip(new Tooltip("Toggle sort order"));
        sortOrderToggle.selectedProperty().addListener((_, _, newVal) -> {
            sortOrderToggle.setGraphic(new FontIcon(newVal ? "mdi2s-sort-ascending" : "mdi2s-sort-descending"));
        });

        sortControls.getChildren().addAll(sortByToggle, sortOrderToggle);
        searchContainer.getChildren().add(sortControls);
        container.getChildren().addAll(searchContainer);
        return container;
    }

    private ListView<T> createListView() {
        listView = new ListView<>();

        // listView.getStyleClass().add("sidebar-list text danger");
        listView.setFocusTraversable(false);

        // Add some dummy data
        items = FXCollections.observableArrayList();

        listView.setItems(items);
        listView.setCellFactory(_ -> new ListCell<>() {
            {
                addEventFilter(MouseEvent.ANY, e -> e.consume());
                addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
                    event.consume();

                    var btn = event.getButton();

                    switch (btn) {
                        case MouseButton.SECONDARY -> {
                            if (onRightClick != null)
                                onRightClick.accept(getItem());
                        }
                        case MouseButton.PRIMARY -> {
                            listView.getSelectionModel().select(this.getIndex());

                            if (onSelect != null)
                                onSelect.accept(getItem());
                        }
                        default -> {
                            return;
                        }
                    }
                });

            }
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle(""); // Reset all styles
                    setPadding(Insets.EMPTY); // Reset padding
                    setOnMouseEntered(null);
                    setOnMouseExited(null);
                    setOnMouseClicked(null);
                    return;
                }
                setText(item.getName());
                setPadding(new Insets(10));
                setStyle("-fx-background-radius: 4; -fx-font-size: 16px; -fx-cursor: hand;");
                setOnMouseEntered(_ -> {
                    if (this.isSelected())
                        return;
                    setStyle(getStyle() + "-fx-background-color: -color-bg-subtle;");
                });
                setOnMouseExited(_ -> {
                    if (this.isSelected())
                        return;
                    setStyle(getStyle() + "-fx-background-color: transparent;");
                });

                // when selected, bold the text
                if (isSelected())
                    setStyle(getStyle() + "-fx-font-weight: bold;");
            }
        });

        return listView;
    }

    public void setOnSelect(Consumer<T> onSelect) {
        this.onSelect = onSelect;
    }

    public void setOnRightClick(Consumer<T> onRightClick) {
        this.onRightClick = onRightClick;
    }

    public Parent getView() {
        return this.root;
    }

    public void setItems(List<T> newItems) {
        this.items.clear();
        this.items.addAll(newItems);
    }

    public SearchControlsDto getSearchControls() {
        return new SearchControlsDto(this.searchField, this.sortByToggle, this.sortOrderToggle);
    }

    public void setSelectedItem(T item) {
        if (item == null) {
            this.listView.getSelectionModel().clearSelection();
            return;
        }
        
        var givenItem = this.items.stream()
            .filter(i -> i.getId().equals(item.getId()))
            .findFirst()
            .orElse(null);
        
        this.listView.getSelectionModel().select(givenItem);
    }

    public ObservableList<T> getItems() {
        return this.items;
    }
}

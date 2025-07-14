package com.doruk.dnotes.views;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

import org.kordamp.ikonli.javafx.FontIcon;

import com.doruk.dnotes.dto.BookDto;
import com.doruk.dnotes.dto.CollectionDto;
import com.doruk.dnotes.dto.SearchControlsDto;
import com.doruk.dnotes.interfaces.IHomeView;
import com.doruk.dnotes.views.components.FAB;
import com.doruk.dnotes.views.components.Sidebar;

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Tooltip;

public class HomePage implements IHomeView {
    private BorderPane root;
    private Sidebar sidebar;
    private TextField searchField;
    private ToggleButton sortByToggle;
    private ToggleButton sortOrderToggle;
    private final ObservableList<BookDto> books;
    private Function<BookDto, Void> booksOnSelect;
    private Function<BookDto, Void> onDeleteBtnClick;

    public HomePage() {
        this.books = FXCollections.observableArrayList();
        
        // Create main layout
        root = new BorderPane();
        
        // Create and add sidebar
        sidebar = new Sidebar();
        root.setLeft(sidebar.getView());
        
        // Create main content container
        VBox mainContent = new VBox();
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setSpacing(20);
        mainContent.setPadding(new Insets(20));
        
        // Create navigation bar
        BorderPane navBar = new BorderPane();
        navBar.setPadding(new Insets(5, 20, 5, 20));
        navBar.setStyle("-fx-background-color: -color-bg-subtle; -fx-background-radius: 8;");
        navBar.setMaxWidth(Double.MAX_VALUE);
        
        // Search bar container
        HBox searchContainer = new HBox(10);
        searchContainer.setAlignment(Pos.CENTER_LEFT);
        searchContainer.setStyle("-fx-background-color: -color-bg-subtle; -fx-background-radius: 8;");
        searchContainer.setPadding(new Insets(0, 50, 0, 10));
        searchContainer.setMinHeight(45);
        searchContainer.setMaxHeight(45);
        
        // Search icon
        FontIcon searchIcon = new FontIcon("mdi2m-magnify");
        searchIcon.setScaleX(1.3);
        searchIcon.setScaleY(1.3);
        searchIcon.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.LARGE);
        
        // Search field
        searchField = new TextField();
        searchField.setPromptText("Search books...");
        searchField.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-border-width: 0; " +
            "-fx-padding: 0 0 0 8; " +
            "-fx-font-size: 18; " +
            "-fx-text-fill: -color-fg-default;"
        );
        searchField.focusTraversableProperty().set(false);
        HBox.setHgrow(searchField, Priority.ALWAYS);
        
        // Sort controls container
        HBox sortControls = new HBox(2);
        sortControls.setAlignment(Pos.CENTER_RIGHT);
        sortControls.setPadding(new Insets(0, 0, 0, 5));
        
        // Sort by toggle (Date/Alphabetical)
        sortByToggle = new ToggleButton("Date");
        sortByToggle.setGraphic(new FontIcon("mdi2c-calendar-month-outline"));
        sortByToggle.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.BUTTON_OUTLINED, Styles.LARGE);
        sortByToggle.setTooltip(new Tooltip("Toggle sort by date & alphabetical"));
        sortByToggle.selectedProperty().addListener((_, _, newVal) -> {
            sortByToggle.setGraphic(new FontIcon(newVal ? "mdi2a-alphabetical-variant" : "mdi2c-calendar-month-outline"));
        });
        
        // Sort order toggle (Ascending/Descending)
        sortOrderToggle = new ToggleButton("");
        sortOrderToggle.setGraphic(new FontIcon("mdi2s-sort-ascending"));
        sortOrderToggle.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.BUTTON_OUTLINED, Styles.LARGE);
        sortOrderToggle.setTooltip(new Tooltip("Toggle sort order"));
        sortOrderToggle.selectedProperty().addListener((_, _, newVal) -> {
            sortOrderToggle.setGraphic(new FontIcon(newVal ? "mdi2s-sort-descending" : "mdi2s-sort-ascending"));
        });
        
        sortControls.getChildren().addAll(sortByToggle, sortOrderToggle);
        searchContainer.getChildren().addAll(searchIcon, searchField, sortControls);
        
        // Add search bar to nav bar
        navBar.setCenter(searchContainer);

        // Create menu button
        MenuButton menuButton = new MenuButton();
        menuButton.setStyle("-fx-cursor: hand; -fx-background-color: transparent;");
        menuButton.setPrefHeight(50);
        FontIcon menuIcon = new FontIcon("mdi2m-menu");
        menuButton.setGraphic(menuIcon);
        menuIcon.setScaleX(2);
        menuIcon.setScaleY(2);
        menuButton.getStyleClass().addAll("no-arrow");
        createMenuList(menuButton);
        navBar.setRight(menuButton);
        
        // Create card grid
        ScrollPane cardGrid = createCardGrid();
        VBox.setVgrow(cardGrid, Priority.ALWAYS);

        
        // Add components to main content
        mainContent.getChildren().addAll(navBar, cardGrid);
        
        // Main content is wrapped in StackPane with FAB
        StackPane contentWrapper = new StackPane();

        // Create floating action button
        FAB fab = new FAB();
        contentWrapper.getChildren().addAll(mainContent, fab);
        fab.setActionsParent(contentWrapper);

        // Position FAB in bottom-right corner
        StackPane.setAlignment(fab, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(fab, new Insets(0, 50, 50, 0));
        
        
        // Set the wrapped content as center
        root.setCenter(contentWrapper);
        
        // Make the main content grow to fill available space
        VBox.setVgrow(mainContent, Priority.ALWAYS);
        HBox.setHgrow(mainContent, Priority.ALWAYS);
    }

    private void createMenuList(MenuButton menuButton) {
        // Add menu items with icons
        String[][] menuData = {
            {"Backup", "mdi2e-export"},
            {"Restore", "mdi2b-backup-restore"},
            {"View Trash", "mdi2d-delete"},
            {"Preferences", "mdi2a-account-cog"}
        };
        
        for (String[] itemData : menuData) {
            MenuItem menuItem = new MenuItem(itemData[0]);
            FontIcon icon = new FontIcon(itemData[1]);
            icon.setIconSize(30);
            menuItem.setGraphic(icon);
            menuItem.getStyleClass().addAll(Styles.ACCENT);
            
            // Style menu items
            menuItem.setStyle(
                "-fx-padding: 8 15 8 10;\n" +
                "-fx-background-radius: 4;\n" +
                "-fx-cursor: hand;\n" +
                "-fx-background-color: transparent;\n" +
                "-fx-font-size: 16px;"
            );

            menuItem.setOnAction(_ -> System.out.println(itemData[0] + " clicked"));
            menuButton.getItems().add(menuItem);
        }
        
        // Style the popup content
        menuButton.getStyleClass().addAll("menu-button");
        menuButton.popupSideProperty().set(javafx.geometry.Side.BOTTOM);
    }

    private ScrollPane createCardGrid() {
        // Create scroll pane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        // Create flow pane for responsive card layout
        FlowPane flowPane = new FlowPane();
        flowPane.setPadding(new Insets(15));
        flowPane.setVgap(20);
        flowPane.setHgap(20);
        flowPane.setStyle("-fx-background-color: transparent;");
        
        // add listener to the observable list
        this.books.addListener((ListChangeListener<? super BookDto>) _ -> {
            flowPane.getChildren().clear();
            
            this.books.forEach(book -> {
                VBox card = createCard(book);

            // Set preferred width for cards with min and max constraints
            card.setMinWidth(280);
            card.setMaxWidth(380);
            card.setPrefWidth(320);
            Platform.runLater(() -> flowPane.getChildren().add(card));
        });
    });

    // Add cards to flow pane
    this.books.addAll(
        new BookDto("1", "Project Ideas that will nevver cease to exist", "Brainstorming for new project ideas and potential features...", LocalDate.now().toString()),
        new BookDto("2", "Meeting Notes", "Discussed project timeline and assigned tasks to team members...", LocalDate.now().toString()),
        new BookDto("3", "Shopping List", "Milk, eggs, bread, fruits, vegetables, and snacks...", LocalDate.now().toString()),
        new BookDto("4", "Book Summaries", "Atomic Habits by James Clear - key takeaways and action items...", LocalDate.now().toString()),
        new BookDto("5", "Work Tasks", "Complete UI redesign, fix critical bugs, prepare presentation...", LocalDate.now().toString()),
        new BookDto("6", "Personal Goals", "Learn JavaFX, Exercise 3x a week, Read 10 books this year...", LocalDate.now().toString()),
        new BookDto("7", "Recipes", "Pasta Carbonara: Ingredients - pasta, eggs, pancetta, parmesan...", LocalDate.now().toString()),
        new BookDto("8", "Travel Plans", "Book flights, reserve hotel, create itinerary for Japan trip...", LocalDate.now().toString())
    );
        
        // Bind flow pane width to scroll pane width
        flowPane.prefWrapLengthProperty().bind(
            scrollPane.widthProperty().subtract(40) // account for padding and scrollbar
        );
        
        // Add flow pane to scroll pane
        scrollPane.setContent(flowPane);
        
        return scrollPane;
    }
    
    private VBox createCard(BookDto book) {
        VBox card = new VBox();
        card.getStyleClass().add("card");
        card.setSpacing(16);
        card.setPadding(new Insets(24));
        card.setStyle(
            "-fx-background-color: -color-bg-default;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: -color-border-muted;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0.1, 0, 4);" +
            "-fx-cursor: hand;" +
            "-fx-transition: all 0.2s;"
        );
        
        // Add hover effect
        card.setOnMouseEntered(_ -> card.setStyle(
            "-fx-background-color: -color-bg-subtle;" +
            "-fx-background-radius: 12;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 20, 0.15, 0, 6);" +
            "-fx-cursor: hand;" +
            "-fx-scale-x: 1.05;" + 
            "-fx-scale-y: 1.05;"
        ));
        
        card.setOnMouseExited(_ -> card.setStyle(
            "-fx-background-color: -color-bg-default;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: -color-border-muted;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0.1, 0, 4);" +
            "-fx-cursor: hand;" +
            "-fx-scale-x: 1;" + 
            "-fx-scale-y: 1;"
        ));
        
        // Title
        Text titleText = new Text(book.getTitle());
        titleText.getStyleClass().add(Styles.TITLE_4);
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 18;");
        titleText.setWrappingWidth(280);
        
        // Content preview (limit to 3 lines)
        Text contentText = new Text(book.getPreview());
        contentText.setStyle("-fx-fill: -color-fg-muted; -fx-font-size: 14;");
        contentText.setWrappingWidth(280);
        
        // Status bar
        HBox statusBar = new HBox();
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.setStyle("-fx-padding: 8 0 0 0; -fx-border-color: -color-border-muted; -fx-border-width: 1 0 0 0;");
        
        // Date
        Text dateText = new Text(book.getUpdatedDate());
        dateText.setStyle("-fx-fill: -color-fg-muted; -fx-font-size: 12;");
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Delete button
        Button deleteButton = new Button();
        FontIcon deleteIcon = new FontIcon("mdi2d-delete");
        deleteIcon.setIconSize(16);
        deleteButton.setGraphic(deleteIcon);
        deleteButton.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT, Styles.DANGER);
        deleteButton.setOnAction(_ -> {
            // Handle delete action
            if (this.onDeleteBtnClick != null)
                this.onDeleteBtnClick.apply(book);
        });
        
        statusBar.getChildren().addAll(dateText, spacer, deleteButton);
        
        // Add all components to card
        card.getChildren().addAll(titleText, contentText, statusBar);

        card.setOnMouseClicked(_ -> {
            if (this.booksOnSelect != null)
                this.booksOnSelect.apply(book);
        });
        
        return card;
    }

    @Override
    public Parent getView() {
        return root;
    }

    @Override
    public void setSidebarItems(List<CollectionDto> items) {
        this.sidebar.setItems(items);
    }

    @Override
    public void setSidebarItemOnSelect(Function<CollectionDto, Void> onSelect) {
        this.sidebar.setOnSelect(onSelect);
    }

    @Override
    public SearchControlsDto getSidebarSearchControls() {
        return sidebar.getSearchControls();
    }

    @Override
    public SearchControlsDto getSearchControls() {
        return new SearchControlsDto(this.searchField, this.sortByToggle, this.sortOrderToggle);
    }

    @Override
    public void setBooks(List<BookDto> books) {
        this.books.clear();
        this.books.addAll(books);
    }

    @Override
    public void setBooksOnSelect(Function<BookDto, Void> onSelect) {
        this.booksOnSelect = onSelect;
    }

    @Override
    public void setOnCardsDeleteBtnClick(Function<BookDto, Void> onDeleteBtnClick) {
        this.onDeleteBtnClick = onDeleteBtnClick;
    }

    @Override
    public void setSelectedSidebarItem(CollectionDto item) {
        this.sidebar.setSelectedItem(item);
    }
}

package com.doruk.dnotes.views;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.kordamp.ikonli.javafx.FontIcon;

import com.doruk.dnotes.interfaces.IHomeView;
import com.doruk.dnotes.views.components.Sidebar;

import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Tooltip;

public class HomePage implements IHomeView {

    private Button bookButton;
    private BorderPane root;


    /**
     * search bar for books
     * import / export buttons
     * add collections btn
     * add books btn
     * sort created/name 
     * sort order asc,desc
     */

    public HomePage() {
        // Create main layout
        root = new BorderPane();
        
        // Create and add sidebar
        Sidebar sidebar = new Sidebar();
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
        searchIcon.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.LARGE);
        
        // Search field
        TextField searchField = new TextField();
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
        ToggleButton sortByToggle = new ToggleButton("Date");
        sortByToggle.setGraphic(new FontIcon("mdi2c-calendar-month-outline"));
        sortByToggle.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.BUTTON_OUTLINED, Styles.LARGE);
        sortByToggle.setTooltip(new Tooltip("Toggle sort by date & alphabetical"));
        sortByToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            sortByToggle.setGraphic(new FontIcon(newVal ? "mdi2a-alphabetical-variant" : "mdi2c-calendar-month-outline"));
        });
        
        // Sort order toggle (Ascending/Descending)
        ToggleButton sortOrderToggle = new ToggleButton("");
        sortOrderToggle.setGraphic(new FontIcon("mdi2s-sort-ascending"));
        sortOrderToggle.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.BUTTON_OUTLINED, Styles.LARGE);
        sortOrderToggle.setTooltip(new Tooltip("Toggle sort order"));
        sortOrderToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            sortOrderToggle.setGraphic(new FontIcon(newVal ? "mdi2s-sort-descending" : "mdi2s-sort-ascending"));
        });
        
        sortControls.getChildren().addAll(sortByToggle, sortOrderToggle);
        searchContainer.getChildren().addAll(searchIcon, searchField, sortControls);
        
        // Add search bar to nav bar
        navBar.setCenter(searchContainer);

        // Create menu button
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-cursor: hand;");
        
        Menu menu = new Menu();
        FontIcon menuIcon = new FontIcon("mdi2m-menu");
        menuIcon.setIconSize(24);
        menu.setGraphic(menuIcon);
        menu.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.BUTTON_OUTLINED);
        
        createMenuList(menu);
        
        menuBar.getMenus().add(menu);
        navBar.setRight(menuBar);

        bookButton = new Button("Open Books");
        
        // Create card grid
        ScrollPane cardGrid = createCardGrid();
        VBox.setVgrow(cardGrid, Priority.ALWAYS);
        
        // Add components to main content
        mainContent.getChildren().addAll(navBar, cardGrid, bookButton);
        
        // Set center content
        root.setCenter(mainContent);
        
        // Make the main content grow to fill available space
        VBox.setVgrow(mainContent, Priority.ALWAYS);
        HBox.setHgrow(mainContent, Priority.ALWAYS);
    }

    private void createMenuList(Menu menu) {
        // Add menu items with icons
        String[][] menuData = {
            {"Backup", "mdi2c-cloud-upload"},
            {"Restore", "mdi2c-cloud-download"},
            {"View Trash", "mdi2d-delete"},
            {"Preferences", "mdi2a-account-cog"}
        };
        
        for (String[] itemData : menuData) {
            MenuItem menuItem = new MenuItem(itemData[0]);
            FontIcon icon = new FontIcon(itemData[1]);
            icon.setIconSize(18);
            menuItem.setGraphic(icon);
            menuItem.getStyleClass().addAll(Styles.ACCENT);
            
            // Style menu items
            menuItem.setStyle(
                "-fx-padding: 8 15 8 10;\n" +
                "-fx-background-radius: 4;\n" +
                "-fx-cursor: hand;\n" +
                "-fx-background-color: transparent;"
            );
            
            menuItem.setOnAction(e -> System.out.println(itemData[0] + " clicked"));
            menu.getItems().add(menuItem);
        }
        
        // Style the context menu using Atlantafx
        menu.setStyle(
            menu.getStyle() + 
            "-fx-background-color: -color-bg-default;\n" +
            "-fx-background-radius: 8;\n" +
            "-fx-padding: 5;\n" +
            "-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 2);\n" +
            "-fx-skin: \"com/sun/javafx/scene/control/skin/ContextMenuSkin\";"
        );
        
        // Add style class for the menu items container
        menu.getStyleClass().add("context-menu");
        
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
        
        // Add cards to flow pane
        List<String> dummyTitles = Arrays.asList(
            "Project Ideas",
            "Meeting Notes",
            "Shopping List",
            "Book Summaries",
            "Work Tasks",
            "Personal Goals",
            "Recipes",
            "Travel Plans"
        );
        
        List<String> dummyContent = Arrays.asList(
            "Brainstorming for new project ideas and potential features...",
            "Discussed project timeline and assigned tasks to team members...",
            "Milk, eggs, bread, fruits, vegetables, and snacks...",
            "Atomic Habits by James Clear - key takeaways and action items...",
            "Complete UI redesign, fix critical bugs, prepare presentation...",
            "Learn JavaFX, Exercise 3x a week, Read 10 books this year...",
            "Pasta Carbonara: Ingredients - pasta, eggs, pancetta, parmesan...",
            "Book flights, reserve hotel, create itinerary for Japan trip..."
        );
        
        for (int i = 0; i < dummyTitles.size(); i++) {
            VBox card = createCard(
                dummyTitles.get(i),
                dummyContent.get(i),
                LocalDate.now().minusDays(i)
            );
            // Set preferred width for cards with min and max constraints
            card.setMinWidth(280);
            card.setMaxWidth(380);
            card.setPrefWidth(320);
            flowPane.getChildren().add(card);
        }
        
        // Bind flow pane width to scroll pane width
        flowPane.prefWrapLengthProperty().bind(
            scrollPane.widthProperty().subtract(40) // account for padding and scrollbar
        );
        
        // Add flow pane to scroll pane
        scrollPane.setContent(flowPane);
        
        return scrollPane;
    }
    
    private VBox createCard(String title, String content, LocalDate date) {
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
        card.setOnMouseEntered(e -> card.setStyle(
            "-fx-background-color: -color-bg-subtle;" +
            "-fx-background-radius: 12;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 20, 0.15, 0, 6);" +
            "-fx-cursor: hand;" +
            "-fx-scale-x: 1.05;" + 
            "-fx-scale-y: 1.05;"
        ));
        
        card.setOnMouseExited(e -> card.setStyle(
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
        Text titleText = new Text(title);
        titleText.getStyleClass().add(Styles.TITLE_4);
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 18;");
        
        // Content preview (limit to 3 lines)
        Text contentText = new Text(content);
        contentText.setStyle("-fx-fill: -color-fg-muted; -fx-font-size: 14;");
        contentText.setWrappingWidth(280);
        
        // Status bar
        HBox statusBar = new HBox();
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.setStyle("-fx-padding: 8 0 0 0; -fx-border-color: -color-border-muted; -fx-border-width: 1 0 0 0;");
        
        // Date
        Text dateText = new Text(date.format(DateTimeFormatter.ofPattern("MMM d, yyyy")));
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
        deleteButton.setOnAction(e -> {
            // Handle delete action
            System.out.println("Delete card: " + title);
        });
        
        statusBar.getChildren().addAll(dateText, spacer, deleteButton);
        
        // Add all components to card
        card.getChildren().addAll(titleText, contentText, statusBar);
        
        return card;
    }

    @Override
    public Parent getView() {
        return root;
    }


    public Button getBookButton() {
        return bookButton;
    }
}

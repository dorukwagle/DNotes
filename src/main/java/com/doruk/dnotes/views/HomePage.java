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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class HomePage implements IHomeView {

    private Button bookButton;
    private BorderPane root;

    public HomePage() {
        // Create main layout
        root = new BorderPane();
        
        // Create and add sidebar
        Sidebar sidebar = new Sidebar();
        root.setLeft(sidebar.getView());
        
        // Create center content
        VBox centerContent = new VBox();
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setSpacing(20);
        
        // Add welcome text
        Text welcomeText = new Text("Welcome to DNotes!");
        welcomeText.setFont(new Font(24));
        
        // Add book button
        bookButton = new Button("Open Books");

        // create card grid
        ScrollPane cardGrid = createCardGrid();
        
        // Add components to center content
        centerContent.getChildren().addAll(welcomeText, cardGrid, bookButton);
        
        // Set center content
        root.setCenter(centerContent);
        
        // Make the center content grow to fill available space
        VBox.setVgrow(centerContent, Priority.ALWAYS);
        HBox.setHgrow(centerContent, Priority.ALWAYS);
    }

    private ScrollPane createCardGrid() {
        // Create scroll pane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        // Create grid for cards
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(10));
        
        // Add cards to grid
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
        
        int columns = 3;
        for (int i = 0; i < dummyTitles.size(); i++) {
            VBox card = createCard(
                dummyTitles.get(i),
                dummyContent.get(i),
                LocalDate.now().minusDays(i)
            );
            grid.add(card, i % columns, i / columns);
        }
        
        // Make grid fill available width
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(100.0 / columns);
        for (int i = 0; i < columns; i++) {
            grid.getColumnConstraints().add(column1);
        }
        
        // Add grid to scroll pane
        scrollPane.setContent(grid);
        
        // Bind grid width to scroll pane width
        grid.prefWidthProperty().bind(scrollPane.widthProperty().subtract(30));
        
        return scrollPane;
    }
    
    private VBox createCard(String title, String content, LocalDate date) {
        VBox card = new VBox();
        card.getStyleClass().add("card");
        card.setSpacing(10);
        card.setPadding(new Insets(15));
        card.setStyle(
            "-fx-background-color: -color-bg-default;" +
            "-fx-background-radius: 8;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);" +
            "-fx-cursor: hand;"
        );
        
        // Add hover effect
        card.setOnMouseEntered(e -> card.setStyle(
            "-fx-background-color: -color-bg-subtle;" +
            "-fx-background-radius: 8;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 12, 0, 0, 4);" +
            "-fx-cursor: hand;"
        ));
        
        card.setOnMouseExited(e -> card.setStyle(
            "-fx-background-color: -color-bg-default;" +
            "-fx-background-radius: 8;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);" +
            "-fx-cursor: hand;"
        ));
        
        // Title
        Text titleText = new Text(title);
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");
        
        // Content preview (limit to 3 lines)
        Text contentText = new Text(content);
        contentText.setStyle("-fx-fill: -color-fg-muted;");
        contentText.setWrappingWidth(250);
        
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
        deleteButton.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT);
        deleteButton.setStyle("-fx-text-fill: -color-danger-fg;");
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

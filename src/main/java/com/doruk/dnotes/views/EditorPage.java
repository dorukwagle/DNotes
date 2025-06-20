package com.doruk.dnotes.views;

import atlantafx.base.theme.Styles;
import com.doruk.dnotes.interfaces.IEditorView;
import com.doruk.dnotes.views.components.Sidebar;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class EditorPage implements IEditorView {
    private final BorderPane root;
    private final Button backButton;
    private final ProgressIndicator loadingIndicator;

    public EditorPage() {
        // 1. Root Layout
        root = new BorderPane();
        root.setPadding(new Insets(10));

        // 2. Sidebar
        Sidebar sidebar = new Sidebar();
        root.setLeft(sidebar.getView());

        // 2. Loading Indicator
        loadingIndicator = new ProgressIndicator();
        loadingIndicator.setMaxSize(50, 50);
        loadingIndicator.setVisible(false);

        // 3. Main Content Area with TabPane
        TabPane tabPane = createTabPane();
        StackPane contentPane = new StackPane(tabPane);
        contentPane.getChildren().add(loadingIndicator);
        StackPane.setAlignment(loadingIndicator, Pos.CENTER);

        root.setCenter(contentPane);

        // 4. Bottom Bar with Back Button
        backButton = new Button("Back to Home");
        backButton.getStyleClass().addAll(Styles.BUTTON_OUTLINED, Styles.ACCENT);
        backButton.setMaxWidth(Double.MAX_VALUE);

        HBox bottomBar = new HBox(backButton);
        bottomBar.setPadding(new Insets(15, 0, 5, 0));
        bottomBar.setAlignment(Pos.CENTER_RIGHT);
        root.setBottom(bottomBar);

        // 5. Simulate initial loading
        simulateLoading();
    }

    /**
     * Creates the main TabPane and its tabs using AtlantaFX floating style.
     */
    private TabPane createTabPane() {
        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add(Styles.TABS_FLOATING);
        tabPane.getTabs().addAll(
                createTab("Buttons & Cards", createButtonsAndCardsTab()),
                createTab("Form Controls", createFormControlsTab()),
                createTab("Data Display", createDataDisplayTab()),
                createTab("Navigation", createNavigationTab())
        );
        return tabPane;
    }

    /**
     * Helper to create a standard, non-closable tab with a ScrollPane for content.
     */
    private Tab createTab(String title, Node content) {
        Tab tab = new Tab(title);
        tab.setClosable(false);
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        // Remove border from scrollpane for a cleaner look inside the tab
        scrollPane.setStyle("-fx-background-color: transparent;");
        tab.setContent(scrollPane);
        return tab;
    }

    // --- TAB 1: BUTTONS & CARDS ---
    private Node createButtonsAndCardsTab() {
        VBox vbox = new VBox(30);
        vbox.setPadding(new Insets(25));

        // Buttons Section
        addSection(vbox, "Buttons", new HBox(10,
                styledButton("Primary", Styles.ACCENT),
                styledButton("Outlined", Styles.BUTTON_OUTLINED, Styles.ACCENT),
                styledButton("Success", Styles.SUCCESS),
                styledButton("Danger", Styles.DANGER)
        ));

        // Toggles Section
        ToggleGroup toggleGroup = new ToggleGroup();
        ToggleButton tb1 = new ToggleButton("Option A");
        ToggleButton tb2 = new ToggleButton("Option B");
        tb1.setToggleGroup(toggleGroup);
        tb2.setToggleGroup(toggleGroup);
        addSection(vbox, "Toggles", new HBox(10, tb1, tb2));


        // Cards Section
        addSection(vbox, "Cards", new HBox(20,
                createCard("John Doe", "Software Engineer", Color.DODGERBLUE),
                createCard("Jane Smith", "UX/UI Designer", Color.HOTPINK),
                createCard("Alice Brown", "Project Manager", Color.MEDIUMSEAGREEN)
        ));

        return vbox;
    }

    private Button styledButton(String text, String... styleClasses) {
        Button btn = new Button(text);
        btn.getStyleClass().addAll(styleClasses);
        return btn;
    }

    private VBox createCard(String name, String role, Color color) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(16));
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(180);
        // Style using AtlantaFX theme variables for consistency and theme-awareness
        card.setStyle(
            "-fx-background-color: -color-bg-default;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: -color-border-default;" +
            "-fx-border-radius: 12;" +
            "-fx-border-width: 1px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );

        Circle avatar = new Circle(30, color);
        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add(Styles.TITLE_4);
        Label roleLabel = new Label(role);
        roleLabel.getStyleClass().add(Styles.TEXT_MUTED);

        card.getChildren().addAll(avatar, nameLabel, roleLabel);
        return card;
    }

    // --- TAB 2: FORM CONTROLS ---
    private Node createFormControlsTab() {
        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(10);
        grid.setPadding(new Insets(25));

        // Add controls to grid for clean alignment
        int rowIndex = 0;
        grid.add(new Label("Text Field (Filled)"), 0, rowIndex);
        TextField textField = new TextField("Some text");
        textField.getStyleClass().add(Styles.ROUNDED);
        grid.add(textField, 1, rowIndex++);

        grid.add(new Label("Password Field (Filled)"), 0, rowIndex);
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.getStyleClass().add(Styles.ROUNDED);
        grid.add(passwordField, 1, rowIndex++);

        grid.add(new Label("Combo Box"), 0, rowIndex);
        ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList("Option 1", "Option 2", "Option 3"));
        comboBox.getSelectionModel().selectFirst();
        grid.add(comboBox, 1, rowIndex++);

        grid.add(new Label("Selection Controls"), 0, rowIndex);
        CheckBox checkBox = new CheckBox("Remember me");
        RadioButton rb1 = new RadioButton("Radio A");
        RadioButton rb2 = new RadioButton("Radio B");
        ToggleGroup radioGroup = new ToggleGroup();
        rb1.setToggleGroup(radioGroup);
        rb2.setToggleGroup(radioGroup);
        rb1.setSelected(true);
        HBox selectionBox = new HBox(20, checkBox, rb1, rb2);
        selectionBox.setAlignment(Pos.CENTER_LEFT);
        grid.add(selectionBox, 1, rowIndex++);

        grid.add(new Label("Slider"), 0, rowIndex);
        Slider slider = new Slider(0, 100, 50);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(25);
        grid.add(slider, 1, rowIndex++);

        grid.add(new Label("Spinner"), 0, rowIndex);
        Spinner<Integer> spinner = new Spinner<>(0, 10, 5);
        spinner.setEditable(true);
        grid.add(spinner, 1, rowIndex++);

        return grid;
    }

    // --- TAB 3: DATA DISPLAY ---
    private Node createDataDisplayTab() {
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(25));

        // TableView
        TableView<Person> table = new TableView<>();
        TableColumn<Person, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(150);
        TableColumn<Person, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleCol.setPrefWidth(200);
        table.getColumns().addAll(nameCol, roleCol);
        table.setItems(FXCollections.observableArrayList(
                new Person("John Doe", "Software Engineer"),
                new Person("Jane Smith", "UX/UI Designer"),
                new Person("Alice Brown", "Project Manager")
        ));
        table.setPrefHeight(150);
        addSection(vbox, "Table View", table);

        // ListView
        ListView<String> listView = new ListView<>(FXCollections.observableArrayList("List Item 1", "List Item 2", "List Item 3", "List Item 4"));
        listView.setPrefHeight(120);
        addSection(vbox, "List View", listView);

        return vbox;
    }

    // --- TAB 4: NAVIGATION ---
    private Node createNavigationTab() {
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(25));

        // Pagination
        Pagination pagination = new Pagination(5, 0);
        pagination.setPageFactory(pageIndex -> {
            Label pageLabel = new Label("Content for Page " + (pageIndex + 1));
            VBox pageContent = new VBox(pageLabel);
            pageContent.setAlignment(Pos.CENTER);
            pageContent.setPadding(new Insets(20));
            return pageContent;
        });
        addSection(vbox, "Pagination", pagination);


        // Accordion
        Accordion accordion = new Accordion();
        TitledPane pane1 = new TitledPane("Section 1", new Label("Here is the content for the first section."));
        TitledPane pane2 = new TitledPane("Section 2", new Label("Here is the content for the second section."));
        TitledPane pane3 = new TitledPane("Section 3 (Disabled)", new Label("This content is not visible."));
        pane3.setDisable(true);
        accordion.getPanes().addAll(pane1, pane2, pane3);
        addSection(vbox, "Accordion", accordion);

        return vbox;
    }

    /**
     * Shows a loading indicator for a short duration.
     */
    private void simulateLoading() {
        showLoading(true);
        Timeline timeline = new Timeline(new KeyFrame(
            Duration.seconds(1.5),
            e -> showLoading(false)
        ));
        timeline.play();
    }

    /**
     * A helper method to add a titled section with a separator to a VBox.
     */
    private void addSection(VBox container, String title, Node content) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add(Styles.TITLE_4);
        VBox contentBox = new VBox(10, titleLabel, content);
        container.getChildren().addAll(contentBox, new Separator());
    }

    // --- Interface Implementations ---
    @Override
    public Parent getView() {
        return root;
    }

    @Override
    public Button getBackButton() {
        return backButton;
    }

    public void showLoading(boolean show) {
        loadingIndicator.setVisible(show);
    }

    /**
     * Simple data class (POJO) for the TableView example.
     * The properties (getName, getRole) must match the PropertyValueFactory strings.
     */
    public static class Person {
        private final String name;
        private final String role;

        public Person(String name, String role) {
            this.name = name;
            this.role = role;
        }

        public String getName() {
            return name;
        }

        public String getRole() {
            return role;
        }
    }
}
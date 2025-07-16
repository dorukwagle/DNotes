package com.doruk.dnotes.views;

import java.util.List;
import java.util.function.Function;

import org.kordamp.ikonli.javafx.FontIcon;

import com.doruk.dnotes.dto.CollectionDto;
import com.doruk.dnotes.dto.SearchControlsDto;
import com.doruk.dnotes.interfaces.IBookView;
import com.doruk.dnotes.views.components.Sidebar;

import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class BookPage implements IBookView {

    private Button backButton;
    private BorderPane root;
    private VBox editorContainer;
    private Sidebar sidebar;
    private BorderPane topBar;

    public BookPage() {
        root = new BorderPane();
        editorContainer = new VBox();

        // add sidebar to the left
        sidebar = new Sidebar();
        root.setLeft(sidebar.getView());

        // Create top bar
        topBar = new BorderPane();
        topBar.getStyleClass().add("top-bar");
        topBar.setStyle(
            "-fx-background-color: -color-bg-subtle;" +
            "-fx-padding: 10 20;" +
            "-fx-border-color: -color-border-default;" +
            "-fx-border-width: 0 0 1 0;"
        );
        
        // Set up main container with top bar
        VBox mainContent = new VBox();
        StackPane mainContainer = new StackPane();
        mainContent.getChildren().addAll(topBar, mainContainer);
        VBox.setVgrow(mainContainer, Priority.ALWAYS);
        root.setCenter(mainContent);

        // create a fab button
        Button fabButton = new Button();
        fabButton.setTooltip(new Tooltip("New Page"));
        fabButton.getStyleClass().addAll(Styles.ACCENT, Styles.ELEVATED_3);
        fabButton.setStyle(
                "-fx-background-radius: 28;\n" +
                        "-fx-min-width: 56;\n" +
                        "-fx-min-height: 56;\n" +
                        "-fx-max-width: 56;\n" +
                        "-fx-max-height: 56;\n" +
                        "-fx-cursor: hand;\n" +
                        "-fx-scale-x: 1;\n" +
                        "-fx-scale-y: 1;\n" +
                        "-fx-transition: all 1s ease;\n" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 8, 0, 0, 2);");
        FontIcon icon = new FontIcon("mdi2f-file-plus");
        icon.setIconSize(24);
        icon.setScaleX(2.3);
        icon.setScaleY(2.3);
        fabButton.setGraphic(icon);

        StackPane.setAlignment(fabButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(fabButton, new Insets(0, 50, 50, 0));
        mainContainer.getChildren().addAll(editorContainer, fabButton);

        VBox.setVgrow(editorContainer, Priority.ALWAYS);

        // shrink, expang fab while hover
        fabButton.hoverProperty().addListener((_, _, newVal) -> {
            if (newVal)
                fabButton.setStyle(fabButton.getStyle() + "-fx-scale-x: 0.9; -fx-scale-y: 0.9;");
            else
                fabButton.setStyle(fabButton.getStyle().replace("-fx-scale-x: 0.9; -fx-scale-y: 0.9;", ""));
        });

        Text bookText = new Text("Click on a page to view/edit");
        bookText.setFont(new Font(24));
        editorContainer.getChildren().add(bookText);
        BorderPane.setAlignment(bookText, Pos.CENTER);

        // Create and style back button
        backButton = new Button();
        FontIcon backIcon = new FontIcon("mdi2b-backspace");
        backIcon.setIconSize(20);
        backIcon.setScaleX(1.3);
        backIcon.setScaleY(1.3);
        backButton.setGraphic(backIcon);
        backButton.setMinWidth(50);
        backButton.setStyle(backButton.getStyle() + "-fx-cursor: hand;");
        backButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_ICON);
        backButton.setTooltip(new Tooltip("Go back"));
        
        // Add button to top right
        topBar.setRight(backButton);
        BorderPane.setAlignment(backButton, Pos.CENTER_RIGHT);
        BorderPane.setMargin(backButton, new Insets(0, 0, 0, 10));

    }

    @Override
    public Parent getView() {
        return root;
    }

    @Override
    public Button getBackButton() {
        return backButton;
    }
    
    @Override
    public void displayEditor(Parent editorView) {
        this.editorContainer.getChildren().clear();
        VBox.setVgrow(editorView, Priority.ALWAYS);
        this.editorContainer.getChildren().add(editorView);
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
    public void setSelectedSidebarItem(CollectionDto item) {
        this.sidebar.setSelectedItem(item);
    }

    @Override
    public void setPlaceholder(String txt) {
        
    }
}

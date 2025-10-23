package com.doruk.dnotes.views;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.doruk.dnotes.views.components.BackButton;
import javafx.scene.input.MouseEvent;
import org.kordamp.ikonli.javafx.FontIcon;

import com.doruk.dnotes.dto.BookPageDto;
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
import org.kordamp.ikonli.materialdesign2.MaterialDesignF;

public class BookPage implements IBookView {
    private Button backButton;
    private BorderPane root;
    private VBox editorContainer;
    private Sidebar<BookPageDto> sidebar;
    private BorderPane topBar;
    private PlaceholderView placeholderView;
    private Button fabButton;
    private BiConsumer<MouseEvent, BookPageDto> sidebarItemOnRightClick;

    public BookPage() {
        root = new BorderPane();
        editorContainer = new VBox();

        // add sidebar to the left
        sidebar = new Sidebar<>();
        root.setLeft(sidebar.getView());
        sidebar.setOnRightClick((e,dto) -> {
            if (this.sidebarItemOnRightClick != null)
                this.sidebarItemOnRightClick.accept(e, dto);
        });

        // Create top bar
        topBar = new BorderPane();
        topBar.getStyleClass().add("top-bar");
        topBar.setStyle(
            "-fx-background-color: -color-bg-subtle;" +
            "-fx-padding: 10 20;" +
            "-fx-border-color: -color-border-default;" +
            "-fx-border-width: 0 0 1 0;"
        );
        editorContainer.getChildren().add(topBar);

        placeholderView = new PlaceholderView();
        editorContainer.getChildren().add(placeholderView.getView());
        
        // Set up main container with top bar
        StackPane mainContainer = new StackPane();
        root.setCenter(mainContainer);

        // create a fab button
        fabButton = new Button();
        fabButton.setTooltip(new Tooltip("New Page"));
        fabButton.getStyleClass().addAll(Styles.ACCENT, Styles.ELEVATED_3);
        FontIcon icon = new FontIcon(MaterialDesignF.FILE_PLUS);
        icon.setIconSize(24);
        icon.setScaleX(2.3);
        icon.setScaleY(2.3);
        fabButton.setGraphic(icon);
        fabButton.getStylesheets().add(getClass().getResource("/styles.scss").toExternalForm());
        fabButton.getStyleClass().add("fab-button");

        StackPane.setAlignment(fabButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(fabButton, new Insets(0, 50, 50, 0));
        mainContainer.getChildren().addAll(editorContainer, fabButton);

        VBox.setVgrow(editorContainer, Priority.ALWAYS);

        // Create and style back button
        backButton = new BackButton();
        backButton.setMinWidth(50);
        
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
    public void setSidebarItems(List<BookPageDto> items) {
        this.sidebar.setItems(items);
    }

    @Override
    public void setSidebarItemOnSelect(Consumer<BookPageDto> onSelect) {
        this.sidebar.setOnSelect(onSelect);
    }
    
    @Override
    public SearchControlsDto getSidebarSearchControls() {
        return sidebar.getSearchControls();
    }

    @Override
    public void setSelectedSidebarItem(BookPageDto item) {
        this.sidebar.setSelectedItem(item);
    }

    @Override
    public void setPlaceholder(String txt) {
        this.placeholderView.setPlaceholder(txt);
    }

    @Override
    public void setSidebarItemOnRightClick(BiConsumer<MouseEvent, BookPageDto> onRightClick) {
        this.sidebarItemOnRightClick = onRightClick;
    }

    @Override
    public Button getNewNoteButton() {
        return this.fabButton;
    }
}

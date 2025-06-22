package com.doruk.dnotes.views.components;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.kordamp.ikonli.javafx.FontIcon;

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class FAB extends Button {
    private VBox itemsPane;
    private Pane parent;
    private boolean isItemsHovering = false;
    private boolean isFabHovering = false;

    public FAB() {
        super();

        this.itemsPane = new VBox(8); // 8px spacing between items
        this.itemsPane.setPadding(new Insets(12));
        this.itemsPane.toFront();
        this.itemsPane.setMaxWidth(175);
        this.itemsPane.setMaxHeight(50);
        this.itemsPane.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-background-radius: 8; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 8, 0, 2, 2);"
        );

        // Create menu items
        Button addBookBtn = createMenuItem("mdi2b-book-plus", "Book");
        Button addCollectionBtn = createMenuItem("mdi2f-folder-plus", "Collection");

        // Add click handlers
        addBookBtn.setOnAction(e -> System.out.println("Add Book clicked"));
        addCollectionBtn.setOnAction(e -> System.out.println("Add Collection clicked"));

        this.itemsPane.getChildren().addAll(addBookBtn, addCollectionBtn);

        this.getStyleClass().addAll(Styles.ACCENT);
        this.setScaleZ(2);
        this.setTooltip(new Tooltip("Add New"));
        this.setStyle(
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
        FontIcon plusIcon = new FontIcon("mdi2b-book-plus-multiple");
        plusIcon.setScaleX(2.3);
        plusIcon.setScaleY(2.3);
        this.setGraphic(plusIcon);

        this.setOnMouseEntered(e -> {
            this.isFabHovering = true;
            this.openMenu();
        });
        this.setOnMouseExited(e -> {
            this.isFabHovering = false;
            this.closeMenu();
        });

        this.itemsPane.setOnMouseEntered(e -> this.isItemsHovering = true);
        this.itemsPane.setOnMouseExited(e -> {
            this.isItemsHovering = false;
            this.closeMenu();
        });
    }

    private void openMenu() {
        this.setStyle(this.getStyle() + "-fx-scale-x: 0.8; -fx-scale-y: 0.8;");
        this.itemsPane.setVisible(true);
    }

    private void close() {
        this.setStyle(this.getStyle().replace("-fx-scale-x: 0.8; -fx-scale-y: 0.8;", ""));
        this.itemsPane.setVisible(false);
    }

    private void closeMenu() {
        // use delay of 200ms ( allow to move mouse hover from  fab to items )
        CompletableFuture.delayedExecutor(200, TimeUnit.MILLISECONDS)
            .execute(() -> {
                if (this.isItemsHovering || this.isFabHovering)
                    return;

                // hovering exited, close the menu
                Platform.runLater(this::close);
            });
    }

    private Button createMenuItem(String iconLiteral, String text) {
        Button menuItem = new Button(text);
        menuItem.setMaxWidth(Double.MAX_VALUE);
        menuItem.setAlignment(Pos.CENTER_LEFT);
        menuItem.setContentDisplay(javafx.scene.control.ContentDisplay.LEFT);
        menuItem.getStyleClass().addAll(Styles.LARGE, Styles.TEXT_BOLD);
        
        // Set style
        menuItem.setStyle(
            "-fx-padding: 8 12; " +
            "-fx-background-radius: 4; " +
            "-fx-background-color: transparent; " +
            "-fx-text-fill: -color-fg-default; " +
            "-fx-cursor: hand;"
        );
        
        // Add hover effect
        menuItem.hoverProperty().addListener((obs, oldVal, isHovering) -> {
            menuItem.setStyle(
                "-fx-padding: 8 12; " +
                "-fx-background-radius: 4; " +
                "-fx-background-color: " + (isHovering ? "-color-accent-fg; " : "transparent; ") +
                "-fx-text-fill: -color-fg-emphasis; " +
                "-fx-cursor: hand;"
            );
        });
        
        // Add icon
        FontIcon icon = new FontIcon(iconLiteral);
        icon.setIconSize(20);
        icon.setScaleX(1.3);
        icon.setScaleY(1.3);
        menuItem.setGraphic(icon);
        
        // Add spacing between icon and text
        menuItem.setGraphicTextGap(20);
        
        return menuItem;
    }
    
    public void setActionsParent(Pane parent) {
        if (this.parent != null)
            return;

        if (!(parent instanceof StackPane))
            throw new IllegalArgumentException("Parent must be a StackPane");

        this.parent = parent;
        this.parent.getChildren().add(this.itemsPane);

        // Position itemsPane above the FAB with same right margin
        this.itemsPane.setVisible(false);

        // Position itemsPane above FAB with same right margin
        if (parent instanceof StackPane) {
            Insets fabMargin = StackPane.getMargin(this);
            if (fabMargin == null) {
                fabMargin = new Insets(0, 50, 50, 0); // Default margin if not set
                StackPane.setMargin(this, fabMargin);
            }
            StackPane.setAlignment(this.itemsPane, Pos.BOTTOM_RIGHT);
            StackPane.setMargin(this.itemsPane, new Insets(0, fabMargin.getRight(), fabMargin.getBottom() + 75, 0)); // 56
                                                                                                                     // is
                                                                                                                     // FAB
                                                                                                                     // height,
                                                                                                                     // 12
                                                                                                                     // is
                                                                                                                     // spacing
        }
    }
}

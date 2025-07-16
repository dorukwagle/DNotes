package com.doruk.dnotes.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PlaceholderView {
    
    private VBox view;
    private Text text;

    public PlaceholderView() {        
        this.view = new VBox();

        this.view.setSpacing(10);
        this.view.setAlignment(Pos.CENTER);

        text = new Text("No items found. Create a new one.");
        text.setFont(new Font(24));
        text.setStyle(text.getStyle() + "-fx-font-weight: bold;");
        this.view.getChildren().add(text);

        VBox.setMargin(text, new Insets(200, 0, 0, 0));
    }

    public Parent getView() {
        return this.view;
    }

    public void setPlaceholder(String txt) {
        this.text.setText(txt);
    }
}

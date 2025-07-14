package com.doruk.dnotes.views;

import com.doruk.dnotes.interfaces.IView;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class PlaceholderView {
    
    private VBox view;
    private Text text;

    public PlaceholderView() {        
        this.view = new VBox();

        this.view.setSpacing(10);
        this.view.setAlignment(Pos.CENTER);

        text = new Text("No items found. Creat a new one.");
        this.view.getChildren().add(text);
    }

    public Parent getView() {
        return this.view;
    }

    public void setPlaceholder(String txt) {
        this.text.setText(txt);
    }
}

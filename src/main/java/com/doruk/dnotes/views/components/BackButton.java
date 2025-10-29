package com.doruk.dnotes.views.components;

import atlantafx.base.theme.Styles;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignB;

public class BackButton extends Button {
    public BackButton() {
        FontIcon backIcon = new FontIcon(MaterialDesignB.BACKSPACE);
        backIcon.setIconSize(20);
        backIcon.setScaleX(1.3);
        backIcon.setScaleY(1.3);
        this.setGraphic(backIcon);

        this.setStyle(this.getStyle() + "-fx-cursor: hand;");
        this.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_ICON);
        this.setTooltip(new Tooltip("Go back"));
    }
}

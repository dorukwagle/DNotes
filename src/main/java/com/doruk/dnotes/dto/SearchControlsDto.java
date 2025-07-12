package com.doruk.dnotes.dto;

import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

public class SearchControlsDto {
    private TextField searchField;
    private ToggleButton sortByToggle;
    private ToggleButton sortOrderToggle;

    public SearchControlsDto(TextField searchField, ToggleButton sortByToggle, ToggleButton sortOrderToggle) {
        this.searchField = searchField;
        this.sortByToggle = sortByToggle;
        this.sortOrderToggle = sortOrderToggle;
    }

    public TextField getSearchField() {
        return searchField;
    }

    public ToggleButton getSortByToggle() {
        return sortByToggle;
    }

    public ToggleButton getSortOrderToggle() {
        return sortOrderToggle;
    }
}

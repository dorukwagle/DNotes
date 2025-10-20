package com.doruk.dnotes.views.components;

import com.doruk.dnotes.dto.BrowserDto;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;

import java.util.List;
import java.util.function.Consumer;

public class BrowserTable extends TableView<BrowserDto> {
    private Consumer<BrowserDto> onDtoClick;

    public BrowserTable() {
        this.setEditable(false);
        this.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // Allow multiple selections

        // Column for 'name'
        TableColumn<BrowserDto, String> nameColumn = new TableColumn<>("Name");
        // PropertyValueFactory maps the "name" property of BrowserDto to this column
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(200); // Give it some initial width

        // Column for 'type'
        TableColumn<BrowserDto, String> typeColumn = new TableColumn<>("Type");
        // Custom cell value factory to display dto.getType().name()
        typeColumn.setCellValueFactory(cellData -> {
            BrowserDto dto = cellData.getValue();
            return new SimpleStringProperty(dto.getType().name());
        });
        typeColumn.setPrefWidth(100);

        // Add columns to the TableView
        this.getColumns().add(nameColumn);
        this.getColumns().add(typeColumn);

        // Optional: Make columns resizeable
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);


        this.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                BrowserDto selectedDto = this.getSelectionModel().getSelectedItem();
                if (selectedDto != null && onDtoClick != null) {
                    onDtoClick.accept(selectedDto);
                }
            }
        });
    }

    /**
     * Updates the contents of the TableView with a new list of BrowserDto items.
     * @param dtoList The list of DTOs to display.
     */
    public void updateContents(List<BrowserDto> dtoList) {
        ObservableList<BrowserDto> observableList = FXCollections.observableArrayList(dtoList);
        this.setItems(observableList);
    }

    /**
     * Returns the currently selected BrowserDto.
     * @return The selected BrowserDto, or null if nothing is selected.
     */
    public BrowserDto getSelectedBrowserDto() {
        return this.getSelectionModel().getSelectedItem();
    }

    /**
     * Returns a list of all currently selected BrowserDto items.
     * @return An ObservableList of selected BrowserDto items.
     */
    public ObservableList<BrowserDto> getSelectedBrowserDtos() {
        return this.getSelectionModel().getSelectedItems();
    }

    /**
     * Sets a handler for when a BrowserDto item is double-clicked.
     * This is intended for navigating into folders.
     * @param handler A Consumer that accepts the double-clicked BrowserDto.
     */
    public void setOnDtoClick(Consumer<BrowserDto> handler) {
        this.onDtoClick = handler;
    }
}

package com.simplinote.simplinote.components;

import com.simplinote.simplinote.model.Events;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.time.LocalDate;

public class EventDialog extends Dialog<Events> {
    public EventDialog(LocalDate date) {
        setTitle("Event Details");
        setHeaderText("Create New Event");

        // Set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create the form grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField();
        titleField.setPromptText("Event Title");
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Event Description");
        descriptionArea.setPrefRowCount(3);

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionArea, 1, 1);

        getDialogPane().setContent(grid);

        // Convert the result to an Event object when the save button is clicked
        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Events(date, titleField.getText(), descriptionArea.getText());
            }
            return null;
        });
    }
}
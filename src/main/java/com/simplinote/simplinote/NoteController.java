package com.simplinote.simplinote;


import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;

/**
 * NoteController handles user interactions for the notepad feature.
 */
public class NoteController {
    private AnchorPane noteArea;

    /**
     * No-argument constructor required by FXMLLoader.
     */
    public NoteController() {
        // This constructor is required by FXMLLoader.
    }

    /**
     * Initializes the controller with the AnchorPane.
     *
     * @param noteArea The AnchorPane acting as the notepad area.
     */
    public void initialize(AnchorPane noteArea) {
        this.noteArea = noteArea;
        setupClickToEdit();
    }

    /**
     * Sets up the click-to-edit functionality.
     * When the user clicks, a new TextField is added at the click location.
     */
    private void setupClickToEdit() {
        noteArea.setOnMouseClicked(event -> {
            // Create a new TextField when the user clicks
            TextField textField = new TextField();
            textField.promptTextProperty();
            textField.setStyle("-fx-font-size: 16px; -fx-background-color: transparent; -fx-border-color: black;");

            // Add the TextField to the AnchorPane
            noteArea.getChildren().add(textField);

            // Position the TextField at the click location
            textField.setLayoutX(event.getX());
            textField.setLayoutY(event.getY());

            // Set a preferred width for the TextField
            textField.setPrefWidth(200);

            // Optionally, focus the TextField so the user can start typing immediately
            textField.requestFocus();
        });
    }
}
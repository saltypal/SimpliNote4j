package com.simplinote.simplinote;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * NoteView class manages the notepad view.
 * It loads the FXML file and provides access to the root node.
 */
public class NoteView {
    private AnchorPane notePane;

    /**
     * Constructor to load the FXML file and initialize the notepad view.
     */
    public NoteView() {
        try {
            // Load the FXML file using FXMLLoader
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NoteView.fxml"));
            notePane = loader.load();

            // Get the controller and initialize it with the AnchorPane
            NoteController controller = loader.getController();
            controller.initialize(notePane);
        } catch (IOException e) {
            // Handle exceptions during FXML loading
            e.printStackTrace();
        }
    }

    /**
     * Returns the root node of the notepad view.
     *
     * @return The AnchorPane containing the notepad UI.
     */
    public AnchorPane getNotePane() {
        return notePane;
    }
}
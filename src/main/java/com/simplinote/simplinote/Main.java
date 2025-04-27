package com.simplinote.simplinote;
// Import necessary JavaFX classes

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class serves as the entry point of the application.
 * It initializes the NoteView and displays the notepad feature.
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Create an instance of NoteView to load the notepad UI
        NoteView noteView = new NoteView();

        // Create a scene with the root node from NoteView
        Scene scene = new Scene(noteView.getNotePane(), 800, 600);

        // Set the stage title and scene
        primaryStage.setTitle("Notepad Feature");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Launches the JavaFX application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
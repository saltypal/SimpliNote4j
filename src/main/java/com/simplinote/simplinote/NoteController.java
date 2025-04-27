package com.simplinote.simplinote;

import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import com.simplinote.simplinote.util.FileHandler;

public class NoteController {
    private AnchorPane noteArea;
    private FileHandler fileHandler;

    public NoteController() {
        fileHandler = new FileHandler();
    }

    public void initialize(AnchorPane noteArea) {
        this.noteArea = noteArea;
        setupNoteArea();
    }

    private void setupNoteArea() {
        noteArea.setOnMouseClicked(event -> {
            TextArea textArea = new TextArea();
            textArea.setPromptText("Type here...");
            textArea.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");

            textArea.setLayoutX(event.getX());
            textArea.setLayoutY(event.getY());

            textArea.textProperty().addListener((obs, old, newText) -> {
                textArea.setPrefRowCount(newText.split("\n").length);
            });

            noteArea.getChildren().add(textArea);
            textArea.requestFocus();
        });
    }

    public void saveNote() {
        StringBuilder content = new StringBuilder();
        noteArea.getChildren().filtered(node -> node instanceof TextArea)
                .forEach(node -> {
                    TextArea textArea = (TextArea) node;
                    content.append(textArea.getText()).append("\n");
                });
        fileHandler.saveToFile(content.toString(), noteArea.getScene().getWindow());
    }

    public void loadNote() {
        String content = fileHandler.loadFromFile(noteArea.getScene().getWindow());
        if (content != null) {
            TextArea textArea = new TextArea(content);
            textArea.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");
            noteArea.getChildren().add(textArea);
        }
    }
}
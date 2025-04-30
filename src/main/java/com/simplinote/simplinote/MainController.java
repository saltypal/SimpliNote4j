package com.simplinote.simplinote;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;
import com.simplinote.simplinote.util.FileHandler;
import com.simplinote.simplinote.features.*;
import javafx.stage.Stage;

public class MainController {
    @FXML
    private Button calendarBtn;
    @FXML
    private Button todoBtn;
    @FXML
    private Button timerBtn;
    @FXML
    private Button aiBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private Button loadBtn;
    @FXML
    private AnchorPane notepadArea;
    @FXML
    private Label placeholderText;
    @FXML
    private ImageView appIcon;

    private FileHandler fileHandler;
    private CalendarFeature calendarFeature;
    private TodoFeature todoFeature;
    private TimerFeature timerFeature;
    private AiChatFeature aiChatFeature;

    @FXML
    public void initialize() {
        System.out.println("Initializing MainController...");
        try {
            fileHandler = new FileHandler();
            initializeFeatures();
            setupButtons();
            setupNotepadArea();
            System.out.println("MainController initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing MainController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeFeatures() {
        try {
            calendarFeature = new CalendarFeature();
            todoFeature = new TodoFeature();
            timerFeature = new TimerFeature();
            aiChatFeature = new AiChatFeature();
            System.out.println("Features initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing features: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupButtons() {
        try {
            calendarBtn.setOnAction(e -> openCalendar());
            todoBtn.setOnAction(e -> openTodoList());
            timerBtn.setOnAction(e -> openTimer());
            aiBtn.setOnAction(e -> openAiChat());
            saveBtn.setOnAction(e -> saveNote());
            loadBtn.setOnAction(e -> loadNote());
            System.out.println("Buttons setup successfully");
        } catch (Exception e) {
            System.err.println("Error setting up buttons: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupNotepadArea() {
        try {
            notepadArea.setOnMouseClicked(event -> {
                if (event.getTarget() == notepadArea) {
                    TextArea textArea = new TextArea();
                    textArea.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");

                    // Position the text area where clicked
                    textArea.setLayoutX(event.getX());
                    textArea.setLayoutY(event.getY());

                    // Set reasonable default size
                    textArea.setPrefWidth(300);
                    textArea.setPrefHeight(100);

                    // Hide placeholder when typing starts
                    textArea.textProperty().addListener((obs, old, newText) -> {
                        placeholderText.setVisible(newText.isEmpty() && notepadArea.getChildren().size() <= 1);
                        textArea.setPrefRowCount(Math.max(1, newText.split("\n").length));
                    });

                    notepadArea.getChildren().add(textArea);
                    textArea.requestFocus();

                    // Hide placeholder when any text area is added
                    placeholderText.setVisible(false);
                }
            });

            // Initialize with placeholder visible
            placeholderText.setVisible(true);
            System.out.println("Notepad area setup successfully");
        } catch (Exception e) {
            System.err.println("Error setting up notepad area: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void openCalendar() {
        try {
            calendarFeature.show((Stage) calendarBtn.getScene().getWindow());
        } catch (Exception e) {
            System.err.println("Error opening calendar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void openTodoList() {
        try {
            todoFeature.show((Stage) todoBtn.getScene().getWindow());
        } catch (Exception e) {
            System.err.println("Error opening todo list: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void openTimer() {
        try {
            timerFeature.show((Stage) timerBtn.getScene().getWindow());
        } catch (Exception e) {
            System.err.println("Error opening timer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void openAiChat() {
        try {
            aiChatFeature.show();
        } catch (Exception e) {
            System.err.println("Error opening AI chat: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveNote() {
        try {
            StringBuilder content = new StringBuilder();
            for (javafx.scene.Node node : notepadArea.getChildren()) {
                if (node instanceof TextArea) {
                    TextArea textArea = (TextArea) node;
                    if (!textArea.getText().isEmpty()) {
                        double x = textArea.getLayoutX();
                        double y = textArea.getLayoutY();
                        // Save position and content
                        content.append(String.format("POS:%f,%f\n", x, y));
                        content.append(textArea.getText()).append("\n");
                        content.append("---SEPARATOR---\n");
                    }
                }
            }
            if (content.length() > 0) {
                fileHandler.saveToFile(content.toString(), notepadArea.getScene().getWindow());
                System.out.println("Note saved successfully");
            } else {
                System.out.println("Nothing to save");
            }
        } catch (Exception e) {
            System.err.println("Error saving note: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadNote() {
        try {
            String content = fileHandler.loadFromFile(notepadArea.getScene().getWindow());
            if (content != null && !content.isEmpty()) {
                clearNotepad();

                // Split content by separator
                String[] notes = content.split("---SEPARATOR---\n");
                for (String note : notes) {
                    if (note.trim().isEmpty()) continue;

                    double x = 10, y = 10;
                    String noteText = note;

                    // Check if position information is available
                    if (note.startsWith("POS:")) {
                        String[] lines = note.split("\n", 2);
                        if (lines.length > 1) {
                            String posLine = lines[0].substring(4);
                            noteText = lines[1];
                            String[] coords = posLine.split(",");
                            if (coords.length == 2) {
                                try {
                                    x = Double.parseDouble(coords[0]);
                                    y = Double.parseDouble(coords[1]);
                                } catch (NumberFormatException e) {
                                    System.err.println("Error parsing coordinates: " + e.getMessage());
                                }
                            }
                        }
                    }

                    TextArea textArea = new TextArea(noteText);
                    textArea.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");
                    textArea.setLayoutX(x);
                    textArea.setLayoutY(y);
                    textArea.setPrefWidth(300);
                    textArea.setPrefHeight(100);
                    notepadArea.getChildren().add(textArea);
                }

                placeholderText.setVisible(false);
                System.out.println("Note loaded successfully");
            } else {
                System.out.println("No note content to load");
            }
        } catch (Exception e) {
            System.err.println("Error loading note: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearNotepad() {
        notepadArea.getChildren().removeIf(node -> node instanceof TextArea);
        placeholderText.setVisible(true);
        System.out.println("Notepad cleared");
    }
}
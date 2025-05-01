package com.simplinote.simplinote;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import com.simplinote.simplinote.util.FileHandler;
import com.simplinote.simplinote.features.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class MainController {
    @FXML private Button calendarBtn;
    @FXML private Button todoBtn;
    @FXML private Button timerBtn;
    @FXML private Button aiBtn;
    @FXML private Button saveBtn;
    @FXML private Button loadBtn;
    @FXML private AnchorPane notepadArea;
    @FXML private Label placeholderText;
    @FXML private ImageView appIcon;

    private FileHandler fileHandler;
    private CalendarFeature calendarFeature;
    private TodoFeature todoFeature;
    private TimerFeature timerFeature;
    private AiChatFeature aiChatFeature;

    // Track whether we're in text area drag mode
    private double dragOffsetX, dragOffsetY;
    private TextArea activeTextArea;

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
            showErrorAlert("Initialization Error", "Failed to initialize application: " + e.getMessage());
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
            showErrorAlert("Feature Initialization Error", "Failed to initialize features: " + e.getMessage());
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

            // Add a clear button
            Button clearBtn = new Button("Clear");
            clearBtn.getStyleClass().add("action-button");
            clearBtn.setOnAction(e -> clearNotepad());

            // Add the clear button next to the other buttons
            if (saveBtn.getParent() instanceof javafx.scene.layout.HBox) {
                ((javafx.scene.layout.HBox) saveBtn.getParent()).getChildren().add(clearBtn);
            }

            System.out.println("Buttons setup successfully");
        } catch (Exception e) {
            System.err.println("Error setting up buttons: " + e.getMessage());
            e.printStackTrace();
            showErrorAlert("Button Setup Error", "Failed to setup buttons: " + e.getMessage());
        }
    }

    private void setupNotepadArea() {
        try {
            notepadArea.setOnMouseClicked(event -> {
                if (event.getTarget() == notepadArea) {
                    createNewTextArea(event.getX(), event.getY());
                }
            });

            // Initialize with placeholder visible
            placeholderText.setVisible(true);
            System.out.println("Notepad area setup successfully");
        } catch (Exception e) {
            System.err.println("Error setting up notepad area: " + e.getMessage());
            e.printStackTrace();
            showErrorAlert("Notepad Setup Error", "Failed to setup notepad area: " + e.getMessage());
        }
    }

    private void createNewTextArea(double x, double y) {
        // Create text area
        TextArea textArea = new TextArea();
        textArea.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 5;");

        // Position the text area where clicked
        textArea.setLayoutX(x);
        textArea.setLayoutY(y);

        // Set reasonable default size
        textArea.setPrefWidth(300);
        textArea.setPrefHeight(100);

        // Create a handler bar for the text area
        Button deleteBtn = new Button("×");
        deleteBtn.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white;");
        deleteBtn.setOnAction(e -> notepadArea.getChildren().remove(textArea.getParent()));

        VBox wrapper = new VBox();
        wrapper.setStyle("-fx-background-color: transparent;");
        wrapper.setLayoutX(x);
        wrapper.setLayoutY(y);

        // Create a title bar
        javafx.scene.layout.HBox titleBar = new javafx.scene.layout.HBox();
        titleBar.setStyle("-fx-background-color: #e9ecef; -fx-padding: 2px;");
        titleBar.setPrefHeight(20);
        titleBar.setSpacing(5);
        titleBar.setPadding(new Insets(2, 5, 2, 5));

        // Add drag functionality to the title bar
        titleBar.setOnMousePressed(e -> {
            activeTextArea = textArea;
            dragOffsetX = e.getSceneX() - wrapper.getLayoutX();
            dragOffsetY = e.getSceneY() - wrapper.getLayoutY();
        });

        titleBar.setOnMouseDragged(e -> {
            if (activeTextArea == textArea) {
                wrapper.setLayoutX(e.getSceneX() - dragOffsetX);
                wrapper.setLayoutY(e.getSceneY() - dragOffsetY);
            }
        });

        titleBar.getChildren().add(new javafx.scene.control.Label("Note"));
        javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
        javafx.scene.layout.HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        titleBar.getChildren().add(spacer);
        titleBar.getChildren().add(deleteBtn);

        wrapper.getChildren().add(titleBar);
        wrapper.getChildren().add(textArea);

        // Hide placeholder when typing starts
        textArea.textProperty().addListener((obs, old, newText) -> {
            placeholderText.setVisible(notepadArea.getChildren().isEmpty());
        });

        notepadArea.getChildren().add(wrapper);
        textArea.requestFocus();

        // Hide placeholder when any text area is added
        placeholderText.setVisible(false);
    }

    private void openCalendar() {
        try {
            if (calendarFeature != null && calendarBtn.getScene() != null) {
                calendarFeature.show((Stage) calendarBtn.getScene().getWindow());
            }
        } catch (Exception e) {
            System.err.println("Error opening calendar: " + e.getMessage());
            e.printStackTrace();
            showErrorAlert("Calendar Error", "Failed to open calendar: " + e.getMessage());
        }
    }

    private void openTodoList() {
        try {
            if (todoFeature != null && todoBtn.getScene() != null) {
                todoFeature.show((Stage) todoBtn.getScene().getWindow());
            }
        } catch (Exception e) {
            System.err.println("Error opening todo list: " + e.getMessage());
            e.printStackTrace();
            showErrorAlert("To-Do List Error", "Failed to open to-do list: " + e.getMessage());
        }
    }

    private void openTimer() {
        try {
            if (timerFeature != null && timerBtn.getScene() != null) {
                timerFeature.show((Stage) timerBtn.getScene().getWindow());
            }
        } catch (Exception e) {
            System.err.println("Error opening timer: " + e.getMessage());
            e.printStackTrace();
            showErrorAlert("Timer Error", "Failed to open timer: " + e.getMessage());
        }
    }

    private void openAiChat() {
        try {
            if (aiChatFeature != null && aiBtn.getScene() != null) {
                aiChatFeature.show((Stage) aiBtn.getScene().getWindow());
            }
        } catch (Exception e) {
            System.err.println("Error opening AI chat: " + e.getMessage());
            e.printStackTrace();
            showErrorAlert("AI Chat Error", "Failed to open AI chat: " + e.getMessage());
        }
    }

    private void saveNote() {
        try {
            StringBuilder content = new StringBuilder();
            for (javafx.scene.Node node : notepadArea.getChildren()) {
                if (node instanceof VBox) {
                    VBox wrapper = (VBox) node;
                    for (javafx.scene.Node child : wrapper.getChildren()) {
                        if (child instanceof TextArea) {
                            TextArea textArea = (TextArea) child;
                            if (!textArea.getText().isEmpty()) {
                                double x = wrapper.getLayoutX();
                                double y = wrapper.getLayoutY();
                                // Save position and content
                                content.append(String.format("POS:%f,%f\n", x, y));
                                content.append(textArea.getText()).append("\n");
                                content.append("---SEPARATOR---\n");
                            }
                        }
                    }
                }
            }
            if (content.length() > 0 && notepadArea.getScene() != null) {
                fileHandler.saveToFile(content.toString(), notepadArea.getScene().getWindow());
                showInfoAlert("Save Successful", "Your notes have been saved successfully.");
            } else {
                showInfoAlert("Nothing to Save", "There are no notes to save.");
            }
        } catch (Exception e) {
            System.err.println("Error saving note: " + e.getMessage());
            e.printStackTrace();
            showErrorAlert("Save Error", "Failed to save notes: " + e.getMessage());
        }
    }

    private void loadNote() {
        try {
            if (notepadArea.getScene() == null) return;

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

                    createNewTextArea(x, y);

                    // Find the text area we just created and set its text
                    if (!notepadArea.getChildren().isEmpty()) {
                        VBox wrapper = (VBox) notepadArea.getChildren().get(notepadArea.getChildren().size() - 1);
                        for (javafx.scene.Node child : wrapper.getChildren()) {
                            if (child instanceof TextArea) {
                                ((TextArea) child).setText(noteText);
                                break;
                            }
                        }
                    }
                }

                showInfoAlert("Load Successful", "Your notes have been loaded successfully.");
            } else {
                showInfoAlert("Nothing to Load", "No saved notes were found.");
            }
        } catch (Exception e) {
            System.err.println("Error loading note: " + e.getMessage());
            e.printStackTrace();
            showErrorAlert("Load Error", "Failed to load notes: " + e.getMessage());
        }
    }

    private void clearNotepad() {
        notepadArea.getChildren().clear();
        placeholderText.setVisible(true);
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
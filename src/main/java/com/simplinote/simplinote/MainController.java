package com.simplinote.simplinote;

    import javafx.fxml.FXML;
    import javafx.scene.control.Button;
    import javafx.scene.control.Label;
    import javafx.scene.control.TextArea;
    import javafx.scene.layout.AnchorPane;
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

        private FileHandler fileHandler;
        private CalendarFeature calendarFeature;
        private TodoFeature todoFeature;
        private TimerFeature timerFeature;
        private AiChatFeature aiChatFeature;

        @FXML
        public void initialize() {
            fileHandler = new FileHandler();
            initializeFeatures();
            setupButtons();
            setupNotepadArea();
        }

        private void initializeFeatures() {
            calendarFeature = new CalendarFeature();
            todoFeature = new TodoFeature();
            timerFeature = new TimerFeature();
            aiChatFeature = new AiChatFeature();
        }

        private void setupButtons() {
            calendarBtn.setOnAction(e -> openCalendar());
            todoBtn.setOnAction(e -> openTodoList());
            timerBtn.setOnAction(e -> openTimer());
            aiBtn.setOnAction(e -> openAiChat());
            saveBtn.setOnAction(e -> saveNote());
            loadBtn.setOnAction(e -> loadNote());
        }

        private void setupNotepadArea() {
            notepadArea.setOnMouseClicked(event -> {
                if (event.getTarget() == notepadArea) {
                    TextArea textArea = new TextArea();
                    textArea.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");

                    // Position the text area where clicked
                    textArea.setLayoutX(event.getX());
                    textArea.setLayoutY(event.getY());

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
        }

        private void openCalendar() {
            calendarFeature.show((Stage) calendarBtn.getScene().getWindow());
        }

        private void openTodoList() {
            todoFeature.show((Stage) todoBtn.getScene().getWindow());
        }

        private void openTimer() {
            timerFeature.show((Stage) timerBtn.getScene().getWindow());
        }

        private void openAiChat() {
            // Method no longer takes arguments
            aiChatFeature.show();
        }

        private void saveNote() {
            StringBuilder content = new StringBuilder();
            for (javafx.scene.Node node : notepadArea.getChildren()) {
                if (node instanceof TextArea) {
                    TextArea textArea = (TextArea) node;
                    if (!textArea.getText().isEmpty()) {
                        content.append(textArea.getText()).append("\n");
                    }
                }
            }
            if (content.length() > 0) {
                fileHandler.saveToFile(content.toString(), notepadArea.getScene().getWindow());
            }
        }

        private void loadNote() {
            String content = fileHandler.loadFromFile(notepadArea.getScene().getWindow());
            if (content != null && !content.isEmpty()) {
                clearNotepad();
                TextArea textArea = new TextArea(content);
                textArea.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");
                textArea.setLayoutX(10);
                textArea.setLayoutY(10);
                notepadArea.getChildren().add(textArea);
                placeholderText.setVisible(false);
            }
        }

        private void clearNotepad() {
            notepadArea.getChildren().removeIf(node -> node instanceof TextArea);
            placeholderText.setVisible(true);
        }
    }
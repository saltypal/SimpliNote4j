//package com.simplinote.simplinote;
//
//                import javafx.fxml.FXML;
//                import javafx.fxml.FXMLLoader;
//                import javafx.scene.control.Button;
//                import javafx.scene.control.TextArea;
//                import javafx.scene.layout.AnchorPane;
//                import javafx.stage.Stage;
//                import com.simplinote.simplinote.util.FileHandler;
//                import com.simplinote.simplinote.features.*;
//
//                import java.io.IOException;
//
//public class MainController {
//                    @FXML private Button calendarBtn;
//                    @FXML private Button todoBtn;
//                    @FXML private Button timerBtn;
//                    @FXML private Button aiBtn;
//                    @FXML private Button saveBtn;
//                    @FXML private Button loadBtn;
//                    @FXML private AnchorPane notepadArea;
//
//                    private FileHandler fileHandler;
//                    private CalendarFeature calendarFeature;
//                    private TodoFeature todoFeature;
//                    private TimerFeature timerFeature;
//                    private AiChatFeature aiChatFeature;
//
//                    @FXML
//                    public void initialize() {
//                        fileHandler = new FileHandler();
//                        initializeFeatures();
//                        setupButtons();
//                        setupNotepadArea();
//                    }
//
//                    private void initializeFeatures() {
//                        calendarFeature = new CalendarFeature();
//                        todoFeature = new TodoFeature();
//                        timerFeature = new TimerFeature();
//                        aiChatFeature = new AiChatFeature();
//                    }
//
//                    private void setupButtons() {
//                        // Feature buttons
//                        calendarBtn.setOnAction(e -> openCalendar());
//                        todoBtn.setOnAction(e -> openTodoList());
//                        timerBtn.setOnAction(e -> openTimer());
//                        aiBtn.setOnAction(e -> openAiChat());
//
//                        // Save and Load buttons
//                        saveBtn.setOnAction(e -> saveNote());
//                        loadBtn.setOnAction(e -> loadNote());
//                    }
//
//                    private void setupNotepadArea() {
//                        notepadArea.setOnMouseClicked(event -> {
//                            TextArea textArea = new TextArea();
//                            textArea.setPromptText("Type here...");
//                            textArea.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");
//
//                            textArea.setLayoutX(event.getX());
//                            textArea.setLayoutY(event.getY());
//
//                            textArea.textProperty().addListener((obs, old, newText) -> {
//                                textArea.setPrefRowCount(newText.split("\n").length);
//                            });
//
//                            notepadArea.getChildren().add(textArea);
//                            textArea.requestFocus();
//                        });
//                    }
//
//                    private void openCalendar() {
//                        calendarFeature.show((Stage) calendarBtn.getScene().getWindow());
//                    }
//
//                    private void openTodoList() {
//                        todoFeature.show((Stage) todoBtn.getScene().getWindow());
//                    }
//
//                    private void openTimer() {
//                        timerFeature.show((Stage) timerBtn.getScene().getWindow());
//                    }
//
//    private void openAiChat() {
//        try {
//            // Create a new stage for SuperPieFX
//            Stage superPieStage = new Stage();
//
//            // Initialize SuperPieFX directly
//            com.simplinote.simplinote.superpie.SuperPieFX superPieApp = new com.simplinote.simplinote.superpie.SuperPieFX();
//            superPieApp.start(superPieStage);
//        } catch (Exception e) {
//            System.err.println("Error launching SuperPieFX: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//                    private void saveNote() {
//                        StringBuilder content = new StringBuilder();
//                        notepadArea.getChildren().filtered(node -> node instanceof TextArea)
//                                .forEach(node -> {
//                                    TextArea textArea = (TextArea) node;
//                                    content.append(textArea.getText()).append("\n");
//                                });
//                        fileHandler.saveToFile(content.toString(), notepadArea.getScene().getWindow());
//                    }
//
//                    private void loadNote() {
//                        String content = fileHandler.loadFromFile(notepadArea.getScene().getWindow());
//                        if (content != null) {
//                            notepadArea.getChildren().clear();
//                            TextArea textArea = new TextArea(content);
//                            textArea.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");
//                            textArea.setPrefSize(notepadArea.getWidth(), notepadArea.getHeight());
//                            textArea.setWrapText(true);
//                            notepadArea.getChildren().add(textArea);
//                        }
//                    }
//
//    /**
//     * NoteView class manages the notepad view.
//     * It loads the FXML file and provides access to the root node.
//     */
//    public static class NoteView {
//        private AnchorPane notePane;
//
//        /**
//         * Constructor to load the FXML file and initialize the notepad view.
//         */
//        public NoteView() {
//            try {
//                // Load the FXML file using FXMLLoader
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NoteView.fxml"));
//                notePane = loader.load();
//
//                // Get the controller and initialize it with the AnchorPane
//                NoteController controller = loader.getController();
//                controller.initialize(notePane);
//            } catch (IOException e) {
//                // Handle exceptions during FXML loading
//                e.printStackTrace();
//            }
//        }
//
//        /**
//         * Returns the root node of the notepad view.
//         *
//         * @return The AnchorPane containing the notepad UI.
//         */
//        public AnchorPane getNotePane() {
//            return notePane;
//        }
//    }
//}
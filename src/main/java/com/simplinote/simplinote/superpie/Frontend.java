package com.simplinote.simplinote.superpie;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class Frontend {
    @FXML private Button NewChat;
    @FXML private Button Exit;
    @FXML private Button SendButton;
    @FXML private Button useImage;
    @FXML private Button usePdf;
    @FXML private SplitMenuButton ModelSelector;
    @FXML private Slider setTemp;
    @FXML private ScrollPane chatScrollPane;
    @FXML private VBox ChatSection;
    @FXML private TextField YourMessages;
    @FXML private ToggleButton DarkMode;
    @FXML private RadioButton setJson;
    @FXML private ProgressIndicator Response;
    @FXML private Text name;


    private Backend SP;
    private String currentImageUrl = null;
    private String Path = null;
    private boolean imageP = false;
    private boolean pdfP = false;
    private Stage stage;

    @FXML
    public void initialize() {
        try {
            // Initialize backend
            SP = new Backend();
            SP.ModelName = "gemma3:4b";
            SP.temperature = 0.7;
            SP.newInit();

            // Setup UI components
            setupScrollPane();
            setupModelMenu();
            setupTemperatureSlider();
            setupButtonHandlers();
            setupChatHistoryHandle();
            // Add initial welcome message

            System.out.println("Frontend initialized successfully");
        } catch (Exception e) {
            System.err.println("Failed to initialize: " + e.getMessage());
            e.printStackTrace();

            // Show error on UI
            Platform.runLater(() -> {
                showError("Error initializing SuperPie: " + e.getMessage());
            });
        }
    }

    @FXML private Button OldChat; // This should match the FXML ID

    private void setupChatHistoryHandle() {
        if (OldChat == null) {
            System.err.println("Old Chats button is null. Check FXML connection.");
            return;
        }

        OldChat.setOnAction(e -> {
            // Get chat history
            Set<String> memoryIds = Backend.SuperPiePersistentMemoryStore.displayList();

            if (memoryIds != null && !memoryIds.isEmpty()) {
                // Create dialog to display chat history
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Chat History");
                dialog.setHeaderText("Select a previous chat to load");

                // Create list view for the dialog
                ListView<String> listView = new ListView<>();
                List<String> sortedIds = new ArrayList<>(memoryIds);
                Collections.sort(sortedIds);
                listView.getItems().addAll(sortedIds);

                dialog.getDialogPane().setContent(listView);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                // Handle selection
                dialog.setResultConverter(buttonType -> {
                    if (buttonType == ButtonType.OK) {
                        return listView.getSelectionModel().getSelectedItem();
                    }
                    return null;
                });

                // Show dialog and process result
                dialog.showAndWait().ifPresent(selectedChat -> {
                    try {
                        // Clear current chat
                        ChatSection.getChildren().clear();

                        // Load selected chat
                        SP.oldInit(selectedChat);

                        addMessageToChat("Loaded chat: " + selectedChat, false);
                    } catch (IOException ex) {
                        showError("Failed to load chat: " + ex.getMessage());
                    }
                });
            } else {
                showError("No previous chats found");
            }
        });
    }

    private void setupScrollPane() {
        if (chatScrollPane != null && ChatSection != null) {
            chatScrollPane.setFitToWidth(true);
            ChatSection.setSpacing(10);
            ChatSection.setPadding(new Insets(10));
        } else {
            System.err.println("Chat section or scroll pane not found in FXML");
        }
    }

    private void setupModelMenu() {
        if (ModelSelector != null) {
            ModelSelector.getItems().clear();
            List<String> models = Arrays.asList(
                    "gemma3:1b", "gemma3:4b",
                    "deepseek-r1:1.5b", "deepseek-r1:7b"
            );

            for (String model : models) {
                MenuItem mi = new MenuItem(model);
                mi.setOnAction(e -> {
                    SP.ModelName = model;
                    SP.VogueModel();
                    ModelSelector.setText("MODEL: " + model);
                    addMessageToChat("Model changed to: " + model, false);
                });
                ModelSelector.getItems().add(mi);
            }
            ModelSelector.setText("MODEL: " + SP.ModelName);
        } else {
            System.err.println("ModelSelector not found in FXML");
        }
    }

    private void setupTemperatureSlider() {
        if (setTemp != null) {
            setTemp.setValue(SP.temperature);
            setTemp.valueProperty().addListener((_, oldVal, newVal) -> {
                SP.temperature = newVal.doubleValue();
                SP.VogueModel();
            });
        } else {
            System.err.println("Temperature slider not found in FXML");
        }
    }

    private void setupButtonHandlers() {
        // Send button
        if (SendButton != null) {
            SendButton.setOnAction(e -> sendMessage());
        } else {
            System.err.println("Send button not found in FXML");
        }

        // New chat button
        if (NewChat != null) {
            NewChat.setOnAction(e -> createNewChat());
        } else {
            System.err.println("New chat button not found in FXML");
        }

        // Image upload button
        if (useImage != null) {
            useImage.setOnAction(e -> uploadImage());
        } else {
            System.err.println("Image button not found in FXML");
        }

        // PDF upload button
//        if (usePdf != null) {
//            usePdf.setOnAction(e -> uploadPDF());
//        } else {
//            System.err.println("PDF button not found in FXML");
//        }

        // Exit button
        if (Exit != null) {
            Exit.setOnAction(e -> exitApplication());
        } else {
            System.err.println("Exit button not found in FXML");
        }

        // Dark mode toggle
        if (DarkMode != null) {
            DarkMode.setOnAction(e -> toggleDarkMode());
        }
    }

    private void toggleDarkMode() {
        boolean isDarkMode = DarkMode.isSelected();
        String style;
        if (isDarkMode) {
            style = "-fx-background-color: #333333; -fx-text-fill: white;";
        } else {
            style = "-fx-background-color: white; -fx-text-fill: black;";
        }
        // Apply style to parent
        DarkMode.getScene().getRoot().setStyle(style);
    }

    private void exitApplication() {
        if (SP != null) {
            SP.quit();
        }
        if (stage != null) {
            stage.close();
        } else {
            Platform.exit();
        }
    }

    // Update field declaration from TextArea to TextField


    private void sendMessage() {
        if (YourMessages == null || SP == null) {
            showError("Chat system not properly initialized");
            return;
        }

        String userMessage = YourMessages.getText().trim();
        if (userMessage.isEmpty()) return;

        // Add user message to chat
        addMessageToChat("You: " + userMessage, true);

        // Show loading indicator
        if (Response != null) {
            Platform.runLater(() -> Response.setProgress(-1));
        }

        // Clear input field - change from clear() to setText("")
        YourMessages.setText("");

        // Process in background thread
        CompletableFuture.supplyAsync(() -> {
            try {
                return SP.ModelTalking(userMessage, imageP, pdfP);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).thenAcceptAsync(aiResponse -> {
            addMessageToChat("SuperPie: " + aiResponse, false);
            if (Response != null) {
                Response.setProgress(0);
            }
        }, Platform::runLater).exceptionally(ex -> {
            Platform.runLater(() -> {
                showError("Error: " + ex.getMessage());
                addMessageToChat("SuperPie: Sorry, I encountered an error.", false);
                if (Response != null) {
                    Response.setProgress(0);
                }
            });
            return null;
        }).whenComplete((v, t) -> {
            imageP = false;
            if (useImage != null) {
                Platform.runLater(() -> useImage.setText("Image"));
            }
        });
    }

    private void createNewChat() {
        try {
            if (ChatSection != null) {
                ChatSection.getChildren().clear();
            } else {
                System.err.println("Chat section not found");
                return;
            }

            SP.newInit();
            SP.VogueModel();
            currentImageUrl = null;

            // Reset buttons
            if (useImage != null) {
                useImage.setText("Image");
            }
            if (usePdf != null) {
                usePdf.setText("PDF");
            }

            addMessageToChat("Started new chat session", false);
        } catch (IOException e) {
            showError("Failed to create new chat: " + e.getMessage());
        }
    }

    private void uploadImage() {
        if (stage == null) {
            showError("Cannot upload image: application window not initialized");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            this.imageP = true;
            currentImageUrl = selectedFile.toURI().toString();
            Path = selectedFile.getAbsolutePath();

            if (useImage != null) {
                useImage.setText("Image Selected");
            }

            addMessageToChat("Image uploaded: " + selectedFile.getName(), false);
            addMessageToChat("You can now send a message to analyze this image.", false);
            SP.returnImageUserMessage(Path, getFileExtension(selectedFile.getName()));
        }
    }
//
//  private void uploadPDF() {
//      if (stage == null) {
//          showError("Cannot upload PDF: application window not initialized");
//          return;
//      }
//      this.pdfP = true;
//      FileChooser fileChooser = new FileChooser();
//      fileChooser.setTitle("Select PDF");
//      fileChooser.getExtensionFilters().add(
//              new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
//      );
//      File selectedFile = fileChooser.showOpenDialog(stage);
//      if (selectedFile != null) {
//          Path = selectedFile.getAbsolutePath();
//
//          if (usePdf != null) {
//              usePdf.setText("PDF Selected");
//          }
//
//          // Process the PDF file and pass it to the backend
//          SP.returnPDFUserMessage(Path, "pdf");
//
//          addMessageToChat("PDF uploaded: " + selectedFile.getName(), false);
//          addMessageToChat("You can now send a message to analyze this PDF.", false);
//      }
//  }

    private void addMessageToChat(String message, boolean isUser) {
        if (ChatSection == null) {
            System.err.println("Chat section is null");
            return;
        }

        Platform.runLater(() -> {
            Label messageLabel = new Label(message);
            messageLabel.setWrapText(true);
            messageLabel.setMaxWidth(780);
            messageLabel.setPadding(new Insets(10));

            String style = isUser ?
                    "-fx-background-color: #e3e896; -fx-text-fill: black; -fx-padding: 10; -fx-background-radius: 5;" :
                    "-fx-background-color: #d0802a; -fx-text-fill: white; -fx-padding: 10; -fx-background-radius: 5;";
            messageLabel.setStyle(style);

            ChatSection.getChildren().add(messageLabel);

            // Ensure scroll to bottom after adding message
            chatScrollPane.applyCss();
            chatScrollPane.layout();
            chatScrollPane.setVvalue(1.0);
        });
    }

    private String getFileExtension(String filename) {
        int lastDotPos = filename.lastIndexOf('.');
        if (lastDotPos > 0) {
            return filename.substring(lastDotPos + 1).toLowerCase();
        }
        return "png"; // default
    }

    private void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
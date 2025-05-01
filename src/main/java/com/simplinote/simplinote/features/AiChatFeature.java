package com.simplinote.simplinote.features;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AiChatFeature {

    public void show(Stage parentStage) {
        try {
            // Load FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/betterSuperPieMainChat.fxml"));
            Parent root = loader.load();

            // Create dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("AI Chat Assistant");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(parentStage);

            // Set scene
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            // Show dialog
            dialogStage.showAndWait();

        } catch (Exception e) {
            showErrorAlert("AI Chat Error", "Could not load AI Chat: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
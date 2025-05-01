package com.simplinote.simplinote;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    private static final double WINDOW_WIDTH = 900;
    private static final double WINDOW_HEIGHT = 700;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML resource
            URL fxmlUrl = getClass().getResource("/fxml/MainView.fxml");
            if (fxmlUrl == null) {
                showErrorAndExit("Resource Error", "Could not find MainView.fxml file");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            // Load CSS
            URL cssUrl = getClass().getResource("/styles/main.css");
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("Warning: Could not find main.css file");
            }

            // Set application icon with error handling
            try (InputStream iconStream = getClass().getResourceAsStream("/images/icon.png")) {
                if (iconStream != null) {
                    primaryStage.getIcons().add(new Image(iconStream));
                } else {
                    System.err.println("Warning: Could not find icon.png");
                }
            } catch (IOException e) {
                System.err.println("Error loading icon: " + e.getMessage());
            }

            // Configure and show stage
            primaryStage.setTitle("SimpliNote");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showErrorAndExit("Application Error", "Failed to start: " + e.getMessage());
        }
    }

    private void showErrorAndExit(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
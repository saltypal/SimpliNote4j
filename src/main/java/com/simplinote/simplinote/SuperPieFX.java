package com.simplinote.simplinote;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class SuperPieFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

            try {
                // Create the FXMLLoader first
                FXMLLoader loader = new FXMLLoader(getClass().getResource("betterSuperPieMainChat.fxml"));

                // Load the FXML file
                Parent root = loader.load();

                // Create the scene
                Scene scene = new Scene(root);

                // Get the controller and pass the stage reference
                Frontend controller = loader.getController();
                if (controller != null) {
                    controller.setStage(stage);
                } else {
                    System.err.println("Controller not found! Check that your FXML file specifies the correct controller class.");
                }

                // Set up the stage
                try {
                    Image icon = new Image(getClass().getResourceAsStream("/pie.png"));
                    stage.getIcons().add(icon);
                } catch (Exception e) {
                    System.err.println("Could not load icon: " + e.getMessage());
                }

                stage.setScene(scene);
                stage.setTitle("SuperPie");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error loading FXML: " + e.getMessage());
            }
        }
}

package com.simplinote.simplinote.superpie;

          import javafx.application.Application;
          import javafx.fxml.FXMLLoader;
          import javafx.scene.Parent;
          import javafx.scene.Scene;
          import javafx.scene.image.Image;
          import javafx.stage.Stage;

          import java.io.IOException;
          import java.util.Objects;

          public class SuperPieFX extends Application {

              public static void main(String[] args) {
                  launch(args);
              }

              @Override
              public void start(Stage stage) {
                  try {
                      // Create the FXMLLoader with proper resource path
                      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/betterSuperPieMainChat.fxml"));

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

                      // Set up the stage with better error handling for icon
                      try {
                          Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pie.png")));
                          stage.getIcons().add(icon);
                      } catch (Exception e) {
                          System.err.println("Could not load icon: " + e.getMessage());
                          e.printStackTrace();
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
package com.simplinote.simplinote;

        import javafx.application.Application;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.scene.image.Image;
        import javafx.stage.Stage;
        import java.io.IOException;
        import java.net.URL;

public class Main extends Application {
            private static final double WINDOW_WIDTH = 900;
            private static final double WINDOW_HEIGHT = 700;

            @Override
            public void start(Stage primaryStage) {
                try {
                    // Simplify resource loading - use direct path without leading slash
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(Main.class.getResource("/com/simplinote/simplinote/MainView.fxml"));

                    // Debug output for troubleshooting
                    System.out.println("FXML Location: " + loader.getLocation());

                    // Load root node
                    Parent root = loader.load();

                    // Create scene
                    Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

                    // Load CSS
                    // Load CSS
            String cssPath = "styles/main.css";  // remove leading slash
            URL cssURL = Main.class.getClassLoader().getResource(cssPath);
            if (cssURL != null) {
                scene.getStylesheets().add(cssURL.toExternalForm());
        }
                    // Set up stage
                    primaryStage.setTitle("SimpliNote");
                    primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));
                    primaryStage.setScene(scene);
                    primaryStage.show();

                } catch (IOException e) {
                    System.err.println("FXML Loading Error: " + e.getMessage());
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    System.err.println("Resource Not Found: " + e.getMessage());
                    e.printStackTrace();
                } catch (Exception e) {
                    System.err.println("Application Error: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            public static void main(String[] args) {
                launch(args);
            }
        }
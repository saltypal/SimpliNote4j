package com.simplinote.simplinote;

    import javafx.application.Application;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.image.Image;
    import javafx.stage.Stage;

    public class Main extends Application {
        private static final double WINDOW_WIDTH = 900;
        private static final double WINDOW_HEIGHT = 700;

        @Override
        public void start(Stage primaryStage) throws Exception {
            // Load the FXML file from the correct path
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/simplinote/simplinote/MainView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

            // Load CSS if it exists
            String cssPath = "/styles/main.css";
            if (getClass().getResource(cssPath) != null) {
                scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
            }

            // Set application icon if it exists
            String iconPath = "/images/icon.png";
            if (getClass().getResourceAsStream(iconPath) != null) {
                primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(iconPath)));
            }

            primaryStage.setTitle("SimpliNote");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.show();
        }

        public static void main(String[] args) {
            launch(args);
        }
    }
package com.simplinote.simplinote.features;

import javafx.application.Platform;
import javafx.stage.Stage;

public class AiChatFeature {
    public void show(Stage parentStage) {
        try {
            // Launch SuperPieFX as a separate application
            Platform.runLater(() -> {
                String[] args = new String[0];
                try {
                    // Use reflection to avoid direct instantiation issues
                    Class<?> superPieClass = Class.forName("com.simplinote.simplinote.superpie.SuperPieFX");
                    java.lang.reflect.Method mainMethod = superPieClass.getMethod("main", String[].class);
                    mainMethod.invoke(null, (Object) args);
                } catch (Exception e) {
                    System.err.println("Error launching SuperPieFX: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            System.err.println("Error in AiChatFeature: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
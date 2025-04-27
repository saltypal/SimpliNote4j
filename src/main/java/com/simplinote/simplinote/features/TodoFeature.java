// src/main/java/com/note/simplinote/features/TodoFeature.java
package com.simplinote.simplinote.features;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class TodoFeature {
    public void show(Stage parentStage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Todo List");
        alert.setHeaderText("Todo List Feature");
        alert.setContentText("it is a part of calendar!");
        alert.initOwner(parentStage);
        alert.showAndWait();
    }
}
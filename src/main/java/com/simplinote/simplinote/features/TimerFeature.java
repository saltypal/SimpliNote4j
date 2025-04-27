// src/main/java/com/note/simplinote/features/TimerFeature.java
package com.simplinote.simplinote.features;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class TimerFeature {
    public void show(Stage parentStage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Pomodoro Timer");
        alert.setHeaderText("Timer Feature");
        alert.setContentText("it is a part of calendar!");
        alert.initOwner(parentStage);
        alert.showAndWait();
    }
}
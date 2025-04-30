package com.simplinote.simplinote.features;

import com.simplinote.simplinote.components.PomodoroTimerComponent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TimerFeature {
    public void show(Stage parentStage) {
        Stage timerStage = new Stage();
        timerStage.initModality(Modality.WINDOW_MODAL);
        timerStage.initOwner(parentStage);
        timerStage.setTitle("Pomodoro Timer");

        PomodoroTimerComponent timerComponent = new PomodoroTimerComponent();
        Scene scene = new Scene(timerComponent, 400, 600);
        scene.getStylesheets().add(getClass().getResource("/styles/timer.css").toExternalForm());

        timerStage.setScene(scene);
        timerStage.setResizable(false);
        timerStage.show();
    }
}
package com.simplinote.simplinote.components;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

public class PomodoroTimerComponent extends VBox {
    private Timeline timeline;
    private IntegerProperty timeSeconds;
    private Label timeLabel;
    private Button startButton;
    private Button resetButton;
    private Button lapButton;
    private ComboBox<Integer> minutesComboBox;
    private ListView<String> lapListView;
    private List<String> laps;
    private boolean isRunning;

    public PomodoroTimerComponent() {
        getStyleClass().add("timer-container");
        setSpacing(20);
        setPadding(new Insets(20));

        timeSeconds = new SimpleIntegerProperty(25 * 60); // Default 25 minutes
        laps = new ArrayList<>();
        isRunning = false;

        initializeComponents();
        setupLayout();
        setupTimeline();
    }

    private void initializeComponents() {
        // Timer display
        timeLabel = new Label();
        timeLabel.getStyleClass().add("time-label");
        updateTimeLabel();

        // Minutes selection
        minutesComboBox = new ComboBox<>();
        minutesComboBox.getItems().addAll(5, 10, 15, 20, 25, 30, 45, 60);
        minutesComboBox.setValue(25);
        minutesComboBox.getStyleClass().add("minutes-combo");
        minutesComboBox.setOnAction(e -> resetTimer());

        // Control buttons
        startButton = new Button("Start");
        startButton.getStyleClass().addAll("timer-button", "start-button");

        resetButton = new Button("Reset");
        resetButton.getStyleClass().addAll("timer-button", "reset-button");

        lapButton = new Button("Lap");
        lapButton.getStyleClass().addAll("timer-button", "lap-button");
        lapButton.setDisable(true);

        // Lap list
        lapListView = new ListView<>();
        lapListView.getStyleClass().add("lap-list");
        VBox.setVgrow(lapListView, Priority.ALWAYS);

        // Button actions
        startButton.setOnAction(e -> toggleTimer());
        resetButton.setOnAction(e -> resetTimer());
        lapButton.setOnAction(e -> recordLap());
    }

    private void setupLayout() {
        // Timer selection box
        HBox timerBox = new HBox(10);
        timerBox.setAlignment(Pos.CENTER);
        Label minutesLabel = new Label("Minutes:");
        minutesLabel.getStyleClass().add("minutes-label");
        timerBox.getChildren().addAll(minutesLabel, minutesComboBox);

        // Timer display
        VBox timerDisplay = new VBox(10);
        timerDisplay.setAlignment(Pos.CENTER);
        timerDisplay.getChildren().addAll(timeLabel);

        // Control buttons box
        HBox controlBox = new HBox(10);
        controlBox.setAlignment(Pos.CENTER);
        controlBox.getChildren().addAll(startButton, resetButton, lapButton);

        // Add all components
        getChildren().addAll(
            timerBox,
            timerDisplay,
            controlBox,
            new Label("Laps:"),
            lapListView
        );
    }

    private void setupTimeline() {
        timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> {
                timeSeconds.set(timeSeconds.get() - 1);
                updateTimeLabel();
                if (timeSeconds.get() <= 0) {
                    stopTimer();
                    showTimerComplete();
                }
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);

        timeSeconds.addListener((obs, oldVal, newVal) -> updateTimeLabel());
    }

    private void toggleTimer() {
        if (!isRunning) {
            startTimer();
        } else {
            stopTimer();
        }
    }

    private void startTimer() {
        timeline.play();
        isRunning = true;
        startButton.setText("Pause");
        lapButton.setDisable(false);
        minutesComboBox.setDisable(true);
    }

    private void stopTimer() {
        timeline.pause();
        isRunning = false;
        startButton.setText("Start");
        lapButton.setDisable(true);
    }

    private void resetTimer() {
        stopTimer();
        timeSeconds.set(minutesComboBox.getValue() * 60);
        laps.clear();
        lapListView.getItems().clear();
        minutesComboBox.setDisable(false);
    }

    private void recordLap() {
        String lapTime = timeLabel.getText();
        String lapText = String.format("Lap %d: %s", laps.size() + 1, lapTime);
        laps.add(lapTime);
        lapListView.getItems().add(0, lapText);
    }

    private void updateTimeLabel() {
        int minutes = timeSeconds.get() / 60;
        int seconds = timeSeconds.get() % 60;
        timeLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void showTimerComplete() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Timer Complete");
        alert.setHeaderText(null);
        alert.setContentText("Time's up! Take a break.");
        alert.show();
    }
}
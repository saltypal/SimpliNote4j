package com.simplinote.simplinote.features;

import com.simplinote.simplinote.components.CalendarComponent;
import com.simplinote.simplinote.model.Events;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.ArrayList;

public class CalendarFeature {
    public void show(Stage parentStage) {
        Stage calendarStage = new Stage();
        calendarStage.initModality(Modality.WINDOW_MODAL);
        calendarStage.initOwner(parentStage);
        calendarStage.setTitle("Calendar");

        // Create calendar component with empty events list (you can add events later)
        CalendarComponent calendar = new CalendarComponent(new ArrayList<>());

        Scene scene = new Scene(calendar, 400, 500);
        scene.getStylesheets().add(getClass().getResource("/styles/calendar.css").toExternalForm());

        calendarStage.setScene(scene);
        calendarStage.setResizable(false);
        calendarStage.show();
    }
}
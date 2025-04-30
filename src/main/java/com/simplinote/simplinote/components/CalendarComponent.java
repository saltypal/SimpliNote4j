package com.simplinote.simplinote.components;

import com.simplinote.simplinote.model.Events;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class CalendarComponent extends BorderPane {
    private LocalDate currentDate = LocalDate.now();
    private List<Events> events;
    private GridPane calendarGrid;
    private Label monthYearLabel;

    public CalendarComponent(List<Events> events) {
        this.events = events;
        initialize();
    }

    private void initialize() {
        getStyleClass().add("calendar-container");
        setTop(createNav());
        calendarGrid = createCalendar();
        setCenter(calendarGrid);
        updateCalendar();
    }

    private HBox createNav() {
        HBox navigationBox = new HBox(10);
        navigationBox.setAlignment(Pos.CENTER);
        navigationBox.getStyleClass().add("nav-container");
        navigationBox.setPadding(new Insets(10));

        Button prevButton = new Button("←");
        Button nextButton = new Button("→");
        monthYearLabel = new Label();

        prevButton.getStyleClass().add("calendar-nav-button");
        nextButton.getStyleClass().add("calendar-nav-button");
        monthYearLabel.getStyleClass().add("calendar-header");

        prevButton.setOnAction(e -> {
            currentDate = currentDate.minusMonths(1);
            updateCalendar();
        });

        nextButton.setOnAction(e -> {
            currentDate = currentDate.plusMonths(1);
            updateCalendar();
        });

        navigationBox.getChildren().addAll(prevButton, monthYearLabel, nextButton);
        return navigationBox;
    }

    private GridPane createCalendar() {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("calendar-grid");
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(10));

        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < 7; i++) {
            Label dayLabel = new Label(daysOfWeek[i]);
            dayLabel.getStyleClass().add("calendar-day-header");
            dayLabel.setMaxWidth(Double.MAX_VALUE);
            dayLabel.setAlignment(Pos.CENTER);
            grid.add(dayLabel, i, 0);
            GridPane.setFillWidth(dayLabel, true);
        }

        return grid;
    }

    private void updateCalendar() {
        YearMonth yearMonth = YearMonth.from(currentDate);
        monthYearLabel.setText(yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));

        // Clear existing date buttons
        calendarGrid.getChildren().removeIf(node ->
                GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);

        LocalDate firstOfMonth = currentDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7;
        int daysInMonth = yearMonth.lengthOfMonth();

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), day);
            Button dateButton = new Button(String.valueOf(day));
            dateButton.getStyleClass().add("calendar-date-button");
            dateButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            // Style today's date
            if (date.equals(LocalDate.now())) {
                dateButton.getStyleClass().add("today");
            }

            // Style dates with events
            boolean hasEvents = events.stream()
                    .anyMatch(e -> e.getDate().equals(date));
            if (hasEvents) {
                dateButton.getStyleClass().add("has-event");
                String eventDetails = events.stream()
                        .filter(e -> e.getDate().equals(date))
                        .map(e -> e.getTitle())
                        .collect(Collectors.joining("\n"));
                dateButton.setTooltip(new Tooltip(eventDetails));
            }

            // Set up button actions
            dateButton.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) {
                    showEventsListDialog(date);
                }
            });

            int row = (dayOfWeek + day - 1) / 7 + 1;
            int col = (dayOfWeek + day - 1) % 7;
            calendarGrid.add(dateButton, col, row);
            GridPane.setFillWidth(dateButton, true);
            GridPane.setFillHeight(dateButton, true);
        }
    }

    private void showEventsListDialog(LocalDate date) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Events for " + date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")));
        dialog.setHeaderText(null);

        ListView<Events> eventListView = new ListView<>();
        eventListView.setPrefHeight(200);

        List<Events> dateEvents = events.stream()
                .filter(e -> e.getDate().equals(date))
                .collect(Collectors.toList());
        eventListView.setItems(FXCollections.observableArrayList(dateEvents));

        eventListView.setCellFactory(lv -> new ListCell<Events>() {
            @Override
            protected void updateItem(Events event, boolean empty) {
                super.updateItem(event, empty);
                if (empty || event == null) {
                    setText(null);
                } else {
                    setText(event.getTitle() + " - " + event.getDescription());
                }
            }
        });

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        content.getChildren().addAll(
                new Label("Events:"),
                eventListView
        );

        ButtonType newEventButton = new ButtonType("New Event", ButtonBar.ButtonData.LEFT);
        ButtonType editButton = new ButtonType("Edit", ButtonBar.ButtonData.LEFT);
        ButtonType deleteButton = new ButtonType("Delete", ButtonBar.ButtonData.LEFT);
        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(newEventButton, editButton, deleteButton, closeButton);
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == newEventButton) {
                showEventDialog(date, null);
            } else if (dialogButton == editButton) {
                Events selectedEvent = eventListView.getSelectionModel().getSelectedItem();
                if (selectedEvent != null) {
                    showEventDialog(date, selectedEvent);
                }
            } else if (dialogButton == deleteButton) {
                Events selectedEvent = eventListView.getSelectionModel().getSelectedItem();
                if (selectedEvent != null) {
                    events.remove(selectedEvent);
                    updateCalendar();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showEventDialog(LocalDate date, Events existingEvent) {
        Dialog<Events> dialog = new Dialog<>();
        dialog.setTitle(existingEvent == null ? "Add New Event" : "Edit Event");
        dialog.setHeaderText(null);

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField();
        titleField.setPromptText("Event Title");
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Event Description");
        descriptionArea.setPrefRowCount(3);

        if (existingEvent != null) {
            titleField.setText(existingEvent.getTitle());
            descriptionArea.setText(existingEvent.getDescription());
        }

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionArea, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                if (existingEvent == null) {
                    Events newEvent = new Events(date, titleField.getText(), descriptionArea.getText());
                    events.add(newEvent);
                } else {
                    existingEvent.setTitle(titleField.getText());
                    existingEvent.setDescription(descriptionArea.getText());
                }
                updateCalendar();
                return existingEvent != null ? existingEvent : null;
            }
            return null;
        });

        dialog.showAndWait();
    }

    public List<Events> getEvents() {
        return events;
    }
}
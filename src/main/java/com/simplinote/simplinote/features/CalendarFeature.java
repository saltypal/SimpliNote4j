// src/main/java/com/note/simplinote/features/CalendarFeature.java
package com.simplinote.simplinote.features;

import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.net.URL;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Animation;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CalendarFeature {




// Add the JavaFX media module dependency to your build file:
// For Maven: <dependency>
//              <groupId>org.openjfx</groupId>
//              <artifactId>javafx-media</artifactId>
//              <version>17</version> <!-- Use appropriate version that matches your JavaFX version -->
//            </dependency>
// For Gradle: implementation 'org.openjfx:javafx-media:17' <!-- Use appropriate version -->


    public class CalendarFeature extends Application {
        // --- Nested domain and service classes ---
        public static class DAccount implements Serializable {
            private String objName, username, pwd;
            private List<Events> events = new ArrayList<>();
            private List<TTask> tTasks = new ArrayList<>();
            private Color calendarBgColor = Color.WHITE;
            private Color eventBgColor = Color.LIGHTGREEN;
            private Color todayBgColor = Color.LIGHTSALMON;
            private int pomodoroWorkMinutes = 25;
            private int pomodoroBreakMinutes = 5;

            public DAccount(String objName, String username, String pwd) {
                this.objName = objName;
                this.username = username;
                this.pwd = pwd;
            }

            public String getUsername() {
                return username;
            }

            public String getPWD() {
                return pwd;
            }

            public List<Events> getList() {
                return events;
            }

            public void addEvent(Events e) {
                events.add(e);
            }

            public void removeEvent(Events e) {
                events.remove(e);
            }

            public List<TTask> getTTasks() {
                return tTasks;
            }

            public void addTTask(TTask t) {
                tTasks.add(t);
            }

            public void removeTTask(TTask t) {
                tTasks.remove(t);
            }

            // Getters and setters for background colors
            public Color getCalendarBgColor() {
                return calendarBgColor;
            }

            public void setCalendarBgColor(Color c) {
                calendarBgColor = c;
            }

            public Color getEventBgColor() {
                return eventBgColor;
            }

            public void setEventBgColor(Color c) {
                eventBgColor = c;
            }

            public Color getTodayBgColor() {
                return todayBgColor;
            }

            public void setTodayBgColor(Color c) {
                todayBgColor = c;
            }

            // Pomodoro settings
            public int getPomodoroWorkMinutes() {
                return pomodoroWorkMinutes;
            }

            public void setPomodoroWorkMinutes(int mins) {
                this.pomodoroWorkMinutes = mins;
            }

            public int getPomodoroBreakMinutes() {
                return pomodoroBreakMinutes;
            }

            public void setPomodoroBreakMinutes(int mins) {
                this.pomodoroBreakMinutes = mins;
            }
        }

        public static class Events implements Serializable {
            private String title, about;
            private LocalDate startDate, endDate;
            private char type;

            public Events(String title, LocalDate s, LocalDate e, String about, char type) {
                this.title = title;
                this.startDate = s;
                this.endDate = e;
                this.about = about;
                this.type = type;
            }

            public LocalDate getDate() {
                return startDate;
            }

            public String getTitle() {
                return title;
            }
        }

        public static class TTask implements Serializable {
            public enum Priority {LOW, MEDIUM, HIGH}

            private UUID id = UUID.randomUUID();
            private String title, description;
            private LocalDate dueDate;
            private Priority priority;
            private boolean completed;
            private int pomodorosCompleted;

            public TTask(String title, String desc, LocalDate due, Priority pr) {
                this.title = title;
                this.description = desc;
                this.dueDate = due;
                this.priority = pr;
                this.pomodorosCompleted = 0;
            }

            public String getTitle() {
                return title;
            }

            public String getDescription() {
                return description;
            }

            public LocalDate getDueDate() {
                return dueDate;
            }

            public Priority getPriority() {
                return priority;
            }

            public boolean isCompleted() {
                return completed;
            }

            public void setCompleted(boolean c) {
                completed = c;
            }

            public void setTitle(String t) {
                title = t;
            }

            public void setDescription(String d) {
                description = d;
            }

            public void setDueDate(LocalDate d) {
                dueDate = d;
            }

            public void setPriority(Priority p) {
                priority = p;
            }

            public int getPomodorosCompleted() {
                return pomodorosCompleted;
            }

            public void incrementPomodoros() {
                pomodorosCompleted++;
            }
        }

        public static class YourAccount {
            private DAccount[] accounts = new DAccount[10];
            private int count = 0;

            public DAccount loginOrCreate(Scanner sc) {
                // Simple CLI login
                System.out.println("Enter username:");
                String u = sc.nextLine();
                for (int i = 0; i < count; i++)
                    if (accounts[i].getUsername().equals(u)) {
                        System.out.println("Enter pwd:");
                        if (sc.nextLine().equals(accounts[i].getPWD())) return accounts[i];
                    }
                // create new
                System.out.println("New account. Enter pwd:");
                String p = sc.nextLine();
                DAccount a = new DAccount("obj", u, p);
                accounts[count++] = a;
                return a;
            }
        }

        public static class YourEvents {
            private List<Events> list;
            private DAccount acc;

            public YourEvents(DAccount acc) {
                this.acc = acc;
                this.list = acc.getList();
            }

            public void show() {
                list.forEach(e -> System.out.println(e.getTitle()));
            }

            public void addEvent(Events e) {
                list.add(e);
                acc.addEvent(e);
            }

            public void remove(int idx) {
                Events e = list.remove(idx);
                acc.removeEvent(e);
            }
        }

        public static class Task implements Serializable {
            private String title;
            private LocalDate due;

            public Task(String t, LocalDate d) {
                title = t;
                due = d;
            }

            public String getTitle() {
                return title;
            }

            public LocalDate getDueDate() {
                return due;
            }
        }

        public static class YourTasks extends YourEvents {
            private List<Task> tasks = new ArrayList<>();

            public YourTasks(DAccount acc) {
                super(acc);
            }

            public void addTask(Task t) {
                tasks.add(t);
            }

            public void showTasks() {
                tasks.forEach(t -> System.out.println(t.getTitle()));
            }
        }

        // --- Application fields ---
        private DAccount currentAccount;
        private List<Events> events = new ArrayList<>();
        private ObservableList<TTask> tTasks = FXCollections.observableArrayList();
        private GridPane calendarGrid;
        private Label monthYearLabel;
        private LocalDate currentDate;
        private Color eventColor;
        private Color todayColor;
        private Color calendarBgColor;
        private BorderPane calPane;
        private Scene scene;

        private TableView<TTask> taskTable;

        // Pomodoro timer fields
        private Timeline pomodoroTimer;
        private AtomicInteger remainingSeconds;
        private boolean isWorkPhase = true;
        private Label timerLabel;
        private ProgressBar timerProgress;
        private TTask currentPomodoroTask;
        private Button startPauseButton;
        private Button resetButton;
        private Button skipButton;
        private int workMinutes;
        private int breakMinutes;
        private boolean isPaused = false;

        public static void main(String[] args) {
            launch(args);
        }

        @Override
        public void start(Stage primaryStage) {
            // Login via CLI
            Scanner sc = new Scanner(System.in);
            YourAccount ya = new YourAccount();
            currentAccount = ya.loginOrCreate(sc);
            events = currentAccount.getList();
            tTasks.addAll(currentAccount.getTTasks());

            // Initialize colors from user account
            eventColor = currentAccount.getEventBgColor();
            todayColor = currentAccount.getTodayBgColor();
            calendarBgColor = currentAccount.getCalendarBgColor();

            // Initialize Pomodoro settings
            workMinutes = currentAccount.getPomodoroWorkMinutes();
            breakMinutes = currentAccount.getPomodoroBreakMinutes();

            // Build UI for the calendar
            BorderPane mainLayout = new BorderPane();
            mainLayout.setPadding(new Insets(10));

            // Apply background color directly to the BorderPane
            calPane = new BorderPane();
            calPane.setPadding(new Insets(10));
            updateCalendarBackground();

            calPane.setTop(createNav());
            calendarGrid = createCalendar();
            calPane.setCenter(calendarGrid);

            // Add color customization controls
            calPane.setBottom(createColorCustomization());

            mainLayout.setCenter(calPane);

            // Add a button to open the To-Do list in a separate window
            Button openTodoButton = new Button("Open To-Do List");
            openTodoButton.setOnAction(e -> openTodoWindow());

            // Create a bottom toolbar
            HBox toolbar = new HBox(15);
            toolbar.setPadding(new Insets(10));
            toolbar.setAlignment(Pos.CENTER);
            toolbar.getChildren().add(openTodoButton);

            mainLayout.setBottom(toolbar);

            scene = new Scene(mainLayout, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("YourPlan Calendar");
            primaryStage.show();
            primaryStage.setOnCloseRequest(e -> {
                if (pomodoroTimer != null) {
                    pomodoroTimer.stop();
                }
                Platform.exit();
            });
            currentDate = LocalDate.now();
            updateCalendar();
        }

        /**
         * Opens the To-Do list in a separate window
         */
        private void openTodoWindow() {
            // Create a new stage for the To-Do list
            Stage todoStage = new Stage();
            todoStage.setTitle("YourPlan - To-Do List");

            // Create the To-Do list content
            BorderPane todoPane = new BorderPane();
            todoPane.setPadding(new Insets(10));

            // Create the internal content - using the same method we used for the tab
            VBox todoContent = createTodoContent();

            todoPane.setCenter(todoContent);

            // Create the scene and show the window
            Scene todoScene = new Scene(todoPane, 700, 650);
            todoStage.setScene(todoScene);
            todoStage.show();

            // Handle window close - stop any running Pomodoro timer
            todoStage.setOnCloseRequest(e -> {
                if (pomodoroTimer != null) {
                    pomodoroTimer.stop();
                }
            });
        }

        /**
         * Creates the content for the To-Do list window
         */
        private VBox createTodoContent() {
            // Create vertical layout for tasks and Pomodoro
            VBox mainLayout = new VBox(15);
            mainLayout.setPadding(new Insets(10));

            // Create table for TTasks
            taskTable = new TableView<>();
            taskTable.setEditable(true);

            // Define columns
            TableColumn<TTask, String> titleCol = new TableColumn<>("Title");
            titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            titleCol.setPrefWidth(150);

            TableColumn<TTask, String> descriptionCol = new TableColumn<>("Description");
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            descriptionCol.setPrefWidth(200);

            TableColumn<TTask, LocalDate> dueDateCol = new TableColumn<>("Due Date");
            dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
            dueDateCol.setPrefWidth(100);

            TableColumn<TTask, TTask.Priority> priorityCol = new TableColumn<>("Priority");
            priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
            priorityCol.setPrefWidth(80);

            TableColumn<TTask, Boolean> completedCol = new TableColumn<>("Completed");
            completedCol.setCellValueFactory(new PropertyValueFactory<>("completed"));
            completedCol.setPrefWidth(80);

            TableColumn<TTask, Integer> pomodorosCol = new TableColumn<>("Pomodoros");
            pomodorosCol.setCellValueFactory(new PropertyValueFactory<>("pomodorosCompleted"));
            pomodorosCol.setPrefWidth(80);

            taskTable.getColumns().addAll(titleCol, descriptionCol, dueDateCol, priorityCol, completedCol, pomodorosCol);
            taskTable.setItems(tTasks);

            // Create buttons for TTask management
            HBox taskButtonsBox = new HBox(10);
            taskButtonsBox.setPadding(new Insets(10));
            taskButtonsBox.setAlignment(Pos.CENTER);

            Button addTaskBtn = new Button("Add Task");
            Button editTaskBtn = new Button("Edit Task");
            Button completeTaskBtn = new Button("Mark Complete");
            Button deleteTaskBtn = new Button("Delete Task");

            addTaskBtn.setOnAction(e -> showEditTTaskDialog(null));

            editTaskBtn.setOnAction(e -> {
                TTask selectedTTask = taskTable.getSelectionModel().getSelectedItem();
                if (selectedTTask != null) {
                    showEditTTaskDialog(selectedTTask);
                } else {
                    showAlert("No Task Selected", "Please select a task to edit.");
                }
            });

            completeTaskBtn.setOnAction(e -> {
                TTask selectedTTask = taskTable.getSelectionModel().getSelectedItem();
                if (selectedTTask != null) {
                    selectedTTask.setCompleted(!selectedTTask.isCompleted());
                    taskTable.refresh();
                } else {
                    showAlert("No Task Selected", "Please select a task to mark as complete.");
                }
            });

            deleteTaskBtn.setOnAction(e -> {
                TTask selectedTTask = taskTable.getSelectionModel().getSelectedItem();
                if (selectedTTask != null) {
                    tTasks.remove(selectedTTask);
                    if (currentAccount != null) {
                        // Get the task list from account and remove the task directly
                        List<TTask> accountTasks = currentAccount.getTTasks();
                        if (accountTasks != null) {
                            accountTasks.remove(selectedTTask);
                        }
                    }
                } else {
                    showAlert("No Task Selected", "Please select a task to delete.");
                }
            });

            taskButtonsBox.getChildren().addAll(addTaskBtn, editTaskBtn, completeTaskBtn, deleteTaskBtn);

            // Add Pomodoro section
            VBox pomodoroBox = createPomodoroSection();

            // Separator between task list and Pomodoro
            Separator separator = new Separator();
            separator.setPadding(new Insets(5));

            // Add everything to the main layout
            mainLayout.getChildren().addAll(taskTable, taskButtonsBox, separator, pomodoroBox);

            return mainLayout;
        }

        // Method to update the calendar background
        private void updateCalendarBackground() {
            // Apply background color to the main BorderPane
            calPane.setBackground(new Background(
                    new BackgroundFill(calendarBgColor, CornerRadii.EMPTY, Insets.EMPTY)));

            // If calendarGrid is already initialized, update its background too
            if (calendarGrid != null) {
                calendarGrid.setBackground(new Background(
                        new BackgroundFill(calendarBgColor, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }

        String formatTime(int totalSeconds) {
            int minutes = totalSeconds / 60;
            int seconds = totalSeconds % 60;

            // Format as MM:SS with leading zeros
            return String.format("%02d:%02d", minutes, seconds);
        }

        // Create color customization controls
        private HBox createColorCustomization() {
            HBox customizationBox = new HBox(15);
            customizationBox.setPadding(new Insets(10));
            customizationBox.setAlignment(Pos.CENTER);

            // Calendar background color
            Label calBgLabel = new Label("Calendar Background:");
            ColorPicker calBgPicker = new ColorPicker(calendarBgColor);
            calBgPicker.setOnAction(e -> {
                calendarBgColor = calBgPicker.getValue();
                currentAccount.setCalendarBgColor(calendarBgColor);
                updateCalendarBackground();
            });

            // Today's date color
            Label todayColorLabel = new Label("Today's Color:");
            ColorPicker todayColorPicker = new ColorPicker(todayColor);
            todayColorPicker.setOnAction(e -> {
                todayColor = todayColorPicker.getValue();
                currentAccount.setTodayBgColor(todayColor);
                updateCalendar();
            });

            // Event color customization
            Label eventColorLabel = new Label("Event Color:");
            ColorPicker eventColorPicker = new ColorPicker(eventColor);
            eventColorPicker.setOnAction(e -> {
                eventColor = eventColorPicker.getValue();
                currentAccount.setEventBgColor(eventColor);
                updateCalendar();
            });

            // Apply button for immediate application
            Button applyButton = new Button("Apply Colors");
            applyButton.setOnAction(e -> {
                updateCalendarBackground();
                updateCalendar();
            });

            customizationBox.getChildren().addAll(
                    calBgLabel, calBgPicker, todayColorLabel, todayColorPicker,
                    eventColorLabel, eventColorPicker, applyButton);
            return customizationBox;
        }

        // Create navigation controls (month/year navigation)
        private HBox createNav() {
            HBox navigationBox = new HBox(10);
            navigationBox.setPadding(new Insets(10));
            navigationBox.setAlignment(Pos.CENTER);

            Button prevButton = new Button("Previous");
            monthYearLabel = new Label();
            Button nextButton = new Button("Next");

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

        // Create calendar grid
        private GridPane createCalendar() {
            GridPane grid = new GridPane();
            grid.setPadding(new Insets(5));
            grid.setHgap(5);
            grid.setVgap(5);

            // Set the grid's background to match the calendar background
            grid.setBackground(new Background(
                    new BackgroundFill(calendarBgColor, CornerRadii.EMPTY, Insets.EMPTY)));

            // Create column constraints
            for (int i = 0; i < 7; i++) {
                ColumnConstraints column = new ColumnConstraints();
                column.setPercentWidth(14.28); // 100% / 7 days
                grid.getColumnConstraints().add(column);
            }

            // Add day of week labels
            String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
            for (int i = 0; i < 7; i++) {
                Label dayLabel = new Label(daysOfWeek[i]);
                dayLabel.setAlignment(Pos.CENTER);
                dayLabel.setPrefWidth(Double.MAX_VALUE);
                dayLabel.setPadding(new Insets(5));
                grid.add(dayLabel, i, 0);
            }

            return grid;
        }

        // Update calendar view based on current date
        private void updateCalendar() {
            // Update month/year label
            YearMonth yearMonth = YearMonth.from(currentDate);
            monthYearLabel.setText(yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));

            // Clear calendar grid (keep day labels at row 0)
            calendarGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);

            // Get date information
            LocalDate firstOfMonth = currentDate.withDayOfMonth(1);
            int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7; // Convert to 0-based (Sunday = 0)
            int daysInMonth = yearMonth.lengthOfMonth();

            // Ensure the calendar grid has the correct background color
            calendarGrid.setBackground(new Background(
                    new BackgroundFill(calendarBgColor, CornerRadii.EMPTY, Insets.EMPTY)));

            // Add date buttons to grid
            for (int day = 1; day <= daysInMonth; day++) {
                LocalDate date = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), day);
                Button dateButton = new Button(String.valueOf(day));
                dateButton.setPrefSize(40, 40);
                dateButton.setMaxWidth(Double.MAX_VALUE);
                dateButton.setMaxHeight(Double.MAX_VALUE);

                // Remove border/box from buttons and make them transparent by default
                // to let the grid background show through
                dateButton.setStyle(
                        "-fx-border-color: transparent; " +
                                "-fx-background-radius: 0; " +
                                "-fx-background-color: transparent;"
                );

                // Highlight today's date
                if (date.equals(LocalDate.now())) {
                    dateButton.setBackground(new Background(new BackgroundFill(todayColor, CornerRadii.EMPTY, Insets.EMPTY)));
                }

                // Highlight if there are events on this date
                boolean hasEvent = false;
                for (Events event : events) {
                    if (event.getDate().equals(date)) {
                        hasEvent = true;
                        break;
                    }
                }

                if (hasEvent) {
                    dateButton.setBackground(new Background(new BackgroundFill(eventColor, CornerRadii.EMPTY, Insets.EMPTY)));
                }

                // Set action to view or add events for this date
                final LocalDate clickedDate = date;
                dateButton.setOnAction(e -> handleDateClick(clickedDate));

                // Calculate grid position
                int row = (dayOfWeek + day - 1) / 7 + 1; // +1 because row 0 is for day labels
                int col = (dayOfWeek + day - 1) % 7;

                calendarGrid.add(dateButton, col, row);
            }
        }

        // Handle date button clicks
        private void handleDateClick(LocalDate date) {
            // Check if there are events on this date
            List<Events> eventsOnDate = new ArrayList<>();
            for (Events event : events) {
                if (event.getDate().equals(date)) {
                    eventsOnDate.add(event);
                }
            }

            if (!eventsOnDate.isEmpty()) {
                showEventsForDate(date, eventsOnDate);
            } else {
                // Offer to add a new event
                boolean addEvent = showConfirmDialog("No Events",
                        "There are no events on " + date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")) +
                                ". Would you like to add one?");
                if (addEvent) {
                    showAddEventDialog(date);
                }
            }
        }

        // Show dialog to add an event
        private void showAddEventDialog() {
            showAddEventDialog(currentDate);
        }

        // Show dialog to add an event on a specific date
        private void showAddEventDialog(LocalDate date) {
            // Implementation for adding an event
            showAlert("Add Event", "Add event functionality would be implemented here");
        }

        // Show dialog to view all events
        private void showViewEventsDialog() {
            // Implementation for viewing all events
            showAlert("View Events", "View events functionality would be implemented here");
        }

        private void showAlert(String title, String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }


        // Show events for a specific date
        private void showEventsForDate(LocalDate date, List<Events> eventsOnDate) {
            // Implementation for showing events on a specific date
            showAlert("Events on " + date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")),
                    "There are " + eventsOnDate.size() + " events on this date.");
        }

        // Show a confirmation dialog
        private boolean showConfirmDialog(String title, String message) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);

            Optional<ButtonType> result = alert.showAndWait();
            return result.isPresent() && result.get() == ButtonType.OK;
        }

        // Method to show edit TTask dialog
        private void showEditTTaskDialog(TTask tTask) {
            boolean isNewTask = (tTask == null);

            Dialog<TTask> dialog = new Dialog<>();
            dialog.setTitle(isNewTask ? "Add New Task" : "Edit Task");

            // Create form for TTask details
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20));

            // Initialize with empty/default values for new task, or existing values for edit
            TextField titleField = new TextField(isNewTask ? "" : tTask.getTitle());
            TextArea descriptionArea = new TextArea(isNewTask ? "" : tTask.getDescription());
            descriptionArea.setPrefRowCount(3);
            DatePicker dueDatePicker = new DatePicker(isNewTask ? LocalDate.now() : tTask.getDueDate());
            ComboBox<CalendarFeature.TTask.Priority> priorityCombo = new ComboBox<>();
            priorityCombo.getItems().addAll(CalendarFeature.TTask.Priority.values());
            priorityCombo.setValue(isNewTask ? CalendarFeature.TTask.Priority.MEDIUM : tTask.getPriority());
            CheckBox completedCheck = new CheckBox("Completed");
            completedCheck.setSelected(isNewTask ? false : tTask.isCompleted());

            grid.add(new Label("Title:"), 0, 0);
            grid.add(titleField, 1, 0);
            grid.add(new Label("Description:"), 0, 1);
            grid.add(descriptionArea, 1, 1);
            grid.add(new Label("Due Date:"), 0, 2);
            grid.add(dueDatePicker, 1, 2);
            grid.add(new Label("Priority:"), 0, 3);
            grid.add(priorityCombo, 1, 3);
            grid.add(completedCheck, 1, 4);

            dialog.getDialogPane().setContent(grid);

            ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButton, cancelButton);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButton) {
                    if (isNewTask) {
                        // Create a new task
                        TTask newTask = new TTask(
                                titleField.getText(),
                                descriptionArea.getText(),
                                dueDatePicker.getValue(),
                                priorityCombo.getValue()
                        );
                        newTask.setCompleted(completedCheck.isSelected());
                        return newTask;
                    } else {
                        // Update existing TTask
                        tTask.setTitle(titleField.getText());
                        tTask.setDescription(descriptionArea.getText());
                        tTask.setDueDate(dueDatePicker.getValue());
                        tTask.setPriority(priorityCombo.getValue());
                        tTask.setCompleted(completedCheck.isSelected());
                        return tTask;
                    }
                }
                return null;
            });

            dialog.showAndWait().ifPresent(resultTask -> {
                if (isNewTask) {
                    // Add new task to both the observable list and the account
                    tTasks.add(resultTask);
                    if (currentAccount != null) {
                        currentAccount.addTTask(resultTask);
                    }
                }
                taskTable.refresh();
            });
        }

        /**
         * Plays an alert sound when a Pomodoro phase completes
         */
        private void playAlertSound() {
            try {
                // Create a Media object with the sound file path
                String soundFile = "alert.wav"; // Sound file in project resources
                Media sound = new Media(getClass().getResource("/sounds/" + soundFile).toURI().toString());

                // Create a MediaPlayer and play the sound
                MediaPlayer mediaPlayer = new MediaPlayer(sound);
                mediaPlayer.setOnEndOfMedia(() -> {
                    mediaPlayer.dispose(); // Clean up resources when done
                });

                // Set volume and play
                mediaPlayer.setVolume(0.5); // 50% volume
                mediaPlayer.play();
            } catch (Exception e) {
                System.err.println("Could not play alert sound: " + e.getMessage());
                // Continue without sound if there's an error
            }
        }


        // This method has been replaced by createTodoContent() and openTodoWindow()

        // Add this to your class fields at the top of your YourCalendarFX class
        private Label statusLabel;

        // Then in your createPomodoroSection() method or where you set up the Pomodoro UI, add:


        // Start Pomodoro timer
        private void startPomodoroTimer() {
            // Stop any existing timer
            if (pomodoroTimer != null) {
                pomodoroTimer.stop();
            }

            // Check if we have a task selected for pomodoro
            if (currentPomodoroTask == null) {
                statusLabel.setText("No task selected for Pomodoro");
                return;
            }

            // Determine duration based on current phase
            int durationInMinutes = isWorkPhase ? workMinutes : breakMinutes;
            remainingSeconds = new AtomicInteger(durationInMinutes * 60);

            // Update the UI
            statusLabel.setText(isWorkPhase ? "Work Phase" : "Break Phase");
            timerLabel.setText(formatTime(remainingSeconds.get()));
            timerProgress.setProgress(1.0);

            // Initialize start/pause button
            startPauseButton.setText("Pause");
            isPaused = false;

            // Create and start the timer
            pomodoroTimer = new Timeline(
                    new KeyFrame(Duration.seconds(1), e -> {
                        // Decrement the remaining time
                        int remaining = remainingSeconds.decrementAndGet();

                        // Update the UI
                        timerLabel.setText(formatTime(remaining));
                        timerProgress.setProgress((double) remaining / (durationInMinutes * 60));

                        // Check if timer has completed
                        if (remaining <= 0) {
                            pomodoroTimer.stop();
                            playAlertSound();

                            // Switch phases
                            isWorkPhase = !isWorkPhase;

                            // If completed a work phase, increment pomodoro count
                            if (!isWorkPhase && currentPomodoroTask != null) {
                                currentPomodoroTask.incrementPomodoros();
                                taskTable.refresh(); // Refresh to show updated pomodoro count
                            }

                            // Show notification
                            String message = isWorkPhase ?
                                    "Break time is over! Ready to work?" :
                                    "Work phase completed! Take a break.";

                            if (showConfirmDialog("Pomodoro Timer", message)) {
                                startPomodoroTimer(); // Start the next phase
                            } else {
                                // Reset the UI
                                isWorkPhase = true;
                                statusLabel.setText("Timer stopped");
                                startPauseButton.setText("Start");
                            }
                        }
                    })
            );

            pomodoroTimer.setCycleCount(Animation.INDEFINITE);
            pomodoroTimer.play();
        }

        // Create Pomodoro timer section
        private VBox createPomodoroSection() {
            VBox pomodoroBox = new VBox(10);
            pomodoroBox.setPadding(new Insets(15));
            pomodoroBox.setAlignment(Pos.CENTER);

            // Pomodoro title
            Label pomodoroTitle = new Label("Pomodoro Timer");
            pomodoroTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
            pomodoroBox.getChildren().add(pomodoroTitle);

            // Initialize remainingSeconds if not done already
            if (remainingSeconds == null) {
                remainingSeconds = new AtomicInteger(workMinutes * 60);
            }

            // Timer display
            timerLabel = new Label(formatTime(remainingSeconds.get()));
            timerLabel.setFont(Font.font("System", FontWeight.BOLD, 36));
            pomodoroBox.getChildren().add(timerLabel);

            // Progress bar
            timerProgress = new ProgressBar(1.0);
            timerProgress.setPrefWidth(300);
            pomodoroBox.getChildren().add(timerProgress);

            // Current task label
            Label currentTaskLabel = new Label("No task selected");
            currentTaskLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
            pomodoroBox.getChildren().add(currentTaskLabel);

            // Status label (Work/Break)
            statusLabel = new Label("Work Phase");
            statusLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
            pomodoroBox.getChildren().add(statusLabel);

            // Buttons for timer control
            HBox timerControlsBox = new HBox(15);
            timerControlsBox.setAlignment(Pos.CENTER);

            startPauseButton = new Button("Start Timer");
            resetButton = new Button("Reset");
            skipButton = new Button("Skip to Break");
            Button selectTaskButton = new Button("Select Task");

            timerControlsBox.getChildren().addAll(
                    startPauseButton, resetButton, skipButton, selectTaskButton
            );
            pomodoroBox.getChildren().add(timerControlsBox);

            // Add a separator
            pomodoroBox.getChildren().add(new Separator());

            // Pomodoro settings
            HBox settingsBox = new HBox(10);
            settingsBox.setAlignment(Pos.CENTER);
            settingsBox.setPadding(new Insets(10, 0, 0, 0));

            Label workLabel = new Label("Work (min):");
            TextField workMinutesField = new TextField(String.valueOf(workMinutes));
            workMinutesField.setPrefWidth(50);

            Label breakLabel = new Label("Break (min):");
            TextField breakMinutesField = new TextField(String.valueOf(breakMinutes));
            breakMinutesField.setPrefWidth(50);

            Button saveSettingsButton = new Button("Save");

            settingsBox.getChildren().addAll(workLabel, workMinutesField, breakLabel, breakMinutesField, saveSettingsButton);
            pomodoroBox.getChildren().add(settingsBox);

            // Set up button actions
            startPauseButton.setOnAction(e -> {
                if (currentPomodoroTask == null) {
                    showAlert("No Task Selected", "Please select a task for the Pomodoro timer.");
                    return;
                }

                if (pomodoroTimer == null || !pomodoroTimer.getStatus().equals(Timeline.Status.RUNNING)) {
                    // Start timer
                    if (isPaused) {
                        // Resume from pause
                        isPaused = false;
                        startPomodoroTimer();
                    } else {
                        // Start new
                        isWorkPhase = true;
                        statusLabel.setText("Work Phase");
                        remainingSeconds.set(workMinutes * 60);
                        timerLabel.setText(formatTime(remainingSeconds.get()));
                        timerProgress.setProgress(1.0);
                        startPomodoroTimer();
                    }
                    startPauseButton.setText("Pause");
                } else {
                    // Pause timer
                    pomodoroTimer.pause();
                    isPaused = true;
                    startPauseButton.setText("Resume");
                }
            });

            resetButton.setOnAction(e -> {
                if (pomodoroTimer != null) {
                    pomodoroTimer.stop();
                }

                isWorkPhase = true;
                statusLabel.setText("Work Phase");
                int minutesToUse = workMinutes;
                remainingSeconds.set(minutesToUse * 60);
                timerLabel.setText(formatTime(remainingSeconds.get()));
                timerProgress.setProgress(1.0);
                isPaused = false;
                startPauseButton.setText("Start Timer");
            });

            skipButton.setOnAction(e -> {
                if (isWorkPhase) {
                    // Skip to break
                    isWorkPhase = false;
                    statusLabel.setText("Break Phase");
                    remainingSeconds.set(breakMinutes * 60);
                    timerLabel.setText(formatTime(remainingSeconds.get()));
                    timerProgress.setProgress(1.0);

                    if (pomodoroTimer != null) {
                        pomodoroTimer.stop();
                    }

                    if (!isPaused) {
                        startPomodoroTimer();
                    }

                    skipButton.setText("Skip to Work");
                } else {
                    // Skip to work
                    isWorkPhase = true;
                    statusLabel.setText("Work Phase");
                    remainingSeconds.set(workMinutes * 60);
                    timerLabel.setText(formatTime(remainingSeconds.get()));
                    timerProgress.setProgress(1.0);

                    if (pomodoroTimer != null) {
                        pomodoroTimer.stop();
                    }

                    if (!isPaused) {
                        startPomodoroTimer();
                    }

                    skipButton.setText("Skip to Break");
                }
            });

            selectTaskButton.setOnAction(e -> {
                TTask selectedTask = taskTable.getSelectionModel().getSelectedItem();
                if (selectedTask != null) {
                    currentPomodoroTask = selectedTask;
                    currentTaskLabel.setText("Current task: " + selectedTask.getTitle());
                } else {
                    showAlert("No Task Selected", "Please select a task from the list.");
                }
            });

            saveSettingsButton.setOnAction(e -> {
                try {
                    int newWorkMinutes = Integer.parseInt(workMinutesField.getText());
                    int newBreakMinutes = Integer.parseInt(breakMinutesField.getText());

                    // Validate inputs
                    if (newWorkMinutes <= 0 || newBreakMinutes <= 0) {
                        statusLabel.setText("Error: Time values must be positive");
                        return;
                    }

                    // Apply the new settings
                    workMinutes = newWorkMinutes;
                    breakMinutes = newBreakMinutes;

                    // Save settings to preferences
                    savePreferences();

                    // Update UI
                    statusLabel.setText("Settings saved successfully");

                    // Update timer display if currently not running
                    if (pomodoroTimer == null || !pomodoroTimer.getStatus().equals(Timeline.Status.RUNNING)) {
                        remainingSeconds.set(isWorkPhase ? workMinutes * 60 : breakMinutes * 60);
                        timerLabel.setText(formatTime(remainingSeconds.get()));
                    }
                } catch (NumberFormatException ex) {
                    statusLabel.setText("Error: Please enter valid numbers");
                } catch (Exception ex) {
                    statusLabel.setText("Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });

            return pomodoroBox;
        }

        /**
         * Saves the Pomodoro timer settings to the current account
         */
        private void savePreferences() {
            try {
                // Save the Pomodoro settings to the current account
                if (currentAccount != null) {
                    // Update the account with current settings
                    currentAccount.setPomodoroWorkMinutes(workMinutes);
                    currentAccount.setPomodoroBreakMinutes(breakMinutes);

                    // Log the saved settings for debugging
                    System.out.println("Preferences saved: work=" + workMinutes + "min, break=" + breakMinutes + "min");
                } else {
                    System.err.println("Cannot save preferences: current account is null");
                }
            } catch (Exception e) {
                System.err.println("Error saving preferences: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
package com.simplinote.simplinote.components;

import com.simplinote.simplinote.model.TTask;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class TodoListComponent extends VBox {
    private ObservableList<TTask> tasks = FXCollections.observableArrayList();
    private TableView<TTask> taskTable;

    public TodoListComponent() {
        getStyleClass().add("todo-container");
        setSpacing(15);
        setPadding(new Insets(10));

        createToolbar();
        initializeTable();
    }

    private void createToolbar() {
        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER);
        toolbar.setPadding(new Insets(10));
        toolbar.getStyleClass().add("todo-toolbar");

        Button addButton = new Button("Add Task");
        Button editButton = new Button("Edit Task");
        Button deleteButton = new Button("Remove Task");
        Button markCompleteButton = new Button("Mark Complete");

        addButton.getStyleClass().addAll("todo-button", "add-button");
        editButton.getStyleClass().addAll("todo-button", "edit-button");
        deleteButton.getStyleClass().addAll("todo-button", "delete-button");
        markCompleteButton.getStyleClass().addAll("todo-button", "complete-button");

        addButton.setOnAction(e -> showTaskDialog(null));
        editButton.setOnAction(e -> {
            TTask selected = taskTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showTaskDialog(selected);
            } else {
                showAlert("No Task Selected", "Please select a task to edit.");
            }
        });
        deleteButton.setOnAction(e -> {
            TTask selected = taskTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Task");
                alert.setHeaderText("Delete Selected Task");
                alert.setContentText("Are you sure you want to delete this task?");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        tasks.remove(selected);
                    }
                });
            } else {
                showAlert("No Task Selected", "Please select a task to delete.");
            }
        });
        markCompleteButton.setOnAction(e -> {
            TTask selected = taskTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setCompleted(!selected.isCompleted());
                taskTable.refresh();
            } else {
                showAlert("No Task Selected", "Please select a task to mark as complete.");
            }
        });

        toolbar.getChildren().addAll(addButton, editButton, deleteButton, markCompleteButton);
        getChildren().add(toolbar);
    }

    private void initializeTable() {
        taskTable = new TableView<>();
        taskTable.setEditable(true);
        taskTable.getStyleClass().add("task-table");

        TableColumn<TTask, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(200);

        TableColumn<TTask, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.setPrefWidth(300);

        TableColumn<TTask, LocalDate> dueCol = new TableColumn<>("Due Date");
        dueCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        dueCol.setPrefWidth(100);

        TableColumn<TTask, TTask.Priority> priorityCol = new TableColumn<>("Priority");
        priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
        priorityCol.setPrefWidth(80);

        TableColumn<TTask, Boolean> completedCol = new TableColumn<>("Done");
        completedCol.setCellValueFactory(new PropertyValueFactory<>("completed"));
        completedCol.setPrefWidth(60);

        taskTable.getColumns().addAll(titleCol, descCol, dueCol, priorityCol, completedCol);
        taskTable.setItems(tasks);

        // Set row factory for styling completed tasks
        taskTable.setRowFactory(tv -> new TableRow<TTask>() {
            @Override
            protected void updateItem(TTask task, boolean empty) {
                super.updateItem(task, empty);
                if (task == null || empty) {
                    setStyle("");
                } else if (task.isCompleted()) {
                    setStyle("-fx-opacity: 0.7;");
                } else {
                    setStyle("");
                }
            }
        });

        getChildren().add(taskTable);
    }

    private void showTaskDialog(TTask task) {
        Dialog<TTask> dialog = new Dialog<>();
        dialog.setTitle(task == null ? "Add New Task" : "Edit Task");
        dialog.setHeaderText(null);

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField();
        titleField.setPromptText("Task Title");
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Task Description");
        DatePicker dueDatePicker = new DatePicker();
        ComboBox<TTask.Priority> priorityCombo = new ComboBox<>(
                FXCollections.observableArrayList(TTask.Priority.values())
        );
        priorityCombo.setPromptText("Select Priority");

        if (task != null) {
            titleField.setText(task.getTitle());
            descriptionArea.setText(task.getDescription());
            dueDatePicker.setValue(task.getDueDate());
            priorityCombo.setValue(task.getPriority());
        }

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionArea, 1, 1);
        grid.add(new Label("Due Date:"), 0, 2);
        grid.add(dueDatePicker, 1, 2);
        grid.add(new Label("Priority:"), 0, 3);
        grid.add(priorityCombo, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                if (titleField.getText().isEmpty() || priorityCombo.getValue() == null) {
                    showAlert("Invalid Input", "Title and Priority are required.");
                    return null;
                }

                if (task == null) {
                    TTask newTask = new TTask(
                            titleField.getText(),
                            descriptionArea.getText(),
                            dueDatePicker.getValue(),
                            priorityCombo.getValue()
                    );
                    tasks.add(newTask);
                    return newTask;
                } else {
                    task.setTitle(titleField.getText());
                    task.setDescription(descriptionArea.getText());
                    task.setDueDate(dueDatePicker.getValue());
                    task.setPriority(priorityCombo.getValue());
                    taskTable.refresh();
                    return task;
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public ObservableList<TTask> getTasks() {
        return tasks;
    }
}
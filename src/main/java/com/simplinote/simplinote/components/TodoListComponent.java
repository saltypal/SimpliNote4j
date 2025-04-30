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

                initializeTable();
                createButtons();
            }

            private void initializeTable() {
                taskTable = new TableView<>();
                taskTable.setEditable(true);
                taskTable.getStyleClass().add("task-table");

                // Define columns
                TableColumn<TTask, String> titleCol = new TableColumn<>("Title");
                titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
                titleCol.setPrefWidth(150);

                TableColumn<TTask, String> descCol = new TableColumn<>("Description");
                descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
                descCol.setPrefWidth(200);

                TableColumn<TTask, LocalDate> dueCol = new TableColumn<>("Due Date");
                dueCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
                dueCol.setPrefWidth(100);

                TableColumn<TTask, TTask.Priority> priorityCol = new TableColumn<>("Priority");
                priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
                priorityCol.setPrefWidth(80);

                TableColumn<TTask, Boolean> completedCol = new TableColumn<>("Completed");
                completedCol.setCellValueFactory(new PropertyValueFactory<>("completed"));
                completedCol.setPrefWidth(80);
                completedCol.setCellFactory(col -> new TableCell<TTask, Boolean>() {
                    private final CheckBox checkBox = new CheckBox();

                    {
                        checkBox.setOnAction(event -> {
                            TTask task = getTableRow().getItem();
                            if (task != null) {
                                task.setCompleted(checkBox.isSelected());
                                updateRowStyle(getTableRow(), task);
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            checkBox.setSelected(item != null && item);
                            setGraphic(checkBox);
                        }
                    }
                });

                taskTable.getColumns().addAll(titleCol, descCol, dueCol, priorityCol, completedCol);
                taskTable.setItems(tasks);

                // Set row factory for styling
                taskTable.setRowFactory(tv -> new TableRow<TTask>() {
                    @Override
                    protected void updateItem(TTask task, boolean empty) {
                        super.updateItem(task, empty);
                        if (task == null || empty) {
                            setStyle("");
                        } else {
                            updateRowStyle(this, task);
                        }
                    }
                });

                getChildren().add(taskTable);
            }

            private void updateRowStyle(TableRow<TTask> row, TTask task) {
                if (task.isCompleted()) {
                    row.setStyle("-fx-opacity: 0.7; -fx-background-color: derive(-background-light, 20%);");
                } else {
                    row.setStyle("");
                }
            }

            private void createButtons() {
                HBox buttonBox = new HBox(10);
                buttonBox.setAlignment(Pos.CENTER);
                buttonBox.getStyleClass().add("todo-button-box");

                Button addButton = new Button("Add Task");
                Button editButton = new Button("Edit Task");
                Button deleteButton = new Button("Delete Task");
                Button toggleButton = new Button("Toggle Complete");

                addButton.getStyleClass().add("todo-button");
                editButton.getStyleClass().add("todo-button");
                deleteButton.getStyleClass().add("todo-button-delete");
                toggleButton.getStyleClass().add("todo-button");

                addButton.setOnAction(e -> addTask());
                editButton.setOnAction(e -> editTask());
                deleteButton.setOnAction(e -> deleteTask());
                toggleButton.setOnAction(e -> toggleTaskCompletion());

                buttonBox.getChildren().addAll(addButton, editButton, deleteButton, toggleButton);
                getChildren().add(buttonBox);
            }

            private void addTask() {
                showTaskDialog(null);
            }

            private void editTask() {
                TTask selectedTask = taskTable.getSelectionModel().getSelectedItem();
                if (selectedTask != null) {
                    showTaskDialog(selectedTask);
                } else {
                    showAlert("No Task Selected", "Please select a task to edit.");
                }
            }

            private void deleteTask() {
                TTask selectedTask = taskTable.getSelectionModel().getSelectedItem();
                if (selectedTask != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Delete Task");
                    alert.setHeaderText("Delete Selected Task");
                    alert.setContentText("Are you sure you want to delete this task?");

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            tasks.remove(selectedTask);
                        }
                    });
                } else {
                    showAlert("No Task Selected", "Please select a task to delete.");
                }
            }

            private void toggleTaskCompletion() {
                TTask selectedTask = taskTable.getSelectionModel().getSelectedItem();
                if (selectedTask != null) {
                    selectedTask.setCompleted(!selectedTask.isCompleted());
                    taskTable.refresh();
                } else {
                    showAlert("No Task Selected", "Please select a task to toggle completion.");
                }
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
        }
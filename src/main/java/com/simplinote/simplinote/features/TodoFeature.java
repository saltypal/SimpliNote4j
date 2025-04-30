package com.simplinote.simplinote.features;

import com.simplinote.simplinote.components.TodoListComponent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TodoFeature {
    public void show(Stage parentStage) {
        Stage todoStage = new Stage();
        todoStage.initModality(Modality.WINDOW_MODAL);
        todoStage.initOwner(parentStage);
        todoStage.setTitle("Todo List");

        TodoListComponent todoList = new TodoListComponent();
        Scene scene = new Scene(todoList, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles/todo.css").toExternalForm());

        todoStage.setScene(scene);
        todoStage.show();
    }
}
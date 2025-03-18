package com.yourplan.yourplan;/*
Life Cycle of JavaFX Application
1. Main class is the entry point of the JavaFX application.
2. init() method is called before the start() method.
3. start(Stage stage)
4. stage.show() method is called to display the stage.


Its like Stage -> Scene -> SceneGraph -> Node
 */

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Experimental extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
    stage.isResizable();
    stage.setTitle("YourPlan");

    stage.show();
    }
}

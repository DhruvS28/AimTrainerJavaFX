package com.example.a4;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        MainUI uiRoot = new MainUI();
        uiRoot.stage = stage;
        Scene scene = new Scene(uiRoot);
        stage.setTitle("Assignment 4 ~ CMPT 381");
        stage.setScene(scene);
        stage.show();
        uiRoot.setKeyboardHandlers(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}
package com.example.a4;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainUI extends StackPane {

    Stage stage;
    TargetController controller;
    TrainerController testController;

    public MainUI() {

        TargetModel model = new TargetModel();
        controller = new TargetController();
        testController = new TrainerController();
        TargetView editView = new TargetView();
        TrainerView testView = new TrainerView();
        InteractionModel iModel = new InteractionModel();

        controller.setModel(model);
        editView.setModel(model);
        controller.setIModel(iModel);
        editView.setIModel(iModel);
        model.addSubscriber(editView);
        iModel.addSubscriber(editView);

        editView.setController(controller);

        testController.setModel(model);
        testView.setModel(model);
        testController.setIModel(iModel);
        testView.setIModel(iModel);
        model.addModeSubscriber(testView);
        iModel.addModeSubscriber(testView);

        testView.setController(testController);

        this.getChildren().addAll(editView, testView);
        testView.setVisible(false);
    }

    public void setKeyboardHandlers(Scene s) {

        s.setOnKeyPressed(controller::handleKeyPressed);
        s.setOnKeyReleased(controller::handleKeyReleased);
    }
}

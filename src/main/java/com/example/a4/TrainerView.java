package com.example.a4;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public class TrainerView extends StackPane implements AppModeListener {
    GraphicsContext gc;
    Canvas myCanvas;
    TargetModel model;
    InteractionModel iModel;

    public TrainerView() {
        myCanvas = new Canvas(800,800);
        gc = myCanvas.getGraphicsContext2D();

        setStyle("-fx-background-color: #f6ddb4;");


        this.getChildren().add(myCanvas);
    }

    // Draw the entire view as needed, after any updates
    private void draw() {
        gc.clearRect(0,0,myCanvas.getWidth(),myCanvas.getHeight());

        if (iModel.newTest)
        {
            iModel.trainerNum = 0;
        }
        while (model.getItems().size() > iModel.trainerNum && model.getItems().get(iModel.trainerNum).checkDeleted())
        {
            iModel.trainerNum++;
        }
        if (model.getItems().size() > iModel.trainerNum) {
            gc.setStroke(Color.STEELBLUE);
            gc.setFill(Color.STEELBLUE);
            drawItem(model.getItems().get(iModel.trainerNum));
        }
        else if (model.getItems().size() <= iModel.trainerNum)
        {
            MainUI uiroot = (MainUI) this.getParent();
            if(iModel.testSave == null)
                iModel.testSave = uiroot.stage.getScene();
            uiroot.stage.setScene(iModel.showReport());

            iModel.currentAppMode = InteractionModel.AppMode.REPORT;
        }
    }

    // Draw specific item as needed, after any updates
    private void drawItem(Groupable g) {
        double w = g.getRight() - g.getLeft();
        double h = g.getBottom() - g.getTop();
        if (g.hasChildren())
        {
            g.getChildren().forEach(item -> drawItem(item));
        }
        else
        {
            gc.fillOval(g.getLeft(), g.getTop(), w, h);
        }
    }

    public void setModel(TargetModel newModel) {
        model = newModel;
    }

    public void setIModel(InteractionModel newIModel) {
        iModel = newIModel;
    }


    public void setController(TrainerController controller) {
        myCanvas.setOnMousePressed(controller::handlePressed);
        myCanvas.setOnMouseDragged(controller::handleDragged);
        myCanvas.setOnMouseReleased(controller::handleReleased);
    }

    public void iModelChanged() {
        draw();
    }

    @Override
    public void AppModeChanged() {
        switch (iModel.currentAppMode)
        {
            case EDIT ->
            {
                this.setVisible(false);
                MainUI uiroot = (MainUI) this.getParent();
                uiroot.stage.setScene(iModel.editSave);
            }
            case TEST ->
            {
                this.setVisible(true);
                iModel.newTest = true;
                MainUI uiroot = (MainUI) this.getParent();
                if (iModel.editSave == null)
                    iModel.editSave = uiroot.stage.getScene();
                if (iModel.testSave != null)
                    uiroot.stage.setScene(iModel.testSave);

                draw();
            }
        }
    }
}

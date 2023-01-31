package com.example.a4;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class TrainerController {
    TargetModel model;
    InteractionModel iModel;


    public TrainerController() {

    }

    public void setModel(TargetModel newModel) {
        model = newModel;
    }

    public void setIModel(InteractionModel newIModel) {
        iModel = newIModel;
    }

    public void handlePressed(MouseEvent event) {
        if (model.hitItem(event.getX(), event.getY())) {
            Groupable g = model.whichHit(event.getX(), event.getY());

            if (iModel.newTest)
            {
                iModel.trialR.trialTimes.clear();
                iModel.trialR.trialIDs.clear();

                iModel.distX1 = g.getX();
                iModel.distY1 = g.getY();
            }
            else
            {
                iModel.distX2 = g.getX();
                iModel.distY2 = g.getY();
                iModel.trialR.trialIDs.add(g.dist(iModel.distX1, iModel.distY1, iModel.distX2, iModel.distY2)/100);
                iModel.distX1 = iModel.distX2;
                iModel.distY1 = iModel.distY2;
            }

            iModel.nextTarget();
        }
    }

    public void handleDragged(MouseEvent event) {

    }

    public void handleReleased(MouseEvent event) {

    }
}

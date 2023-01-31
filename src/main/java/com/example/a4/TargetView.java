package com.example.a4;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public class TargetView extends StackPane implements TargetModelListener, IModelListener {
    GraphicsContext gc;
    Canvas myCanvas;
    TargetModel model;
    InteractionModel iModel;

    public TargetView() {
        myCanvas = new Canvas(800,800);
        gc = myCanvas.getGraphicsContext2D();

        setStyle("-fx-background-color: skyblue;");


        this.getChildren().add(myCanvas);
    }

    // Draw the entire view as needed, after any updates
    private void draw() {
        gc.clearRect(0,0,myCanvas.getWidth(),myCanvas.getHeight());
        model.getItems().forEach(g -> {
            if (g.checkDeleted())
                return;
            if (iModel.isSelected(g)) {
                gc.setStroke(Color.DEEPPINK);
                gc.setFill(Color.DEEPPINK);
            } else {
                gc.setStroke(Color.STEELBLUE);
                gc.setFill(Color.STEELBLUE);
            }
            drawItem(g);
        });

        if (!iModel.pathComplete) {
            if (iModel.points.size() <= 1)
                return;
            gc.setFill(Color.RED);
            iModel.points.forEach(p -> {
                gc.fillOval(p.getX(), p.getY(), 2, 2);
            });

            double x = (iModel.points.get(0).getX() + iModel.points.get(iModel.points.size()-1).getX())/2;
            double y = (iModel.points.get(0).getY() + iModel.points.get(iModel.points.size()-1).getY())/2;
            double w = Math.abs(iModel.points.get(0).getX() - iModel.points.get(iModel.points.size()-1).getX());
            double h = Math.abs(iModel.points.get(0).getY() - iModel.points.get(iModel.points.size()-1).getY());
            x -= w/2;
            y -= h/2;
            gc.setFill(null);
            gc.setStroke(Color.GREEN);
            gc.strokeRect(x,y,w,h);

            iModel.rubberbandCoords = new ArrayList<>(List.of(x,y,w,h));

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
            gc.setFill(Color.BLACK);
            gc.setFont(new Font("Arial", 15));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText(g.getLabel(), (g.getLeft()+g.getRight())/2, (g.getTop()+g.getBottom())/2, 1000);

        }
    }

    public void setModel(TargetModel newModel) {
        model = newModel;
    }

    public void setIModel(InteractionModel newIModel) {
        iModel = newIModel;
    }

    @Override
    public void modelChanged() {
        draw();

    }

    @Override
    public void iModelChanged() {
        draw();
    }

    public void setController(TargetController controller) {
        myCanvas.setOnMousePressed(controller::handlePressed);
        myCanvas.setOnMouseDragged(controller::handleDragged);
        myCanvas.setOnMouseReleased(controller::handleReleased);
    }
}

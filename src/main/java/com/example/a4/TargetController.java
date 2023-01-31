package com.example.a4;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class TargetController {
    TargetModel model;
    InteractionModel iModel;
    double prevX, prevY;
    double dX, dY;

    enum State {READY, PREPARE_CREATE, DRAGGING}
    enum DragState {NONE, MOVE, RESIZE}

    State currentState = State.READY;
    DragState currentDragState = DragState.NONE;
    KeyCode keyDown = null;

    public TargetController() {

    }

    public void setModel(TargetModel newModel) {
        model = newModel;
    }

    public void setIModel(InteractionModel newIModel) {
        iModel = newIModel;
    }

    public void handlePressed(MouseEvent event) {

        switch (currentState) {
            case READY -> {
                if (model.hitItem(event.getX(), event.getY())) {
                    Groupable g = model.whichHit(event.getX(), event.getY());
                    if (event.isControlDown()) {
                        iModel.select(g);
                    } else {
                        if (!iModel.isSelected(g)) {
                            iModel.clearSelection();
                            iModel.select(g);
                        }
                    }
                    prevX = event.getX();
                    prevY = event.getY();
                } else {
                    if (!event.isControlDown())
                        iModel.clearSelection();
                    iModel.points.clear();
                    iModel.pathComplete = false;
                    iModel.points.add(new Point2D(event.getX(), event.getY()));

                    currentState = State.PREPARE_CREATE;
                }
            }
        }
    }

    public void handleDragged(MouseEvent event) {

        if (currentState.equals(State.PREPARE_CREATE))
        {
            iModel.points.add(new Point2D(event.getX(), event.getY()));
            iModel.refresh();
        }
        else
            currentState = State.DRAGGING;

        if (keyDown != null && currentState.equals(State.DRAGGING) && keyDown.equals(KeyCode.SHIFT))
        {
            if (currentDragState.equals(DragState.NONE)) {
                TargetCommand tc = new ResizeCommand(model, model.getItems());
                iModel.undoStack.add(tc);
                iModel.redoStack.clear();
                currentDragState = DragState.MOVE;
            }
            dX = event.getX() - prevX;
            dY = event.getY() - prevY;
            prevX = event.getX();
            prevY = event.getY();
            model.resizeTargets(iModel.getSelection(),dX);
            currentDragState = DragState.RESIZE;
        }
        else if (currentState.equals(State.DRAGGING))
        {
            if (currentDragState.equals(DragState.NONE)) {
                TargetCommand tc = new MoveCommand(model, model.getItems());
                iModel.undoStack.add(tc);
                iModel.redoStack.clear();
                currentDragState = DragState.RESIZE;
            }
            dX = event.getX() - prevX;
            dY = event.getY() - prevY;
            prevX = event.getX();
            prevY = event.getY();
            model.moveTargets(iModel.getSelection(), dX, dY);
            currentDragState = DragState.MOVE;
        }
    }

    public void handleReleased(MouseEvent event) {

        iModel.points.clear();
        iModel.refresh();

        List<Double> rbc = iModel.rubberbandCoords;
        model.getItems().forEach(g -> {
            if (g.getLeft() > rbc.get(0) && g.getTop() > rbc.get(1) &&
                g.getRight() < (rbc.get(0)+rbc.get(2)) && g.getBottom() < (rbc.get(1)+rbc.get(3)))
            {
                iModel.select(g);
            }
            iModel.rubberbandCoords = new ArrayList<>(List.of(0.0,0.0,0.0,0.0));
        });


        if (currentState.equals(State.DRAGGING))
        {
            // only to save the target data after mouse release
            // used for redo-ing purposes
            switch (currentDragState)
            {
                case MOVE -> {
                    MoveCommand rc = (MoveCommand) iModel.undoStack.get(iModel.undoStack.size() - 1);
                    model.getItems().forEach(g -> rc.setEndData(g.getTargetData()));
                }
                case RESIZE -> {
                    ResizeCommand rc = (ResizeCommand) iModel.undoStack.get(iModel.undoStack.size() - 1);
                    model.getItems().forEach(g -> rc.setEndData(g.getTargetData()));
                }
            }
            if (!currentDragState.equals(DragState.NONE))
                currentDragState = DragState.NONE;
        }

        if (keyDown != null && currentState.equals(State.PREPARE_CREATE) && keyDown.equals(KeyCode.SHIFT))
        {
            Groupable nt = model.addTarget(event.getX(), event.getY(), 50);
            TargetCommand tc = new CreateCommand(model, nt);
            iModel.undoStack.add(tc);
            iModel.redoStack.clear();
        }
        currentState = State.READY;
    }


    public void handleKeyPressed(KeyEvent keyEvent) {
        keyDown = keyEvent.getCode();

        if (keyDown.equals(KeyCode.DELETE))
        {
            TargetCommand tc = new DeleteCommand(model, iModel.getSelection());
            iModel.undoStack.add(tc);
            iModel.redoStack.clear();

            model.deleteTargets(iModel.getSelection());
            iModel.clearSelection();
        }

        if (keyEvent.isControlDown() && keyDown.equals(KeyCode.Z) && iModel.undoStack.size() > 0)
        {
            iModel.undoStack.get(iModel.undoStack.size()-1).undo();
            iModel.redoStack.add(iModel.undoStack.get(iModel.undoStack.size()-1));
            iModel.undoStack.remove(iModel.undoStack.size()-1);
            iModel.clearSelection();

//            System.out.println(iModel.undoStack);
        }

        if (keyEvent.isControlDown() && keyDown.equals(KeyCode.R) && iModel.redoStack.size() > 0)
        {
            iModel.redoStack.get(iModel.redoStack.size()-1).redo();
            iModel.undoStack.add(iModel.redoStack.get(iModel.redoStack.size()-1));
            iModel.redoStack.remove(iModel.redoStack.size()-1);
            iModel.clearSelection();
        }

        if (keyEvent.isControlDown() && keyDown.equals(KeyCode.C) && iModel.getSelection().size() > 0)
        {
//            System.out.println("Copy");
            iModel.tClipB.clearClipboard();
            iModel.getSelection().forEach(g -> iModel.tClipB.saveToClipboard(g));
        }

        if (keyEvent.isControlDown() && keyDown.equals(KeyCode.X) && iModel.getSelection().size() > 0)
        {
//            System.out.println("Cut");
            TargetCommand tc = new DeleteCommand(model, iModel.getSelection());
            iModel.undoStack.add(tc);
            iModel.redoStack.clear();

            iModel.tClipB.clearClipboard();
            iModel.getSelection().forEach(g -> iModel.tClipB.saveToClipboard(g));

            model.deleteTargets(iModel.getSelection());
            iModel.clearSelection();
        }

        if (keyEvent.isControlDown() && keyDown.equals(KeyCode.V) && iModel.tClipB.cbSize() > 0)
        {
//            System.out.println("Paste");
            iModel.tClipB.getClipboard().forEach(g -> {
                Groupable nt = model.addTarget(g.get(0), g.get(1), g.get(2));
                TargetCommand tc = new CreateCommand(model, nt);
                iModel.undoStack.add(tc);
                iModel.redoStack.clear();
            });
        }

        if (keyEvent.isControlDown() && keyDown.equals(KeyCode.T))
        {
            iModel.appModeChanged("test");
        }

        if (keyEvent.isControlDown() && keyDown.equals(KeyCode.E))
        {
            iModel.appModeChanged("edit");
        }
    }

    public void handleKeyReleased(KeyEvent keyEvent)
    {
        keyDown = null;
    }
}

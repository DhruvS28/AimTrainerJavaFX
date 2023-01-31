package com.example.a4;

import java.util.ArrayList;
import java.util.List;

public class MoveCommand implements TargetCommand{

    TargetModel model;
    List<Groupable> g;

    List<Double> x;
    List<Double> y;

    List<Double> endDataX;
    List<Double> endDataY;

    public MoveCommand(TargetModel tmodel, List<Groupable> ng){
        model = tmodel;
        g = new ArrayList<>(ng);
        x = new ArrayList<>();
        y = new ArrayList<>();
        endDataX = new ArrayList<>();
        endDataY = new ArrayList<>();

        ng.forEach(g -> {
            x.add(g.getTargetData().get(0));
            y.add(g.getTargetData().get(1));
        });
    }

    @Override
    public void undo() {
        for (int i = 0; i < model.getItems().size(); i++) {
            model.getItems().get(model.getItems().indexOf(g.get(i))).setTargetData(x.get(i), y.get(i), -1);
        }
    }

    @Override
    public void redo() {
        for (int i = 0; i < model.getItems().size(); i++) {
            model.getItems().get(model.getItems().indexOf(g.get(i))).setTargetData(endDataX.get(i), endDataY.get(i), -1);
        }
    }

    // only to save the target data after mouse release
    // used for redo-ing purposes
    public void setEndData(List<Double> ed) {
        endDataX.add(ed.get(0));
        endDataY.add(ed.get(1));
    }
}

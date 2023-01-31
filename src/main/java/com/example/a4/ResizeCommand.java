package com.example.a4;

import java.util.ArrayList;
import java.util.List;

public class ResizeCommand implements TargetCommand{

    TargetModel model;
    List<Groupable> g;

    List<Double> r;
    List<Double> endData;

    public ResizeCommand(TargetModel tmodel, List<Groupable> ng){
        model = tmodel;
        g = new ArrayList<>(ng);
        r = new ArrayList<>();
        endData = new ArrayList<>();

        ng.forEach(g -> {
            r.add(g.getTargetData().get(2));
        });
    }

    @Override
    public void undo() {
        for (int i = 0; i < model.getItems().size(); i++) {
            model.getItems().get(model.getItems().indexOf(g.get(i))).setTargetData(-1, -1, r.get(i));
        }
    }

    @Override
    public void redo() {
        for (int i = 0; i < model.getItems().size(); i++) {
            model.getItems().get(model.getItems().indexOf(g.get(i))).setTargetData(-1, -1, endData.get(i));
        }
    }

    // only to save the target data after mouse release
    // used for redo-ing purposes
    public void setEndData(List<Double> ed) {
        endData.add(ed.get(2));
    }
}

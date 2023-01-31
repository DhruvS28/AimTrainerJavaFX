package com.example.a4;

import java.util.ArrayList;
import java.util.List;

public class DeleteCommand implements TargetCommand{

    TargetModel model;
    List<Groupable> g;

    public DeleteCommand(TargetModel tmodel, List<Groupable> ng){
        model = tmodel;
        g = new ArrayList<>(ng);
    }

    @Override
    public void undo() {
        g.forEach(t -> model.getItems().get(model.getItems().indexOf(t)).recoverT());
    }

    @Override
    public void redo() {
        g.forEach(t -> model.getItems().get(model.getItems().indexOf(t)).deleteT());
    }
}

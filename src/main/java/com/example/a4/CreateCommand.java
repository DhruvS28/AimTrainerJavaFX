package com.example.a4;

public class CreateCommand implements TargetCommand{

    TargetModel model;
    Groupable g;

    public CreateCommand(TargetModel tmodel, Groupable ng){
        model = tmodel;
        g = ng;
    }

    @Override
    public void undo() {
        model.getItems().remove(g);
    }

    @Override
    public void redo() {
        model.getItems().add(g);
    }
}

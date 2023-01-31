package com.example.a4;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InteractionModel {
    List<IModelListener> subscribers;
    List<AppModeListener> modeSubscribers;

    List<Target> allTargets;
    List<Groupable> selection;

    List<Point2D> points;
    boolean pathComplete = false;

    List<Double> rubberbandCoords;

    ArrayList<TargetCommand> undoStack;
    ArrayList<TargetCommand> redoStack;

    TargetClipboard tClipB;

    enum AppMode {EDIT, TEST, REPORT}
    AppMode currentAppMode = AppMode.EDIT;
    boolean newTest = true;
    int trainerNum;

    TrialRecord trialR;
    double time1 = 0, time2 = 0;
    double distX1, distY1, distX2, distY2;

    Scene editSave, testSave;

    public InteractionModel() {
        subscribers = new ArrayList<>();
        modeSubscribers = new ArrayList<>();

        selection = new ArrayList<>();
        allTargets = new ArrayList<>();
        points = new ArrayList<>();
        rubberbandCoords = new ArrayList<>(List.of(0.0,0.0,0.0,0.0));

        undoStack = new ArrayList<>();
        redoStack = new ArrayList<>();

        tClipB = new TargetClipboard();

        trialR = new TrialRecord();
    }

    public void addSubscriber(IModelListener sub) {
        subscribers.add(sub);
    }

    private void notifySubscribers() {
        subscribers.forEach(s -> s.iModelChanged());
    }

    public void addModeSubscriber(AppModeListener sub) {
        modeSubscribers.add(sub);
    }

    private void notifyModeSubscribers() {
        modeSubscribers.forEach(s -> s.AppModeChanged());
    }

    public void appModeChanged(String s) {
        if (s.equals("test"))
        {
            currentAppMode = AppMode.TEST;
        }
        if (s.equals("edit"))
        {
            currentAppMode = AppMode.EDIT;
        }

        notifyModeSubscribers();
    }

    public void nextTarget() {
        if (newTest)
        {
            time1 = System.currentTimeMillis();
            trialR.trialTimes.clear();
            newTest = false;
        }
        else {
            time2 = System.currentTimeMillis();
            trialR.trialTimes.add(time2 - time1);
            time1 = time2;
        }
        trainerNum++;
        modeSubscribers.forEach(s -> s.iModelChanged());
    }

    public Scene showReport()
    {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setAutoRanging(true);
        yAxis.setAutoRanging(true);

        final ScatterChart<Number,Number> sc = new
                ScatterChart<Number,Number>(xAxis,yAxis);
        xAxis.setLabel("ID (bits)");
        yAxis.setLabel("MT (ms)");
        sc.setTitle("Trail Performance");


        sc.setBackground(new Background(new BackgroundFill(Color.WHITE, null, new Insets(-10))));

        XYChart.Series series = new XYChart.Series();
        series.setName("Trial Performance");
        for (int i = 0; i < trialR.trialIDs.size(); i++)
        {
            series.getData().add(new XYChart.Data(trialR.trialIDs.get(i), trialR.trialTimes.get(i)));
        }

        sc.getData().addAll(series);
        Group root = new Group(sc);

        Scene scene = new Scene(root, 800, 800);

        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.isControlDown() && keyEvent.getCode().equals(KeyCode.T))
            {
                appModeChanged("test");
            }
            if (keyEvent.isControlDown() && keyEvent.getCode().equals(KeyCode.E))
            {
                appModeChanged("edit");
            }
        });

        return scene;
    }

    public void refresh() {
        notifySubscribers();
    }


    public void select(Groupable g) {
        addSubtract(g);
        notifySubscribers();
    }


    private void addSubtract(Groupable g) {
        if (selection.contains(g)) {
            selection.remove(g);
        } else {
            selection.add(g);
        }
    }


    public boolean isSelected(Groupable g) {
        return selection.contains(g);
    }


    public void clearSelection() {
        selection.clear();
        notifySubscribers();
    }


    public List<Groupable> getSelection() {
        return selection;
    }
}


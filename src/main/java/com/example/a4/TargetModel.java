package com.example.a4;

import java.util.ArrayList;
import java.util.List;

public class TargetModel {
    private List<TargetModelListener> subscribers;
    private List<AppModeListener> modeSubscribers;
    private List<Groupable> items;

    public TargetModel() {
        subscribers = new ArrayList<>();
        modeSubscribers = new ArrayList<>();
        items = new ArrayList<>();
    }

    public Groupable addTarget(double x, double y, double r) {
        Groupable nt = new Target(x, y, r, items.size()+1);
        items.add(nt);
        notifySubscribers();
        return nt;
    }

    public void moveTargets(List<Groupable> gs, double dx, double dy) {
        gs.forEach(g -> {
            g.move(dx, dy);
        });
        notifySubscribers();
    }

    public void resizeTargets(List<Groupable> gs, double dx) {
        gs.forEach(g -> {
            g.resize(dx);
        });
        notifySubscribers();
    }

    public void deleteTargets(List<Groupable> gs) {
        gs.forEach(g -> g.deleteT());
        notifySubscribers();
    }

    public void setItems(List<Groupable> gs) {
        items = gs;
        notifySubscribers();
    }

    public void addSubscriber(TargetModelListener sub) {
        subscribers.add(sub);
    }

    private void notifySubscribers() {
        subscribers.forEach(s -> s.modelChanged());
    }

    public void addModeSubscriber(AppModeListener sub) {
        modeSubscribers.add(sub);
    }

    private void notifyModeSubscribers() {
        modeSubscribers.forEach(s -> s.AppModeChanged());
    }

    public List<Groupable> getItems() {
        return items;
    }

    public boolean hitItem(double x, double y) {
        for (Groupable g : items) {
            if (g.contains(x, y)) return true;
        }
        return false;
    }

    public Groupable whichHit(double x, double y) {
        for (Groupable g : items) {
            if (g.contains(x, y)) return g;
        }
        return null;
    }
}

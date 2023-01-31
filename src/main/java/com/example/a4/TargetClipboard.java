package com.example.a4;

import java.util.ArrayList;

public class TargetClipboard {

    ArrayList<Groupable> savedTargets;
    ArrayList<Double> cx;
    ArrayList<Double> cy;
    ArrayList<Double> cr;

    public TargetClipboard() {
        savedTargets = new ArrayList<>();
        cx = new ArrayList<>();
        cy = new ArrayList<>();
        cr = new ArrayList<>();
    }

    public void clearClipboard() {
        savedTargets.clear();
        cx.clear();
        cy.clear();
        cr.clear();
    }

    public void saveToClipboard(Groupable sg) {
        savedTargets.add(sg);
        cx.add(sg.getTargetData().get(0));
        cy.add(sg.getTargetData().get(1));
        cr.add(sg.getTargetData().get(2));
    }

    public ArrayList<ArrayList<Double>> getClipboard() {
        ArrayList<ArrayList<Double>> data = new ArrayList<>();
        for (int i = 0; i < savedTargets.size(); i++) {
            data.add(new ArrayList<>());
            data.get(i).add(cx.get(i));
            data.get(i).add(cy.get(i));
            data.get(i).add(cr.get(i));
        }
        return data;
    }

    public int cbSize() {
        return savedTargets.size();
    }
}

package com.example.a4;

import java.util.ArrayList;
import java.util.List;

public interface Groupable {
    void move(double dX, double dY);
    void resize(double dx);
    boolean contains(double x, double y);
    double getX();
    double getY();
    double getLeft();
    double getTop();
    double getRight();
    double getBottom();
    boolean hasChildren();
    void deleteT();
    void recoverT();
    boolean checkDeleted();
    String getLabel();
    List<Double> getTargetData();
    void setTargetData(double nx, double ny, double nr);
    double dist(double x1, double y1, double x2, double y2);
    ArrayList<Groupable> getChildren();
}

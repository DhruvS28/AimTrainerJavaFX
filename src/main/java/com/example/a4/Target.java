package com.example.a4;

import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class Target implements Groupable {
    double x, y;
    double r;

    Label number;
    boolean del = false;

    List<Double> targetData;

    public Target(double nx, double ny, double nr, int num) {
        x = nx;
        y = ny;
        r = nr;
        number = new Label(""+num+"");

        targetData = new ArrayList<>();
        targetData.add(x);
        targetData.add(y);
        targetData.add(r);
    }

    public List<Double> getTargetData(){
        return targetData;
    }

    public void setTargetData(double nx, double ny, double nr){
        if (nx != -1){
            targetData.set(0, nx);
            x = nx;
        }

        if (ny != -1){
            targetData.set(1, ny);
            y = ny;
        }

        if (nr != -1){
            targetData.set(2, nr);
            r = nr;
        }
    }
    public void deleteT()
    {
        del = true;
    }
    public void recoverT()
    {
        del = false;
    }
    public boolean checkDeleted()
    {
        return del;
    }

    public String getLabel()
    {
        return number.getText();
    }

    public void move(double dx, double dy) {
//        System.out.println("moving");
        x += dx;
        y += dy;

        this.setTargetData(x,y, -1);
    }

    public void resize(double dx) {
//        System.out.println("resizing");
        if (x+dx < x && r > 5)
            r -= 1;
        if (x+dx > x && r < 100)
            r += 1;
        this.setTargetData(-1,-1,r);

    }
    public boolean contains(double cx, double cy) {
        return dist(cx, cy, x, y) <= r;
    }

    public double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getLeft() {
        return x - r;
    }

    public double getTop() {
        return y - r;
    }

    public double getRight() {
        return x + r;
    }

    public double getBottom() {
        return y + r;
    }

    public boolean hasChildren() {
        return false;
    }

    public ArrayList<Groupable> getChildren() {
        return null;
    }
}

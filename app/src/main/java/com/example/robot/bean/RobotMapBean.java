package com.example.robot.bean;

public class RobotMapBean {
    private String map_Name;
    private int gridWidth;
    private int gridHeight;
    private double originX;
    private double originY;
    private double resolution;

    public String getMap_Name() {
        return map_Name;
    }

    public void setMap_Name(String map_Name) {
        this.map_Name = map_Name;
    }

    public double getOriginX() {
        return originX;
    }

    public void setOriginX(double originX) {
        this.originX = originX;
    }

    public double getOriginY() {
        return originY;
    }

    public void setOriginY(double originY) {
        this.originY = originY;
    }

    public double getResolution() {
        return resolution;
    }

    public void setResolution(double resolution) {
        this.resolution = resolution;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public void setGridHeight(int gridHeight) {
        this.gridHeight = gridHeight;
    }
}

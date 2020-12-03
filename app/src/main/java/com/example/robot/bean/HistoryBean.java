package com.example.robot.bean;

public class HistoryBean {
    private String mapName;
    private String taskName;
    private String time;
    private String date;

    public HistoryBean(String mapName, String taskName, String time, String date) {
        this.mapName = mapName;
        this.taskName = taskName;
        this.time = time;
        this.date = date;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

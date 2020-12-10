package com.example.robot.bean;

public class TaskStateList {
    private String pointName;
    private String taskState;
    private int aIcon;

    public TaskStateList(String pointName, String taskState, int aIcon) {
        this.pointName = pointName;
        this.taskState = taskState;
        this.aIcon = aIcon;
    }

    public String getPointName() {
        return pointName;
    }

    public String getTaskState() {
        return taskState;
    }

    public int getaIcon() {
        return aIcon;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public void setaIcon(int aIcon) {
        this.aIcon = aIcon;
    }
}

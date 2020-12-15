package com.example.robot.bean;

public class TaskStateList {
    private String pointName;
    private String taskState;
    private int spinnerTime;

    public TaskStateList(String pointName, int spinnerTime) {
        this.pointName = pointName;
        this.spinnerTime = spinnerTime;
    }

    public TaskStateList(String pointName, String taskState) {
        this.pointName = pointName;
        this.taskState = taskState;
    }

    public int getSpinnerTime() {
        return spinnerTime;
    }

    public void setSpinnerTime(int spinnerTime) {
        this.spinnerTime = spinnerTime;
    }

    public String getPointName() {
        return pointName;
    }

    public String getTaskState() {
        return taskState;
    }


    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }
}

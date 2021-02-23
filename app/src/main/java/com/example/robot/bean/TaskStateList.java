package com.example.robot.bean;

public class TaskStateList {
    private int number;
    private String pointName;
    private String taskState;
    private String pointTime;
    private int spinnerTime;

    public TaskStateList(String pointName, int spinnerTime) {
        this.pointName = pointName;
        this.spinnerTime = spinnerTime;
    }

    public TaskStateList(int number, String pointName, String pointTime, String taskState) {
        this.number = number;
        this.pointName = pointName;
        this.pointTime = pointTime;
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

    public String getPointTime() {
        return pointTime;
    }

    public int getNumber() {
        return number;
    }


    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public void setTPointTime(String pointTime) {
        this.pointTime = pointTime;
    }

    public void setTNumber(int number) {
        this.number = number;
    }
}

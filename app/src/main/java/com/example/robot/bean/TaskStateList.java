package com.example.robot.bean;

public class TaskStateList {
    private String pointName;
    private String taskState;

    public TaskStateList(String pointName, String taskState) {
        this.pointName = pointName;
        this.taskState = taskState;
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

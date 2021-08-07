package com.example.cp670_finalprojectgroup4.data.model;

import java.util.Date;

public class TimerModel {
    private int timerId;
    private int userId;
    private int todoId;
    private Date startTime;
    private Date endTime;

    public TimerModel() {
    }


    public TimerModel(int timerId, int userId, int todoId, Date startTime, Date endTime) {
        this.timerId = timerId;
        this.userId = userId;
        this.todoId = todoId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "TimerModel{" +
                "timerId=" + timerId +
                ", userId=" + userId +
                ", todoId=" + todoId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    public int getTimerId() {
        return timerId;
    }

    public void setTimerId(int timerId) {
        this.timerId = timerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTodoId() {
        return todoId;
    }

    public void setTodoId(int todoId) {
        this.todoId = todoId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}

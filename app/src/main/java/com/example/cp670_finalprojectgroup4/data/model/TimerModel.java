package com.example.cp670_finalprojectgroup4.data.model;

import java.util.Date;

public class TimerModel {
    private int timerId;
    private int userId;
    private int todoId;
    private Date totalTime;

    public TimerModel() {
    }

    public TimerModel(int timerId, int userId, int todoId, Date totalTime) {
        this.timerId = timerId;
        this.userId = userId;
        this.todoId = todoId;
        this.totalTime = totalTime;
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

    public Date getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Date totalTime) {
        this.totalTime = totalTime;
    }

    @Override
    public String toString() {
        return "TimerModel{" +
                "timerId=" + timerId +
                ", userId=" + userId +
                ", todoId=" + todoId +
                ", totalTime=" + totalTime +
                '}';
    }
}

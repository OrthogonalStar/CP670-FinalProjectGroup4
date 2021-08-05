package com.example.cp670_finalprojectgroup4;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;



public class Todo implements Serializable {
    int todoId;
    int userId;
    String title;
    String description;
    String location;
    Date startdate;
    int duration;
    Time starttime;
    Status status;

    @Override
    public String toString() {
        return "Todo{" +
                "todoId=" + todoId +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", startdate=" + startdate +
                ", duration=" + duration +
                ", starttime=" + starttime +
                ", status=" + status +
                '}';
    }

    public Todo() {
    }

    public Todo(int todoId, int userId, String title, String description, String location, Date startdate, int duration, Time starttime, Status status) {
        this.todoId = todoId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.startdate = startdate;
        this.userId = userId;
        this.duration = duration;
        this.starttime = starttime;
        this.status = status;
    }

    public int getTodoId() {
        return todoId;
    }

    public void setTodoId(int todoId) {
        this.todoId = todoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Time getStarttime() {
        return starttime;
    }

    public void setStarttime(Time starttime) {
        this.starttime = starttime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}


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
    Date startdate;
    Status status;

    @Override
    public String toString() {
        return "Todo{" +
                "todoId=" + todoId +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startdate=" + startdate +
                ", status=" + status +
                '}';
    }

    public Todo() {
    }

    public Todo(int todoId, int userId, String title, String description, Date startdate, Status status) {
        this.todoId = todoId;
        this.title = title;
        this.description = description;
        this.startdate = startdate;
        this.userId = userId;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}


package com.example.cp670_finalprojectgroup4;

import android.app.Application;

import com.example.cp670_finalprojectgroup4.data.model.UserModel;

public class CurrentUser extends Application {

    private UserModel user = null;

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}

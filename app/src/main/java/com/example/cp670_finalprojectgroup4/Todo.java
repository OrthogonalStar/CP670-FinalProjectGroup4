package com.example.cp670_finalprojectgroup4;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;



public class Todo implements Serializable {
    String title;
    String description;
    String location;
    Date startdate;
    String username;
    int duration;
    Time starttime;
    Status status;
}

enum Status {
    INPROGRESS,
    TBD,
    FINISHED
}

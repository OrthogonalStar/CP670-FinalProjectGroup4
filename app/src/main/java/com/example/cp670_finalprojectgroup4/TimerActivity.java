package com.example.cp670_finalprojectgroup4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Integer.parseInt;

public class TimerActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "MainActivity";
    Button start, stop, reset;
    int timerlength;
    int time_left;
    CountDownTimer timer;
    boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        running=false;
        Log.i(ACTIVITY_NAME, "In onCreate()");
        setContentView(R.layout.activity_timer);
        ((TextView)findViewById(R.id.timer)).setText("0:00");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

    public void click_start(View view) {
        timer.start();
    }

    public void click_stop(View view) {
        timer.cancel();
        create_timer(time_left);

    }

    public void click_reset(View view) {
        timer.cancel();
        ((TextView)findViewById(R.id.timer)).setText("0:00");
        running=false;
    }


    public void five_min_timer(View view) {
        if (!running){
            time_left=300000;
            create_timer (300000);
            running=true;
        }
    }

    public void twentyfive_min_timer(View view) {
        if (!running){
            time_left=1500000;
            create_timer(1500000);
            running=true;
        }
    }

    private void create_timer(int length){
        timer=new CountDownTimer(length, 1000) {

            public void onTick(long millisUntilFinished) {
                ((TextView)findViewById(R.id.timer)).setText(millisUntilFinished/60000+":"+(millisUntilFinished%60000)/ 1000);
                time_left=(int)millisUntilFinished;
            }

            public void onFinish() {
                Toast.makeText(getApplicationContext(),"Timer Done",Toast.LENGTH_SHORT).show();
                running=false;
            }
        };
    }
}
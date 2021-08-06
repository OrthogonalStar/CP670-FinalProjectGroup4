package com.example.cp670_finalprojectgroup4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cp670_finalprojectgroup4.data.model.UserData;
import com.example.cp670_finalprojectgroup4.data.model.UserModel;

public class MainActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "MainActivity";
    Button todo, timer, trends,myCalender,login;
    private SharedPreferences sharedPreferences;
    private static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_NAME, "In onCreate()");
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());
        updateUserName();

        login = (Button) findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(new Intent(MainActivity.this, LoginActivity.class));
                startActivity(intent);
            }
        });
        todo = (Button) findViewById(R.id.todo_button);
        todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(new Intent(MainActivity.this, TodolActivity.class));
                startActivity(intent);
            }
        });

        timer = (Button) findViewById(R.id.timer_button);
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(new Intent(MainActivity.this, TimerActivity.class));
                startActivity(intent);
            }
        });

        trends = (Button) findViewById(R.id.trends_button);
        trends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(new Intent(MainActivity.this, TrendsActivity.class));
                startActivity(intent);
            }
        });



    }

    private void updateUserName(){
        UserModel user= ((CurrentUser) getApplication()).getUser();
        if(user != null)
            ((TextView) findViewById(R.id.todo_txt)).setText(context.getString(R.string.title_txt) + "  -  " + context.getString(R.string.welcome) +  ", "+ user.getName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
        updateUserName();
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
}
package com.example.cp670_finalprojectgroup4;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cp670_finalprojectgroup4.data.connector.DatabaseAccessConnector;
import com.example.cp670_finalprojectgroup4.data.dao.TimerDAO;
import com.example.cp670_finalprojectgroup4.data.dao.TodoDAO;
import com.example.cp670_finalprojectgroup4.data.model.TimerModel;
import com.example.cp670_finalprojectgroup4.data.model.UserModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TimerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    protected static final String ACTIVITY_NAME = "TimerActivity";
    Button start, stop, reset;
    Spinner dropdown;
    int timerlength;
    int time_left;
    ArrayList<Todo> todos;
    CountDownTimer timer;
    boolean running;
    String selected;
    int selectedPos;
    boolean recording;
    Date startD;
    Date endD;

    UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user= ((CurrentUser) getApplication()).getUser();
        if(user == null)
            finish();

        running=false;
        recording=false;
        Log.i(ACTIVITY_NAME, "In onCreate()");
        setContentView(R.layout.activity_timer);
        ((TextView)findViewById(R.id.timer)).setText("0:00");

        TodoDAO todoDAO = new TodoDAO();
        TodoDAO.setConnection(DatabaseAccessConnector.getConnection());

        TimerDAO timerDAO = new TimerDAO();
        TimerDAO.setConnection(DatabaseAccessConnector.getConnection());

        System.out.println(TimerDAO.getAllTimers());

    }

    private ArrayList<String> getToDoNames() {
        ArrayList<String> s=new ArrayList<>();
        if (todos!=null) {
            for (int x = 0; x < todos.size(); x++) {
                s.add(todos.get(x).getTitle());
            }
        }
        else{
            s.add("empty");
        }
        return s;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
        todos=(ArrayList<Todo>) TodoDAO.getAllActiveToDoItemsForUser(user.getId());
        dropdown = findViewById(R.id.selectedToDo);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getToDoNames());
        dropdown.setAdapter(adapter);
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
        startD = Calendar.getInstance().getTime();
        timer.start();
        recording = true;

    }

    public void click_stop(View view) {
        timer.cancel();
        create_timer(time_left);
        if (recording){
            Date c = Calendar.getInstance().getTime();

            TimerModel tm =new TimerModel(0,user.getId(),todos.get(dropdown.getSelectedItemPosition()).getTodoId(),startD,c);
            TimerDAO.addTimer(tm);
        }
        recording = false;

    }

    public void click_reset(View view) {
        timer.cancel();
        ((TextView)findViewById(R.id.timer)).setText("0:00");
        running=false;

        if (recording){
            Date c = Calendar.getInstance().getTime();

            TimerModel tm =new TimerModel(0,user.getId(),todos.get(dropdown.getSelectedItemPosition()).getTodoId(),startD,c);
            TimerDAO.addTimer(tm);
        }
        recording = false;
    }


    public void five_min_timer(View view) {
        if (!running){
            ((TextView)findViewById(R.id.timer)).setText("5:00");
            time_left=300000;
            create_timer (300000);
            running=true;
        }
    }

    public void twentyfive_min_timer(View view) {
        if (!running){
            ((TextView)findViewById(R.id.timer)).setText("25:00");
            time_left=1500000;
            create_timer(1500000);

            running=true;
            if (selected !=null) recording=true;
        }
    }




    private void create_timer(int length){
        timer=new CountDownTimer(length, 1000) {

            public void onTick(long millisUntilFinished) {
                ((TextView)findViewById(R.id.timer)).setText(millisUntilFinished/60000+":"+String.format("%02d", (millisUntilFinished%60000)/ 1000));
                time_left=(int)millisUntilFinished;
            }

            public void onFinish() {
                Toast.makeText(getApplicationContext(),"Timer Done",Toast.LENGTH_SHORT).show();
                if (recording){
                    Date c = Calendar.getInstance().getTime();

                    TimerModel tm =new TimerModel(0,user.getId(),todos.get(selectedPos).getTodoId(),startD,c);
                    TimerDAO.addTimer(tm);
                }

                running=false;
                recording=false;

            }
        };
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selected=(String)parent.getItemAtPosition(position);
        selectedPos=position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        selected=null;
    }
}
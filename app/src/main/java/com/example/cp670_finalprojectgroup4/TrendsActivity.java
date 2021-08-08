package com.example.cp670_finalprojectgroup4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cp670_finalprojectgroup4.data.connector.DatabaseAccessConnector;
import com.example.cp670_finalprojectgroup4.data.dao.TimerDAO;
import com.example.cp670_finalprojectgroup4.data.dao.TodoDAO;
import com.example.cp670_finalprojectgroup4.data.model.TimerModel;
import com.example.cp670_finalprojectgroup4.data.model.UserModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
// ****************************************************************

// This Class shows trend in todo item activity over the last week
public class TrendsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static String ACTIVITY_NAME = "TrendsActivity";
    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList<Entry> lineEntries;
    Button goButton;
    TimerDAO timerDAO;
    TodoDAO todoDAO;
    UserModel user;
    ArrayList<TimerModel> timers;
    Spinner dropdown;
    ArrayList<Todo> todos;
    String selected;
    int selectedPos;

    private static Connection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trends);


        timerDAO = new TimerDAO();
        TimerDAO.setConnection(DatabaseAccessConnector.getConnection());

        todoDAO = new TodoDAO();
        TodoDAO.setConnection(DatabaseAccessConnector.getConnection());

        user = ((CurrentUser) getApplication()).getUser();
        Log.d(ACTIVITY_NAME, "onCreate: user conntected is -->" + user);
        if (user == null)
            finish();

        lineChart = findViewById(R.id.LineChart);

        // Check the Go button to display chart
        goButton = (Button) findViewById(R.id.go_but);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEntries();
                lineDataSet = new LineDataSet(lineEntries, "");
                lineData = new LineData(lineDataSet);
                lineChart.setData(lineData);
                //lineChart.invalidate();
            }
        });
    }

    private void getEntries(){
        timers = (ArrayList<TimerModel>) TimerDAO.getAllTimersForUser(user.getId(), todos.get(selectedPos).getTodoId());
        SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
        Map<String, Long> entries = new Hashtable();

        for(int i=0; i<timers.size(); i++){
            TimerModel current = timers.get(i);
            Log.i(ACTIVITY_NAME, current.getStartTime().getTime() + "");
            Log.i(ACTIVITY_NAME, current.getEndTime().getTime() + "");

            long time = current.getEndTime().getTime() - current.getStartTime().getTime();

            Log.i(ACTIVITY_NAME, time + "");

            String date = ft.format(current.getEndTime());

             if(entries.get(date) != null){
                entries.put(date, entries.get(date) + time);
             }else{
                 entries.put(date, time);
             }
        }
        Log.i(ACTIVITY_NAME, entries.toString());
        lineEntries = new ArrayList<Entry>();
        for(String key: entries.keySet()){
            Date dt = new Date();
            try {
                dt = new SimpleDateFormat("dd/MM/yyyy").parse(key);
            }catch (ParseException e) {
                e.printStackTrace();
            }
            lineEntries.add(new Entry(dt.getTime(), entries.get(key)));
        }
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
        todos=(ArrayList<Todo>) TodoDAO.getAllToDoItemsForUser(user.getId());
        dropdown = findViewById(R.id.select_todo);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getToDoNames());
        dropdown.setAdapter(adapter);
    }

}
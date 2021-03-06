package com.example.cp670_finalprojectgroup4;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cp670_finalprojectgroup4.data.connector.DatabaseAccessConnector;
import com.example.cp670_finalprojectgroup4.data.dao.TimerDAO;
import com.example.cp670_finalprojectgroup4.data.dao.TodoDAO;
import com.example.cp670_finalprojectgroup4.data.model.TimerModel;
import com.example.cp670_finalprojectgroup4.data.model.UserModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
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
                Description desc = lineChart.getDescription();
                desc.setText("Hours/day");
                XAxis xAxis = lineChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        Date dt = new Date((long) value);
                        SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
                        return ft.format(dt);
                    }
                });
                lineChart.setBackgroundColor(Color.WHITE);
                lineChart.invalidate();
            }
        });
    }

    private void getEntries(){
        timers = (ArrayList<TimerModel>) TimerDAO.getAllTimersForUser(user.getId(), todos.get(dropdown.getSelectedItemPosition()).getTodoId());
        System.out.println(timers);
        SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
        Map<String, Float> entries = new Hashtable();

        for(int i=0; i<timers.size(); i++){
            TimerModel current = timers.get(i);

            float time = current.getEndTime().getTime() - current.getStartTime().getTime();

            String date = ft.format(current.getEndTime());

             if(entries.get(date) != null){
                entries.put(date, entries.get(date) + time);
             }else{
                 entries.put(date, time);
             }
        }

        lineEntries = new ArrayList<Entry>();
        SortedSet<String> keys = new TreeSet<>(entries.keySet());
        for (String key : keys) {
            Date dt = new Date();
            try {
                dt = new SimpleDateFormat("dd/MM/yyyy").parse(key);
            }catch (ParseException e) {
                e.printStackTrace();
            }
            lineEntries.add(new Entry(dt.getTime(), entries.get(key)/3600000));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedPos=position;
        Log.i(ACTIVITY_NAME,String.valueOf(selectedPos));
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
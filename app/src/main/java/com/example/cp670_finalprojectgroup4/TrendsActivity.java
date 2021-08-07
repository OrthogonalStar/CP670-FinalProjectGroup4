package com.example.cp670_finalprojectgroup4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cp670_finalprojectgroup4.data.connector.DatabaseAccessConnector;
import com.example.cp670_finalprojectgroup4.data.dao.TodoDAO;
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
import java.util.List;
// ****************************************************************
// This Class shows aggregation of activity for the given dates
// Each activity can be repeated, based on same title


public class TrendsActivity extends AppCompatActivity {
    private static String TAG = "TrendsActivity";
    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList<Entry> lineEntries;
    Button goButton;
    TodoDAO todoDAO;
    UserModel user;
    ArrayList<Todo> todos;

    private static Connection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trends);


        todoDAO = new TodoDAO();
        TodoDAO.setConnection(DatabaseAccessConnector.getConnection());

        user = ((CurrentUser) getApplication()).getUser();
        Log.d(TAG, "onCreate: user conntected is -->" + user);
        if (user == null)
            finish();

        lineChart = findViewById(R.id.LineChart);

        getEntries();
        lineDataSet = new LineDataSet(lineEntries, "");
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineDataSet.setValueTextColor(Color.WHITE);
        lineDataSet.setValueTextSize(18f);

        // Check the Go button to display chart
        goButton = (Button) findViewById(R.id.go_but);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEntries();
                lineDataSet = new LineDataSet(lineEntries, "");
                lineData = new LineData(lineDataSet);
                lineChart.setData(lineData);
                lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                lineDataSet.setValueTextColor(Color.BLACK);
                lineDataSet.setValueTextSize(18f);
            }
        });
    }

    private void getEntries(){
        lineEntries = new ArrayList<Entry>();
        lineEntries.add(new Entry(2f, 0));
        lineEntries.add(new Entry(4f, 1));
        lineEntries.add(new Entry(6f, 1));
        lineEntries.add(new Entry(8f, 3));
        lineEntries.add(new Entry(7f, 4));
        lineEntries.add(new Entry(3f, 3));
    }

}
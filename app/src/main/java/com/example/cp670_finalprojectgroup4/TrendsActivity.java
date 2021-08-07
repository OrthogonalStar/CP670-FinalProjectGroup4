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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

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
    public  static ArrayList<String> xData = new ArrayList<String>();
    public static ArrayList<Integer> yData = new ArrayList<Integer>();
    PieChart pieChart;
    private String mStartDate = "08/04/2021";
    Description description = new Description();
    Button goButton;
    private TextView mDisplayDate;
    private String inputDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
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

        user= ((CurrentUser) getApplication()).getUser();
        Log.d(TAG, "onCreate: user conntected is -->"+user);
        if(user == null)
            finish();

        // Cleanup before re-running
        xData.clear();
        yData.clear();
      //  pieChart.clear();
        // Call Calendar method to get the date, input
        getCalendarDate();
        // Check the Go button to display chart
        goButton = (Button) findViewById(R.id.go_but);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (todos.isEmpty()) {
                    Log.d(TAG, "onDateSet:  there is no acvitity for selected date:"+inputDate);
                    Toast.makeText(TrendsActivity.this,"There is no activity for selected date "+inputDate,Toast.LENGTH_LONG).show();
                }
                displayPiChart();
            }
        });
    }
// Method to set the Calendar date
    private void getCalendarDate() {

        mDisplayDate = (TextView) findViewById(R.id.tv_date);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(TrendsActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String sYear;
                String sMonth;
                String sDay;
                month = month +1;
                // append 0 to month digit
                if (month <10) {
                    sMonth = "0"+ month;
                }
                else {
                    sMonth = String.valueOf(month);
                }

                // append 0 to Day digit

                if (dayOfMonth <10) {
                    sDay = "0"+ dayOfMonth;
                }
                else {
                    sDay = String.valueOf(dayOfMonth);
                }

                Log.d(TAG, "onDateSet: Selected date is"+year+"/"+month+"/"+dayOfMonth);
                String date = year +"-"+sMonth+"-"+sDay;//sMonth + "/" + sDay +"/"+year;
                inputDate = date;
                Log.d(TAG, "onDateSet: inputDate "+inputDate);
                mDisplayDate.setText(date);
                todos = (ArrayList<Todo>) TodoDAO.getTrendActivity(user.getId(),inputDate);
                Log.d(TAG, "onDateSet: "+todos);

                // Set data to Array based on return value is todos
                    setxyData();
            }
        };

    }



// Method to draw the chart based on title and SUM of duration


    private void displayPiChart(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        description.setText("Trend Activity ");
        pieChart =(PieChart) findViewById(R.id.PieChart);
        pieChart.clear();
        pieChart.setRotationEnabled(true);
        pieChart.setDescription(description);
        // pieChart.setDescendantFocusability(20);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Date:"+inputDate);
        pieChart.setCenterTextSize(10);
        pieChart.setDrawEntryLabels(true);
        Log.d(TAG, "displayPiChart: input Date formated date in --->"+inputDate);
        Date dt = null;
        try {
            dt = new SimpleDateFormat("MM/dd/yyyy").parse(inputDate);
            Log.d(TAG, "displayPiChart: inside try "+dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

            addDataSet();

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "onValueSelected : Value selected from chart");
                Log.d(TAG, "onValueSelected: "+ e.toString());
                Log.d(TAG, "onValueSelected: "+ h.toString());

                int pos1 = e.toString().indexOf("y:");
                String activityItem = e.toString().substring(pos1 +3);

                for (int i = 0 ; i <yData.size();i++) {
                    if (yData.get(i)== Float.parseFloat(activityItem)) {
                        pos1 =i;
                        break;
                    }
                }
                String itemChart = xData.get(pos1 + 1);
                Toast.makeText(TrendsActivity.this,itemChart+"\n"+activityItem,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

// This method add data to Pie Chart
    private void addDataSet() {
        Log.d(TAG, "addDataSet started");
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();
        ArrayList<String> zEntrys = new ArrayList<>();
        List<PieEntry> pieEntries = new ArrayList<>();

        for (int i=0 ; i< yData.size(); i++){
            pieEntries.add(new PieEntry(yData.get(i),xData.get(i)));

            zEntrys.add(yData.get(i) + xData.get(i));
            yEntrys.add(new PieEntry(yData.get(i),i));
            Log.d(TAG, "addDataSet: Loop yData"+yData.get(i));
        }


        for (int i=0 ; i<xData.size(); i++){
            xEntrys.add(xData.get(i));
            Log.d(TAG, "addDataSet: Loop yData"+xData.get(i));
        }
        // create the data set
        PieDataSet pieDataSet = new PieDataSet(pieEntries,"Activity list");
        //   PieData data = new PieData(pieDataSet);

        pieDataSet.setSliceSpace(2);
        Log.d(TAG, "addDataSet: SetSlice Done");
        pieDataSet.setValueTextSize(12);
        Log.d(TAG, "addDataSet: Set Size Done");


        // add colors
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GRAY);
        colors.add(Color.WHITE);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);

        pieDataSet.setColors(colors);

        // add legend to chart
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.animateY(1000);
        pieChart.invalidate();





    }

// Method to set data based on todos,
// Array will be used for chart
    private void setxyData() {
        int count = todos.size();
        xData.clear();
        yData.clear();
        for(int i=0; i<count; i++){
            Todo item = todos.get(i);
            xData.add(item.getTitle());
            yData.add(item.getDuration());
            Log.d(TAG, "setxyDaya: x["+i+"]-----> "+xData+".... y["+i+"] ---->"+yData);
        }

    }

}
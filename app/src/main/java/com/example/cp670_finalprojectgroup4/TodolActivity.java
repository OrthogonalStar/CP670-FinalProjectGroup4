package com.example.cp670_finalprojectgroup4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class TodolActivity extends AppCompatActivity {
    EditText title;
    Button add;
    ArrayList<Todo> todos;
    ChatAdapter messageAdapter;
    protected static final String ACTIVITY_NAME = "TodolActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todol);
        setResoruces();
    }

    void setResoruces(){
        title = findViewById(R.id.editTitle);
        add = findViewById(R.id.btnSend);
    }

    public void onItemAdd(View v) {
        //Pop up a new dialog and submit to add an item here
    }

    @Override
    protected void onStart() {
        Log.i(ACTIVITY_NAME, "In onStart()");
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.i(ACTIVITY_NAME, "In onPause()");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.i(ACTIVITY_NAME, "In onResume()");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.i(ACTIVITY_NAME, "In onDestroy()");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.i(ACTIVITY_NAME, "In onStop()");
        super.onStop();
    }

    private class ChatAdapter extends ArrayAdapter<Todo> {

        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public int getCount() {
            //return super.getCount();
            return todos.size();
        }

        @Override
        public Todo getItem(int position) {
            return todos.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) TodolActivity.this.getLayoutInflater();

            View result = null;
            result = inflater.inflate(R.layout.todo_row, null);
            TextView message = (TextView) result.findViewById(R.id.todo_title);
            message.setText(getItem(position).title); // get the string at position
            return result;
            //return super.getView(position, convertView, parent);
        }
    }

}
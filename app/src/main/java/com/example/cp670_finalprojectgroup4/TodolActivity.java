package com.example.cp670_finalprojectgroup4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

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
        //Pop up a new dialog and suhHimit to add an item here
        showAddItemDialog();
    }

    void showAddItemDialog(){
        AlertDialog.Builder customDialog =
                new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.activity_add_to_do_dialog, null);
        customDialog.setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Fetching values and insert to update the list adapter
                        EditText edit = view.findViewById(R.id.editDesc);
                        String message = edit.getText().toString();

                        //Tested
                        Snackbar.make(findViewById(R.id.TodoList), message, Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                })
                .setNegativeButton(R.string.todo_list_custom_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
        Dialog dialog = customDialog.create();
        dialog.show();
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
            if(getItem(position).status == Status.TBD){
                result = inflater.inflate(R.layout.todo_status_tbd,null);
            }else if(getItem(position).status == Status.INPROGRESS){
                result = inflater.inflate(R.layout.todo_status_inprogress,null);
            }else{
                result = inflater.inflate(R.layout.toto_status_done,null);
            }
            TextView message = (TextView) result.findViewById(R.id.todo_title);
            message.setText(getItem(position).title); // get the string at position
            return result;
            //return super.getView(position, convertView, parent);
        }
    }

}
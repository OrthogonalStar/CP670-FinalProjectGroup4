package com.example.cp670_finalprojectgroup4;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cp670_finalprojectgroup4.data.connector.DatabaseAccessConnector;
import com.example.cp670_finalprojectgroup4.data.dao.TodoDAO;
import com.example.cp670_finalprojectgroup4.data.model.UserModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TodolActivity extends AppCompatActivity {
    Button add,update,delete,clear;
    EditText title;
    ArrayList<Todo> todos;
    ChatAdapter listAdapter;
    ListView todoList;
    Todo selected;
    int selectedPosition;
    TodoDAO todoDAO;
    UserModel user;
    protected static final String ACTIVITY_NAME = "TodolActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todol);
        setResoruces();
        listAdapter = new ChatAdapter( this);
        todoList.setAdapter (listAdapter);

        todoDAO = new TodoDAO();
        TodoDAO.setConnection(DatabaseAccessConnector.getConnection());

        user= ((CurrentUser) getApplication()).getUser();
        Log.i(ACTIVITY_NAME,user.getId().toString());
        if(user == null)
            finish();

        todos = (ArrayList<Todo>) TodoDAO.getAllToDoItemsForUser(user.getId());
        listAdapter.notifyDataSetChanged();
    }

    void setResoruces(){
        add = findViewById(R.id.btnSend);
        todoList = findViewById(R.id.todoList);
        clear = findViewById(R.id.btnTitleTxtClear);
        title = findViewById(R.id.editTitle);
        todos = new ArrayList<Todo>();
        todoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(ACTIVITY_NAME,"long click");
                selectedPosition = position;
                selected = todos.get(selectedPosition);
                if(selected.status == Status.TBD || selected.status == null){
                    selected.status = Status.INPROGRESS;
                }else if(selected.status == Status.INPROGRESS){
                    selected.status = Status.FINISHED;
                }else{
                    selected.status = Status.TBD;
                }
                //TodoDAO.deleteTodo(todos.get(position).todoId);
                todos.remove(position);
                selected.setUserId(user.getId());
                //selected.setTodoId(TodoDAO.addTodo(selected));
                todos.add(position,selected);
                TodoDAO.updateTodo(selected);



                listAdapter.notifyDataSetChanged();
                return false;
            }
        });

        todoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
                selected = todos.get(selectedPosition);
            }
        });

        update = findViewById(R.id.btnUpdate);
        delete = findViewById(R.id.btnDelete);
        clear = findViewById(R.id.btnTitleTxtClear);
    }

    public void onItemAdd(View v) {
        //Pop up a new dialog and suhHimit to add an item here
        showAddItemDialog();
    }

    public void onTodoSave(View v) {
        SharedPreferences  mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        TodoList list = new TodoList();
        list.todos = todos;
        String json = gson.toJson(list);
        prefsEditor.putString("toods", json);
        prefsEditor.commit();
        TestGettingTodosFromSharedPlace();
    }

    public void TestGettingTodosFromSharedPlace(){
        SharedPreferences  mPrefs = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("toods", "");
        TodoList list = new TodoList();
        list = gson.fromJson(json, TodoList.class);
        Log.i(ACTIVITY_NAME,String.valueOf(list.todos.toArray().length));
        //todos = list.todos;
    }

    public void OnItemUpdate(View v){
        //&& inTodo(title.getText().toString())
        ShowEditDialog();

    }

    public boolean inTodo(String title){
        for (Todo o:todos) {
            if(o.title==title){
                return true;
            }
        }
        return false;
    }

    public void ShowEditDialog(){
        AlertDialog.Builder customDialog =
                new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.activity_update_to_do_dialog, null);

        EditText txtTitle = view.findViewById(R.id.editUpdateTitle);
        EditText txtdescription = view.findViewById(R.id.editUpdateDesc); //Fields are defined in the dialog resource xml
        EditText txtstartDate = view.findViewById(R.id.edtUpdateStartDate);


        customDialog.setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String description, startdate, title;
                title = txtTitle.getText().toString();
                description = txtdescription.getText().toString();
                startdate = txtstartDate.getText().toString();

                if(startdate.equals("")){
                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    startdate = formatter.format(date);
                }

                if(title.equals("")){
                    Snackbar.make(findViewById(R.id.TodoList), description, Snackbar.LENGTH_LONG)
                            .setAction("Please enter a name", null).show();
                    return;
                }

                Todo todo = new Todo();
                todo.title = title;
                todo.description = description;

                try {
                    Date dt=new SimpleDateFormat("dd/MM/yyyy").parse(startdate);
                    todo.startdate = dt;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                todo.setUserId(user.getId());

                //TodoDAO.deleteTodo(selected.todoId);

                todos.remove(selected);
                todo.setTodoId(selected.todoId);
                selected = todo;
                //todo.setTodoId(TodoDAO.addTodo(todo));
                todos.add(selectedPosition,todo);
                TodoDAO.updateTodo(todo);

                listAdapter.notifyDataSetChanged();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        Dialog dialog = customDialog.create();
        dialog.show();

        txtdescription.setText(selected.description);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        txtstartDate.setText(formatter.format(selected.startdate));
    }

    public void OnItemDelete(View v){
        TodoDAO.deleteTodo(selected.todoId);
        todos.remove(selected);
        ClearSeleted();
        listAdapter.notifyDataSetChanged();
    }

    public void OnClearSelected(View v){
        ClearSeleted();
    }


    public void ClearSeleted(){
        selected = null;
        title.setText("");
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
                        EditText txtTitle = view.findViewById(R.id.editTitle);
                        EditText txtdescription = view.findViewById(R.id.editDesc); //Fields are defined in the dialog resource xml
                        EditText txtstartDate = view.findViewById(R.id.edtStartDate);

                        String title, description, startdate;
                        title = txtTitle.getText().toString();
                        description = txtdescription.getText().toString();
                        startdate = txtstartDate.getText().toString();

                        if(startdate.equals("")){
                            Date date = new Date();
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            startdate = formatter.format(date);
                        }

                        if(title.equals("")){
                            Snackbar.make(findViewById(R.id.TodoList), description, Snackbar.LENGTH_LONG)
                                    .setAction("Please enter a name", null).show();
                            return;
                        }

                        //Tested
                        Snackbar.make(findViewById(R.id.TodoList), description, Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();

                        Todo todo = new Todo();
                        todo.title = title;
                        todo.description = description;
                        try {
                            Date dt=new SimpleDateFormat("dd/MM/yyyy").parse(startdate);
                            todo.startdate = dt;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        todo.setUserId(user.getId());
                        todo.setTodoId(TodoDAO.addTodo(todo));
                        todos.add(todo);
                        selected = todo; //Let's get the created todo selected first to prevent selection errors
                        listAdapter.notifyDataSetChanged();
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
            String statusDisplay;
            View result = null;
            result = inflater.inflate(R.layout.todo_row, null);
            if(getItem(position).status == Status.TBD || getItem(position).status == null){
                result = inflater.inflate(R.layout.todo_status_tbd,null);
                statusDisplay = "TBD";
            }else if(getItem(position).status == Status.INPROGRESS){
                result = inflater.inflate(R.layout.todo_status_inprogress,null);
                statusDisplay = "INPROGRESS";
            }else{
                result = inflater.inflate(R.layout.toto_status_done,null);
                statusDisplay = "FINISHED";
            }
            TextView title = result.findViewById(R.id.todo_title);
            TextView status = result.findViewById(R.id.todo_status);
            title.setText(getItem(position).title);
            status.setText(statusDisplay);
            return result;
            //return super.getView(position, convertView, parent);
        }
    }

}
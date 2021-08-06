package com.example.cp670_finalprojectgroup4.data.dao;

import android.util.Log;

import com.example.cp670_finalprojectgroup4.Status;
import com.example.cp670_finalprojectgroup4.Todo;
import com.example.cp670_finalprojectgroup4.TrendsActivity;
import com.example.cp670_finalprojectgroup4.data.model.UserModel;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TodoDAO {

    private static Connection connection;

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        TodoDAO.connection = connection;
    }

    public TodoDAO() {
    }

    public static List<Todo> getAllToDoItems(){
        List<Todo> todos = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select todoId, userId, title, description, location, startdate, duration, starttime, status from todo_activity;");
            while (resultSet.next()){
                Todo newTodo = new Todo();

                newTodo.setTodoId(resultSet.getInt(1));
                newTodo.setUserId(resultSet.getInt(2));
                newTodo.setTitle(resultSet.getString(3));
                newTodo.setDescription(resultSet.getString(4));
                newTodo.setLocation(resultSet.getString(5));
                newTodo.setStartdate(resultSet.getDate(6));
                newTodo.setDuration(resultSet.getInt(7));
                newTodo.setStarttime(resultSet.getTime(8));
                newTodo.setStatus(Status.INPROGRESS);
                todos.add(newTodo);
            }
            return todos;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Todo> getAllToDoItemsForUser(int userId){
        List<Todo> todos = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select todoId, userId, title, description, location, startdate, duration, starttime, status from todo_activity where userId =" + userId + ";");
            while (resultSet.next()){
                Todo newTodo = new Todo();

                newTodo.setTodoId(resultSet.getInt(1));
                newTodo.setUserId(resultSet.getInt(2));
                newTodo.setTitle(resultSet.getString(3));
                newTodo.setDescription(resultSet.getString(4));
                newTodo.setLocation(resultSet.getString(5));
                newTodo.setStartdate(resultSet.getDate(6));
                newTodo.setDuration(resultSet.getInt(7));
                newTodo.setStarttime(resultSet.getTime(8));
                String status = resultSet.getString(9);
                if(status.equals(String.valueOf(Status.INPROGRESS))){
                    newTodo.setStatus(Status.INPROGRESS);
                }
                if(status.equals(String.valueOf(Status.TBD))){
                    newTodo.setStatus(Status.TBD);
                }
                if(status.equals(String.valueOf(Status.FINISHED))){
                    newTodo.setStatus(Status.FINISHED);
                }

                todos.add(newTodo);
            }
            return todos;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    // This method will be called by TrendsActivity to get the data based on each day

    public static List<Todo> getTrendActivity(int userId, String startDt){
        List<Todo> todos = new ArrayList<>();
        java.util.Date dt = Calendar.getInstance().getTime();
        TrendsActivity td = new TrendsActivity();
        int i=0;
        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select  title,  startdate, SUM(duration) duration from todo_activity where userId =" + userId + "  group by title,  startdate;");
            while (resultSet.next()){
                Todo newTodo = new Todo();

                newTodo.setTitle(resultSet.getString(1));
                newTodo.setStartdate(resultSet.getDate(2));
                newTodo.setDuration(resultSet.getInt(3));
                todos.add(newTodo);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String strDate = dateFormat.format(newTodo.getStartdate());
                Log.d("TODO", "getTrendActivity: Date convrted to String"+strDate);
                // check input date with start date
                if (startDt.equals(strDate)) {
                    //TrendsActivity.setxyData(i,,resultSet.getString(1));
                    Log.d("getTrendActivity", "getTrendActivity: "+newTodo.getTitle());
                    Log.d("getTrendActivity", "getTrendActivity: "+newTodo.getDuration());
                    Log.d("TODO", "getTrendActivity: returning data for "+strDate+" and "+startDt);
                }
                else {
                    todos.remove(newTodo);
                }
            }
            return todos;
        } catch (SQLException  e) {
            e.printStackTrace();
            return null;
        }
    }
    public static int addTodo(Todo todo){
        try {
            String query = "" +
                    "insert into todo_activity( userId, title, description, location, startdate, duration, starttime, status)"
                    + " values (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setInt (1, todo.getUserId());
            preparedStmt.setString (2, todo.getTitle());
            preparedStmt.setString   (3, todo.getDescription());
            preparedStmt.setString(4, todo.getLocation());
            preparedStmt.setDate (5, new java.sql.Date(todo.getStartdate().getTime()));
            preparedStmt.setInt (6, todo.getDuration());
            preparedStmt.setTime(7, todo.getStarttime());
            preparedStmt.setString(8, String.valueOf(todo.getStatus()));

            int rows = preparedStmt.executeUpdate();

            if(rows != 0){
                ResultSet generatedKeys = preparedStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = (int)generatedKeys.getLong(1);
                    return id;
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void deleteTodo(int todoId){
        try {
            String query = "" +
                    "delete from todo_activity where todoId = " + todoId +";";

            Statement stmt = connection.createStatement();

            stmt.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

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

    //constant for core of the sql statement to retrieve items
    private static final String GET_ITEMS_BASE = "select todoId, userId, title, description, startdate, status from todo_activity";

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        TodoDAO.connection = connection;
    }

    public TodoDAO() {
    }

    //get all todo items in the database regardless of user
    public static List<Todo> getAllToDoItems(){
        List<Todo> todos = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(GET_ITEMS_BASE +";");
            while (resultSet.next()){
                Todo newTodo = new Todo();

                setCoreTodo(newTodo, resultSet);

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

    //get todo items in database associated with a particular user
    public static List<Todo> getAllToDoItemsForUser(int userId){
        List<Todo> todos = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(GET_ITEMS_BASE + " where userId =" + userId + ";");
            while (resultSet.next()){
                Todo newTodo = new Todo();

                setCoreTodo(newTodo, resultSet);

                String status = resultSet.getString(6);
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

    public static List<Todo> getAllActiveToDoItemsForUser(int userId){
        List<Todo> todos = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(GET_ITEMS_BASE + " where deleted = 'FALSE' and userId =" + userId + ";");
            while (resultSet.next()){
                Todo newTodo = new Todo();

                setCoreTodo(newTodo, resultSet);

                String status = resultSet.getString(6);
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
  
  //set the core parameters for a todo object
    private static void setCoreTodo(Todo newTodo, ResultSet resultSet) throws SQLException{
        newTodo.setTodoId(resultSet.getInt(1));
        newTodo.setUserId(resultSet.getInt(2));
        newTodo.setTitle(resultSet.getString(3));
        newTodo.setDescription(resultSet.getString(4));
        newTodo.setStartdate(resultSet.getDate(5));
    }

    public static String getItemName(int todoId, int userId){
        String title = "";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select title from from todo_activity where todoId =" + todoId + " and userId=" + userId + ";");
            while (resultSet.next()){
                title = resultSet.getString("title");
            }
            return title;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int addTodo(Todo todo){
        try {
            String query = "" +
                    "insert into todo_activity( userId, title, description, startdate, status)"
                    + " values (?, ?, ?, ?, ?)";

            PreparedStatement preparedStmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setInt (1, todo.getUserId());
            preparedStmt.setString (2, todo.getTitle());
            preparedStmt.setString   (3, todo.getDescription());
            preparedStmt.setDate (4, new java.sql.Date(todo.getStartdate().getTime()));
            preparedStmt.setString(5, String.valueOf(todo.getStatus()));

            int rows = preparedStmt.executeUpdate();

            if(rows != 0){
                ResultSet generatedKeys = preparedStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return (int) generatedKeys.getLong(1);
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int updateTodo(Todo todo){
        try {
            String query =
                    "update todo_activity set title = ? , description = ?, startdate = ?, status = ? where todoId = ?;";

            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString (1, todo.getTitle());
            preparedStmt.setString (2, todo.getDescription());
            preparedStmt.setDate (3, new java.sql.Date(todo.getStartdate().getTime()));
            preparedStmt.setString (4, String.valueOf(todo.getStatus()));
            preparedStmt.setInt (5, todo.getTodoId());
            preparedStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void deleteTodo(int todoId){
        try {
            String query = "" +
                    "update todo_activity set deleted = 'TRUE' where todoId = " + todoId +";";

            Statement stmt = connection.createStatement();

            stmt.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

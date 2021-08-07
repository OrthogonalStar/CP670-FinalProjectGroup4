package com.example.cp670_finalprojectgroup4.data.dao;

import com.example.cp670_finalprojectgroup4.data.model.TimerModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TimerDAO {

    private static Connection connection;


    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        TimerDAO.connection = connection;
    }

    public TimerDAO() {
    }

    public static List<TimerModel> getAllTimers(){
        List<TimerModel> timers = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from todo_timer" +";");
            while (resultSet.next()){
                TimerModel newTimer = new TimerModel();

                newTimer.setTimerId(resultSet.getInt(1));
                newTimer.setUserId(resultSet.getInt(2));
                newTimer.setTodoId(resultSet.getInt(3));
                newTimer.setStartTime(resultSet.getDate(4));
                newTimer.setEndTime(resultSet.getDate(4));
            }
            return timers;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<TimerModel> getAllTimersForUser(int userId){
        List<TimerModel> timers = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from todo_timer where userId = " + userId + ";");
            while (resultSet.next()){
                TimerModel newTimer = new TimerModel();

                newTimer.setTimerId(resultSet.getInt(1));
                newTimer.setUserId(resultSet.getInt(2));
                newTimer.setTodoId(resultSet.getInt(3));
                newTimer.setStartTime(resultSet.getDate(4));
                newTimer.setEndTime(resultSet.getDate(5));
            }
            return timers;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int addTimer(TimerModel timer){
        try {
            String query = "" +
                    "insert into todo_timer( userId, todoId, startTime, endTime)"
                    + " values (?, ?, ?, ?)";

            PreparedStatement preparedStmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setInt (1, timer.getUserId());
            preparedStmt.setInt (2, timer.getTodoId());
            preparedStmt.setDate   (3, new java.sql.Date(timer.getStartTime().getTime()));
            preparedStmt.setDate   (4, new java.sql.Date(timer.getEndTime().getTime()));
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
}

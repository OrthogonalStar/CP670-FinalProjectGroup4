package com.example.cp670_finalprojectgroup4.data.dao;

import com.example.cp670_finalprojectgroup4.data.model.UserModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private static Connection connection;

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        UserDAO.connection = connection;
    }

    public UserDAO() {
    }

    public UserDAO(Connection connection) {
        UserDAO.connection = connection;
    }

    public static Boolean userExists(String email){
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select count(*) from Users where email ='" + email + "';");
            while (resultSet.next()){
                if (resultSet.getInt(1) == 1)
                        return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static UserModel getUserByEmail(String email){
        UserModel newUser = null;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select * from Users where email ='" + email + "';");
            while (resultSet.next()){
                newUser = new UserModel();
                newUser.setName(resultSet.getString(1));
                newUser.setEmail(resultSet.getString(2));
                newUser.setSalt(resultSet.getString(3));
                newUser.setPassword(resultSet.getString(4));
                newUser.setId(resultSet.getInt(5));
            }
            return newUser;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void addUser(UserModel userModel){
        try {
            String query = "" +
                    "insert into users (name, email, salt, password)"
                    + " values (?, ?, ?, ?)";

            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString (1, userModel.getName());
            preparedStmt.setString (2, userModel.getEmail());
            preparedStmt.setString   (3, userModel.getSalt());
            preparedStmt.setString(4, userModel.getPassword());

            preparedStmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<UserModel> getAllUsers(){
        List<UserModel> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select * from Users;");
            while (resultSet.next()){
                UserModel newUser = new UserModel();
                newUser.setName(resultSet.getString(1));
                newUser.setEmail(resultSet.getString(2));
                newUser.setSalt(resultSet.getString(3));
                newUser.setPassword(resultSet.getString(4));
                newUser.setId(resultSet.getInt(5));
                users.add(newUser);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}

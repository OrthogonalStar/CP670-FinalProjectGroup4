package com.example.cp670_finalprojectgroup4.data.connector;

import android.os.StrictMode;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseAccessConnector {

    private static final String LOG = "DEBUG";
    public static Connection connection;

    public static Connection connect() {
        Connection conn = null;
        String ip = "34.136.221.1";
        String port = "1433";
        String ConnURL = "jdbc:jtds:sqlserver://" + ip +":"+port+";"
                + "databaseName=cp670;user=avi;password=avi;";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String password = "avi";
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException e) {
            Log.d(LOG, e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.d(LOG, e.getMessage());
        }
        connection = conn;
        return connection;
    }

    public static void closeConnection() throws SQLException {
        connect().close();
    }
}

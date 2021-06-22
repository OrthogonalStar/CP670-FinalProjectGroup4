package com.example.cp670_finalprojectgroup4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.cp670_finalprojectgroup4.data.connector.DatabaseAccessConnector;
import com.example.cp670_finalprojectgroup4.data.dao.UserDAO;
import com.example.cp670_finalprojectgroup4.data.service.UserService;

public class LoginActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "LoginActivity";
    Button btnlogin, registerLogin;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findResources();

        DatabaseAccessConnector.connect();
        UserService userService = new UserService(new UserDAO(DatabaseAccessConnector.connection));

        View.OnClickListener loginListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserService.verifyUserByPassword(String.valueOf(username.getText()),String.valueOf(password.getText()))){
                    Intent intent = new Intent(new Intent(LoginActivity.this, MainActivity.class));
                    startActivity(intent);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Login Failed")
                            .setTitle("LOGIN")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            }).show();
                }
            }
        };

        btnlogin.setOnClickListener(loginListener);

        View.OnClickListener registerListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(new Intent(LoginActivity.this, RegisterUserActivity.class));
                startActivity(intent);
            }
        };

        registerLogin.setOnClickListener(registerListener);

    }

    private void findResources(){
        btnlogin = findViewById(R.id.btnLogin);
        username = findViewById(R.id.editUsername);
        password = findViewById(R.id.editPassword);
        registerLogin = findViewById(R.id.registerLogin);
    }



    @Override
    protected void onResume() {
        super.onResume();
        username.setText("");
        password.setText("");
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

}
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
import com.example.cp670_finalprojectgroup4.data.model.UserModel;
import com.example.cp670_finalprojectgroup4.data.service.UserService;

public class RegisterUserActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "RegisterUserActivity";

    private EditText username, email, password1, password2;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        findResources();

        View.OnClickListener loginListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!String.valueOf(password1.getText()).equals(String.valueOf(password2.getText()))){
                    password1.setText("");
                    password2.setText("");
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
                    builder.setMessage("Register Failed, passwords must be the same")
                            .setTitle("REGISTER")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            }).show();
                } else if (UserService.getUserByEmail(String.valueOf(email.getText())) != null){
                    password1.setText("");
                    password2.setText("");
                    email.setText("");
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
                    builder.setMessage("Register Failed, email already exists")
                            .setTitle("REGISTER")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            }).show();
                } else {
                    UserModel newUser = new UserModel(String.valueOf(username.getText()),
                            String.valueOf(email.getText()), "", String.valueOf(password1.getText()));
                    UserService.addUser(newUser);
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
                    builder.setMessage("Register Success! Please Log in")
                            .setTitle("REGISTER")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            }).show();
                }

            }
        };

        register.setOnClickListener(loginListener);

    }

    private void findResources(){
        username = findViewById(R.id.registerName);
        email = findViewById(R.id.registerEmail);
        password1 = findViewById(R.id.registerPassword);
        password2 = findViewById(R.id.registerPassword2);
        register = findViewById(R.id.registerButton);
    }

    @Override
    protected void onResume() {
        Log.i(ACTIVITY_NAME, "In onResume()");
        super.onResume();
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
    protected void onStop() {
        Log.i(ACTIVITY_NAME, "In onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(ACTIVITY_NAME, "In onDestroy()");
        super.onDestroy();
    }
}
package com.example.cp670_finalprojectgroup4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cp670_finalprojectgroup4.data.connector.DatabaseAccessConnector;
import com.example.cp670_finalprojectgroup4.data.dao.UserDAO;
import com.example.cp670_finalprojectgroup4.data.model.UserData;
import com.example.cp670_finalprojectgroup4.data.model.UserModel;
import com.example.cp670_finalprojectgroup4.data.service.UserService;

public class LoginActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "LoginActivity";
    Button btnlogin, registerLogin;
    EditText username, password;
    private CheckBox rememberMe;
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "PrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPrefs = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        findResources();
        loadLogin();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());

        //Spring Framework would help here, dependency management like this is stupid
        UserDAO.setConnection(DatabaseAccessConnector.getConnection());
        UserService userService = new UserService(new UserDAO());

        View.OnClickListener loginListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserService.verifyUserByPassword(String.valueOf(username.getText()),String.valueOf(password.getText()))){

                    UserModel user = UserDAO.getUserByEmail(String.valueOf(username.getText()));

                    assert user != null;
                    ((CurrentUser) getApplication()).setUser(user);

                    if (rememberMe.isChecked()){
                        Boolean boolIsChecked = rememberMe.isChecked();
                        SharedPreferences.Editor editor = mPrefs.edit();
                        editor.putString("pref_name",username.getText().toString());
                        editor.putString("pref_pass",password.getText().toString());
                        editor.putBoolean("pref_rememberMe",boolIsChecked);
                        editor.commit();
                        Toast.makeText(getApplicationContext(),"Login is saved!",Toast.LENGTH_SHORT).show();

                    }else
                        mPrefs.edit().clear().commit();

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
        rememberMe = findViewById(R.id.remindMe_cb);
    }

    private void loadLogin(){
        if(mPrefs.getBoolean("pref_rememberMe", false)){
            username.setText(mPrefs.getString("pref_name", ""));
            password.setText(mPrefs.getString("pref_pass", ""));
            rememberMe.setChecked(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!mPrefs.getBoolean("pref_rememberMe", false)) {
            username.setText("");
            password.setText("");
        }
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
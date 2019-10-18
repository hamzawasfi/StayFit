package com.example.myapplication.Activities.Sign;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.Activities.Home.Home;
import com.example.myapplication.DataBase.users.UsersDataBase;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Utils;

public class Login extends AppCompatActivity {

    private UsersDataBase usersDB;

    public static Activity loginActivity;

    private TextView signup, warning;
    private Button login;
    private EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginActivity = this;

        signup = (TextView)findViewById(R.id.no_accountbtn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignup();
            }
        });

        username = (EditText)findViewById(R.id.username);
        username.setText(Utils.getUsername());

        password = (EditText)findViewById(R.id.password);

        warning = (TextView)findViewById(R.id.warning);

        usersDB = new UsersDataBase(this);

        login = (Button)findViewById(R.id.loginbtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkInputs()){
                    password.setText("");
                    return;
                }
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        password.setText("");
        username.setText("");
    }

    private void goToSignup() {
        Intent signupIntent = new Intent(this, SignUp.class);
        startActivity(signupIntent);
    }

    public void goToHome(){
        if(SignUp.signupActivity != null) {
            SignUp.signupActivity.finish();
        }
        finish();
        Intent homeIntent = new Intent(this, Home.class);
        startActivity(homeIntent);
    }

    private boolean checkInputs(){
        String usernameInput = username.getText().toString();
        String passwordInput = password.getText().toString();

        if(usernameInput.length() < 4 || passwordInput.length() < 8){
            warning.setText(R.string.blankInputs);
            return false;
        } else if(usersDB.match(usernameInput, passwordInput)){
            Utils.setUsername(usernameInput);
            goToHome();
            return true;
        }else{
            warning.setText(R.string.wrongInput);
            return false;
        }
    }
}

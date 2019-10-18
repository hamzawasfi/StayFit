package com.example.myapplication.Activities.Sign;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.myapplication.DataBase.users.UsersDataBase;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Utils;

public class SignUp extends AppCompatActivity {

    private UsersDataBase usersDB;

    public static Activity signupActivity;

    private Button signup;
    private EditText username, password, age, weight, height;
    private RadioGroup sex, goal;
    private TextView warning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signupActivity = this;

        signup = (Button)findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkInputs()){
                    username.setText("");
                    password.setText("");
                    return;
                }
            }
        });

        username = (EditText)findViewById(R.id.username);

        password = (EditText)findViewById(R.id.password);

        age = (EditText)findViewById(R.id.age);
        age.setInputType(InputType.TYPE_CLASS_NUMBER);

        weight = (EditText)findViewById(R.id.weight);
        weight.setInputType(InputType.TYPE_CLASS_NUMBER);

        height = (EditText)findViewById(R.id.height);
        height.setInputType(InputType.TYPE_CLASS_NUMBER);

        sex = (RadioGroup)findViewById(R.id.sex_container);

        goal = (RadioGroup)findViewById(R.id.goal);

        warning = (TextView)findViewById(R.id.warning_signup);

        usersDB = new UsersDataBase(this);

    }

    public boolean checkInputs() {
        String usernameInput = username.getText().toString();
        String passwordInput = password.getText().toString();
        int ageInput = 0;
        int weightInput = 0;
        int heightInput = 0;
        int sexInput = 0;
        int goalInput = 0;

        try{
            ageInput = Integer.parseInt(age.getText().toString());
        }catch (Exception e){
            ageInput = 0;
        }
        try{
            weightInput = Integer.parseInt(weight.getText().toString());
        }catch (Exception e){
            ageInput = 0;
        }
        try{
            heightInput = Integer.parseInt(height.getText().toString());
        }catch (Exception e){
            ageInput = 0;
        }

        RadioButton sexbtn = (RadioButton) findViewById(sex.getCheckedRadioButtonId());
        try{
            sexInput = sexbtn.getId();
        }catch (Exception e){
            sexInput = 0;
        }

        RadioButton goalbtn = (RadioButton) findViewById(goal.getCheckedRadioButtonId());
        try{
            goalInput = goalbtn.getId();
        }catch (Exception e){
            goalInput = 0;
        }

        if (ageInput == 0 || weightInput == 0 || heightInput == 0 || sexInput == 0 || goalInput == 0) {
            warning.setText(R.string.blankInput);
            return false;
        } else if (passwordInput.length() < 8) {
            warning.setText(R.string.shortPassword);
            return false;
        } else if (usernameInput.length() < 4) {
            warning.setText(R.string.shortUsername);
            return false;
        }else if(usernameInput.contains(" ")){
            warning.setText(R.string.username_taken);
            return false;
        }else if(usersDB.usernameExist(usernameInput)){
            warning.setText(R.string.usernameExist);
            return false;
        }else {
            int calories = calculateCalories(ageInput, weightInput, heightInput, sexInput, goalInput);
            usersDB.insertData(usernameInput, passwordInput, calories);
            Utils.setUsername(usernameInput);
            goToLogin();
            return true;
        }
    }

    public void goToLogin(){
        Login.loginActivity.finish();
        finish();
        Intent loginIntent = new Intent(this, Login.class);
        startActivity(loginIntent);
    }

    public int calculateCalories(int age, int weight, int height, int sex, int goal){
        double estimatedCalories = 0;
        double BMR = 0;
        if(sex == R.id.malebtn){
            BMR = 10 * weight + 6.25 * height - 5 * age + 5;
        }else if(sex == R.id.femalebtn){
            BMR = 10 * weight + 6.25 * height - 5 * age - 161;
        }
        if(goal == R.id.lossbtn){
            estimatedCalories = BMR + 250;
        }else if(goal == R.id.maintainbtn){
            estimatedCalories = BMR + 500;
        }else if(goal == R.id.gainbtn){
            estimatedCalories = BMR + 750;
        }
        return (int)estimatedCalories;
    }
}

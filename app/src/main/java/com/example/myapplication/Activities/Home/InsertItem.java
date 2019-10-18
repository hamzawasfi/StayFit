package com.example.myapplication.Activities.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.DataBase.users.FoodDataBase;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Utils;

public class InsertItem extends AppCompatActivity {

    private EditText foodName, foodCalories;
    private TextView warning;
    private Button add;

    private FoodDataBase foodDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_item);

        foodName = (EditText)findViewById(R.id.food_name);

        foodCalories = (EditText)(findViewById(R.id.calories));
        foodCalories.setInputType(InputType.TYPE_CLASS_NUMBER);

        warning = (TextView)findViewById(R.id.food_warning);

        add = (Button)findViewById(R.id.addbtn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkInputs()){
                    return;
                }else {
                    String foodNameInput = foodName.getText().toString();
                    Utils.setNewItemName(foodNameInput);
                    Utils.setInserting(true);
                    finish();
                }
            }
        });

        foodDB = new FoodDataBase(this);

        Utils.setClickedItemName("");
    }

    public boolean checkInputs(){
        String foodNameInput = foodName.getText().toString();
        int foodCaloriesInput = 0;

        try {
            foodCaloriesInput = Integer.parseInt(foodCalories.getText().toString());
        }catch (Exception e){
            foodCaloriesInput = 0;
        }

        if(foodNameInput.length() == 0 || foodCaloriesInput == 0){
            warning.setText(R.string.enter_inputs);
            return false;
        }else {
            foodDB.insertData(foodNameInput, foodCaloriesInput);
            return true;
        }
    }
}

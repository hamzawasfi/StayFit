package com.example.myapplication.Activities.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.DataBase.users.DaysDataBase;
import com.example.myapplication.DataBase.users.FoodDataBase;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Utils;

import java.util.ArrayList;

public class FoodInfo extends AppCompatActivity {

    private TextView title, calories;
    private EditText weight;
    private Button done;

    private DaysDataBase daysDB;
    private FoodDataBase foodDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_info);

        daysDB = new DaysDataBase(this);
        foodDB = new FoodDataBase(this);

        final String[] foodName = Utils.getSelectedItemContent().split(" ");
        Cursor data = daysDB.getAddedData(foodName[0], Utils.getUsername(), Utils.getDate());

        title = (TextView)findViewById(R.id.titletxt);
        title.setText(foodName[0]);

        final String[] foodCal = foodName[4].split("c");
        calories = (TextView)findViewById(R.id.caloriestxt1);
        calories.setText(foodCal[0]);

        final String foodWeight = getFoodWeight(foodName[0], Utils.getUsername(), Utils.getDate());
        weight = (EditText)findViewById(R.id.food_weighttxt);
        weight.setText(foodWeight);
        weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        done = (Button)findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double newWeight = Integer.parseInt(weight.getText().toString());
                double newCalories = foodDB.getFoodCalories(foodName[0]) / 100 * newWeight;

                daysDB.deleteRow(Utils.getUsername(), foodName[0], Utils.getDate());
                removeAddedItem(Utils.getAddedItems(), Utils.getSelectedItemContent());
                boolean d = daysDB.insertData(Utils.getUsername(), foodDB.getStringFoodID(foodName[0]), foodName[0], newCalories, (int)newWeight, Utils.getDate());
                if(!d){
                    Toast.makeText(getApplicationContext(), "food info.insertdata", Toast.LENGTH_SHORT).show();
                }
                Utils.setCalories(newCalories);
                Utils.setEditing(true);
                finish();
            }
        });
    }

    public String getFoodWeight(String foodName, String username, String date) {
        Cursor data = daysDB.getAddedData(foodName, username, date);
        String weight = "";
        if (data.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "display added item", Toast.LENGTH_SHORT).show();
        } else {
            while (data.moveToNext()) {
                weight = data.getString(4);
            }
        }
        return weight;
    }

    public ArrayList<String> removeAddedItem(ArrayList<String> list, String content){
        final String[] foodName = Utils.getSelectedItemContent().split(" ");
        daysDB.deleteRow(Utils.getUsername(), foodDB.getStringFoodID(foodName[0]), Utils.getDate());
        for(int i = 0; i < list.size(); i++) {
            if (list.get(i) == content) {
                list.remove(i);
            }
        }
        Utils.setAddedItems(list);
        return list;
    }
}

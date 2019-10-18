package com.example.myapplication.Activities.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.DataBase.users.DaysDataBase;
import com.example.myapplication.DataBase.users.FoodDataBase;
import com.example.myapplication.DataBase.users.UsersDataBase;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Utils;

public class AddItem extends AppCompatActivity {

    private TextView itemName, warning;
    private EditText itemWeight;
    private Button addItem;

    private FoodDataBase foodDB;
    private DaysDataBase daysDB;
    private UsersDataBase userDB;

    String[] title = Utils.getClickedItemName().split(" ");

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        foodDB = new FoodDataBase(this);
        daysDB = new DaysDataBase(this);
        userDB = new UsersDataBase(this);

        warning = (TextView)findViewById(R.id.warning_add);

        itemName = (TextView)findViewById(R.id.item_name);
        itemName.setText(title[0]);

        itemWeight = (EditText)findViewById(R.id.item_weight);
        itemWeight.setInputType(InputType.TYPE_CLASS_NUMBER);

        addItem = (Button)findViewById(R.id.add_item);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputs()) {
                    double calories = foodDB.getFoodCalories(Utils.getClickedItemName());
                    double weight = Integer.parseInt(itemWeight.getText().toString());
                    double foodCalories = calories / 100 * weight;

                    boolean d = daysDB.insertData(Utils.getUsername(), foodDB.getStringFoodID(title[0]), title[0], foodCalories, (int)weight, Utils.getDate());
                    if(!d){
                        Toast.makeText(getApplicationContext(), "add item.insertdata", Toast.LENGTH_SHORT).show();
                    }

                    Utils.setCalories(foodCalories);
                    Utils.setAdding(true);
                    finish();
                }else{
                    return;
                }
            }
        });
    }

    private boolean checkInputs(){
        String weightInput = itemWeight.getText().toString();
        if(weightInput.length() == 0){
            warning.setText(R.string.blankInput);
            return false;
        }else
            return true;
    }
}

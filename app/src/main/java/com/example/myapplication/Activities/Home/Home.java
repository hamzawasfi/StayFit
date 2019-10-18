package com.example.myapplication.Activities.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.DataBase.users.DaysDataBase;
import com.example.myapplication.DataBase.users.FoodDataBase;
import com.example.myapplication.DataBase.users.UsersDataBase;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Home extends AppCompatActivity {

    //lists
    private ArrayList<String> listItems;
    private ArrayList<String> addedItems;
    private ArrayAdapter listAdapter;
    //

    //objects
    private FoodDataBase  foodDB;
    private DaysDataBase dayDB;
    private UsersDataBase userDB;
    //

    //strings
    private String username;
    private String clickedItemName;
    private String goalCalories;
    private String currentDate;
    //

    private SimpleDateFormat date;

    //doubles
    private double totalCalories;
    private double caloriePrecentage;
    //

    //design objects
    private ImageView searchbtn, deletebtn, editbtn;
    private EditText searchContent;
    private Button nextDay, previousDay, addFood;
    private ProgressBar calorieProgress;
    private TextView calorieCount;
    private ListView itemsList, addedList;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //variable intializations
        dayDB = new DaysDataBase(this);
        userDB = new UsersDataBase(this);
        foodDB = new FoodDataBase(this);

        username = Utils.getUsername();
        goalCalories = userDB.getCalories(Utils.getUsername());

        listItems = new ArrayList<String>();
        addedItems = new ArrayList<String>();

        date = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = date.format(new Date());
        Utils.setDate(currentDate);
        //

        //UI casting
        deletebtn = (ImageView)findViewById(R.id.delete_item);
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAddedItemsAfterRemoval(removeAddedItem(Utils.getAddedItems(), Utils.getSelectedItemContent()));
                deletebtn.setVisibility(View.INVISIBLE);
                editbtn.setVisibility(View.INVISIBLE);
            }
        });

        addedList = (ListView)findViewById(R.id.added_list);
        addedList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Utils.setSelectedItemContent(addedList.getItemAtPosition(position).toString());
                Toast.makeText(getApplicationContext(), Utils.getSelectedItemContent(), Toast.LENGTH_SHORT).show();
                deletebtn.setVisibility(View.VISIBLE);
                editbtn.setVisibility(View.VISIBLE);
                return false;
            }
        });

        searchbtn = (ImageView) findViewById(R.id.searchbtn);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        editbtn = (ImageView)findViewById(R.id.edit_item);
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFoodInfo();
                editbtn.setVisibility(View.INVISIBLE);
                deletebtn.setVisibility(View.INVISIBLE);
            }
        });

        searchContent = (EditText)findViewById(R.id.search_content);

        addFood = (Button)findViewById(R.id.add_foodbtn);
        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInsertItem();
            }
        });

        nextDay = (Button)findViewById(R.id.next_daybtn);
        nextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextDay();
            }
        });

        previousDay = (Button)findViewById(R.id.previous_daybtn);
        previousDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPreviousDay();
            }
        });

        calorieCount = (TextView)findViewById(R.id.estimated_caloriestxt);
        calorieCount.setText(goalCalories);

        calorieProgress = (ProgressBar)findViewById(R.id.calorie_progress_bar);

        itemsList = (ListView)findViewById(R.id.food_list);
        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickedItemName = itemsList.getItemAtPosition(position).toString();
                String[] itemName = clickedItemName.split(" ");
                Utils.setClickedItemName(itemName[0]);
                openAddItem();
            }
        });

        searchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (Home.this).listAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //

        //functions onStart
        displayGlobalItems(listItems);
        displayUserInsertedItems(listItems, username);
        displayUserAddedItems(username, currentDate);
        //
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if(Utils.isDateChanged()){
            displayUserAddedItems(username, currentDate);
            Utils.setDateChanged(false);
        }

        if (Utils.isAdding()) {
            displayAddedItem(Utils.getAddedItems(), username, currentDate);
            Utils.setAdding(false);
        }else if(Utils.isInserting()){
            displayInsertedItems(Utils.getListItems(), Utils.getNewItemName(), username);
            Utils.setInserting(false);
        }else if(Utils.isEditing()){
            displayEditedItem(Utils.getAddedItems(), username, currentDate);
            Utils.setEditing(false);
        }

        displayAddedItemsAfterRemoval(Utils.getAddedItems());
    }

    //onCreate

    public void displayGlobalItems(ArrayList<String> list){
        Cursor data = foodDB.getGlobalData();

        if(data.getCount() == 0){
            Toast.makeText(Home.this, "display global items", Toast.LENGTH_SHORT).show();
        }else {
            while (data.moveToNext()){
                list.add(data.getString(1) + "    " + data.getString(2) + "cal/100gm.");
                listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
                itemsList.setAdapter(listAdapter);
            }
        }
        Utils.setListItems(list);
    }


    public void displayUserInsertedItems(ArrayList<String> list, String itemName){
        Cursor data = foodDB.getUserData(itemName);

        if(data.getCount() == 0){
            Toast.makeText(Home.this, "display user inserted items", Toast.LENGTH_SHORT).show();
        }else {
            while (data.moveToNext()){
                list.add(data.getString(1) + "    " + data.getString(2) + "cal/100gm.");
                listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
                itemsList.setAdapter(listAdapter);
            }
        }
        Utils.setListItems(list);
    }

    public void displayUserAddedItems(String username, String date) {
        Cursor data = dayDB.getAddData(username, date);
        ArrayList <String> list = new ArrayList<String>();
        if (data.getCount() == 0) {
            Toast.makeText(Home.this, "display user added items", Toast.LENGTH_SHORT).show();
            addedList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list));
        } else {
            while (data.moveToNext()) {
                list.add(data.getString(2) + "    " + data.getString(3) + "cal.");
                ArrayAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
                addedList.setAdapter(listAdapter);
            }
            Utils.setAddedItems(list);
        }
    }

    public void displayAddedItems(String username, String date) {
        Cursor data = dayDB.getAddData(username, date);
        ArrayList <String> list = new ArrayList<String>();
        if (data.getCount() == 0) {
            Toast.makeText(Home.this, "display user added items", Toast.LENGTH_SHORT).show();
        } else {
            while (data.moveToNext()) {
                list.add(data.getString(2) + "    " + data.getString(3) + "cal.");
            }
            ArrayAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            addedList.setAdapter(listAdapter);
            Utils.setAddedItems(list);
        }
    }

    //

    //on resume

    public void displayAddedItem(ArrayList<String> list, String username, String date) {
        String[] title = Utils.getClickedItemName().split(" ");
        Cursor data = dayDB.getAddedData(title[0], username, date);
        if (data.getCount() == 0) {
            Toast.makeText(Home.this, "display added item", Toast.LENGTH_SHORT).show();
            addedList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list));
        } else {
            while (data.moveToNext()) {
                list.add(data.getString(2) + "    " + data.getString(3) + "cal.");
                listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
                addedList.setAdapter(listAdapter);
            }
            Utils.setAddedItems(list);
        }
    }

    public void displayEditedItem(ArrayList<String> list, String username, String date) {
        String[] title = Utils.getSelectedItemContent().split(" ");
        Cursor data = dayDB.getAddedData(title[0], username, date);
        if (data.getCount() == 0) {
            Toast.makeText(Home.this, "display added item", Toast.LENGTH_SHORT).show();
            addedList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list));
        } else {
            while (data.moveToNext()) {
                list.add(data.getString(2) + "    " + data.getString(3) + "cal.");
                listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
                addedList.setAdapter(listAdapter);
            }
            Utils.setAddedItems(list);
        }
    }

    public void displayInsertedItems(ArrayList<String> list, String foodID, String username){
        Cursor data = foodDB.getInsertedData(foodID, username);
        if(data.getCount() == 0){
            Toast.makeText(getApplicationContext(), "display inserted items", Toast.LENGTH_SHORT).show();
        }else {
            while (data.moveToNext()){
                list.add(data.getString(1) + "    " + data.getString(2) + "cal/100gm.");
                listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
                itemsList.setAdapter(listAdapter);
            }
        }
        Utils.setListItems(list);
    }

    public int DisplayTotalCalories(String date, String username){
        int total = 0;
        Cursor cursor = dayDB.getAddData(username, date);
        if(cursor.getCount() == 0){
            Toast.makeText(getApplicationContext(), "diplay total calories", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()) {
                total += Integer.parseInt(cursor.getString(3));
            }
        }
        return total;
    }

    //

    //edit and remove

    public ArrayList<String> removeAddedItem(ArrayList<String> list, String content){
        String[] title = Utils.getSelectedItemContent().split(" ");
        dayDB.deleteRow(username, title[0], Utils.getDate());
        for(int i = 0; i < list.size(); i++) {
            if (list.get(i) == content) {
                list.remove(i);
            }
        }
        return list;
    }

    public void displayAddedItemsAfterRemoval(ArrayList<String> listItems){
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        addedList.setAdapter(listAdapter);
        Utils.setAddedItems(listItems);

        totalCalories = DisplayTotalCalories(currentDate, username);
        caloriePrecentage = progress(totalCalories);
        calorieProgress.setProgress((int)caloriePrecentage);
    }

    //

    public double progress(double currentCalories){
        double userCalories = Integer.parseInt(goalCalories);
        double progress = (currentCalories / userCalories) * 100;
        if(progress > 100){
            progress = 100;
        }
        return progress;
    }

    //intents

    public void openFoodInfo(){
        Intent foodInfoIntent = new Intent(this, FoodInfo.class);
        startActivity(foodInfoIntent);
    }

    public void openInsertItem(){
        Intent insertItemIntent = new Intent(this, InsertItem.class);
        startActivity(insertItemIntent);
    }

    public void openAddItem(){
        Intent addItemIntent = new Intent(this, AddItem.class);
        startActivity(addItemIntent);
    }

    public void goToNextDay(){

        Utils.setDateChanged(true);
        Utils.setAddedItems(new ArrayList<String>());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(currentDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 1);
        currentDate = sdf.format(c.getTime());
        Toast.makeText(getApplicationContext(), currentDate, Toast.LENGTH_SHORT).show();

        Utils.setDate(currentDate);
        onPostResume();
    }

    public void goToPreviousDay(){

        Utils.setDateChanged(true);
        Utils.setAddedItems(new ArrayList<String>());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(currentDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, -1);  // number of days to add
        currentDate = sdf.format(c.getTime());  // dt is now the new date
        Toast.makeText(getApplicationContext(), currentDate, Toast.LENGTH_SHORT).show();

        Utils.setDate(currentDate);
        onPostResume();
    }

    //
}

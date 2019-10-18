package com.example.myapplication.Utils;

import java.util.ArrayList;

public class Utils {

    private static String clickedItemName;
    private static String addedClickedItem;
    private static String newItemName;
    private static String date;
    private static String username;
    private static String selectedItemContent;

    private static double calories;

    private static boolean adding;
    private static boolean inserting;
    private static boolean dateChanged;
    private static boolean editing;

    private static ArrayList<String> listItems;
    private static ArrayList<String> addedItems = new ArrayList<String>();

    public static String getClickedItemName() {
        return clickedItemName;
    }

    public static void setClickedItemName(String clickedItemName) {
        Utils.clickedItemName = clickedItemName;
    }

    public static ArrayList<String> getListItems() {
        return listItems;
    }

    public static void setListItems(ArrayList<String> listItems) {
        Utils.listItems = listItems;
    }


    public static double getCalories() {
        return calories;
    }

    public static void setCalories(double calories) {
        Utils.calories = calories;
    }

    public static boolean isAdding() {
        return adding;
    }

    public static void setAdding(boolean adding) {
        Utils.adding = adding;
    }

    public static String getNewItemName() {
        return newItemName;
    }

    public static void setNewItemName(String newItemName) {
        Utils.newItemName = newItemName;
    }

    public static boolean isInserting() {
        return inserting;
    }

    public static void setInserting(boolean inserting) {
        Utils.inserting = inserting;
    }

    public static ArrayList<String> getAddedItems() {
        return addedItems;
    }

    public static void setAddedItems(ArrayList<String> addedItems) {
        Utils.addedItems = addedItems;
    }

    public static String getDate() {
        return date;
    }

    public static void setDate(String date) {
        Utils.date = date;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Utils.username = username;
    }

    public static boolean isDateChanged() {
        return dateChanged;
    }

    public static void setDateChanged(boolean dateChanged) {
        Utils.dateChanged = dateChanged;
    }

    public static String getSelectedItemContent() {
        return selectedItemContent;
    }

    public static void setSelectedItemContent(String selectedItemContent) {
        Utils.selectedItemContent = selectedItemContent;
    }

    public static String getAddedClickedItem() {
        return addedClickedItem;
    }

    public static void setAddedClickedItem(String addedClickedItem) {
        Utils.addedClickedItem = addedClickedItem;
    }

    public static boolean isEditing() {
        return editing;
    }

    public static void setEditing(boolean editing) {
        Utils.editing = editing;
    }

}

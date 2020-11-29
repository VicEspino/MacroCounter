package com.example.macrocounter.UI.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HistorialItem {

    int calorieAmount;
    String date ;

    public HistorialItem(int calorieAmount, String date) {
        this.calorieAmount = calorieAmount;
        this.date = date;
    }

    public int getCalorieAmount() {
        return calorieAmount;
    }

    public String getDate() {
        return date;
    }

    public void setCalorieAmount(int calorie){

        calorieAmount=calorie;
    }

    public void setDate(String date){

        this.date=date;
    }
}

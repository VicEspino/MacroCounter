package com.example.macrocounter.UI.model;

import java.util.Date;

public class HistorialItem {

    long calorieAmount;
    Date date ;

    public HistorialItem(long calorieAmount,Date date) {
        this.calorieAmount = calorieAmount;
        this.date = date;
    }

    public long getCalorieAmount() {
        return calorieAmount;
    }

    public Date getDate() {
        return date;
    }
}

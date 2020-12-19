package com.example.macrocounter.UI.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HistorialItem {

    private int calorieAmount;
    private Date historialItemTime;
    public static SimpleDateFormat FORMAT_DATE_ITEM_HISTORIAL = new SimpleDateFormat("dd/MMMM/yyyy");

    public HistorialItem(int calorieAmount, Date date) {
        this.calorieAmount = calorieAmount;
        this.historialItemTime = date;
    }

    public int getCalorieAmount() {
        return calorieAmount;
    }

    public Date getDate() {
        return historialItemTime;
    }

    public void setCalorieAmount(int calorie){

        calorieAmount=calorie;
    }

    public void setDate(Date date){

        this.historialItemTime=date;
    }

    public Date getHistorialItemFullDate() {
        return historialItemTime;
    }

    public String getHistorialItemTime(){

        String time = FORMAT_DATE_ITEM_HISTORIAL.format(this.historialItemTime);

        return time;
    }
}

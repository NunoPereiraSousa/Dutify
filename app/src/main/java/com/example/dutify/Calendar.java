package com.example.dutify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Calendar extends AppCompatActivity {

    private TextView currentDateTxt;
    private TextView selectedMonthTxt;
    private String tokenToBeSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Intent receivedIntend = getIntent();
        Bundle intendExtras = receivedIntend.getExtras();
        if (intendExtras != null) {

            tokenToBeSent = intendExtras.getString("token");

            Log.d("Calendar token", tokenToBeSent);
        } else {
            Log.d("justTest", "An idea is being cooked");
        }

        setTimeAndDate();
    }

    private void setTimeAndDate() {
        String dayName = new SimpleDateFormat("EEEE", Locale.getDefault()).format(new Date()); // Get day of the week
        String MonthName = new SimpleDateFormat("MMMM", Locale.getDefault()).format(new Date()); // get full name month
        String year = new SimpleDateFormat("YYYY", Locale.getDefault()).format(new Date()); // get year
        String currentDay = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()); // get current day

        currentDateTxt = (TextView) findViewById(R.id.currentDateTxt);
        selectedMonthTxt = (TextView) findViewById(R.id.selectedMonthTxt);

        currentDay = setDayFormat(currentDay);
        String dayNameAndTime = dayName;
        String monthAndDay = MonthName.toLowerCase() + " " + currentDay;

        currentDateTxt.setText(dayNameAndTime + ", " + monthAndDay);
        selectedMonthTxt.setText(MonthName + ", " + year);
    }

    private String setDayFormat(String currentDay) {
        switch (currentDay) {
            case "01":
            case "31":
                currentDay += "st";
                break;
            case "02":
                currentDay += "nd";
                break;
            case "03":
                currentDay += "rd";
                break;
            default:
                currentDay += "th";
                break;
        }
        return currentDay;
    }
}
package com.example.dutify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

public class MotivationalSmsSection extends AppCompatActivity {

    private static int TIMEOUT = 3000;
    private String tokenToBeSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motivational_sms_section);

        Intent receivedIntend = getIntent();
        Bundle intendExtras = receivedIntend.getExtras();
        if (intendExtras != null) {

            tokenToBeSent = intendExtras.getString("token");

            Log.d("Motivation token", tokenToBeSent);
        } else {
            Log.d("justTest", "An idea is being cooked");
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent calendarIntent = new Intent(MotivationalSmsSection.this, Calendar.class);
                calendarIntent.putExtra("token", tokenToBeSent);
                startActivity(calendarIntent);
                finish();
            }
        }, TIMEOUT);
    }
}
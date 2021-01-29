package com.example.dutify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static int TIMEOUT = 2000;
    private String tokenToBeSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                Intent calendarIntent = new Intent(MainActivity.this, Login.class);
                calendarIntent.putExtra("token", tokenToBeSent);
                startActivity(calendarIntent);
                finish();
            }
        }, TIMEOUT);
    }
}
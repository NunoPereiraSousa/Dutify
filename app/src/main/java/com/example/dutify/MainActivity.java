package com.example.dutify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openLogin(View view){
        Intent i = new Intent(this, Login.class);
        startActivity(i);
    }

    public void openCalendar(View view){
        Intent i = new Intent(this, Calendar.class);
        startActivity(i);
    }


    public void openWelcome(View view){
        Intent i = new Intent(this, Welcome.class);
        startActivity(i);
    }


    public void openDashboard(View view){
        Intent i = new Intent(this, Dashboard.class);
        startActivity(i);
    }


    public void openDashboardStats(View view){
        Intent i = new Intent(this, DashboardBoard.class);
        startActivity(i);
    }


    public void openDashboardProject(View view){
        Intent i = new Intent(this, DashboardProjectDescription.class);
        startActivity(i);
    }


    public void openProfile(View view){
        Intent i = new Intent(this, Profile.class);
        startActivity(i);
    }
}
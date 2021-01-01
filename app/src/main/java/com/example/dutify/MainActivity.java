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

    public void welcome(View view) {
        Intent intent = new Intent(this, Welcome.class);
        startActivity(intent);
    }

    public void login(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void profile(View view) {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }
}
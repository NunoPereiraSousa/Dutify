package com.example.dutify;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Calendar extends AppCompatActivity {

    TextView addPersonalTaskBtnLabel;
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        definePopUpForm();
        addPersonalTaskBtnLabel= (TextView) findViewById(R.id.addPersonalTaskBtnLabel);
        addPersonalTaskBtnLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.show();
            }
        });
    }
    public void definePopUpForm(){
        myDialog= new Dialog(this);
        myDialog.setContentView(R.layout.fragment_personal_task_adding_form);
    }
}
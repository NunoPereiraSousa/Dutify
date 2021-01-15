package com.example.dutify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Welcome extends AppCompatActivity {
    TextView userNameTxt;
    TextView dayNameAndTimeTxt;
    TextView monthAndDayTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Intent receivedIntend = getIntent();
        Bundle intendExtras = receivedIntend.getExtras();
        if (intendExtras != null) {
            userNameTxt = (TextView) findViewById(R.id.userNameTxt);
            userNameTxt.setText(intendExtras.getString("userFullName"));
            setTimeAndDate();
            IdentificationByToken(intendExtras.getString("token"));
        }else{
            setTimeAndDate();
            Log.d("justTest","An idea is being cooked");
        }
    }

    //Function that sets the interface time and date displayed
    private void setTimeAndDate(){
        String dayName = new SimpleDateFormat("EEEE",Locale.getDefault()).format(new Date()); // Get day of the week name
        String MonthName = new SimpleDateFormat("MMM",Locale.getDefault()).format(new Date());// get month name
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());//current Time
        String currentDay = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());//current Day

        dayNameAndTimeTxt = (TextView) findViewById(R.id.dayNameAndTimeTxt);
        monthAndDayTxt = (TextView) findViewById(R.id.monthAndDayTxt);

        currentDay = setDayFormat(currentDay);
        currentTime += setTimeFormatSuffix();
        String dayNameAndTime = dayName+", "+ currentTime;
        String monthAndDay = MonthName + " "+ currentDay;
        dayNameAndTimeTxt.setText(dayNameAndTime);
        monthAndDayTxt.setText(monthAndDay);
    }
    private String setDayFormat( String currentDay){
        switch (currentDay) {
            case "01":
            case "31":
                currentDay += "st";
                break;
            case "02":
                currentDay  += "nd";
                break;
            case "03":
                currentDay += "rd";
                break;
            default:
                currentDay +=  "th";
                break;
        }
        return  currentDay;
    }
    private String setTimeFormatSuffix (){
        String suffix ="am";

        if (Integer.parseInt(new SimpleDateFormat("HH", Locale.getDefault()).format(new Date()))>=12){
            suffix ="pm";
        }
        return suffix;
    }

    private void getUserInformation(){



    }
    public   void IdentificationByToken(final String token){

        String url = "https://dutify.herokuapp.com/identification";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(),String.valueOf(response), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("authorization", token);

                return params;
            }
        };

        queue.add(jsonObjectRequest);



    }
}
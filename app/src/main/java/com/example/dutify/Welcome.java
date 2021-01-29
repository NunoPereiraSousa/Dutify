package com.example.dutify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Welcome extends AppCompatActivity {
    private TextView userNameTxt;
    private TextView dayNameAndTimeTxt;
    private TextView monthAndDayTxt;
    private TextView swipeGreetingTxt;
    private String tokenToBeSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Intent receivedIntend = getIntent();
        Bundle intendExtras = receivedIntend.getExtras();
        if (intendExtras != null) {
            userNameTxt = (TextView) findViewById(R.id.userNameTxt);
            userNameTxt.setText(intendExtras.getString("userFullName"));

            swipeGreetingTxt = (TextView) findViewById(R.id.swipeGreetingTxt);
            swipeGreetingTxt.setText(getResources().getString(R.string.hi) + intendExtras.getString("userFullName"));

            tokenToBeSent = intendExtras.getString("token");

            setTimeAndDate();
            IdentificationByToken(tokenToBeSent);
        } else {
            setTimeAndDate();
        }
    }

    private void setTimeAndDate() {
        String dayName = new SimpleDateFormat("EEEE", Locale.getDefault()).format(new Date());
        String MonthName = new SimpleDateFormat("MMM", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        String currentDay = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());

        dayNameAndTimeTxt = (TextView) findViewById(R.id.dayNameAndTimeTxt);
        monthAndDayTxt = (TextView) findViewById(R.id.monthAndDayTxt);

        currentDay = setDayFormat(currentDay);
        currentTime += setTimeFormatSuffix();
        String dayNameAndTime = dayName + ", " + currentTime;
        String monthAndDay = MonthName + " " + currentDay;

        dayNameAndTimeTxt.setText(dayNameAndTime);
        monthAndDayTxt.setText(monthAndDay);
    }

    private String setDayFormat(String currentDay) {
        switch (currentDay) {
            case "01":
            case "21":
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

    private String setTimeFormatSuffix() {
        String suffix = "am";

        if (Integer.parseInt(new SimpleDateFormat("HH", Locale.getDefault()).format(new Date())) >= 12) {
            suffix = "pm";
        }
        return suffix;
    }

    private void getUserInformation(String userId, final String token) {
        String url = "https://dutify.herokuapp.com/users/" + userId;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String data = response;

                try {
                    JSONArray userArray = new JSONArray(response);
                    String picture;

                    JSONObject user = userArray.getJSONObject(0);
                    picture = user.getString("picture");

                    ImageView userPicture = (ImageView) findViewById(R.id.userPicture);
                    Picasso.get()
                            .load(picture)
                            .resize(256, 256)
                            .centerCrop()
                            .into(userPicture);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", token);

                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void IdentificationByToken(final String token) {
        String url = "https://dutify.herokuapp.com/identification";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getUserInformation(response, token);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", token);

                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void contactAdmin(View view) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);

        int phoneNumber = 911587712;

        callIntent.setData(Uri.parse("tel:" + phoneNumber));

        if (ContextCompat.checkSelfPermission(Welcome.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Welcome.this, new String[]{Manifest.permission.CALL_PHONE}, 100);
        } else {
            try {
                startActivity(callIntent);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    public void openMotivationPage(View view) {
        Intent motivationIntent = new Intent(this, MotivationalSmsSection.class);
        motivationIntent.putExtra("token", tokenToBeSent);
        startActivity(motivationIntent);
    }
}
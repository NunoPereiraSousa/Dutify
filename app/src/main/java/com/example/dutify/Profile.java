package com.example.dutify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    private String tokenToBeSent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent receivedIntend = getIntent();
        Bundle intendExtras = receivedIntend.getExtras();
        if (intendExtras != null) {

            bottomNavigation = findViewById(R.id.bottomNavigation);
            bottomNavigation.setOnNavigationItemSelectedListener(navListener);

            tokenToBeSent = intendExtras.getString("token");
            IdentificationByToken(intendExtras.getString("token"));

            Log.d("Calendar token", tokenToBeSent);
        } else {
            Log.d("justTest", "An idea is being cooked");
        }

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


    private void getUserInformation(String userId, final String token) {
        String url = "https://dutify.herokuapp.com/users/" + userId;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String data = response;
                Log.d("date", data);

                try {
                    JSONArray userArray = new JSONArray(response);
                    String picture = null;
                    String fullName = null;
                    String description = null;
                    String contact = null;
                    String email = null;
                    Integer userType = null;
                    String website = "nothing to show";

                    JSONObject user = userArray.getJSONObject(0);
                    picture = user.getString("picture");
                    fullName = user.getString("firstName") + " " + user.getString("lastName");
                    description = user.getString("description");
                    contact = user.getString("contact");
                    email = user.getString("email");
                    userType = user.getInt("id_user_type");
                    displayUserInformation(picture, fullName, description, contact, email, website, userType);

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

    private void displayUserInformation(String picture, String fullName, String description, String contact, String email, String website, Integer userType) {
        //#1-Display Image
        ImageView userPicture = (ImageView) findViewById(R.id.profileImg);
        Picasso.get()
                .load(picture)
                .resize(256, 256)
                .centerCrop()
                .into(userPicture);
        //#2-Display Name and user function
        TextView fullNameTxt = (TextView) findViewById(R.id.fullNameTxt);
        fullNameTxt.setText(fullName);
        TextView levelTxt = (TextView) findViewById(R.id.levelTxt);
        if (userType == 1) {
            levelTxt.setText("Collaborator");
        } else if (userType == 2) {
            levelTxt.setText("Team Leader");
        }
        //#3-Display FullName
        TextView descriptionTxt = (TextView) findViewById(R.id.descriptionTxt);
        descriptionTxt.setText(description);
        //#4-Display Contact, email and website

        PhoneEmailWebsite contactsFragment = new PhoneEmailWebsite();

        FrameLayout contactsPlaceholder = (FrameLayout) findViewById(R.id.contacts_placeholder);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.contacts_placeholder, contactsFragment);
        ft.commit();
//        contactsFragment.changeTextViews(contact,email,website);





    }


    // Navigation codes are bellow (!Important)
    private void changePage(String toPage) {
        if (!toPage.equals("profile")) {
            Intent myIntent = null;
            if (toPage.equals("calendar")) {
                myIntent = new Intent(this, Calendar.class);
            } else if (toPage.equals("dashboard")) {
//            myIntent = new Intent(this,DashboardBoard.class);
            } else if (toPage.equals("awards")) {
                myIntent = new Intent(this, Catalog.class);
            }
            myIntent.putExtra("token", tokenToBeSent);
            startActivity(myIntent);
        }

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_calendar:
                    changePage("calendar");
                    break;
                case R.id.nav_dashboard:
                    //changePage("calendar");
                    break;
                case R.id.nav_awards:
                    changePage("awards");
                    break;
                case R.id.nav_profile:
                    changePage("profile");
                    break;
            }
            return true;
        }

    };


}
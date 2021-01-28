package com.example.dutify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dutify.RecyclerViewAdapterAward.CatalogAward;
import com.example.dutify.RecyclerViewAdapterAward.CatalogAwardClickInterface;
import com.example.dutify.RecyclerViewAdapterAward.CatalogAwardsViewAdapter;
import com.example.dutify.RecyclerViewAdapterProfileAwards.Award;
import com.example.dutify.RecyclerViewAdapterProfileAwards.ProfileAwardsViewAdapter;
import com.example.dutify.RecyleViewAdapterProjectsCard.Project;
import com.example.dutify.RecyleViewAdapterProjectsCard.ProjectsViewAdapter;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Catalog extends AppCompatActivity implements CatalogAwardClickInterface {
    private String tokenToBeSent;
    private String id_user;
    private int userTotalDuties;

    BottomNavigationView bottomNavigation;
    CatalogAwardsViewAdapter adapter;
    RecyclerView recyclerView;
    List<CatalogAward> awards;
    List<JSONObject> takenAwards;

    Catalog self;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    MaterialToolbar topCatalogBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        Intent receivedIntend = getIntent();
        Bundle intendExtras = receivedIntend.getExtras();
        self = this;
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
                id_user = response;
                getCredits(response, token);
                getBoughtAwards(response, token);
                getAwards(token);

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

    private void getCredits(String userId, final String token) {
        String url = "https://dutify.herokuapp.com/users/" + userId + "/credits";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray userArray = new JSONArray(response);
                    JSONObject user = userArray.getJSONObject(0);
                    userTotalDuties = user.getInt("credits");
                    View view = findViewById(R.id.navTopCredits);

                    if (view != null && view instanceof TextView) {
                        ((TextView) view).setTextColor(ResourcesCompat.getColor(getResources(), R.color.mainYellow, null));
                        ((TextView) view).setText(userTotalDuties + " " + getResources().getString(R.string.duties_top_navbar));
                    }

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

    private void getAwards(final String token) {
        String url = "https://dutify.herokuapp.com/awards";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    awards = new ArrayList<>();
                    JSONArray awardsArray = new JSONArray(response);
                    for (int i = 0; i < awardsArray.length(); i++) {
                        boolean bought = false;
                        JSONObject dataObj = awardsArray.getJSONObject(i);
                        for (int j = 0; j < takenAwards.size(); j++) {

                            if (dataObj.getInt("id_award") == takenAwards.get(j).getInt("id_award")) {
                                bought = true;
                            }
                        }
                        awards.add(new CatalogAward(dataObj.getInt("id_award"), dataObj.getString("name"), dataObj.getInt("price"), dataObj.getString("picture"), dataObj.getString("description"), bought));
                    }

                    FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(Catalog.this);
                    layoutManager.setFlexDirection(FlexDirection.ROW);
                    layoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);

                    recyclerView = findViewById(R.id.catalogAwardRecycleView);
                    recyclerView.setLayoutManager(layoutManager);
                    adapter = new CatalogAwardsViewAdapter(awards, self);
                    recyclerView.setAdapter(adapter);

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


    private void getBoughtAwards(String id_user, final String token) {
        takenAwards = new ArrayList<JSONObject>();
        String url = "https://dutify.herokuapp.com/users/" + id_user + "/bought-awards";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray takenAwardsArray = new JSONArray(response);
                    for (int i = 0; i < takenAwardsArray.length(); i++) {
                        JSONObject dataObj = takenAwardsArray.getJSONObject(i);
                        takenAwards.add(dataObj);
                    }
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

    public void buyAward(final String id_user, String id_award, int price) {

        String postUrl = "https://dutify.herokuapp.com/users/" + id_user + "/buy-award/" + id_award;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject postData = new JSONObject();
        try {
            postData.put("amount", price);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                if (Integer.parseInt(String.valueOf(error.networkResponse.statusCode)) >= 500) {
//                }

//                Toast.makeText(getApplicationContext(), new String(error.networkResponse.data, StandardCharsets.UTF_8), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", tokenToBeSent);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }



    public  void confirmBuyPopUp(){
        dialogBuilder = new AlertDialog.Builder(self);
        final View awardBuyConfirmPoPView = getLayoutInflater().inflate(R.layout.award_buy_confirm_pop_up, null);
        dialogBuilder.setView(awardBuyConfirmPoPView);
        dialog = dialogBuilder.create();
        dialog.show();
        MaterialButton backToMenuBtn = awardBuyConfirmPoPView.findViewById(R.id.backToMenuBtn);
        backToMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    public void buildAwardBuyPopUp(String awardName, final String id_award, final int price) {
        dialogBuilder = new AlertDialog.Builder(self);
        final View awardBuyPoPView = getLayoutInflater().inflate(R.layout.award_buy_pop_up, null);
        TextView awardNameTxt = (TextView) awardBuyPoPView.findViewById(R.id.awardNameTxt);
        TextView forDutiesTxt = (TextView) awardBuyPoPView.findViewById(R.id.forDutiesTxt);
        forDutiesTxt.setText("for " + price + " duties");
        awardNameTxt.setText(awardName);
        MaterialButton cancelBtn = awardBuyPoPView.findViewById(R.id.cancelBtn);
        MaterialButton buyBtn = awardBuyPoPView.findViewById(R.id.buyBtn);
        dialogBuilder.setView(awardBuyPoPView);
        dialog = dialogBuilder.create();
        dialog.show();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyAward(id_user, id_award, price);
                getCredits(id_user, tokenToBeSent);
                getBoughtAwards(id_user, tokenToBeSent);
                getAwards(tokenToBeSent);
                dialog.dismiss();
                confirmBuyPopUp();
            }
        });
    }


    public void buildAwardDescriptionPopUp(String awardName, String description) {
        dialogBuilder = new AlertDialog.Builder(self);
        final View taskDescriptionPopUpView = getLayoutInflater().inflate(R.layout.award_desc_pop_up, null);
        TextView awardDescriptionTxt = (TextView) taskDescriptionPopUpView.findViewById(R.id.awardDescriptionTxt);
        TextView awardNameTxt = (TextView) taskDescriptionPopUpView.findViewById(R.id.awardNameTxt);
        awardNameTxt.setText(awardName);
        awardDescriptionTxt.setText(description);


        MaterialButton backToMenuBtn = taskDescriptionPopUpView.findViewById(R.id.backToMenuBtn);
        dialogBuilder.setView(taskDescriptionPopUpView);
        dialog = dialogBuilder.create();
        dialog.show();
        backToMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void changePage(String toPage) {
        if (!toPage.equals("calendar")) {
            Intent myIntent = null;
            if (toPage.equals("profile")) {
                myIntent = new Intent(this, Profile.class);
            } else if (toPage.equals("dashboard")) {
                myIntent = new Intent(this, DashboardBoard.class);
            } else if (toPage.equals("awards")) {
                myIntent = new Intent(this, Catalog.class);
            }
            myIntent.putExtra("token", tokenToBeSent);
            startActivity(myIntent);
        }
    }

    public void openErrorPage() {
        Intent i = new Intent(this, ErrorPage.class);
        startActivity(i);
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
                    changePage("dashboard");
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
    @Override
    public void onAwardClick(int position) {
        buildAwardDescriptionPopUp(awards.get(position).getName(), awards.get(position).getDescription());
    }

    @Override
    public void onAwardLongClick(int position) {
        if (!awards.get(position).getBought()) {
            if ((userTotalDuties- awards.get(position).getPrice())>0){
                buildAwardBuyPopUp(awards.get(position).getName(), String.valueOf(awards.get(position).getId()), awards.get(position).getPrice());
            }else {
                Toast.makeText(this, "You don't have enough duties to buy this award", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "You Have already acquire this award", Toast.LENGTH_SHORT).show();
        }
    }
}
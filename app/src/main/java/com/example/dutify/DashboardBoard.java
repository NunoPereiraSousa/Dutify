package com.example.dutify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dutify.RecyclerViewAdapterDashDescMyTasks.DashDescTasksViewAdapter;
import com.example.dutify.RecyclerViewAdapterDashListTaskCard.DashListTasksClickInterface;
import com.example.dutify.RecyclerViewAdapterDashListTaskCard.DashListTasksViewAdapter;
import com.example.dutify.RecyclerViewAdapterDashListTaskCard.Task;
import com.example.dutify.RecyclerViewAdapterProjectsTeamMember.TeamMember;
import com.example.dutify.RecyclerViewAdapterProjectsTeamMember.TeamMembersViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardBoard extends AppCompatActivity implements DashListTasksClickInterface {
    String tokenToBeSent;
    DashboardBoard self;
    List<com.example.dutify.RecyclerViewAdapterDashListTaskCard.Task> myTasks;
    List<com.example.dutify.RecyclerViewAdapterDashListTaskCard.Task> displayedTasks;
    RecyclerView recyclerViewMyTasks;
    DashListTasksViewAdapter adapterMyTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_board);
        self = this;
        Intent receivedIntend = getIntent();
        Bundle intendExtras = receivedIntend.getExtras();
        if (intendExtras != null) {
            tokenToBeSent = intendExtras.getString("token");
            IdentificationByToken(intendExtras.getString("token"));
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
                getUserTasks(response, token);
//                getUserPersonalTasks(response, token);
//                displayTasks(1);
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


    public void getUserTasks(String userId, final String token) {
        myTasks = new ArrayList<>();
        final String url = "https://dutify.herokuapp.com/users/" + userId + "/operations/data";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray responseArray = new JSONArray(response);
                    for (int i = 0; i < responseArray.length(); i++) {
                        JSONObject dataObj = responseArray.getJSONObject(i);
                        myTasks.add(new Task(dataObj.getInt("id_task"), dataObj.getString("taskTitle"), dataObj.getString("taskDescription"), dataObj.getInt("taskIdProgressStatus"), dataObj.getInt("creditsValue"), dataObj.getInt("id_project"), dataObj.getString("projectTitle"), dataObj.getInt("projectProgressStatus"), false));
                    }
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

                    recyclerViewMyTasks = findViewById(R.id.tasksRecycleView);
                    recyclerViewMyTasks.setHasFixedSize(true);
                    recyclerViewMyTasks.setLayoutManager(layoutManager);
                    adapterMyTasks = new DashListTasksViewAdapter(myTasks, self);
                    recyclerViewMyTasks.setAdapter(adapterMyTasks);

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


//    public void getUserPersonalTasks(String userId, final String token) {
//        final String url = "https://dutify.herokuapp.com/users/" + userId + "/personalTasks";
//        RequestQueue queue = Volley.newRequestQueue(this);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONArray responseArray = new JSONArray(response);
//
//
//                    Toast.makeText(getApplicationContext(), String.valueOf(myTasks.size()), Toast.LENGTH_SHORT).show();
//
//                    for (int i = 0; i < responseArray.length(); i++) {
//                        JSONObject dataObj = responseArray.getJSONObject(i);
//                        myTasks.add(new Task(dataObj.getInt("id_personal_task"), dataObj.getString("title"), dataObj.getString("todo"), dataObj.getInt("id_progress_status"), dataObj.getInt("creditsValue"), 0, "",0, true));
//                    }
//
//                    Toast.makeText(getApplicationContext(), String.valueOf(myTasks.size()), Toast.LENGTH_SHORT).show();
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("titasLi", String.valueOf(error));
//                error.printStackTrace();
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("authorization", token);
//                return params;
//            }
//        };
//        queue.add(stringRequest);
//    }



    private void changePage(String toPage) {
        if (!toPage.equals("dashboard")) {
            Intent myIntent = null;
            if (toPage.equals("calendar")) {
                myIntent = new Intent(this, Calendar.class);
            } else if (toPage.equals("profile")) {
                myIntent = new Intent(this, Profile.class);
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
    public void onTaskCardClick(int position) {

    }

    @Override
    public void onTaskCardLongClick(int position) {

    }
}
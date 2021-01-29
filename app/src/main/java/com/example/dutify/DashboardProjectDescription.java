package com.example.dutify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.dutify.RecyclerViewAdapterDashDescMyTasks.DashDescTasksClickInterface;
import com.example.dutify.RecyclerViewAdapterDashDescMyTasks.DashDescTasksViewAdapter;
import com.example.dutify.RecyclerViewAdapterDashDescMyTasks.Task;
import com.example.dutify.RecyclerViewAdapterProjectsTeamMember.TeamMember;
import com.example.dutify.RecyclerViewAdapterProjectsTeamMember.TeamMembersViewAdapter;
import com.example.dutify.RecyclerViewAdapterProjectsTeamMember.TeamMembersViewClickInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class DashboardProjectDescription extends AppCompatActivity implements TeamMembersViewClickInterface, DashDescTasksClickInterface {
    BottomNavigationView bottomNavigation;
    TextView projectTitleTxt;
    TextView projectDescriptionTxt;
    TextView projectsDaysLeftTxt;
    TextView projectDevelopmentTxt;
    DashboardProjectDescription self;
    String tokenToBeSent = "";
    String userId = "";
    int projectId;
    List<TeamMember> teamMembers;
    List<JSONObject> categoryTags;
    List<JSONObject> progressStatus;
    List<Task> myTasks;
    RecyclerView recyclerViewTeamMembers;
    TeamMembersViewAdapter adapterTeamMembers;
    RecyclerView recyclerViewMyTasks;
    DashDescTasksViewAdapter adapterMyTasks;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_project_description);
        self = this;
        Intent receivedIntend = getIntent();
        Bundle intendExtras = receivedIntend.getExtras();
        if (intendExtras != null) {
            bottomNavigation = findViewById(R.id.bottomNavigation);
            bottomNavigation.setOnNavigationItemSelectedListener(navListener);

            tokenToBeSent = intendExtras.getString("token");
            projectId = intendExtras.getInt("id_project");
            IdentificationByToken(intendExtras.getString("token"), String.valueOf(projectId));
        }
    }

    public void IdentificationByToken(final String token, final String projectId) {
        String url = "https://dutify.herokuapp.com/identification";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                userId = response;
                getCategoryTags(token);
                getProgressStatus(token);
                getOperationsData(response, token, projectId);
                getUserProjectTasks(response, token, projectId);
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

    private void getCategoryTags(final String token) {
        categoryTags = new ArrayList<JSONObject>();
        String url = "https://dutify.herokuapp.com/categoryTags";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray categoryTagsArray = new JSONArray(response);
                    for (int i = 0; i < categoryTagsArray.length(); i++) {
                        JSONObject dataObj = categoryTagsArray.getJSONObject(i);
                        categoryTags.add(dataObj);
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

    private void getOperationsData(final String userId, final String token, String projectId) {
        teamMembers = new ArrayList<>();
        final String url = "https://dutify.herokuapp.com/operations/" + projectId + "/data";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<JSONObject> selectedInformation = new ArrayList<JSONObject>();
                    JSONArray operations = new JSONArray(response);
                    for (int j = 0; j < operations.length(); j++) {
                        int permitted = 1;
                        JSONObject dataObj = operations.getJSONObject(j);
                        for (int x = 0; x < selectedInformation.size(); x++) {
                            if (dataObj.getInt("id_team") == selectedInformation.get(x).getInt("id_team") && dataObj.getInt("id_user") == selectedInformation.get(x).getInt("id_user")) {
                                permitted = 0;
                            }
                        }
                        if (permitted == 1) {
                            selectedInformation.add(dataObj);
                        }
                    }
                    projectTitleTxt = (TextView) findViewById(R.id.projectTitleTxt);
                    projectDescriptionTxt = (TextView) findViewById(R.id.projectDescriptionTxt);
                    projectTitleTxt.setText(selectedInformation.get(0).getString("projectTitle"));
                    projectDescriptionTxt.setText(selectedInformation.get(0).getString("projectDescription"));

                    setProjectDaysLeft(selectedInformation.get(0).getString("projectEndDate").split("T")[0]);
                    setProjectStatus(selectedInformation.get(0).getInt("projectIdProgressStatus"));

                    for (int i = 0; i < selectedInformation.size(); i++) {
                        int personId = selectedInformation.get(i).getInt("id_user");
                        String personName = selectedInformation.get(i).getString("firstName");
                        String photoUrl = selectedInformation.get(i).getString("picture");
                        String teamColor = "";
                        for (int z = 0; z < categoryTags.size(); z++) {
                            if (categoryTags.get(z).getInt("id_category_tag") == selectedInformation.get(i).getInt("teamIdCategoryTag")) {
                                teamColor = categoryTags.get(z).getString("color");
                            }
                        }
                        teamMembers.add(new TeamMember(personId, personName, photoUrl, teamColor));
                    }
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

                    recyclerViewTeamMembers = findViewById(R.id.teamMembersHolder);
                    recyclerViewTeamMembers.setHasFixedSize(true);
                    recyclerViewTeamMembers.setLayoutManager(layoutManager);
                    adapterTeamMembers = new TeamMembersViewAdapter(teamMembers, self);
                    recyclerViewTeamMembers.setAdapter(adapterTeamMembers);

                } catch (JSONException | ParseException e) {
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

    public void setProjectDaysLeft(String endDate) throws ParseException {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = myFormat.parse(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        Date date2 = myFormat.parse(endDate);
        int daysLeft = 0;
        String suffix = " days left";
        if (Integer.parseInt(String.valueOf(date2.getTime() - date1.getTime())) >= 0) {
            daysLeft = Integer.parseInt(String.valueOf(date2.getTime() - date1.getTime()));
        }
        if (daysLeft == 1) {
            suffix = " day left";
        }
        projectsDaysLeftTxt = (TextView) findViewById(R.id.projectsDaysLeftTxt);
        projectsDaysLeftTxt.setText(String.valueOf(daysLeft) + suffix);
    }

    public void getProgressStatus(final String token) {
        progressStatus = new ArrayList<>();

        final String url = "https://dutify.herokuapp.com/progressStatus";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray responseArray = new JSONArray(response);
                    for (int i = 0; i < responseArray.length(); i++) {
                        JSONObject dataObj = responseArray.getJSONObject(i);
                        progressStatus.add(dataObj);
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

    public void setProjectStatus(int id_progress_status) throws JSONException {
        for (int i = 0; i < progressStatus.size(); i++) {
            JSONObject dataObj = progressStatus.get(i);
            if (id_progress_status == dataObj.getInt("id_progress_status")) {
                projectDevelopmentTxt = (TextView) findViewById(R.id.projectDevelopmentTxt);
                projectDevelopmentTxt.setText(dataObj.getString("description"));
            }
        }
    }

    public void getUserProjectTasks(String userId, final String token, String projectId) {
        myTasks = new ArrayList<>();
        final String url = "https://dutify.herokuapp.com/projects/" + projectId + "/users/" + userId + "/tasks";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray responseArray = new JSONArray(response);
                    for (int i = 0; i < responseArray.length(); i++) {
                        JSONObject dataObj = responseArray.getJSONObject(i);
                        myTasks.add(new Task(dataObj.getInt("id_task"), dataObj.getString("title"), dataObj.getString("description"), dataObj.getInt("id_progress_status"), dataObj.getString("endDate"), dataObj.getInt("creditsValue")));
                    }

                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

                    recyclerViewMyTasks = findViewById(R.id.myTasksRecycleView);
                    recyclerViewMyTasks.setHasFixedSize(true);
                    recyclerViewMyTasks.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapterMyTasks = new DashDescTasksViewAdapter(myTasks, self);
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

    public void changeTaskStatus(int idTask) {

        String postUrl = "https://dutify.herokuapp.com/tasks/" + String.valueOf(idTask) + "/status";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject postData = new JSONObject();
        try {
            postData.put("id_progress_status", "2");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(), "The task Status has been updated, Good job", Toast.LENGTH_SHORT).show();
                getUserProjectTasks(userId, tokenToBeSent, String.valueOf(projectId));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (Integer.parseInt(String.valueOf(error.networkResponse.statusCode)) >= 500) {

                }
                Toast.makeText(getApplicationContext(), new String(error.networkResponse.data, StandardCharsets.UTF_8), Toast.LENGTH_LONG).show();
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

    public void addIncomeToUser() {
        String postUrl = "https://dutify.herokuapp.com/users/" + String.valueOf(userId) + "/credits";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject postData = new JSONObject();
        try {
            postData.put("operation", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(), "Your income has been updated", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (Integer.parseInt(String.valueOf(error.networkResponse.statusCode)) >= 500) {

                }
                Toast.makeText(getApplicationContext(), new String(error.networkResponse.data, StandardCharsets.UTF_8), Toast.LENGTH_LONG).show();
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

    public void buildTasksDescriptionPopUp(String title, String description) {
        dialogBuilder = new AlertDialog.Builder(self);
        final View taskDescriptionPopUpView = getLayoutInflater().inflate(R.layout.tasks_desc_pop_up, null);
        TextView tasksTitleTxt = (TextView) taskDescriptionPopUpView.findViewById(R.id.tasksTitleTxt);
        TextView taskDescriptionTxt = (TextView) taskDescriptionPopUpView.findViewById(R.id.taskDescriptionTxt);
        tasksTitleTxt.setText(title);
        taskDescriptionTxt.setText(description);
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

    public void buildTasksStateChangePopUp(String title, final int idTask) {
        dialogBuilder = new AlertDialog.Builder(self);
        final View taskStateChangePopUpView = getLayoutInflater().inflate(R.layout.tasks_change_state_pop_up, null);
        TextView tasksTitleTxt = (TextView) taskStateChangePopUpView.findViewById(R.id.tasksTitleTxt);
        tasksTitleTxt.setText(title);
        MaterialButton cancelBtn = taskStateChangePopUpView.findViewById(R.id.cancelBtn);
        MaterialButton confirmBtn = taskStateChangePopUpView.findViewById(R.id.confirmBtn);
        dialogBuilder.setView(taskStateChangePopUpView);
        dialog = dialogBuilder.create();
        dialog.show();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTaskStatus(idTask);
                addIncomeToUser();
                dialog.dismiss();
            }
        });
    }

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
    public void onTeamMemberClick(int position) {
    }

    @Override
    public void onTeamMemberLongClick(int position) {
    }

    @Override
    public void onTaskCardClick(int position) {
        buildTasksDescriptionPopUp(myTasks.get(position).getTaskTitle(), myTasks.get(position).getTasksDescription());
    }

    @Override
    public void onTaskCardLongClick(int position) {
        if (myTasks.get(position).getId_progress_status() == 1) {
            buildTasksStateChangePopUp(myTasks.get(position).getTaskTitle(), myTasks.get(position).getTaskId());
        } else {
            Toast.makeText(getApplicationContext(), "The task must be on development so u can access this feature", Toast.LENGTH_LONG).show();
        }
    }
}
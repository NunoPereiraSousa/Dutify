package com.example.dutify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dutify.RecyclerViewAdapterProfileAwards.Award;
import com.example.dutify.RecyclerViewAdapterProfileAwards.ProfileAwardsViewAdapter;
import com.example.dutify.RecyclerViewAdapterProfileAwards.ProfileAwardsViewClickInterface;
import com.example.dutify.RecyleViewAdapterProjectsCard.Project;
import com.example.dutify.RecyleViewAdapterProjectsCard.ProjectsViewAdapter;
import com.example.dutify.RecyleViewAdapterProjectsCard.ProjectsViewClickInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile extends AppCompatActivity implements ProjectsViewClickInterface {
    BottomNavigationView bottomNavigation;
    private String tokenToBeSent;

    ProfileAwardsViewAdapter adapter;
    ProjectsViewAdapter adapterProjects;
    RecyclerView recyclerView;
    RecyclerView recyclerViewProject;
    List<Award> awards;
    List<Project> projects;
    Profile self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        self = this;

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
                displayUserCategoriesTag(response, token);
                getUserTasks(response, token);
                getUserAwards(response, token);
//                getUserProjects(response, token);
                getOperationsData(response, token);
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
                try {
                    JSONArray userArray = new JSONArray(response);
                    String picture = null;
                    String fullName = null;
                    String description = null;
                    String contact = null;
                    String email = null;
                    Integer userType = null;
                    String website = null;

                    JSONObject user = userArray.getJSONObject(0);
                    picture = user.getString("picture");
                    fullName = user.getString("firstName") + " " + user.getString("lastName");
                    description = user.getString("description");
                    contact = user.getString("contact");
                    email = user.getString("email");
                    userType = user.getInt("id_user_type");
                    website = user.getString("website");
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
        Bundle myBundle = new Bundle();
        myBundle.putString("contact", contact);
        myBundle.putString("email", email);
        myBundle.putString("website", website);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        contactsFragment.setArguments(myBundle);
        ft.add(R.id.contacts_placeholder, contactsFragment);
        ft.commit();
    }

    //Function that displays the user category
    private void displayUserCategoriesTag(String userId, final String token) {
        String url = "https://dutify.herokuapp.com/users/" + userId + "/tags";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray tagsArray = new JSONArray(response);

                    Chip firstChip = (Chip) findViewById(R.id.firstChip);
                    Chip secondChip = (Chip) findViewById(R.id.secondChip);
                    Chip thirdChip = (Chip) findViewById(R.id.thirdChip);
                    for (int i = 0; i < tagsArray.length(); i++) {
                        JSONObject dataObj = tagsArray.getJSONObject(i);
                        if (i == 0) {
                            firstChip.setText(dataObj.getString("description"));
                        } else if (i == 1) {
                            secondChip.setText(dataObj.getString("description"));
                        } else {
                            thirdChip.setText(dataObj.getString("description"));
                        }
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

    private void getUserAwards(String userId, final String token) {
        String url = "https://dutify.herokuapp.com/users/" + userId + "/awards";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    awards = new ArrayList<>();
                    JSONArray awardsArray = new JSONArray(response);
                    for (int i = 0; i < awardsArray.length(); i++) {
                        JSONObject dataObj = awardsArray.getJSONObject(i);
                        awards.add(new Award(dataObj.getString("name"), dataObj.getString("description"), dataObj.getInt("price"), dataObj.getString("picture")));
                    }

                    recyclerView = findViewById(R.id.awardsRecycleView);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter = new ProfileAwardsViewAdapter(awards);
                    // problem
                    //adapter.setClickListener(this);
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

    private void getUserProjects(String userId, final String token) {
        String url = "https://dutify.herokuapp.com/projects";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    projects = new ArrayList<>();
                    JSONArray projectsArray = new JSONArray(response);
                    for (int i = 0; i < projectsArray.length(); i++) {
                        JSONObject dataObj = projectsArray.getJSONObject(i);
//                        projects.add(new Project(dataObj.getString("title"), dataObj.getString("description")));
                    }

                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

//                    recyclerViewProject = findViewById(R.id.projectRecycleView);
//                    recyclerViewProject.setHasFixedSize(true);
//                    recyclerViewProject.setLayoutManager(layoutManager);
//                    adapterProjects = new ProjectsViewAdapter(projects, self);
//                    // problem
//                    //adapterProjects.setClickListener(this);
//                    recyclerViewProject.setAdapter(adapterProjects);

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

    private void getUserTasks(String userId, final String token) {
        String url = "https://dutify.herokuapp.com/users/" + userId + "/tasks";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    int nFailed = 0;
                    int nUnderDevelopment = 0;

                    JSONArray tasksArray = new JSONArray(response);
                    for (int i = 0; i < tasksArray.length(); i++) {
                        JSONObject dataObj = tasksArray.getJSONObject(i);
                        if (dataObj.getInt("id_progress_status") == 1) {
                            nUnderDevelopment += 1;
                        } else if (dataObj.getInt("id_progress_status") == 3) {
                            nFailed += 1;
                        }
                    }
                    TextView totalTaskTxt = (TextView) findViewById(R.id.totalTaskTxt);
                    totalTaskTxt.setText(String.valueOf(tasksArray.length()));
                    TextView notCompletedTxt = (TextView) findViewById(R.id.notCompletedTxt);
                    notCompletedTxt.setText(String.valueOf(nFailed));
                    TextView underDevTxt = (TextView) findViewById(R.id.underDevTxt);
                    underDevTxt.setText(String.valueOf(nUnderDevelopment));

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

    private void getOperationsData(final String userId, final String token) {
        projects = new ArrayList<>();
        String url = "https://dutify.herokuapp.com/operations/data";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<Integer> projectsIds = new ArrayList<Integer>(); // Create an ArrayList object
                    ArrayList<JSONObject> selectedInformation = new ArrayList<JSONObject>(); // Create an ArrayList object
                    JSONArray operations = new JSONArray(response);

                    //#1-GET ALL PROJECTS IDS THAT THE USER IS INSIDE
                    for (int i = 0; i < operations.length(); i++) {
                        int permitted = 1;
                        JSONObject dataObj = operations.getJSONObject(i);

                        if (dataObj.getInt("id_user") == Integer.parseInt(userId)) {
                            for (int j = 0; j < projectsIds.size(); j++) {
                                if (dataObj.getInt("id_project") == projectsIds.get(j)) {
                                    permitted = 0;
                                    break;
                                }
                            }
                            if (permitted == 1) {
                                projectsIds.add(dataObj.getInt("id_project"));
                            }
                        }
                    }

                    //#2-GET ALL data that  can relate with the project defined
                    for (int i = 0; i < operations.length(); i++) {
                        int permitted = 1;
                        JSONObject dataObj = operations.getJSONObject(i);

                        for (int j = 0; j < projectsIds.size(); j++) {
                            if (dataObj.getInt("id_project") == projectsIds.get(j)) {
                                for (int x = 0; x < selectedInformation.size(); x++) {
                                    if (selectedInformation.get(x).getInt("id_user") == dataObj.getInt("id_user")) {
                                        permitted = 0;
                                        break;
                                    }
                                }
                            }
                        }
                        if (permitted == 1) {
                            selectedInformation.add(dataObj);
                        }
                    }
                    //#3-DISPLAY THE INFORMATION
                    for (int i = 0; i < projectsIds.size(); i++) {
                        String projectTitle = "";
                        String team = null;
                        String firstUrl = null;
                        String secondUrl = null;
                        String thirdUrl = null;
                        int toSavePictureIn = 1;

                        for (int j = 0; j < selectedInformation.size(); j++) {
                            if (selectedInformation.get(j).getInt("id_project") == projectsIds.get(i)) {
                                if (projectTitle.equals("")) {
                                    projectTitle = selectedInformation.get(j).getString("projectTitle");
                                }
                                if (toSavePictureIn == 1) {
                                    firstUrl = selectedInformation.get(j).getString("picture");
                                    toSavePictureIn += 1;
                                } else if (toSavePictureIn == 2) {
                                    secondUrl = selectedInformation.get(j).getString("picture");
                                    toSavePictureIn += 1;
                                } else if (toSavePictureIn == 3) {
                                    thirdUrl = selectedInformation.get(j).getString("picture");
                                    toSavePictureIn += 1;
                                    break;
                                }
                            }
                        }

                        Log.d("moka", projectTitle);
                        projects.add(new Project(projectTitle, "something", firstUrl, secondUrl, thirdUrl, projectsIds.get(i)));
                    }

                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

                    recyclerViewProject = findViewById(R.id.projectRecycleView);
                    recyclerViewProject.setHasFixedSize(true);
                    recyclerViewProject.setLayoutManager(layoutManager);
                    adapterProjects = new ProjectsViewAdapter(projects, self);
                    // problem
                    //adapterProjects.setClickListener(this);
                    recyclerViewProject.setAdapter(adapterProjects);

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

    private void changePage(String toPage) {
        if (!toPage.equals("profile")) {
            Intent myIntent = null;
            if (toPage.equals("calendar")) {
                myIntent = new Intent(this, Calendar.class);
            } else if (toPage.equals("dashboard")) {
                myIntent = new Intent(this, Dashboard.class);
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

    @Override
    public void onProjectCardClick(int position) {
        Toast.makeText(this, String.valueOf(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongClick(int position) {

    }
}
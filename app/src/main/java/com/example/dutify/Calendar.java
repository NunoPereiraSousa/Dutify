package com.example.dutify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dutify.RecyclerViewAdapterTaskCard.Task;
import com.example.dutify.RecyclerViewAdapterTaskCard.TaskViewAdapter;
import com.example.dutify.RecyleViewAdapterProjectsCard.Project;
import com.example.dutify.RecyleViewAdapterProjectsCard.ProjectsViewAdapter;
import com.example.dutify.RecyleViewAdapterProjectsCard.ProjectsViewClickInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Calendar extends AppCompatActivity implements ProjectsViewClickInterface {
    BottomNavigationView bottomNavigation;

    private TextView currentDateTxt;
    private TextView selectedMonthTxt;
    private String tokenToBeSent;

    private TextInputEditText personalTaskNameInput;
    private TextInputEditText personalTaskDescInput;
    private TextInputEditText startingDateInput;
    private TextInputEditText endingDateInput;

    private CalendarView calendar;

    List<Project> projects;
    List<JSONObject> categoryTags;
    ProjectsViewAdapter adapterProjects;
    RecyclerView recyclerViewProject;
    Calendar self;

    TaskViewAdapter adapterTasks;
    RecyclerView recyclerViewTasks;
    List<Task> tasks;
    ArrayList<JSONObject> dayTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

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

        setTimeAndDate();
    }

    private void setTimeAndDate() {
        String dayName = new SimpleDateFormat("EEEE", Locale.getDefault()).format(new Date()); // Get day of the week
        String MonthName = new SimpleDateFormat("MMMM", Locale.getDefault()).format(new Date()); // get full name month
        String year = new SimpleDateFormat("YYYY", Locale.getDefault()).format(new Date()); // get year
        String currentDay = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()); // get current day

        currentDateTxt = (TextView) findViewById(R.id.currentDateTxt);
        selectedMonthTxt = (TextView) findViewById(R.id.selectedMonthTxt);

        currentDay = setDayFormat(currentDay);
        String dayNameAndTime = dayName;
        String monthAndDay = MonthName.toLowerCase() + " " + currentDay;

        currentDateTxt.setText(dayNameAndTime + ", " + monthAndDay);
        selectedMonthTxt.setText(MonthName + ", " + year);
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

    public void openPanel(View v) {
        final SlidingUpPanelLayout slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.calendarPanel);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    public void submitPersonalTask(View v) {
        personalTaskNameInput = findViewById(R.id.personalTaskNameInput);
        personalTaskDescInput = findViewById(R.id.personalTaskDescInput);
        startingDateInput = findViewById(R.id.startingDateInput);
        endingDateInput = findViewById(R.id.endingDateInput);

        String personalTaskNameTxt = personalTaskNameInput.getEditableText().toString();
        String personalTaskDescTxt = personalTaskDescInput.getEditableText().toString();
        String startingDateTaskTxt = startingDateInput.getEditableText().toString();
        String endingDateDescTxt = endingDateInput.getEditableText().toString();

        if (personalTaskNameTxt.equals("") ||
                personalTaskDescTxt.equals("") ||
                startingDateTaskTxt.equals("") ||
                endingDateDescTxt.equals("")) {
            Toast.makeText(getApplicationContext(), "Please fill all your inputs", Toast.LENGTH_SHORT).show();
        } else {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("*/*");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"nunopereirasousa00@gmail.com"});
            //emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Personal Task");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Personal task name: " + personalTaskNameTxt + "\n" +
                    "Personal task description: " + personalTaskDescTxt + "\n" +
                    "Starting date: " + startingDateTaskTxt + "\n" +
                    "Ending date: " + endingDateDescTxt + "\n");

            if (emailIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(emailIntent);
            }
        }
    }

    public void IdentificationByToken(final String token) {
        String url = "https://dutify.herokuapp.com/identification";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getUserTodayTasks(response, token);
                getUserTasks(response, token);
                getCategoryTags(token);
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

    private void getCalendarSelectedDay(final ArrayList<JSONObject> dayTasks) {
        calendar = (CalendarView) findViewById(R.id.calendar);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {

                int monthInt = month + 1;
                String monthConverted = "" + monthInt;
                if (month < 10) monthConverted = "0" + monthConverted;

                String dayConverted = "" + day;
                if (day < 10) dayConverted = "0" + dayConverted;

                String date = year + "-" + monthConverted + "-" + dayConverted;

                for (int i = 0; i < dayTasks.size(); i++) {
                    JSONObject dataObj = dayTasks.get(i);

                    try {
                        String endDate = dataObj.getString("endDate");

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date1 = null;
                        try {
                            date1 = sdf.parse(dataObj.getString("endDate"));
                            Date date2 = sdf.parse(currentDate());

                            if (date1.compareTo(date2) > 0 && date.equals(endDate)) {
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.toast,
                                        (ViewGroup) findViewById(R.id.toast_layout));

                                ImageView image = (ImageView) layout.findViewById(R.id.image);
                                image.setImageResource(R.drawable.ic_logo);
                                TextView text = (TextView) layout.findViewById(R.id.text);

                                text.setText(dataObj.getString("title") + getResources().getString(R.string.task_ending));

                                Toast toast = new Toast(getApplicationContext());
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setView(layout);
                                toast.show();

                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getUserTasks(String userId, final String token) {
        String url = "https://dutify.herokuapp.com/users/" + userId + "/tasks/endDate";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dayTasks = new ArrayList<>();

                    JSONArray tasksArray = new JSONArray(response);
                    for (int i = 0; i < tasksArray.length(); i++) {
                        JSONObject dataObj = tasksArray.getJSONObject(i);

                        dayTasks.add(dataObj);
                    }

                    getCalendarSelectedDay(dayTasks);

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

    public String currentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.now();
        return date.format(formatter);
    }

    public Boolean compareDates(String a, String b, String d) {
        return a.compareTo(d) * b.compareTo(d) > 0;
    }

    public Boolean compareDates2(String a, String b, String d) {
        return a.compareTo(d) * b.compareTo(d) < 0;
    }

    private void getUserTodayTasks(String userId, final String token) {
        String url = "https://dutify.herokuapp.com/users/" + userId + "/tasks/project";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    tasks = new ArrayList<>();
                    JSONArray tasksArray = new JSONArray(response);
                    for (int i = 0; i < tasksArray.length(); i++) {
                        JSONObject dataObj = tasksArray.getJSONObject(i);

                        String startDate = dataObj.getString("startDate");
                        String endDate = dataObj.getString("endDate");
                        Log.d("asd", dataObj.getString("title"));

                        if (!compareDates(startDate,
                                endDate,
                                currentDate())) {

                            tasks.add(new Task(dataObj.getString("title"),
                                    dataObj.getString("description"),
                                    dataObj.getString("startDate"),
                                    dataObj.getString("endDate"),
                                    dataObj.getInt("id_progress_status")));
                        }
                    }

                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

                    recyclerViewTasks = findViewById(R.id.calendarTaskRecycleView);
                    recyclerViewTasks.setHasFixedSize(true);
                    recyclerViewTasks.setLayoutManager(layoutManager);
                    adapterTasks = new TaskViewAdapter(getApplicationContext(), tasks);
                    recyclerViewTasks.setAdapter(adapterTasks);

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
        final String url = "https://dutify.herokuapp.com/operations/data";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<Integer> projectsIds = new ArrayList<Integer>(); // Create an ArrayList object
                    ArrayList<Integer> teamsInsideIds = new ArrayList<Integer>(); //
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
                    //#2- GET THE TEAMS THAT THE PERSON IS INSIDE based in his projects
                    for (int i = 0; i < projectsIds.size(); i++) {
                        for (int j = 0; j < operations.length(); j++) {
                            int permitted = 1;
                            JSONObject dataObj = operations.getJSONObject(j);

                            if (dataObj.getInt("id_user") == Integer.parseInt(userId) && dataObj.getInt("id_project") == projectsIds.get(i)) {
                                permitted = 1;
                            } else if (dataObj.getInt("id_user") != Integer.parseInt(userId)) {
                                permitted = 0;
                            }
                            if (permitted == 1) {
                                int isNew = 1;
                                for (int x = 0; x < teamsInsideIds.size(); x++) {
                                    if (teamsInsideIds.get(x) == dataObj.getInt("id_team")) {
                                        isNew = 0;
                                        break;
                                    }
                                }
                                if (isNew == 1) {
                                    teamsInsideIds.add(dataObj.getInt("id_team"));
                                }
                            }
                        }
                    }

                    //#3-GET ALL data that  can relate with the project and the team that the user is inserted
                    for (int i = 0; i < operations.length(); i++) {
                        JSONObject dataObj = operations.getJSONObject(i);
                        for (int j = 0; j < projectsIds.size(); j++) {
                            int permitted = 1;
                            for (int y = 0; y < teamsInsideIds.size(); y++) {
                                if (dataObj.getInt("id_project") == projectsIds.get(j) && dataObj.getInt("id_team") == teamsInsideIds.get(y)) {
                                    permitted = 1;
                                } else {
                                    permitted = 0;
                                }

                                if (permitted == 1) {
                                    int isNew = 1;
                                    for (int x = 0; x < selectedInformation.size(); x++) {
                                        if (selectedInformation.get(x).getInt("id_user") == dataObj.getInt("id_user") && selectedInformation.get(x).getInt("id_team") == dataObj.getInt("id_team") && selectedInformation.get(x).getInt("id_project") == dataObj.getInt("id_project")) {
                                            isNew = 0;
                                            break;
                                        }
                                    }
                                    if (isNew == 1) {
                                        selectedInformation.add(dataObj);
                                    }
                                }
                            }
                        }
                    }
                    Log.d("Bitches", String.valueOf(selectedInformation));
                    String needed = "";
                    //#4-DISPLAY THE INFORMATION
                    for (int i = 0; i < projectsIds.size(); i++) {
                        for (int j = 0; j < teamsInsideIds.size(); j++) {
                            String projectTitle = "";
                            String teamName = "";
                            String firstUrl = null;
                            String secondUrl = null;
                            String thirdUrl = null;
                            String color = "";
                            String daysLeft = "";

                            int toSavePictureIn = 1;
                            for (int x = 0; x < selectedInformation.size(); x++) {
                                if (selectedInformation.get(x).getInt("id_project") == projectsIds.get(i) && selectedInformation.get(x).getInt("id_team") == teamsInsideIds.get(j)) {
                                    if (projectTitle.equals("") && teamName.equals("") && color.equals("")) {
                                        projectTitle = selectedInformation.get(x).getString("projectTitle");
                                        teamName = selectedInformation.get(x).getString("teamName");
                                        for (int z = 0; z < categoryTags.size(); z++) {
                                            if (categoryTags.get(z).getInt("id_category_tag") == selectedInformation.get(x).getInt("teamIdCategoryTag")) {
                                                color = categoryTags.get(z).getString("color");
                                            }
                                        }

                                        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        Date date1 = myFormat.parse(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
                                        Date date2 = myFormat.parse(selectedInformation.get(x).getString("projectEndDate").split("T")[0]);
                                        long diff = date2.getTime() - date1.getTime();
                                        if (Integer.parseInt(String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS))) < 0) {
                                            daysLeft = "0";
                                        } else {
                                            daysLeft = String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                                        }
                                    }
                                    if (toSavePictureIn == 1) {
                                        firstUrl = selectedInformation.get(x).getString("picture");
                                    } else if (toSavePictureIn == 2) {
                                        secondUrl = selectedInformation.get(x).getString("picture");
                                    } else if (toSavePictureIn == 3) {
                                        thirdUrl = selectedInformation.get(x).getString("picture");
                                        break;
                                    }
                                    toSavePictureIn += 1;
                                }
                            }
                            if (!projectTitle.equals("") && firstUrl != null && secondUrl != null && thirdUrl != null && !teamName.equals("") && !color.equals("")) {
                                int totalTasks = 0;
                                int totalToDo = 0;
                                for (int h = 0; h < operations.length(); h++) {
                                    if (operations.getJSONObject(h).getInt("id_team") == teamsInsideIds.get(j) && operations.getJSONObject(h).getInt("id_project") == projectsIds.get(i)) {
                                        totalTasks++;
                                        if (operations.getJSONObject(h).getInt("taskIdProgressStatus") == 2) {
                                            totalToDo++;
                                        }
                                    }
                                }
                                projects.add(new Project(projectTitle, "something", firstUrl, secondUrl, thirdUrl, projectsIds.get(i), teamName, color, totalTasks, totalToDo, daysLeft));
                            }
                        }
                    }

                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

                    recyclerViewProject = findViewById(R.id.projectRecycleView);
                    recyclerViewProject.setHasFixedSize(true);
                    recyclerViewProject.setLayoutManager(layoutManager);
                    adapterProjects = new ProjectsViewAdapter(projects, self);
                    recyclerViewProject.setAdapter(adapterProjects);


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
    public void onProjectCardClick(int position) {
        Intent dashboardProjectDescriptionIntent = new Intent(this, DashboardProjectDescription.class);
        dashboardProjectDescriptionIntent.putExtra("token", tokenToBeSent);
        dashboardProjectDescriptionIntent.putExtra("id_project", projects.get(position).getId());
        startActivity(dashboardProjectDescriptionIntent);
    }

    @Override
    public void onLongClick(int position) {

    }
}
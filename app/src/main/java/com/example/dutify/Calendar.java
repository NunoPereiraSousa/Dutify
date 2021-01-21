package com.example.dutify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Calendar extends AppCompatActivity {
    BottomNavigationView bottomNavigation;

    private TextView currentDateTxt;
    private TextView selectedMonthTxt;
    private String tokenToBeSent;

    private TextInputEditText personalTaskNameInput;
    private TextInputEditText personalTaskDescInput;
    private TextInputEditText startingDateInput;
    private TextInputEditText endingDateInput;

    private CalendarView calendar;

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
                        if (date.equals(dataObj.getString("endDate"))) {
                            Toast.makeText(getApplicationContext(), dataObj.getString("title") + " task end here!", Toast.LENGTH_SHORT).show();
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.now();
        return date.format(formatter);
    }

    public Boolean compareDates(String a, String b, String d) {
        return a.compareTo(d) * b.compareTo(d) >= 0;
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

                        if (startDate.equals(currentDate()) ) {
                            Log.d("today", "check");
                        } else {
                            Log.d("not today", "upsi");
                        }

                        tasks.add(new Task(dataObj.getString("title"),
                                dataObj.getString("description"),
                                dataObj.getString("startDate"),
                                dataObj.getString("endDate"),
                                dataObj.getInt("id_progress_status")));
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
}
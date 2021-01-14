package com.example.dutify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    public TextInputEditText emailInput;
    public TextInputEditText passwordInput;
    private MaterialButton loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(final View v) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://dutify.herokuapp.com/login";

        Map<String, String> params = new HashMap<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://dutify.herokuapp.com/login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    emailInput = (TextInputEditText) findViewById(R.id.emailInput);
                    String emailTxt = emailInput.getText().toString();

                    passwordInput = (TextInputEditText) findViewById(R.id.passwordInput);
                    String passwordTxt = passwordInput.getText().toString();

                    JSONObject postData = new JSONObject();

                    postData.put("email", emailTxt);
                    postData.put("password", passwordTxt);

                    Log.d("postData: ", String.valueOf(postData));

                    String email = postData.getString("email");
                    String password = postData.getString("password");

                    Log.d("email", email);
                    Log.d("password", password);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Context context = getApplicationContext();
                Log.d("error:", String.valueOf(error));
                CharSequence text = "Could not connect to Dutify" + String.valueOf(error);
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", "admin");
                params.put("password", "root");
                return params;
            }
        };
        queue.add(stringRequest);
    }


    public void loginFunction(View v) {
        String postUrl = "https://dutify.herokuapp.com/login";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject postData = new JSONObject();
        try {
            emailInput = findViewById(R.id.emailInput);
            passwordInput = findViewById(R.id.passwordInput);
            String emailTxt = emailInput.getEditableText().toString();
            String passwordTxt = passwordInput.getEditableText().toString();

            postData.put("email", emailTxt);
            postData.put("password", passwordTxt);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String token = null;
                Integer personName = null;
                String userFullName = null;

                try {
                    token = response.getString("token");
                    personName = response.getInt("userType");
                    userFullName = response.getString("useFullName");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Context context = getApplicationContext();
                CharSequence text = String.valueOf(response);
                Toast toast = Toast.makeText(context, token, Toast.LENGTH_SHORT);
                toast.show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // String message1 = String.valueOf(error.networkResponse.statusCode);
                String message = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    public void saveToken(String Token) {
//        Context context = getApplicationContext();
//        File file = new File(context.getFilesDir(), "Dutify");
//        String filename = "Dutify";
//        String fileContents = "Hello world!";
//        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
//            fos.write(fileContents.toB);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }
}
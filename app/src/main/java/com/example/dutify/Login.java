package com.example.dutify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(final View v) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://dutify.herokuapp.com/login";

        Map<String, String> params = new HashMap<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                CharSequence text = "Could not connect to Dutify";
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
}
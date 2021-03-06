package com.example.dutify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class Login extends AppCompatActivity {

    public TextInputEditText emailInput;
    public TextInputEditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void loginFunction(View v) {
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        if (emailInput.getEditableText().toString().equals("") || passwordInput.getEditableText().toString().equals("")) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast,
                    (ViewGroup) findViewById(R.id.toast_layout));

            ImageView image = (ImageView) layout.findViewById(R.id.image);
            image.setImageResource(R.drawable.ic_logo);
            TextView text = (TextView) layout.findViewById(R.id.text);

            text.setText(getResources().getString(R.string.fill_inputs));

            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        } else {
            String postUrl = "https://dutify.herokuapp.com/login";
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject postData = new JSONObject();
            try {
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
                    Integer userType = null;
                    String userFullName = null;
                    try {
                        token = response.getString("token");
                        userType = response.getInt("userType");
                        userFullName = response.getString("userFullName");
                        enterApp(token, userType, userFullName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), new String(error.networkResponse.data, StandardCharsets.UTF_8), Toast.LENGTH_LONG).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
        }
    }

    public void openErrorPage() {
        Intent i = new Intent(this, ErrorPage.class);
        startActivity(i);
    }

    private void enterApp(String token, Integer userType, String userFullName) {
        if (userType != 3) {
            Intent welcomeIntent = new Intent(this, Welcome.class);
            welcomeIntent.putExtra("token", token);
            welcomeIntent.putExtra("userFullName", userFullName);
            startActivity(welcomeIntent);
        } else {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast,
                    (ViewGroup) findViewById(R.id.toast_layout));

            ImageView image = (ImageView) layout.findViewById(R.id.image);
            image.setImageResource(R.drawable.ic_logo);
            TextView text = (TextView) layout.findViewById(R.id.text);

            text.setText(getResources().getString(R.string.admin_pages));

            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }
    }
}
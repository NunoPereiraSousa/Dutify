package com.example.dutify;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PhoneEmailWebsite extends Fragment {

    TextView contactTxt;
    TextView emailTxt;
    TextView websiteTxt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_phone_email_website, container, false);
        contactTxt = (TextView) v.findViewById(R.id.contactTxt);
        emailTxt = (TextView) v.findViewById(R.id.emailTxt);
        websiteTxt = (TextView) v.findViewById(R.id.websiteTxt);
        Bundle receivingBundle = getArguments();

        contactTxt.setText(receivingBundle.getString("contact"));
        emailTxt.setText(receivingBundle.getString("email"));
        websiteTxt.setText(receivingBundle.getString("website"));

        return v;
    }






}
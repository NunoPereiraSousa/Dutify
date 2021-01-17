package com.example.dutify;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PhoneEmailWebsite extends Fragment {
    private PhoneEmailWebsiteListener listener;

    TextView contactTxt;
    TextView emailTxt;
    TextView websiteTxt;



    public interface PhoneEmailWebsiteListener {
        void changeTextViews(String contact, String email, String website);
    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_phone_email_website, container, false);
        contactTxt = (TextView) v.findViewById(R.id.contactTxt);
        emailTxt = (TextView) v.findViewById(R.id.emailTxt);
        websiteTxt = (TextView) v.findViewById(R.id.websiteTxt);

        // Inflate the layout for this fragment
        return v;
    }


    public void changeTextViews(String contact, String email, String website) {
        contactTxt.setText(contact);
        emailTxt.setText(email);
        websiteTxt.setText(website);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof PhoneEmailWebsiteListener) {
            listener = (PhoneEmailWebsiteListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement PhoneEmailWebsiteAListener");
        }
        ;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
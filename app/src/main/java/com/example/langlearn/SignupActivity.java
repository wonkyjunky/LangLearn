package com.example.langlearn;

import android.os.Bundle;

import com.example.langlearn.ui.login.LangLearnActivity;

public class SignupActivity extends LangLearnActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initInterface();
    }
}
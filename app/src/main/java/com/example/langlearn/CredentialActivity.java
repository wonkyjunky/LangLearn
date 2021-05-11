package com.example.langlearn;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.langlearn.fragment.LoginFragment;

public class CredentialActivity extends AppCompatActivity {

    TextView usernameText;
    TextView passwordText;

    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credential);

        fragmentManager.beginTransaction().replace(R.id.cred_frame_layout, new LoginFragment()).commit();
    }




}

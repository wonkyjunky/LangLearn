package com.example.langlearn;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.langlearn.Fragment.PostFragment;
import com.example.langlearn.fragment.ContactsFragment;
import com.example.langlearn.fragment.HomeFragment;

import com.example.langlearn.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        boolean newAccount = getIntent().getBooleanExtra("newacc", false);

        ParseUser u = ParseUser.getCurrentUser();

        if (u == null) {

            startActivity(new Intent(this, CredentialActivity.class));
            finish();
        }


        bottomNavigationView.setOnNavigationItemSelectedListener((MenuItem item) -> {
            Fragment fragment;
            switch(item.getItemId()) {
                case R.id.action_feed:
                    fragment = new PostFragment();
                    break;
                case R.id.action_profile:
                    fragment = new ProfileFragment();
                    break;
                case R.id.action_chat:
                    fragment = new ContactsFragment();
                    break;
                default:
                    fragment = new HomeFragment();
                    break;
            }
            fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
            return true;
        });

        if (newAccount) {
            bottomNavigationView.setSelectedItemId(R.id.action_profile);
        } else {
            bottomNavigationView.setSelectedItemId(R.id.action_home);
        }

    }
}


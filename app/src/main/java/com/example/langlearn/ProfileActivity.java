package com.example.langlearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfileActivity extends AppCompatActivity {
    Button btHome;
    Button btMessage;
    Button btPost;
    Button btProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btHome = findViewById(R.id.btHome);
        btMessage = findViewById(R.id.btMessage);
        btPost = findViewById(R.id.btPost);
        btProfile = findViewById(R.id.btProfile);


        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, MessageActivity.class);
                startActivity(i);
                finish();
            }
        });

        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, PostActivity.class);
                startActivity(i);
                finish();
            }
        });

        btProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, ProfileActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
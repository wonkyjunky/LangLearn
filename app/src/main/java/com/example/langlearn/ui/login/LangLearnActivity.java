package com.example.langlearn.ui.login;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.langlearn.MainActivity;
import com.example.langlearn.MessageActivity;
import com.example.langlearn.PostActivity;
import com.example.langlearn.ProfileActivity;
import com.example.langlearn.R;

public abstract class LangLearnActivity extends AppCompatActivity {

    protected Button homeButton;
    protected Button messageButton;
    protected Button postButton;
    protected Button profileButton;

    protected void initInterface() {

        homeButton = findViewById(R.id.nav_home_button);
        messageButton = findViewById(R.id.nav_message_button);
        postButton = findViewById(R.id.nav_post_button);
        profileButton = findViewById(R.id.nav_profile_button);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent i = new Intent(v.getContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), MessageActivity.class);
                startActivity(i);
                finish();
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), PostActivity.class);
                startActivity(i);
                finish();
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ProfileActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}

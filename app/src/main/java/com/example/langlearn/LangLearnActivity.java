package com.example.langlearn;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 *
 * This class is a wrapper for all the common UI elements that most activities will share.
 * There was a lot of boiler plate in all the Activities and I wanted to cut down on it
 *  - Ike
 *
 */
public abstract class LangLearnActivity extends AppCompatActivity {

    protected Button homeButton;
    protected Button messageButton;
    protected Button postButton;
    protected Button profileButton;

    protected void initInterface() {

        Context currentContext = this;
        homeButton = findViewById(R.id.nav_home_button);
        messageButton = findViewById(R.id.nav_message_button);
        postButton = findViewById(R.id.nav_post_button);
        profileButton = findViewById(R.id.nav_profile_button);

        homeButton.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {
                if (!(currentContext instanceof MainActivity)) {
                    Intent i = new Intent(v.getContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {
                if (!(currentContext instanceof MessageActivity)) {
                    Intent i = new Intent(v.getContext(), MessageActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {
                if (!(currentContext instanceof PostActivity)) {
                    Intent i = new Intent(v.getContext(), PostActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {
                if (!(currentContext instanceof ProfileActivity)) {
                    Intent i = new Intent(v.getContext(), ProfileActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }
}

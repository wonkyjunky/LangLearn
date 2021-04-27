package com.example.langlearn;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

    // UI elements
    protected Button homeButton;
    protected Button messageButton;
    protected Button postButton;
    protected Button profileButton;

//
//    @Override protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

    protected void initNavBar() {

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

    protected void logError(String err) {
        Log.e(this.getClass().getSimpleName(), err);
    }

    protected void logInfo(String info) {
        Log.i(this.getClass().getSimpleName(), info);
    }

    protected static String[] langNames = new String[] {
        "English",
        "German",
        "Korean",
        "Spanish",
        "French",
        "Japanese",
        "Simplified Chinese",
        "Traditional Chinese",
        "Vietnamese",
        "Russian",
        "Italian",
        "Indonesian"
    };

    protected static String[] langCodes = new String[] {
        "en",
        "de",
        "ko",
        "es",
        "fr",
        "ja",
        "zh-CN",
        "zh-TW",
        "vi",
        "ru",
        "it",
        "id",
    };

    public static String langNameFromCode(String langCode) {

        for (int i = 0; i < langCodes.length; ++i) {
            if (langCodes[i].equals(langCode)) return langNames[i];
        }
        return null;
    }

    public static String langCodeFromName(String langName) {

        for (int i = 0; i < langNames.length; ++i) {
            if (langNames[i].equals(langName)) return langCodes[i];
        }
        return null;
    }
}

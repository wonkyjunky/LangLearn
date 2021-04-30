package com.example.langlearn.Fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.langlearn.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragExampleActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag_example);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch(item.getItemId()) {
                    case R.id.action_home:
                        fragment = new HomeFragment();
                        break;
                    default:
                        fragment = new ExampleFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.action_home);

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
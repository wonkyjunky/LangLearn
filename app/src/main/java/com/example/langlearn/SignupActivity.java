package com.example.langlearn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;

public class SignupActivity extends AppCompatActivity {

    private Spinner langSpinner;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        langSpinner = findViewById(R.id.signup_lang_spinner);

        ArrayAdapter<String> langAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Util.langNames);
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langSpinner.setAdapter(langAdapter);

        Button signupButton = findViewById(R.id.signup_button);
        signupButton.setOnClickListener(oc -> signUp());

    }

    private void signUp() {

        TextView usernameText = findViewById(R.id.signup_username_text);
        TextView passwordText = findViewById(R.id.signup_password_text);

        // get username and password from ui
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        boolean incomplete = false;

        // handle empty username
        if (username.isEmpty()) {
            incomplete = true;
            usernameText.setError("Username must not be empty");
        }

        // handle empty password
        if (password.isEmpty()) {
            incomplete = true;
            passwordText.setError("Password must not be empty");
        }

        // if either was invalid, do no more
        if (incomplete) return;

        String langCode = Util.langCodes[langSpinner.getSelectedItemPosition()];

        Log.i("SignupActivity", "User has language: " + langCode);

        ParseUser user = new ParseUser();
        // setting credentials
        user.setUsername(username);
        user.setPassword(password);
        user.put("nativelang", langCode);

        user.signUpInBackground(e -> {
            if (e != null) {
                usernameText.setError(e.getMessage());
                Log.e(getClass().getSimpleName(), "Sign up failed: " + e.getMessage());
                return;
            }

            Log.i("SignupActivity", "Sign up successful for user: " + username);
            Toast.makeText(this, "Signed up user: " + username, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}

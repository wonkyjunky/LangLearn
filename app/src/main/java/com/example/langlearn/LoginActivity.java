package com.example.langlearn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    @Override public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initializing ui elements
        Button loginButton = findViewById(R.id.login_button);
        Button signupButton = findViewById(R.id.login_signup_button);

        // setting click listeners
        loginButton.setOnClickListener(oc -> logIn());
        signupButton.setOnClickListener(oc -> {
            startActivity(new Intent(this, SignupActivity.class));
            finish();
        });
    }

    private void logIn() {

        TextView usernameText = findViewById(R.id.login_username_text);
        TextView passwordText = findViewById(R.id.login_password_text);

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

        // attempt login with current credentials
        ParseUser.logInInBackground(username, password, (user, e) -> {

            if (e != null) {
                Log.e("LoginActivity", "Login failed: " + e.getMessage());
                usernameText.setError(e.getMessage());
                return;
            }
            Log.i("LoginActivity", "Login successful");

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        });
    }
}

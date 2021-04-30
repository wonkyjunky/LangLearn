package com.example.langlearn.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.langlearn.Fragment.FragExampleActivity;
import com.example.langlearn.LangLearnActivity;
import com.example.langlearn.MainActivity;
import com.example.langlearn.R;
import com.parse.ParseUser;

public class LoginActivity extends LangLearnActivity {

    private TextView usernameText;
    private TextView passwordText;
    private Button loginButton;
    private Button signupButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initializing ui elements
        usernameText = findViewById(R.id.login_username_text);
        passwordText = findViewById(R.id.login_password_text);
        loginButton = findViewById(R.id.login_button);
        signupButton = findViewById(R.id.login_signup_button);

        // setting click listeners
        loginButton.setOnClickListener(oc -> { logIn(); });
        signupButton.setOnClickListener(oc -> {
            startActivity(new Intent(this, SignupActivity.class));
            finish();
        });
    }

    private void logIn() {

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
                logError("Login failed: " + e.getMessage());
                usernameText.setError(e.getMessage());
                return;
            }
            logInfo("Login successful");

            Intent intent = new Intent(this, FragExampleActivity.class);
            startActivity(intent);
            finish();

        });
    }
}

package com.example.langlearn.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.langlearn.Fragment.FragExampleActivity;
import com.example.langlearn.LangLearnActivity;
import com.example.langlearn.MainActivity;
import com.example.langlearn.R;
import com.parse.ParseUser;

public class SignupActivity extends LangLearnActivity {

    private TextView usernameText;
    private TextView passwordText;
    private Button signupButton;
    private Spinner langSpinner;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameText = findViewById(R.id.signup_username_text);
        passwordText = findViewById(R.id.signup_password_text);
        signupButton = findViewById(R.id.signup_button);
        langSpinner = findViewById(R.id.signup_lang_spinner);

        ArrayAdapter<String> langAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, langNames);
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langSpinner.setAdapter(langAdapter);

        signupButton.setOnClickListener(oc -> { signUp(); });

    }

    private void signUp() {
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

        String langCode = langCodes[langSpinner.getSelectedItemPosition()];

        logInfo("User has language: " + langCode);

        ParseUser user = new ParseUser();
        // setting credentials
        user.setUsername(username);
        user.setPassword(password);
        user.put("nativelang", langCode);

        user.signUpInBackground(e -> {
            if (e != null) {
                usernameText.setError(e.getMessage());
                logError("Sign up failed: " + e.getMessage());
                return;
            }

            logInfo("Sign up successful for user: " + username);
            Toast.makeText(this, "Signed up user: " + username, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, FragExampleActivity.class);
            startActivity(intent);
            finish();
        });
    }
}

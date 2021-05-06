package com.example.langlearn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;

import java.util.Locale;

public class CredentialActivity extends AppCompatActivity {

    TextView usernameText;
    TextView passwordText;

    @Override public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials);

        // initializing ui elements
        usernameText = findViewById(R.id.cred_username_text);
        passwordText = findViewById(R.id.cred_password_text);
        Button loginButton = findViewById(R.id.cred_login_button);
        Button signupButton = findViewById(R.id.cred_signup_button);

        // setting click listeners
        loginButton.setOnClickListener(e -> logIn());
        signupButton.setOnClickListener(e -> signUp());
    }

    Pair<String, String> getCredentials() {

        if (usernameText.getText() == null){
            Toast.makeText(this, "Username is not entered", Toast.LENGTH_SHORT).show();
            return null;
        }

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

        if (incomplete) return null;

        return new Pair<>(username, password);
    }

    private void logIn() {

        Pair<String, String> cred = getCredentials();
        if (cred == null) return;

        // attempt login with current credentials
        ParseUser.logInInBackground(cred.first, cred.second, (user, e) -> {

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

    private void signUp() {

        Pair<String, String> cred = getCredentials();
        if (cred == null) return;

        ParseUser user = new ParseUser();
        // setting credentials
        user.setUsername(cred.first);
        user.setPassword(cred.second);
        String langCode = Locale.getDefault().getLanguage();
        Log.i("SignUp", "User has language: " + langCode);

        // check to see if the user's locale is supported by LangLearn
        boolean validLang = false;
        for (String s : Util.langCodes) {
            if (s.equals(langCode)) {
                validLang = true;
                break;
            }
        }

        // if not, set it to english
        if (!validLang) {
            langCode = Util.ENGLISH;
        }

        user.put("nativelang", langCode);

        user.signUpInBackground(e -> {

            if (e != null) {
                usernameText.setError(e.getMessage());
                Log.e(getClass().getSimpleName(), "Sign up failed: " + e.getMessage());
                return;
            }

            Log.i("SignupActivity", "Sign up successful for user: " + cred.first);
            Toast.makeText(this, "Signed up user: " + cred.first, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("newacc", true);
            startActivity(intent);
            finish();
        });
    }
}

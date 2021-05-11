package com.example.langlearn.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.langlearn.MainActivity;
import com.example.langlearn.R;
import com.parse.ParseUser;

public class LoginFragment extends Fragment
{
    EditText usernameEdit;
    EditText passwordEdit;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameEdit = view.findViewById(R.id.login_username_edit);
        passwordEdit = view.findViewById(R.id.login_password_edit);
        Button loginButton = view.findViewById(R.id.login_button);
        Button signupButton = view.findViewById(R.id.login_signup_button);

        // setting click listeners
        loginButton.setOnClickListener(e -> logIn());
        signupButton.setOnClickListener(e -> getActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.cred_frame_layout, new SignupFragment()).commit());

    }

    private void logIn() {

        String username = usernameEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        boolean incomplete = false;

        // handle empty username
        if (username.isEmpty()) {
            incomplete = true;
            usernameEdit.setError("Username must not be empty");
        }

        // handle empty password
        if (password.isEmpty()) {
            incomplete = true;
            passwordEdit.setError("Password must not be empty");
        }

        if (incomplete) return;

        // attempt login with current credentials
        ParseUser.logInInBackground(username, password, (user, e) -> {

            if (e != null) {
                Log.e("LoginActivity", "Login failed: " + e.getMessage());
                usernameEdit.setError(e.getMessage());
                return;
            }

            Log.i("LoginActivity", "Login successful");

            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
    }
}

package com.example.langlearn.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.langlearn.MainActivity;
import com.example.langlearn.R;
import com.example.langlearn.Util;
import com.parse.ParseUser;

public class SignupFragment extends Fragment {

    EditText usernameEdit, passwordEdit, confirmEdit;
    Spinner langSpinner;
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button signupButton = view.findViewById(R.id.signup_button);
        Button loginButton = view.findViewById(R.id.signup_login_button);

        usernameEdit = view.findViewById(R.id.signup_username_edit);
        passwordEdit = view.findViewById(R.id.signup_password_edit);
        confirmEdit = view.findViewById(R.id.signup_confirm_edit);
        langSpinner = view.findViewById(R.id.signup_lang_spinner);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Util.langNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langSpinner.setAdapter(dataAdapter);

        signupButton.setOnClickListener(e -> signUp());
        loginButton.setOnClickListener(e -> getActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.cred_frame_layout, new LoginFragment()).commit());
    }

    private void signUp() {

        String username = usernameEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String confirm = confirmEdit.getText().toString();

        boolean incomplete = false;
        if (username.isEmpty()) {
            usernameEdit.setError("Username must not be empty");
            incomplete = true;
        }

        if (password.isEmpty()) {
            passwordEdit.setError("Password must not be empty");
            incomplete = true;
        }
        if (confirm.isEmpty()) {
            confirmEdit.setError("Password confirmation must not be empty");
            incomplete = true;
        }

        if (incomplete) return;

        if (!password.equals(confirm)) {
            confirmEdit.setError("Passwords must match");
            return;
        }

        ParseUser user = new ParseUser();
        // setting credentials
        user.setUsername(username);
        user.setPassword(password);
        String langCode = Util.langCodes[langSpinner.getSelectedItemPosition()];
        Log.i("SignUp", "User has language: " + langCode);

        user.put("nativelang", langCode);

        user.signUpInBackground(e -> {

            if (e != null) {
                usernameEdit.setError(e.getMessage());
                Log.e(getClass().getSimpleName(), "Sign up failed: " + e.getMessage());
                return;
            }

            Log.i("SignupActivity", "Sign up successful for user: " + username);

            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
    }
}

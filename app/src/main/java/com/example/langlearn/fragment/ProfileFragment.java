package com.example.langlearn.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.langlearn.CredentialActivity;
import com.example.langlearn.R;
import com.example.langlearn.Util;
import com.parse.ParseUser;

import java.net.URL;

public class ProfileFragment extends Fragment {

    Spinner langSpinner;
    EditText imgEdit;
    TextView greetingText;
    ImageView profileImage;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // getting ui elements
        Button updateButton = view.findViewById(R.id.profile_update_button);
        Button logoutButton = view.findViewById(R.id.profile_logout_button);

        imgEdit = view.findViewById(R.id.profile_img_edit);
        greetingText = view.findViewById(R.id.profile_greeting_text);
        profileImage = view.findViewById(R.id.profile_img);

        langSpinner = view.findViewById(R.id.profile_lang_spinner);

        // setting up lang spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Util.langNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langSpinner.setAdapter(dataAdapter);

        logoutButton.setOnClickListener(e -> logOut());
        updateButton.setOnClickListener(e -> updateAccount());

        updateUi();
    }

    private void updateUi() {

        ParseUser u = ParseUser.getCurrentUser();

        // downloag profile image
        new Thread(() -> {
            try {
                URL imageUrl = new URL(u.getString(Util.PROFILE_IMG));
                Bitmap bmp = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                if (bmp != null) {
                    getActivity().runOnUiThread(() -> profileImage.setImageBitmap(bmp));
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Update UI
        getActivity().runOnUiThread(() -> {
            greetingText.setText("Welcome, " + u.getUsername() + "!");
            String imgUrl = u.getString(Util.PROFILE_IMG);
            imgEdit.setText((imgUrl != null) ? imgUrl : "");
        });

        // getting language code
        String langCode = u.getString("nativelang");

        // getting spinner index based on lang code
        int spinnerIndex = Util.getLangCodeIndex(langCode);
        if (spinnerIndex == -1) {
            Log.e("ProfileFragement", "User has invalid language: " + langCode);
            spinnerIndex = 0;
        }

        langSpinner.setSelection(spinnerIndex);

    }

    private void updateAccount() {

        ParseUser u = ParseUser.getCurrentUser();

        String langCode = Util.langCodes[langSpinner.getSelectedItemPosition()];
        u.put(Util.NATIVE_LANG, langCode);

        String imgUrl = imgEdit.getText().toString();
        u.put(Util.PROFILE_IMG, imgUrl);

        u.saveInBackground(e -> {
            if (e != null) {

                imgEdit.setError(e.getMessage());
                Log.e("ProfileActivity", e.getMessage());
                return;
            }
            Log.i("ProfileActivity", "Profile updated");
            Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
        });

        updateUi();
    }

    private void logOut() {
        String username = ParseUser.getCurrentUser().getString("username");
        Log.i("LoginActivity", "Logging out user: " + username);
        ParseUser.logOutInBackground();
        Intent intent = new Intent(getActivity(), CredentialActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
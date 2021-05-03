package com.example.langlearn.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.langlearn.LoginActivity;
import com.example.langlearn.R;
import com.example.langlearn.Util;
import com.parse.ParseUser;

import java.net.URL;

public class ProfileFragment extends Fragment {

    private Button logoutButton;
    private TextView greetingText;
    private TextView descrText;
    private ImageView profileImage;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        logoutButton = view.findViewById(R.id.profile_logout_button);
        greetingText = view.findViewById(R.id.profile_greeting_text);
        descrText = view.findViewById(R.id.profile_descr_text);
        profileImage = view.findViewById(R.id.profile_img);

        logoutButton.setOnClickListener(v1 -> { logOut(); });

        // Horrible disgusting callback mess to download image and change imageview
        new Thread() {
            @Override public void run() {
                try {
                    URL imageUrl = new URL(ParseUser.getCurrentUser().getString("profileimg"));
                    Bitmap bmp = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override public void run() {
                            profileImage.setImageBitmap(bmp);

                        }
                    });

                } catch(Exception e) {
                    e.printStackTrace();

                }
            }
        }.start();

        ParseUser u = ParseUser.getCurrentUser();
        updateUi(u.getUsername(), "Native Language: " + Util.langNameFromCode(u.getString("nativelang")));
    }

    private void logOut() {
        Log.i("LoginActivity", "Log out button pressed");
        String username = ParseUser.getCurrentUser().getString("username");
        Toast.makeText(getActivity(), "Logging out user: " + username, Toast.LENGTH_SHORT).show();
        ParseUser.logOutInBackground();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void updateUi(String username, String descr) {
        getActivity().runOnUiThread(new Runnable() {
            @Override public void run() {
                greetingText.setText("Welcome, " + username + "!");
                descrText.setText(descr);
            }
        });
    }
}
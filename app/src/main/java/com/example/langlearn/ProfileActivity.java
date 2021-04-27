package com.example.langlearn;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.langlearn.ui.login.LoginActivity;
import com.parse.ParseUser;

import java.net.URL;

public class ProfileActivity extends LangLearnActivity {

	private Button logoutButton;
	private TextView greetingText;
	private TextView descrText;
	private ImageView profileImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		initNavBar();

		logoutButton = findViewById(R.id.profile_logout_button);
		greetingText = findViewById(R.id.profile_greeting_text);
		descrText = findViewById(R.id.profile_descr_text);
		profileImage = findViewById(R.id.profile_img);

		logoutButton.setOnClickListener(v1 -> { logOut(); });

		// Horrible disgusting callback mess to download image and change imageview
		new Thread() {
			@Override public void run() {
			try {
				URL imageUrl = new URL(ParseUser.getCurrentUser().getString("profileimg"));
				Bitmap bmp = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
				runOnUiThread(new Runnable() {
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
		updateUi(u.getUsername(), "Native Language: " + langNameFromCode(u.getString("nativelang")));
	}

	private void logOut() {
		Log.i("LoginActivity", "Log out button pressed");
		String username = ParseUser.getCurrentUser().getString("username");
		Toast.makeText(this, "Logging out user: " + username, Toast.LENGTH_SHORT).show();
		ParseUser.logOutInBackground();
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	private void updateUi(String username, String descr) {
		runOnUiThread(new Runnable() {
			@Override public void run() {
				greetingText.setText("Welcome, " + username + "!");
				descrText.setText(descr);
			}
		});
	}
}


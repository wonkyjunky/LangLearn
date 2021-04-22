package com.example.langlearn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

	private TextView greetingText;
	private TextView descrText;

	Button btHome;
	Button btMessage;
	Button btPost;
	Button btProfile;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		btHome = findViewById(R.id.btHome);
		btMessage = findViewById(R.id.btMessage);
		btPost = findViewById(R.id.btPost);
		btProfile = findViewById(R.id.btProfile);

		greetingText = findViewById(R.id.profile_greeting_text);
		descrText = findViewById(R.id.profile_descr_text);

		//
		// Code to get account info in which name and descr would be set
		//

		String name = "Ike";
		String descr = "This is a placeholder description for the profile page";

		greetingText.setText("Welcome, " + name + "!");
		descrText.setText(descr);

		btHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ProfileActivity.this, MainActivity.class);
				startActivity(i);
				finish();
			}
		});

		btMessage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ProfileActivity.this, MessageActivity.class);
				startActivity(i);
				finish();
			}
		});

		btPost.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ProfileActivity.this, PostActivity.class);
				startActivity(i);
				finish();
			}
		});

		btProfile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ProfileActivity.this, ProfileActivity.class);
				startActivity(i);
				finish();
			}
		});


	}
}


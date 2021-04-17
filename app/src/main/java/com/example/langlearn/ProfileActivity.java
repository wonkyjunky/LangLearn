package com.example.langlearn;

<<<<<<< HEAD
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

	private TextView greetingText;
	private TextView descrText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		greetingText = findViewById(R.id.profile_greeting_text);
		descrText = findViewById(R.id.profile_descr_text);

		//
		// Code to get account info in which name and descr would be set
		//

		String name = "Ike";
		String descr = "This is a placeholder description for the profile page";

		greetingText.setText("Welcome, " + name + "!");
		descrText.setText(descr);

	}
}
=======
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }
}
>>>>>>> 154085833970ee9230ac911bfa91c05bacdd9a5a

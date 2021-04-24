package com.example.langlearn;

import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends LangLearnActivity {

	private TextView greetingText;
	private TextView descrText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		initInterface();

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


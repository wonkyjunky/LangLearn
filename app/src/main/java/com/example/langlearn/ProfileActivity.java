package com.example.langlearn;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;

public class ProfileActivity extends LangLearnActivity {

	private TextView greetingText;
	private TextView descrText;
	private ImageView profileImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		initInterface();

		greetingText = findViewById(R.id.profile_greeting_text);
		descrText = findViewById(R.id.profile_descr_text);
		profileImage = findViewById(R.id.profile_img);
		
		// Horrible horrible disgusting callback mess to download image and change imageview
		new Thread() {
			@Override public void run() {
				try {

					Log.i("Hello", "My Guy");
					URL imageUrl = new URL("https://www.tenforums.com/geek/gars/images/2/types/thumb_15951118880user.png");
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



		// https://www.tenforums.com/geek/gars/images/2/types/thumb_15951118880user.png

		//
		// Code to get account info in which name and descr would be set
		//

		String name = "Ike";
		String descr = "This is a placeholder description for the profile page";

		greetingText.setText("Welcome, " + name + "!");
		descrText.setText(descr);
	}
}


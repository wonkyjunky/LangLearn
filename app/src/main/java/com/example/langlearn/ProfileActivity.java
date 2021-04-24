package com.example.langlearn;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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

		userId = "CABSdzlZz2";

		ParseQuery<ParseUser> query = ParseUser.getQuery();

		query.getInBackground(userId, new GetCallback<ParseUser>() {

			@Override public void done(ParseUser o, ParseException e) {

				if (e == null) {

					String username = o.getString("username");
					String language = o.getString("nativelang");
					String descr = "Native Language: " + langNameFromCode(language);
					updateUi(username, descr);

				} else {
					// Error
					logError("Failed to create query!");
					e.printStackTrace();

				}
			}
		});
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


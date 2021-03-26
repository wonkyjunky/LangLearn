package com.example.langlearn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);

        ParseObject firstObject = new ParseObject("FirstClass");

        firstObject.put("Message", "Hey! Welcome to LangLearn. Parse is now connected");


        firstObject.saveInBackground(e -> {

            if (e != null) {
                Toast.makeText(this, "Fail to add data..." + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            else {
                textView.setText(String.format("Data saved is: \n %s", firstObject.get("Message")));
            }
        });
    }
}
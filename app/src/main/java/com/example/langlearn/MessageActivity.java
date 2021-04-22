package com.example.langlearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MessageActivity extends AppCompatActivity {
    EditText text;
    Button send;
    Button retrieve;

    Button btHome;
    Button btMessage;
    Button btPost;
    Button btProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        btHome = findViewById(R.id.btHome);
        btMessage = findViewById(R.id.btMessage);
        btPost = findViewById(R.id.btPost);
        btProfile = findViewById(R.id.btProfile);


        int userCount = 1;
        String[] Users = new String[userCount];
        text = findViewById(R.id.editTextTextMultiLine);
        send = findViewById(R.id.sendButton);
        retrieve = findViewById(R.id.buttonRetrieve);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = findViewById(R.id.editTextTextMultiLine);
                TextView textView = findViewById(R.id.textView2);
                ParseObject firstObject = new ParseObject("FirstClass");
                firstObject.put("Message", text.getText().toString());
                firstObject.saveInBackground(e -> {
                    if (e != null) {
                        textView.setText(String.format("Fail to add data..." + e.getLocalizedMessage(), firstObject.get("Message")));
                    }
                    else {
                        textView.setText(String.format("Data saved is: \n %s", firstObject.get("Message")));
                    }
                });
            }
        });
        retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMessage();
            }
        });

        wireUpButtons();

    }

    void getMessage(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FirstClass");
        query.whereEqualTo("objectId", "Uv2xUdlOdi");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject message, ParseException e) {
                if (e == null) {
                    String Message = message.getString("Message");
                    Log.e("GFSDGSFDGFDSGFSD", "done: " + Message );
                } else {
                    // Something is wrong
                }
            }
        });
    }

    void wireUpButtons(){
        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MessageActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MessageActivity.this, MessageActivity.class);
                startActivity(i);
                finish();
            }
        });

        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MessageActivity.this, PostActivity.class);
                startActivity(i);
                finish();
            }
        });

        btProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MessageActivity.this, ProfileActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
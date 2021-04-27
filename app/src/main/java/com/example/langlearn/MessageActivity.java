package com.example.langlearn;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MessageActivity extends LangLearnActivity {

    EditText text;
    Button send;
    Button retrieve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initNavBar();

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
}
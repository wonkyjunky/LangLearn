package com.example.langlearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ContactsActivity extends LangLearnActivity {
    final String TAG = "DEBUG";
    ConstraintLayout layout;
    LinearLayout linearLay;

    Button but;
    ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        initNavBar();
        layout = findViewById(R.id.layout);
        linearLay = findViewById(R.id.linLay);
        but = findViewById(R.id.buttonTesting);
        currentUser = ParseUser.getCurrentUser();
        fillUsers();


    }

    public void fillUsers() {
        LinearLayout Users = new LinearLayout(this);
        Users.setOrientation(LinearLayout.VERTICAL);
        Context Screen = this.getApplicationContext();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        for (int i = 0; i < 6; i++) {
            query.whereEqualTo("UserId", i);
            query.findInBackground((users, e) -> {
                if (e == null) {
                    for (ParseUser user1 : users) {
                      String Username = user1.getString("username");
                        Log.d(TAG, "fillUsers: " + Username);
                        LinearLayout Wrap = new LinearLayout(Screen);
                        TextView Userinfo = new TextView(Screen);
                        Userinfo.setText(Username);
                        Userinfo.setPadding(500,0,0,0);
                        Wrap.addView(Userinfo);
                        Wrap.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout Interaction = new LinearLayout(Screen);
                        Interaction.setOrientation(LinearLayout.VERTICAL);
                        //Message Buttons
                        Button Message = new Button(Screen);
                        Message.setPadding(10,0,10,0);
                        Message.setText("Message");
                        Message.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(getApplicationContext(), MessageActivity.class);
                                i.putExtra("UserTo", Userinfo.getText().toString());
                                i.putExtra("UserFrom", currentUser);
                                //startActivity(i);
                            }
                        });
                        Interaction.addView(Message);

                        Wrap.addView(Interaction);
                        Users.addView(Wrap);

                    }
                    runOnUiThread(()->{
                        if(Users.getParent() != null) {
                            ((ViewGroup)Users.getParent()).removeView(Users); // <- fix
                        }
                        linearLay.addView(Users);
                    });
                } else {
                    // Something went wrong.
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
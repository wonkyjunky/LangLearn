package com.example.langlearn;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Arrays;

public class PostActivity extends LangLearnActivity {
    Button CreatePOST;
    ConstraintLayout board;
    LinearLayout ll;

    int post_count =0;
    String post_data = "";
    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initNavBar();
        board = findViewById(R.id.post_act_board);
        ll = findViewById(R.id.ll);

        CreatePOST = findViewById(R.id.POST);
        user = ParseUser.getCurrentUser();

        CreatePOST.setOnClickListener(v1->{
            /*
            * hide navbar
            * */
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Create Post");

                // Set an EditText view to get user input
            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("POST", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    post_data = input.getText().toString();
                    post();
                    // Do something with value!
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });

            alert.show();
        });

        get_post();

    }

    public void comment(String OG_post){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        if(OG_post.length()>10){
            alert.setTitle("Replying to: "+OG_post.substring(0,15)+"...");
        }else{
            alert.setTitle("Replying to: "+OG_post);
        }


        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Comment", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                post_data = input.getText().toString();
                post();
                // Do something with value!
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.show();
    }

    public void get_post(){
        LinearLayout Post = new LinearLayout(this);
        Post.setOrientation(LinearLayout.VERTICAL);
        Context screen = this.getApplicationContext();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
        query.addDescendingOrder("createdAt");
        try {
            query.setLimit(query.count());//this too
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //global count of posts to make for offset

        for (int i =0; i <10;i++){
            query.setSkip(i+post_count);//important
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject player, ParseException e) {
                    if (e == null) {
                        String return_Post = player.getString("Post");
                        LinearLayout Post_interaction_wrapper = new LinearLayout(screen);
                        //get message
                        TextView Post_info = new TextView(screen);
                        Post_info.setTextSize(20);
                        Post_info.setBackgroundColor(0x222222);
                        Post_info.setText(return_Post);
                        Post_interaction_wrapper.addView(Post_info);
                        Post_interaction_wrapper.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout Post_interaction = new LinearLayout(screen);
                        Post_interaction.setOrientation(LinearLayout.HORIZONTAL);

                        //Like button
                        Button Like = new Button(screen);
                        //Like.setBackgroundColor(Color.GRAY);
                        Like.setPadding(10,0,10,0);
                        Like.setText("Like");
                        Post_interaction.addView(Like);

                        //Comment button
                        Button Comment = new Button(screen);
                        Comment.setPadding(10,0,10,0);
                        Comment.setText("comment");
                        Comment.setOnClickListener(v1->{
                            comment(return_Post);
                        });
                        //Comment.setBackgroundColor(Color.DKGRAY);
                        Post_interaction.addView(Comment);

                        Button Message = new Button(screen);
                        Message.setPadding(10,0,10,0);
                        Message.setText("Message");
                        //Message.setBackgroundColor(Color.GRAY);
                        Post_interaction.addView(Message);

                        Post_interaction_wrapper.addView(Post_interaction);
                        Post.addView(Post_interaction_wrapper);
                    } else {
                        // Something is wrong
                    }
                }
            });
        }




        runOnUiThread(()->{
            ll.addView(Post);
        });
    }

    public void post(){
        ParseObject soccerPlayers = new ParseObject("Posts");
        // Store an object
        soccerPlayers.put("User", user.getUsername());
        soccerPlayers.put("Post", post_data);
        soccerPlayers.addAllUnique("attributes", Arrays.asList("fast", "good conditioning"));
        // Saving object
        soccerPlayers.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("update:","table");
                    // Success
                } else {
                    // Error
                }
            }
        });
    }


}
/*
* future code
*
* */

package com.example.langlearn;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class PostActivity extends LangLearnActivity {
    Button CreatePOST;
    ConstraintLayout board;
    LinearLayout ll;

    int post_count =0;
    String post_data = "";
    String post_comment = "";
    ParseUser user;
    Context screen;
    int screen_width =0;
    int screen_height=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initNavBar();
        screen = this.getApplicationContext();
        board = findViewById(R.id.post_act_board);
        ll = findViewById(R.id.ll);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screen_width = displayMetrics.widthPixels;
        screen_height = displayMetrics.heightPixels;

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
        Thread setup = new Thread(()->{
            get_post();
        });
        setup.start();

    }

    public void Post_Comment(String OG_ID){
        ParseObject soccerPlayers = new ParseObject("Comments");
        // Store an object
        soccerPlayers.put("Origin",OG_ID);
        soccerPlayers.put("replier", user.getUsername());
        soccerPlayers.put("Comment", post_comment);

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

    public void comment(String OG_post,String Origin_post){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        if(OG_post.length()>15){
            alert.setTitle("Replying to: "+OG_post.substring(0,15)+"...");
        }else{
            alert.setTitle("Replying to: "+OG_post);
        }


        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Comment", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                post_comment = input.getText().toString();
                Post_Comment(Origin_post);
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
                        String Orgin_post = player.getString("User")+player.getString("Post");
                        LinearLayout tmp = PostFrame(return_Post,Orgin_post);
                        Post.addView(tmp);

                    } else {
                        // Something is wrong
                        return;
                    }
                }
            });
        }

        runOnUiThread(()->{
            ll.addView(Post);
        });
    }

    public void View_Comment(String OG_post,String Origin_post){
        //get content
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(OG_post);

        ArrayList<String> comment_seciont = new ArrayList<>();
        Context screen = this.getApplicationContext();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Comments");
            query.addDescendingOrder("Likes");
            Log.d("Origin_post",Origin_post);
            query.whereContains("Origin",Origin_post);
            AtomicBoolean show = new AtomicBoolean(true);
            ScrollView scroller = new ScrollView(screen);
            LinearLayout tmp = new LinearLayout(screen);
            for (int i =0;i<10;i++){
                query.setSkip(i);
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject comments, ParseException e) {
                        if(e==null){
                            String return_Post = comments.getString("Comment");
                            alert.setMessage(return_Post);
                            alert.setNegativeButton("Done", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            });
                            tmp.addView(PostFrame(return_Post,""));

                            Log.d("Post","ed");
                           // return;
                        }else{
                            runOnUiThread(()->{
                                if(show.get()){
                                    scroller.addView(tmp);
                                    alert.setView(scroller);
                                    alert.show();
                                    show.set(false);
                                }

                            });
                            return;
                            //e.printStackTrace();
                        }

                    }
                });
            }


        //finish content

        //loading spinner

        //end spinner

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


    public LinearLayout PostFrame(String return_Post, String Orgin_post){
        LinearLayout Post = new LinearLayout(this);
        LinearLayout Post_interaction_wrapper = new LinearLayout(screen);

        Post_interaction_wrapper.setMinimumWidth(screen_width);
        //get message
        TextView Post_info = new TextView(screen);
        Post_info.setWidth(screen_width-40);
        Post_info.setHeight(screen_height/6);
        Post_info.setTextSize(20);
        Post_info.setBackgroundColor(Color.rgb(230,230,230));
        Post_info.setPadding(10,100,10,0);
        Post_info.setX(20);
        Post_info.setText(return_Post);

        Post_interaction_wrapper.addView(Post_info);
        Post_interaction_wrapper.setOrientation(LinearLayout.VERTICAL);
        Post_interaction_wrapper.setBackgroundColor(Color.rgb(230,230,230));


        LinearLayout Post_interaction = new LinearLayout(screen);
        Post_interaction.setBackgroundColor(Color.rgb(230,230,230));

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
            comment(return_Post,Orgin_post);
        });
        //Comment.setBackgroundColor(Color.DKGRAY);
        Post_interaction.addView(Comment);

        Button ViewComment = new Button(screen);
        ViewComment.setPadding(10,0,10,0);
        ViewComment.setText("View Comments");
        ViewComment.setOnClickListener(v1->{
            Thread task1 = new Thread(()->{
                View_Comment(return_Post,Orgin_post);
            });
            task1.start();
        });
        //Message.setBackgroundColor(Color.GRAY);
        Post_interaction.addView(ViewComment);

        Button Message = new Button(screen);
        Message.setPadding(10,0,10,0);
        Message.setText("Message");
        //Message.setBackgroundColor(Color.GRAY);
        Post_interaction.addView(Message);

        Post_interaction_wrapper.addView(Post_interaction);
        TextView Boarder = new TextView(screen);
        Boarder.setWidth(screen_width);
        Boarder.setHeight(3);
        Boarder.setBackgroundColor(Color.rgb(150,150,150));
        Post_interaction_wrapper.addView(Boarder);
        Post.addView(Post_interaction_wrapper);
        return Post;
    }

}
/*
* future code
*
* */

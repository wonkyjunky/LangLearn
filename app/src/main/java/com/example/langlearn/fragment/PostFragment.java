package com.example.langlearn.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.langlearn.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Arrays;
import java.util.List;


public class PostFragment extends Fragment {

    ImageButton CreatePOST;
    ConstraintLayout board;
    LinearLayout ll;
    Dialog alert;
    Activity activity;

    String post_data = "";
    String post_comment = "";
    ParseUser user;
    Context screen;
    int screen_width = 0;
    int screen_height = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        screen = activity.getApplicationContext();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screen_width = displayMetrics.widthPixels;
        screen_height = displayMetrics.heightPixels;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        board = view.findViewById(R.id.post_act_board);
        ll = view.findViewById(R.id.ll);


        CreatePOST = view.findViewById(R.id.btnMessage);
        user = ParseUser.getCurrentUser();

        CreatePOST.setOnClickListener(v1->{
            /*
             * hide navbar
             * */
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

            alert.setTitle("Create Post");

            // Set an EditText view to get user input
            final EditText input = new EditText(getActivity());
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
        AlertDialog.Builder alert = new AlertDialog.Builder(screen);
        if(OG_post.length()>15){
            alert.setTitle("Replying to: "+OG_post.substring(0,15)+"...");
        }else{
            alert.setTitle("Replying to: "+OG_post);
        }


        // Set an EditText view to get user input
        final EditText input = new EditText(screen);
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

    public void get_post() {
        LinearLayout Post = new LinearLayout(screen);
        Post.setOrientation(LinearLayout.VERTICAL);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
        query.addDescendingOrder("createdAt");
        //global count of posts to make for offset
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (ParseObject player : objects) {
                    if (e == null) {
                        String return_Post = player.getString("Post");
                        String Orgin_post = player.getString("User") + player.getString("Post");
                        LinearLayout tmp = PostFrame(return_Post, Orgin_post);
                        Post.addView(tmp);
                    } else {
                        // Something is wrong
                        return;
                    }
                }

                activity.runOnUiThread(() -> {
                    ll.addView(Post);
                });
            }
        });
    }

    public void View_Comment(String OG_post,String Origin_post){
        //get content

        alert.dismiss();
        activity.runOnUiThread(()->{
            alert.setTitle(OG_post);
        });

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Comments");
        query.addDescendingOrder("Likes");
        Log.d("Origin_post",Origin_post);
        query.whereContains("Origin",Origin_post);
        ScrollView scroller = new ScrollView(screen);
        LinearLayout tmp = new LinearLayout(screen);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tmp.setLayoutParams(params);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for(ParseObject comments : objects) {
                    if (e == null) {
                        String return_Post = comments.getString("Comment");
                        tmp.addView(PostFrame(return_Post, ""));
                        Log.d("Post", tmp.getChildCount() + " " + return_Post);
                        // return;
                    }
                }

                activity.runOnUiThread(() -> {
                    scroller.invalidate();
                    scroller.requestLayout();
                    scroller.addView(tmp);
                    LinearLayout scroll_wrapper = new LinearLayout(screen);
                    scroll_wrapper.setOrientation(LinearLayout.VERTICAL);
                    scroll_wrapper.addView(scroller);
                    alert.addContentView(
                            scroll_wrapper, new LinearLayout.LayoutParams(
                                    new LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)));
                    alert.show();
                });

            }

        });
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
        LinearLayout Post = new LinearLayout(screen);
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
package com.example.langlearn.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.langlearn.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {

    ImageButton CreatePOST;
    ConstraintLayout board;
    LinearLayout ll;

    int post_count =0;
    String post_data = "";
    String post_comment = "";
    ParseUser user;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance(String param1, String param2) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        CreatePOST = view.findViewById(R.id.POST);
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

        try {
            get_post();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        if(OG_post.length()>10){
            alert.setTitle("Replying to: "+OG_post.substring(0,15)+"...");
        }else{
            alert.setTitle("Replying to: "+OG_post);
        }


        // Set an EditText view to get user input
        final EditText input = new EditText(getActivity());
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

    public void get_post() throws ParseException {
        LinearLayout Post = new LinearLayout(getActivity());
        Post.setOrientation(LinearLayout.VERTICAL);
        Context screen = getActivity();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
        query.addDescendingOrder("createdAt");
        try {
            query.setLimit(query.count());//this too
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //global count of posts to make for offset

        for (int i =0; i <query.count(); i++){
            query.setSkip(i+post_count);//important
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject player, ParseException e) {
                    if (e == null) {
                        String return_Post = player.getString("Post");
                        String Orgin_post = player.getString("User")+player.getString("Post");
                        Log.d("ID",Orgin_post);
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
                            comment(return_Post,Orgin_post);
                        });
                        //Comment.setBackgroundColor(Color.DKGRAY);
                        Post_interaction.addView(Comment);

                        Button ViewComment = new Button(screen);
                        ViewComment.setPadding(10,0,10,0);
                        ViewComment.setText("View Comments");
                        //Message.setBackgroundColor(Color.GRAY);
                        Post_interaction.addView(ViewComment);

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




        getActivity().runOnUiThread(()->{
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
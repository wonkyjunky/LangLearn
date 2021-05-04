package com.example.langlearn.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.langlearn.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MessageFragment extends Fragment {
    ParseUser currentUser;
    ConstraintLayout layout;
    Activity activity;
    LinearLayout linearLay;
    Context Screen;
    EditText text;
    Button send;
    String UserTo;
    String UserToName;
    final String TAG = "DEBUG: MESSAGE ACTIVITY:";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        Screen = activity.getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layout = view.findViewById(R.id.LayoutM);
        linearLay = view.findViewById(R.id.MessageLayout);
        send = view.findViewById(R.id.sendMessageButt);
        text = view.findViewById(R.id.messageText);
        currentUser = ParseUser.getCurrentUser();
        Bundle args = getArguments();
        UserTo = args.getString("OID");
        UserToName = args.getString("name");
        Log.d(TAG, "onCreate: " + UserTo);
        try {
            getInitMessages();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text.getText().toString();
                ParseObject message = new ParseObject("Messages");
                message.put("message", msg);
                message.put("to",UserTo);
                message.put("from",currentUser.getObjectId());
                message.saveInBackground(e -> {
                    if(e==null){
                        Log.d(TAG, "onClick: POGCHAMP MESSAGE SUBMITTED JUST DO LIVE UPDATE AND WE GUCCI");
                    }
                });
            }
        });
    }


    public void getInitMessages() throws ParseException {
        LinearLayout Messages = new LinearLayout(Screen);
        Messages.setOrientation(LinearLayout.VERTICAL);
        Log.d(TAG, "getInitMessages: " + ParseUser.getCurrentUser().getObjectId());
        ParseQuery<ParseUser> query = ParseQuery.getQuery("Messages");
        Log.d(TAG, "getInitMessages: " + query.count());
        for (int i = 0; i < 1; i++) {
            query.orderByDescending("to");
            query.findInBackground((messages, e) -> {
                if (e == null) {
                    for (ParseObject messages1 : messages) {
                        if(UserTo.equals(messages1.getString("to")) && currentUser.getObjectId().equals(messages1.getString("from")) ||
                                UserTo.equals(messages1.getString("from")) && currentUser.getObjectId().equals(messages1.getString("to"))) {
                            if(UserTo.equals(messages1.get("to"))){
                                String Message = messages1.getString("message");
                                Log.d(TAG, "fillUsers: " + Message);
                                LinearLayout Wrap = new LinearLayout(Screen);
                                //Messages
                                TextView Userinfo = new TextView(Screen);
                                Userinfo.setText(currentUser.getUsername()+": "+ Message);
                                Userinfo.setPadding(300, 0, 0, 0);
                                Wrap.addView(Userinfo);

                                Wrap.setOrientation(LinearLayout.VERTICAL);
                                LinearLayout Interaction = new LinearLayout(Screen);
                                Interaction.setOrientation(LinearLayout.VERTICAL);
                                //add wrapper to constrant
                                Wrap.addView(Interaction);
                                Messages.addView(Wrap);
                            } else{
                                String Message = messages1.getString("message");
                                Log.d(TAG, "fillUsers: " + Message);
                                LinearLayout Wrap = new LinearLayout(Screen);
                                //Messages
                                TextView Userinfo = new TextView(Screen);
                                Userinfo.setText(UserToName+": "+Message);
                                Userinfo.setPadding(300, 0, 0, 0);
                                Wrap.addView(Userinfo);

                                Wrap.setOrientation(LinearLayout.VERTICAL);
                                LinearLayout Interaction = new LinearLayout(Screen);
                                Interaction.setOrientation(LinearLayout.VERTICAL);
                                //add wrapper to constrant
                                Wrap.addView(Interaction);
                                Messages.addView(Wrap);
                            }

                        }
                    }
                    activity.runOnUiThread(() -> {
                        if (Messages.getParent() != null) {
                            ((ViewGroup) Messages.getParent()).removeView(Messages); // <- fix
                        }
                        //add to Main Constraint which displays on fragment;
                        linearLay.addView(Messages);
                    });
                } else {
                }
            });
        }
    }
}
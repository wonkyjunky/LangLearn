package com.example.langlearn.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.langlearn.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExampleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        Screen = activity.getApplicationContext();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
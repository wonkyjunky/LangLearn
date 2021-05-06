package com.example.langlearn.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.fragment.app.FragmentTransaction;

import com.parse.Parse;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;


import com.example.langlearn.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.net.URI;
import java.net.URISyntaxException;

import static com.example.langlearn.Util.translate;

public class MessageFragment extends Fragment {
    ParseUser currentUser;
    ConstraintLayout layout;
    Activity activity;
    LinearLayout linearLay;
    Context Screen;
    EditText text;
    Button send;

    String nativeLang;
    String UserTo;
    String UserToLang;
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
        nativeLang = args.getString("lang");
        Log.d(TAG, "fdsafdsa: " + nativeLang);

        Parse.initialize(new Parse.Configuration.Builder(view.getContext())
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server("http://langlearn.b4a.io/").build()
        );
        ParseLiveQueryClient parseLiveQueryClient = null;
        try {
            getInitMessages();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI("wss://langlearn.b4a.io/"));
            if (parseLiveQueryClient != null) {
                ParseQuery<ParseObject> parseQuery = new ParseQuery("Messages");
                parseQuery.orderByAscending("createdAt");
                SubscriptionHandling<ParseObject> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

                subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, new SubscriptionHandling.HandleEventCallback<ParseObject>() {
                    @Override
                    public void onEvent(ParseQuery<ParseObject> query, final ParseObject object) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            public void run() {
                                ParseObject o = object;
                                Log.d(TAG, "run: LIVEQUERY???" + object.getString("message"));
                                LinearLayout Messages = new LinearLayout(Screen);
                                Messages.setOrientation(LinearLayout.VERTICAL);
                                if (UserTo.equals(o.getString("to")) && currentUser.getObjectId().equals(o.getString("from")) ||
                                        UserTo.equals(o.getString("from")) && currentUser.getObjectId().equals(o.getString("to"))) {
                                    Log.d(TAG, "run: fdsfdsafdas");
                                    MessageFragment mf = new MessageFragment();
                                    Bundle arguments = new Bundle();
                                    arguments.putString("OID", UserTo);
                                    arguments.putString("name",UserToName);
                                    arguments.putString("lang",nativeLang);
                                    mf.setArguments(arguments);
                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.flContainer, mf);
                                    fragmentTransaction.commit();


                                    }
                                }
                        });
                    }
                });
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text.getText().toString();
                ParseObject message = new ParseObject("Messages");
                message.put("message", msg);
                message.put("to", UserTo);
                message.put("from", currentUser.getObjectId());
                message.saveInBackground(e -> {
                    if (e == null) {
                        Log.d(TAG, "onClick: ");
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
            query.orderByAscending("createdAt");
            query.findInBackground((messages, e) -> {
                if (e == null) {
                    for (ParseObject messages1 : messages) {
                        if (UserTo.equals(messages1.getString("to")) && currentUser.getObjectId().equals(messages1.getString("from")) ||
                                UserTo.equals(messages1.getString("from")) && currentUser.getObjectId().equals(messages1.getString("to"))) {
                            if (UserTo.equals(messages1.get("to"))) {
                                String Message = messages1.getString("message");
                                LinearLayout Wrap = new LinearLayout(Screen);
                                TextView Userinfo = new TextView(Screen);
                                //translate
                                translate(Message, nativeLang, nativeLang, new Handler.Callback() {
                                    @Override
                                    public boolean handleMessage(@NonNull android.os.Message msg) {
                                        Bundle bundle = msg.getData();
                                        String result = bundle.getString("result");
                                        Log.d(TAG, "handleMessage: fdsfdsa " + result);
                                        //Messages
                                        Userinfo.setText(currentUser.getUsername() + ": " + result);
                                        Userinfo.setPadding(300, 0, 0, 0);
                                        Wrap.addView(Userinfo);

                                        Wrap.setOrientation(LinearLayout.VERTICAL);
                                        LinearLayout Interaction = new LinearLayout(Screen);
                                        Interaction.setOrientation(LinearLayout.VERTICAL);
                                        //add wrapper to constrant
                                        Wrap.addView(Interaction);
                                        Messages.addView(Wrap);
                                        return false;
                                    }
                                });

                            } else {
                                String Message = messages1.getString("message");
                                LinearLayout Wrap = new LinearLayout(Screen);
                                TextView Userinfo = new TextView(Screen);
                                //translate
                                translate(Message, nativeLang, currentUser.getString("nativelang"), new Handler.Callback() {
                                    @Override
                                    public boolean handleMessage(@NonNull android.os.Message msg) {
                                        Bundle bundle = msg.getData();
                                        String result = bundle.getString("result");
                                        Log.d(TAG, "handleMessage: line 144 " + result);
                                        Userinfo.setText(UserToName + ": " + result);
                                        Userinfo.setPadding(300, 0, 0, 0);
                                        Wrap.addView(Userinfo);

                                        Wrap.setOrientation(LinearLayout.VERTICAL);
                                        LinearLayout Interaction = new LinearLayout(Screen);
                                        Interaction.setOrientation(LinearLayout.VERTICAL);
                                        //add wrapper to constrant
                                        Wrap.addView(Interaction);
                                        Messages.addView(Wrap);
                                        return false;
                                    }
                                });
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

    public void addMessageLayout(String m){
        LinearLayout Messages = new LinearLayout(Screen);
        Messages.setOrientation(LinearLayout.VERTICAL);
        String Message = m;
        LinearLayout Wrap = new LinearLayout(Screen);
        TextView Userinfo = new TextView(Screen);
        Userinfo.setText(currentUser.getUsername() + ": " + Message);
        Userinfo.setPadding(300, 0, 0, 0);
        Wrap.addView(Userinfo);

        Wrap.setOrientation(LinearLayout.VERTICAL);
        LinearLayout Interaction = new LinearLayout(Screen);
        Interaction.setOrientation(LinearLayout.VERTICAL);
        //add wrapper to constrant
        Wrap.addView(Interaction);
        Messages.addView(Wrap);

        activity.runOnUiThread(() -> {
            if (Messages.getParent() != null) {
                ((ViewGroup) Messages.getParent()).removeView(Messages); // <- fix
            }
            //add to Main Constraint which displays on fragment;
            linearLay.addView(Messages);
        });
    }
}
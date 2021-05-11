package com.example.langlearn.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.langlearn.Adapter.MessageAdapter;
import com.example.langlearn.model.Message;
import com.parse.FindCallback;
import com.example.langlearn.R;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.example.langlearn.Util.translate;

public class MessageFragment extends Fragment {
    ParseUser currentUser;
    RecyclerView rvMessage;
    EditText text;
    Button send;
    List<com.example.langlearn.model.Message> mMessages;
    MessageAdapter messageAdapter;

    String nativeLang;
    String UserTo;
    String UserToLang;
    String UserToName;


    final String TAG = "DEBUG: MESSAGE ACTIVITY:";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        rvMessage = view.findViewById(R.id.rvMessage);
        mMessages = new ArrayList<>();
        messageAdapter = new MessageAdapter(getContext(), mMessages);
        rvMessage.setAdapter(messageAdapter);
        rvMessage.setLayoutManager(new LinearLayoutManager(getContext()));
        messageAdapter.notifyDataSetChanged();
        rvMessage.getLayoutManager().scrollToPosition(messageAdapter.getItemCount()-1);
        currentUser = ParseUser.getCurrentUser();
        Bundle args = getArguments();
        send = view.findViewById(R.id.sendMessageButt);
        text = view.findViewById(R.id.messageText);
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
                ParseQuery<com.example.langlearn.model.Message> parseQuery = new ParseQuery("Messages");
                parseQuery.orderByAscending("createdAt");
                SubscriptionHandling<com.example.langlearn.model.Message> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

                subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, new SubscriptionHandling.HandleEventCallback<com.example.langlearn.model.Message>() {
                    @Override
                    public void onEvent(ParseQuery<com.example.langlearn.model.Message> query, final com.example.langlearn.model.Message object) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            public void run() {
                                com.example.langlearn.model.Message o = object;
                                Log.d(TAG, "run: LIVEQUERY???" + object.getString("message"));
                                if (UserTo.equals(o.getString("to")) && currentUser.getObjectId().equals(o.getString("from")) ||
                                        UserTo.equals(o.getString("from")) && currentUser.getObjectId().equals(o.getString("to"))) {
                                    Log.d(TAG, "run: fdsfdsafdas");
                                    mMessages.add(object);
                                    messageAdapter.notifyDataSetChanged();
                                    rvMessage.getLayoutManager().scrollToPosition(messageAdapter.getItemCount()-1);
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
                Message message = new Message();
                message.put("message", msg);
                message.put("to", UserTo);
                message.put("from", currentUser.getObjectId());
                message.saveInBackground(e -> {
                    if (e == null) {
                        Log.d(TAG, "onClick: ");
                    }
                });
                text.setText("");
            }
        });
    }

    boolean matchUser(String userId, String fromId, String toId) {
        if (!userId.equals(fromId) && !userId.equals(toId)) return false;
        return true;
    }

    public void getInitMessages() throws ParseException {
        Log.d(TAG, "getInitMessages: " + ParseUser.getCurrentUser().getObjectId());
        ParseQuery<com.example.langlearn.model.Message> query = ParseQuery.getQuery("Messages");
        Log.d(TAG, "getInitMessages: " + query.count());
        query.orderByAscending("createdAt");
        // Getting messages
        query.findInBackground((messages, e) -> {

                    if (e != null) {
                        Log.e(TAG, "Failed to get messages: " + e.getMessage());
                        return;
                    }
                    String currUserId = ParseUser.getCurrentUser().getObjectId();
                    String currUserLang = ParseUser.getCurrentUser().getString("nativelang");
                    String recipientId = UserTo;

//            ArrayList<Message> relevantMessages = new ArrayList<>();

            final String[] a = new String[1];
                for (Message messages1 : messages) {
                    if (UserTo.equals(messages1.getString("to")) && currentUser.getObjectId().equals(messages1.getString("from")) ||
                            UserTo.equals(messages1.getString("from")) && currentUser.getObjectId().equals(messages1.getString("to"))) {
                            mMessages.add(messages1);
                        }
                    }
                    Log.d(TAG, String.valueOf(mMessages.size()));
                    messageAdapter.notifyDataSetChanged();
                    rvMessage.getLayoutManager().scrollToPosition(messageAdapter.getItemCount()-1);
                });

            // Display messages

//            for (Message m : relevantMessages) {
//
//                LinearLayout Wrap = new LinearLayout(Screen);
//                TextView Userinfo = new TextView(Screen);
//
//                //translate
//                translate(m.getText(), m.getFromLang(), m.getToLang(), (msg) -> {
//                    //translations[i] = new Translation()
//                    Bundle bundle = msg.getData();
//                    String result = bundle.getString("result");
//
//                    TextView messageText = new TextView(Screen);
//
//                    TextView textView = Messages.findViewWithTag("msg" + m.getIndex());
//
//                    textView.setText(m.getFromName() + ": " + result);
//
//                    Log.d(TAG, "handleMessage: line 144 " + result);
//
//                    return false;
//                });
//            }
//
//            activity.runOnUiThread(() -> {
//                if (Messages.getParent() != null) {
//                    ((ViewGroup) Messages.getParent()).removeView(Messages); // <- fix
//                }
//                //add to Main Constraint which displays on fragment;
//                linearLay.addView(Messages);
//            });
//
//        });
    }

//    private class Message {
//
//        private String fromLang, toLang, fromName, text;
//        int index;
//
//        public Message(String fromLang, String toLang, String fromName, String text, int index) {
//            this.fromLang = fromLang;
//            this.toLang = toLang;
//            this.fromName = fromName;
//            this.text = text;
//            this.index = index;
//        }
//
//        public String getFromLang() {
//            return fromLang;
//        }
//
//        public String getToLang() {
//            return toLang;
//        }
//
//        public String getFromName() {
//            return fromName;
//        }
//
//        public String getText() {
//            return text;
//        }
//
//        public int getIndex() {
//            return index;
//        }
//    }


//        nativeLang = args.getString("lang");
//        Log.d(TAG, "fdsafdsa: " + nativeLang);
//
//        Parse.initialize(new Parse.Configuration.Builder(view.getContext())
//                .applicationId(getString(R.string.back4app_app_id))
//                .clientKey(getString(R.string.back4app_client_key))
//                .server("http://langlearn.b4a.io/").build()
//        );
//        ParseLiveQueryClient parseLiveQueryClient = null;
//        try {
//            getInitMessages();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        try {
//            parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI("wss://langlearn.b4a.io/"));
//            if (parseLiveQueryClient != null) {
//                ParseQuery<ParseObject> parseQuery = new ParseQuery("Messages");
//                parseQuery.orderByAscending("createdAt");
//                SubscriptionHandling<ParseObject> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);
//
//                subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, new SubscriptionHandling.HandleEventCallback<ParseObject>() {
//                    @Override
//                    public void onEvent(ParseQuery<ParseObject> query, final ParseObject object) {
//                        Handler handler = new Handler(Looper.getMainLooper());
//                        handler.post(new Runnable() {
//                            public void run() {
//                                ParseObject o = object;
//                                Log.d(TAG, "run: LIVEQUERY???" + object.getString("message"));
//                                LinearLayout Messages = new LinearLayout(Screen);
//                                Messages.setOrientation(LinearLayout.VERTICAL);
//                                if (UserTo.equals(o.getString("to")) && currentUser.getObjectId().equals(o.getString("from")) ||
//                                        UserTo.equals(o.getString("from")) && currentUser.getObjectId().equals(o.getString("to"))) {
//                                    Log.d(TAG, "run: fdsfdsafdas");
//                                    MessageFragment mf = new MessageFragment();
//                                    Bundle arguments = new Bundle();
//                                    arguments.putString("OID", UserTo);
//                                    arguments.putString("name",UserToName);
//                                    arguments.putString("lang",nativeLang);
//                                    mf.setArguments(arguments);
//                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, mf).addToBackStack("name");
//                                    fragmentTransaction.commit();
//
//
//                                    }
//                                }
//                        });
//                    }
//                });
//            }
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//
//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String msg = text.getText().toString();
//                ParseObject message = new Message();
//                message.put("message", msg);
//                message.put("to", UserTo);
//                message.put("from", currentUser.getObjectId());
//                message.saveInBackground(e -> {
//                    if (e == null) {
//                        Log.d(TAG, "onClick: ");
//                    }
//                });
//            }
//        });
//        rvMessage = view.findViewById(R.id.rvMessage);
//        mMessages = new ArrayList<>();
//        messageAdapter = new MessageAdapter(getContext(), mMessages);
//        rvMessage.setAdapter(messageAdapter);
//        rvMessage.setLayoutManager(new LinearLayoutManager(getContext()));
//        messageAdapter.notifyDataSetChanged();
//        rvMessage.getLayoutManager().scrollToPosition(0);
//        queryMessage();
//    }
//
//    private void queryMessage() {
//        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
//        query.orderByAscending("createdAt");
//        query.findInBackground(new FindCallback<Message>() {
//            @Override
//            public void done(List<Message> messages, ParseException e) {
//                if (e != null){
//                    Log.e(TAG, "Issue with getting Messages", e);
//                }
//                for (Message messages1 : messages) {
//                    if (UserTo.equals(messages1.getString("to")) && currentUser.getObjectId().equals(messages1.getString("from")) ||
//                            UserTo.equals(messages1.getString("from")) && currentUser.getObjectId().equals(messages1.getString("to"))) {
//                            mMessages.add(messages1);
//                        }
//                    }
//                Log.d(TAG, String.valueOf(mMessages.size()));
//                messageAdapter.notifyDataSetChanged();
//            }
//        });
//    }

//    public void getInitMessages() throws ParseException {
//        LinearLayout Messages = new LinearLayout(Screen);
//        Messages.setOrientation(LinearLayout.VERTICAL);
//        Log.d(TAG, "getInitMessages: " + ParseUser.getCurrentUser().getObjectId());
//        ParseQuery<ParseUser> query = ParseQuery.getQuery("Messages");
//        Log.d(TAG, "getInitMessages: " + query.count());
//        for (int i = 0; i < 1; i++) {
//            query.orderByAscending("createdAt");
//            query.findInBackground((messages, e) -> {
//                if (e == null) {
//                    for (ParseObject messages1 : messages) {
//                        if (UserTo.equals(messages1.getString("to")) && currentUser.getObjectId().equals(messages1.getString("from")) ||
//                                UserTo.equals(messages1.getString("from")) && currentUser.getObjectId().equals(messages1.getString("to"))) {
//                            if (UserTo.equals(messages1.get("to"))) {
//                                String Message = messages1.getString("message");
//                                LinearLayout Wrap = new LinearLayout(Screen);
//                                TextView Userinfo = new TextView(Screen);
//                                //translate
//                                translate(Message, nativeLang, nativeLang, new Handler.Callback() {
//                                    @Override
//                                    public boolean handleMessage(@NonNull android.os.Message msg) {
//                                        Bundle bundle = msg.getData();
//                                        String result = bundle.getString("result");
//                                        Log.d(TAG, "handleMessage: fdsfdsa " + result);
//                                        //Messages
//                                        Userinfo.setText(currentUser.getUsername() + ": " + result);
//                                        Userinfo.setPadding(300, 0, 0, 0);
//                                        Wrap.addView(Userinfo);
//
//                                        Wrap.setOrientation(LinearLayout.VERTICAL);
//                                        LinearLayout Interaction = new LinearLayout(Screen);
//                                        Interaction.setOrientation(LinearLayout.VERTICAL);
//                                        //add wrapper to constrant
//                                        Wrap.addView(Interaction);
//                                        Messages.addView(Wrap);
//                                        return false;
//                                    }
//                                });
//
//                            } else {
//                                String Message = messages1.getString("message");
//                                LinearLayout Wrap = new LinearLayout(Screen);
//                                TextView Userinfo = new TextView(Screen);
//                                //translate
//                                translate(Message, nativeLang, currentUser.getString("nativelang"), new Handler.Callback() {
//                                    @Override
//                                    public boolean handleMessage(@NonNull android.os.Message msg) {
//                                        Bundle bundle = msg.getData();
//                                        String result = bundle.getString("result");
//                                        Log.d(TAG, "handleMessage: line 144 " + result);
//                                        Userinfo.setText(UserToName + ": " + result);
//                                        Userinfo.setPadding(300, 0, 0, 0);
//                                        Wrap.addView(Userinfo);
//
//                                        Wrap.setOrientation(LinearLayout.VERTICAL);
//                                        LinearLayout Interaction = new LinearLayout(Screen);
//                                        Interaction.setOrientation(LinearLayout.VERTICAL);
//                                        //add wrapper to constrant
//                                        Wrap.addView(Interaction);
//                                        Messages.addView(Wrap);
//                                        return false;
//                                    }
//                                });
//                            }
//
//                        }
//                    }
//                    activity.runOnUiThread(() -> {
//                        if (Messages.getParent() != null) {
//                            ((ViewGroup) Messages.getParent()).removeView(Messages); // <- fix
//                        }
//                        //add to Main Constraint which displays on fragment;
//                        linearLay.addView(Messages);
//                    });
//                } else {
//                }
//            });
//        }
//    }
//
//    public void addMessageLayout(String m){
//        LinearLayout Messages = new LinearLayout(Screen);
//        Messages.setOrientation(LinearLayout.VERTICAL);
//        String Message = m;
//        LinearLayout Wrap = new LinearLayout(Screen);
//        TextView Userinfo = new TextView(Screen);
//        Userinfo.setText(currentUser.getUsername() + ": " + Message);
//        Userinfo.setPadding(300, 0, 0, 0);
//        Wrap.addView(Userinfo);
//
//        Wrap.setOrientation(LinearLayout.VERTICAL);
//        LinearLayout Interaction = new LinearLayout(Screen);
//        Interaction.setOrientation(LinearLayout.VERTICAL);
//        //add wrapper to constrant
//        Wrap.addView(Interaction);
//        Messages.addView(Wrap);
//
//        activity.runOnUiThread(() -> {
//            if (Messages.getParent() != null) {
//                ((ViewGroup) Messages.getParent()).removeView(Messages); // <- fix
//            }
//            //add to Main Constraint which displays on fragment;
//            linearLay.addView(Messages);
//        });
//    }
}
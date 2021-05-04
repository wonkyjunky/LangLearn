package com.example.langlearn.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.langlearn.R;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ContactsFragment extends Fragment {

    final String TAG = "DEBUG";

    LinearLayout linearLay;
    Button but;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linearLay = view.findViewById(R.id.linLay);
        but = view.findViewById(R.id.buttonTesting);
        try {
            fillUsers();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void fillUsers() throws ParseException {

        LinearLayout Users = new LinearLayout(getActivity());
        Users.setOrientation(LinearLayout.VERTICAL);
        Context Screen = getContext();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        for (int i = 0; i < 1; i++) {
            query.orderByDescending("username");
            query.findInBackground((users, e) -> {
                if (e == null) {
                    for (ParseUser user1 : users) {
                        String Username = user1.getString("username");
                        String objectId = user1.getObjectId();
                        String nativelang = user1.getString("nativelang");
                        Log.d(TAG, "fillUsers: " + objectId);
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
                                MessageFragment mf = new MessageFragment();
                                Bundle arguments = new Bundle();
                                arguments.putString("OID", objectId);
                                arguments.putString("name",Username);
                                arguments.putString("lang",nativelang);
                                mf.setArguments(arguments);
                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.flContainer, mf);
                                fragmentTransaction.commit();
                            }
                        });
                        Interaction.addView(Message);

                        Wrap.addView(Interaction);
                        Users.addView(Wrap);

                    }
                    getActivity().runOnUiThread(()->{
                        if(Users.getParent() != null) {
                            ((ViewGroup)Users.getParent()).removeView(Users); // <- fix
                        }
                        linearLay.addView(Users);
                    });
                } else {
                    // Something went wrong.
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
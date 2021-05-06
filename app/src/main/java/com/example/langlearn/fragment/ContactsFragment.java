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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.langlearn.R;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
        fillUsers();

    }

    public void fillUsers() {

        LinearLayout Users = new LinearLayout(getActivity());
        Users.setOrientation(LinearLayout.VERTICAL);
        Context Screen = getContext();
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
                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.flContainer, new MessageFragment());
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
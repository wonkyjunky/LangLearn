package com.example.langlearn.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.langlearn.MessageActivity;
import com.example.langlearn.R;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import static com.parse.Parse.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment {

    final String TAG = "DEBUG";
    ConstraintLayout layout;
    LinearLayout linearLay;

    Button but;
    ParseUser currentUser;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ContactsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactsFragment newInstance(String param1, String param2) {
        ContactsFragment fragment = new ContactsFragment();
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
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layout = view.findViewById(R.id.layout);
        linearLay = view.findViewById(R.id.linLay);
        but = view.findViewById(R.id.buttonTesting);
        currentUser = ParseUser.getCurrentUser();
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
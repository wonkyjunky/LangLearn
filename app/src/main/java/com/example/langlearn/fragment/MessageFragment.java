package com.example.langlearn.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.langlearn.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment {

    EditText text;
    Button send;
    Button retrieve;

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
        int userCount = 1;
        String[] Users = new String[userCount];
        text = view.findViewById(R.id.editTextTextMultiLine);
        send = view.findViewById(R.id.sendButton);
        retrieve = view.findViewById(R.id.buttonRetrieve);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = view.findViewById(R.id.editTextTextMultiLine);
                TextView textView = view.findViewById(R.id.textView2);
                ParseObject firstObject = new ParseObject("FirstClass");
                firstObject.put("Message", text.getText().toString());
                firstObject.saveInBackground(e -> {
                    if (e != null) {
                        textView.setText(String.format("Fail to add data..." + e.getLocalizedMessage(), firstObject.get("Message")));
                    }
                    else {
                        textView.setText(String.format("Data saved is: \n %s", firstObject.get("Message")));
                    }
                });
            }
        });
        retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMessage();
            }
        });

    }


    void getMessage(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FirstClass");
        query.whereEqualTo("objectId", "Uv2xUdlOdi");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject message, ParseException e) {
                if (e == null) {
                    String Message = message.getString("Message");
                    Log.e("GFSDGSFDGFDSGFSD", "done: " + Message );
                } else {
                    // Something is wrong
                }
            }
        });
    }
}
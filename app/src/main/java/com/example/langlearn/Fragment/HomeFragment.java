package com.example.langlearn.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.langlearn.Menu_Papago;
import com.example.langlearn.R;
import com.parse.ParseUser;

import static com.example.langlearn.Fragment.FragExampleActivity.langCodes;
import static com.example.langlearn.Fragment.FragExampleActivity.langNames;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    EditText etTranslate;
    TextView tvResult;
    Button btTranslate;
    String target;


//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment defaultFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static defaultFragment newInstance(String param1, String param2) {
//        defaultFragment fragment = new defaultFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, langNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        btTranslate = view.findViewById(R.id.btTranslate);
        etTranslate = view.findViewById(R.id.etTranslate);
        tvResult = view.findViewById(R.id.tvResult);

        /**
         * This is eventlistener that get text from edittext and pass to
         * papago_handler
         *
         *
         * when translation is not supported directly, it translate the source language to korean and then
         * translate it to the destination language.
         * So It needs 2 API calls. but need to come up with better algorithm even though it works fine.
         * now it's wasting an api call when english -> french, english-> chinese ... etc.
         */
        btTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        String langCode = ParseUser.getCurrentUser().getString("nativelang");

                        String word = etTranslate.getText().toString();
                        Menu_Papago papago = new Menu_Papago();
                        String resultWord;
                        if (!langCode.equals("ko") && target != "ko"){
                            word = papago.getTranslation(word, langCode, "ko");
                            resultWord = papago.getTranslation(word, "ko", target);
                        } else {
                            resultWord = papago.getTranslation(word, langCode, "ko");
                        }


                        Bundle papagoBundle = new Bundle();
                        papagoBundle.putString("resultWord", resultWord);

                        Message msg = papago_handler.obtainMessage();
                        msg.setData(papagoBundle);
                        papago_handler.sendMessage(msg);
                    }
                }.start();
            }
        });
    }


    /**
     * papago handler
     */
    @SuppressLint("HandlerLeak")
    Handler papago_handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle bundle = msg.getData();
            String resultWord = bundle.getString("resultWord");
            tvResult.setText(resultWord);
        }
    };

    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            target = langCodes[position];
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
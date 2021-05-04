package com.example.langlearn.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.langlearn.Menu_Papago;
import com.example.langlearn.R;
import com.example.langlearn.Util;
import com.parse.ParseUser;

public class HomeFragment extends Fragment {

    EditText etTranslate;
    TextView tvResult;
    Button btTranslate;
    String target;


    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Util.langNames);
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

                        if (langCode.equals(target)){
                            tvResult.setText(word);
                            return;
                        }

                        if (!langCode.equals("ko") && target != "ko"){
                            word = papago.getTranslation(word, langCode, "ko");
                            resultWord = papago.getTranslation(word, "ko", target);
                        } else if(langCode.equals("ko")) {
                            resultWord = papago.getTranslation(word, "ko", target);
                        }
                        else {
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
            target = Util.langCodes[position];
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
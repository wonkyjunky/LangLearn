package com.example.langlearn;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends LangLearnActivity {

    EditText etTranslate;
    TextView tvResult;
    Button btTranslate;
    String source = "en"; // default = English, but after we need to get native lanuage of the user from user database.
    String target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initInterface();

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        List<String> languages = new ArrayList<String>();
        languages.add("Select a Language");
        languages.add("English");
        languages.add("Spanish");
        languages.add("French");
        languages.add("German");
        languages.add("Korean");
        languages.add("Japanese");
        languages.add("Simple Chinese");
        languages.add("Traditional Chinese");
        languages.add("Vietnamese");
        languages.add("Russian");
        languages.add("Italian");
        languages.add("Indonesian");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languages);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());



        btTranslate = findViewById(R.id.btTranslate);
        etTranslate = findViewById(R.id.etTranslate);
        tvResult = findViewById(R.id.tvResult);

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
                        String word = etTranslate.getText().toString();
                        Menu_Papago papago = new Menu_Papago();
                        String resultWord;
                        if (!source.equals("ko") && target != "ko"){
                            word = papago.getTranslation(word, source, "ko");
                            resultWord = papago.getTranslation(word, "ko", target);
                        } else {
                            resultWord = papago.getTranslation(word, source, "ko");
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


        /**
         * this is function for the parse data into database
         * connecting back4app with android app.
         */
//        TextView textView = findViewById(R.id.textView);
//
//        ParseObject firstObject = new ParseObject("FirstClass");
//
//        firstObject.put("Message", "Hey! Welcome to LangLearn. Parse is now connected");
//
//
//        firstObject.saveInBackground(e -> {
//
//            if (e != null) {
//                Toast.makeText(this, "Fail to add data..." + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//            }
//            else {
//                textView.setText(String.format("Data saved is: \n %s", firstObject.get("Message")));
//            }
//        });
//    }


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
            if (parent.getItemAtPosition(position).toString().equals("German")) {
                target = "de";
            } else if (parent.getItemAtPosition(position).toString().equals("Spanish")){
                target = "es";
            } else if (parent.getItemAtPosition(position).toString().equals("French")){
                target = "fr";
            } else if (parent.getItemAtPosition(position).toString().equals("Korean")) {
                target = "ko";
            } else if (parent.getItemAtPosition(position).toString().equals("Japanese")){
                target = "ja";
            } else if (parent.getItemAtPosition(position).toString().equals("Simple Chinese")){
                target = "zh-CN";
            } else if (parent.getItemAtPosition(position).toString().equals("Traditional Chinese")){
                target = "zh-TW";
            } else if (parent.getItemAtPosition(position).toString().equals("Vietnamese")){
                target = "vi";
            } else if (parent.getItemAtPosition(position).toString().equals("Russian")){
                target = "ru";
            } else if (parent.getItemAtPosition(position).toString().equals("Italian")){
                target = "it";
            } else if (parent.getItemAtPosition(position).toString().equals("Indonesian")){
                target = "id";
            } else if (parent.getItemAtPosition(position).toString().equals("English")){
                target = "en";
            }
            Toast.makeText(MainActivity.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

}


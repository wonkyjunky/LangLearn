package com.example.langlearn;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.example.langlearn.Fragment.FragExampleActivity;
import com.example.langlearn.ui.login.LoginActivity;
import com.parse.ParseUser;

public class MainActivity extends LangLearnActivity {

    EditText etTranslate;
    TextView tvResult;
    Button btTranslate;
    String target;
    Button btFragment;

    @Override protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btFragment = findViewById(R.id.btFragment);

        btFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FragExampleActivity.class);
                startActivity(intent);
                finish();
            }
        });

        if (ParseUser.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            logInfo("Currently logged in: " + ParseUser.getCurrentUser().getUsername());
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // init universal app bar
        initNavBar();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, langNames);
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
                        String langCode = ParseUser.getCurrentUser().getString("nativelang");

                        if (langCode == null) {
                            logError("User info has not been gathered! Source langauge not set!");
                            return;
                        }
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
            target = langCodes[position];
            Toast.makeText(view.getContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

}


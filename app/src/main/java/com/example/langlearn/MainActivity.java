package com.example.langlearn;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends LangLearnActivity {

    EditText etTranslate;
    TextView tvResult;
    Button btTranslate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initInterface();

        btTranslate = findViewById(R.id.btTranslate);
        etTranslate = findViewById(R.id.etTranslate);
        tvResult = findViewById(R.id.tvResult);

        /**
         * This is eventlistener that get text from edittext and pass to
         * papago_handler
          */
//        btTranslate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new Thread(){
//                    @Override
//                    public void run() {
//                        String word = etTranslate.getText().toString();
//                        Menu_Papago papago = new Menu_Papago();
//                        String resultWord;
//                        resultWord = papago.getTranslation(word, "en", "ko");
//
//                        Bundle papagoBundle = new Bundle();
//                        papagoBundle.putString("resultWord", resultWord);
//
//                        Message msg = papago_handler.obtainMessage();
//                        msg.setData(papagoBundle);
//                        papago_handler.sendMessage(msg);
//                    }
//                }.start();
//            }
//        });


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


    /**
     * papago handler
     */
//    @SuppressLint("HandlerLeak")
//    Handler papago_handler = new Handler(){
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            Bundle bundle = msg.getData();
//            String resultWord = bundle.getString("resultWord");
//            tvResult.setText(resultWord);
//        }
    }
}

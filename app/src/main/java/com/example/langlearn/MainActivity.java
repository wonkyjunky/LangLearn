package com.example.langlearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.langlearn.ui.login.LoginActivity;
import com.parse.Parse;
import com.parse.ParseObject;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    EditText etTranslate;
    TextView tvResult;

    Button btTranslate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        btTranslate = findViewById(R.id.btTranslate);
        etTranslate = findViewById(R.id.etTranslate);
        tvResult = findViewById(R.id.tvResult);
        Button loginact = findViewById(R.id.loginact);
        loginact.setOnClickListener(v1->{
            Intent intent = new Intent(this.getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });
       /* btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MessageActivity.class);
                startActivity(i);
                finish();
            }
        });

        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PostActivity.class);
                startActivity(i);
                finish();
            }
        });

        btProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(i);
                finish();
            }
        });*/

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
    }


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
    };
//}

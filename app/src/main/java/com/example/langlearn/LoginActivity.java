package com.example.langlearn;

import androidx.appcompat.app.AppCompatActivity;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    static boolean checkuserName_if_it_has_special_character(String username) {
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(username);

        boolean b = m.find();

        if (b) {
            return true;
        }
        return false;
    }

    static boolean checkuserName_if_it_has_morethan_ten_characters(String username) {
        if (username.length() > 10) {
            return true;
        }
        return false;
    }

    static boolean checkuserName_upperCharacter(String username) {
        char[] charArray = username.toCharArray();
        for (int i = 0; i < charArray.length; i++){
            if (Character.isUpperCase(charArray[i])){
                return true;
            }
        }
        return false;
    }


}
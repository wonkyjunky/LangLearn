package com.example.langlearn.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Messages")
public class Message extends ParseObject {

    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TO = "to";
    public static final String KEY_FROM = "from";

    public  String getKeyMessage() {
        return getString(KEY_MESSAGE);
    }

    public  String getKeyTo() {
        return getString(KEY_TO);
    }

    public  String getKeyFrom() {
        return getString(KEY_FROM);
    }
}

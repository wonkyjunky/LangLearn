package com.example.langlearn;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Util {

    public static final String KOREAN = "ko";

    public static final String[] langNames = new String[]{
            "English",
            "German",
            "Korean",
            "Spanish",
            "French",
            "Japanese",
            "Simplified Chinese",
            "Traditional Chinese",
            "Vietnamese",
            "Russian",
            "Italian",
            "Indonesian"
    };

    public static final String[] langCodes = new String[]{
            "en",
            "de",
            "ko",
            "es",
            "fr",
            "ja",
            "zh-CN",
            "zh-TW",
            "vi",
            "ru",
            "it",
            "id",
    };

    public static String langNameFromCode(String langCode) {

        for (int i = 0; i < langCodes.length; ++i) {
            if (langCodes[i].equals(langCode)) return langNames[i];
        }
        return null;
    }

    public static String langCodeFromName(String langName) {

        for (int i = 0; i < langNames.length; ++i) {
            if (langNames[i].equals(langName)) return langCodes[i];
        }
        return null;
    }

    public static void translate(String text, String codeFrom, String codeTo, Handler.Callback callback) {
        // memory safety
        if (text == null || codeFrom == null || codeTo == null || callback == null) return;

        // nothing to do
        if (text.isEmpty() || codeFrom.isEmpty() || codeTo.isEmpty()) return;

        // creating callback handler
        Handler handler = new Handler(callback);

        new Thread(() -> {

            String result;
            // if translating to same langue, return origin text
            if (codeFrom.equals(codeTo)) {
                result = text;
            }
            else
            {
                Menu_Papago papago = new Menu_Papago();
                // if translating between non-korean languages
                if (!codeFrom.equals(KOREAN) && !codeTo.equals(KOREAN)) {

                    String intermediate = papago.getTranslation(text, codeFrom, KOREAN);
                    result = papago.getTranslation(intermediate, KOREAN, codeTo);

                    // if translating to or from korean
                } else {

                    result = papago.getTranslation(text, codeFrom, codeTo);
                }
            }


            Bundle b = new Bundle();
            b.putString("result", result);
            Message msg = handler.obtainMessage();
            msg.setData(b);
            handler.sendMessage(msg);

        }).start();
    }

}

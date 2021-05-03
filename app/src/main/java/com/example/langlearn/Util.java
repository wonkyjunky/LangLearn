package com.example.langlearn;

public class Util {

    public static String[] langNames = new String[] {
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

    public static String[] langCodes = new String[] {
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
}

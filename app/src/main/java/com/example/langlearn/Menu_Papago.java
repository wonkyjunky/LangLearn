package com.example.langlearn;

import android.app.Application;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Menu_Papago extends Application {
    public String getTranslation(String word, String source, String target){


        String clientId = "TpknL16lHWDKMusB4A2n";
        String clientSecret = "zf79887h_w";


        try {
            String wordSource, wordTarget;
            String text = URLEncoder.encode(word, "UTF-8");
            wordSource = URLEncoder.encode(source, "UTF-8");
            wordTarget = URLEncoder.encode(target, "UTF-8");

            String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

            String postParams = "source=" + wordSource + "&target=" + wordTarget + "&text=" + text;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            //system.out.println(response.toString());
            String s = response.toString();
            s = s.split("\"")[27];
            return s;

        } catch( Exception e){
            e.printStackTrace();
        }
        return "0";
    }
}

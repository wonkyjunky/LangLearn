/**
*
*Launage Supported 
*
* Source Language -> destination Language
*
Korean(ko)	→	English(en)	                            |	English(en)	→	Korean(ko)
Korean(ko)	→	Japanese(ja)	                        |	Japanese(ja)	→	Korean(ko)
Korean(ko)	→	Simple_Chinese(zh-CN)	                |	Simple_Chinese(zh-CN)	→	Korean(ko)
Korean(ko)	→	Traditional_Chinese(zh-TW)           	|	Traditional_Chinese(zh-TW)	→	Korean(ko)
Korean(ko)	→	vietnamese(vi)                       	|	vietnamese(vi)	→	Korean(ko)
Korean(ko)	→	indonesian(id)                       	|	indonesian(id)	→	Korean(ko)
Korean(ko)	→	Thai(th)                            	|	Thai(th)	→	Korean(ko)
Korean(ko)	→	German(de)                           	|	German(de)	→	Korean(ko)
Korean(ko)	→	Russian(ru)	                            |	Russian(ru)	→	Korean(ko)
Korean(ko)	→	Spanish(es)	                            |	Spanish(es)	→	Korean(ko)
Korean(ko)	→	Italian(it)                          	|	Italian(it)	→	Korean(ko)
Korean(ko)	→	French(fr)	                            |	French(fr)	→	Korean(ko)
English(en)	→	Japanese(ja)                        	|	Japanese(ja)	→	English(en)
English(en)	→	French(fr)	                            |	French(fr)	→	English(en)
English(en)	→	Simple_Chinese(zh-CN)                	|	Simple_Chinese(zh-CN)	→	English(en)
English(en)	→	Traditional_Chinese(zh-TW)           	|	Traditional_Chinese(zh-TW)	→	English(en)
Japanese(ja)	→	Simple_Chinese(zh-CN)	            |	Simple_Chinese(zh-CN)	→	Japanese(ja)
Japanese(ja)	→	Traditional_Chinese(zh-TW)       	|	Traditional_Chinese(zh-TW)	→	Japanese(ja)
Simple_Chinese(zh-CN)	→	Traditional_Chinese(zh-TW)	|	Traditional_Chinese(zh-TW)	→	Simple_Chinese(zh-CN)
*
*/ 

/**
*
* Example of using the papago translation API in Java.
*
*
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

// 네이버 기계번역 (Papago SMT) API 예제
public class ApiExamTranslateNmt {

    public static void main(String[] args) {
        String clientId = "YOUR_CLIENT_ID";//Application clientID";
        String clientSecret = "YOUR_CLIENT_SECRET";//Application Client_SecretKey";

        String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
        String text;
        try {
            text = URLEncoder.encode("How Do you feel today?", "UTF-8"); 
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to encode", e);
        }

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);

        String responseBody = post(apiURL, requestHeaders, text);

        System.out.println(responseBody);
    }

    private static String post(String apiUrl, Map<String, String> requestHeaders, String text){
        HttpURLConnection con = connect(apiUrl);
        String postParams = "source=en&target=ko&text=" + text; //Original Language: English (en) -> Destination Lanuage: Korean (ko)
        try {
            con.setRequestMethod("POST");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postParams.getBytes());
                wr.flush();
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // Success Response
                return readBody(con.getInputStream());
            } else {  // Error Response
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to API request and response", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Something wrong with API URL. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to connect : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read API response.", e);
        }
    }
}



 */

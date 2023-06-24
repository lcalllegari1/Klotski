package com.klotski.Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class NextMoveController {
    private static final String API_URL = "https://iyei3btpck.execute-api.eu-north-1.amazonaws.com/default/getNextMove";
    private static final String CONFIG = "config";
    private static final String TOKEN = "token";
    private static final String PARAM_DELIM = "&";
    private static final String URL_SEP = "?";
    private static final String KEY_VAL_SEP = "=";

    public static String next(String config, String token) {
        String encoded_token = URLEncoder.encode(token, StandardCharsets.UTF_8);
        String full_url = API_URL + URL_SEP +
                CONFIG + KEY_VAL_SEP + config + PARAM_DELIM +
                TOKEN + KEY_VAL_SEP + encoded_token;
        try {
            URL url = (new URI(full_url)).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                return response.toString();
            }
        } catch (Exception e) {
            System.out.println("Could not get the API connection or data: " + e.getMessage());
        }
        return "NULL";
    }
}

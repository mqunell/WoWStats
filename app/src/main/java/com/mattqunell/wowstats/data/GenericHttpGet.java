package com.mattqunell.wowstats.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Provides boilerplate HTTP GET code for use in API connection classes' doInBackground() methods.
 * Since get() is static, simply call GenericHttpGet.get(<address>) and the resulting JSON or error
 * message will be returned as a String.
 */
public class GenericHttpGet {
    public static String get(String address) {
        String result;

        try {
            URL url = new URL(address);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();

            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader br = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String temp;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }

            result = sb.toString();
        }
        catch (MalformedURLException e) {
            result = "Error with URL";
        }
        catch (IOException e) {
            result = "Error with HTTP connection";
        }

        return result;
    }
}

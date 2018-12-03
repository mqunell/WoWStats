package com.mattqunell.wowstats.data;

import android.os.AsyncTask;
import android.util.Log;

import com.mattqunell.wowstats.Secret;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BlizzardAuthConnection  extends AsyncTask<String, Void, String> {

    // The class that implements BlizzardAsyncResponse and listens for onPostExecute
    private BlizzardAuthAsyncResponse mResponse;

    // Debugging tag
    private static final String TAG = "BlizzardAuthConnection";

    public BlizzardAuthConnection(BlizzardAuthAsyncResponse response) {
        mResponse = response;
    }

    @Override
    protected String doInBackground(String... strings) {
        String result;

        try {
            // The OAuth address
            String address = "https://us.battle.net/oauth/token?grant_type=client_credentials" +
                    "&client_id=" + Secret.CLIENT_ID +
                    "&client_secret=" + Secret.CLIENT_SECRET;
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
            Log.e(TAG, e.toString());
            result = "Error with URL";
        }
        catch (IOException e) {
            Log.e(TAG, e.toString());
            result = "Error with HTTP connection";
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (!result.startsWith("Error with")) {
            try {
                JSONArray jsonArray = new JSONArray("[" + result + "]");
                JSONObject auth = jsonArray.getJSONObject(0);

                String token = auth.getString("access_token");

                // Return the token to BlizzardConnection.processBlizzardAuth(token)
                mResponse.processBlizzardAuth(token);
            }
            catch (JSONException e) {
                Log.e(TAG, e.toString());
            }
        }
        else {
            mResponse.processBlizzardAuth(null);
        }
    }
}

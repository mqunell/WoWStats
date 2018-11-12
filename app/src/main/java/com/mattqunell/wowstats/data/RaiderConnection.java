package com.mattqunell.wowstats.data;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Accesses Raider.IO's API to retrieve Mythic Plus data about Toons.
 */
public class RaiderConnection extends AsyncTask<String, Void, String> {

    // The class that implements RaiderAsyncResponse and listens for onPostExecute
    private RaiderAsyncResponse mResponse;

    // The Toon being updated with mythic data
    private Toon mToon;

    // Debugging tag
    private static final String TAG = "RaiderConnection";

    public RaiderConnection(RaiderAsyncResponse response, Toon toon) {
        mResponse = response;
        mToon = toon;
    }

    @Override
    protected String doInBackground(String... strings) {

        String name = mToon.getName();
        String realm = mToon.getRealm();

        // Format the realm, if necessary
        if (realm.contains(" ")) {
            realm = realm.substring(0, realm.indexOf(" ")) + "%20" + realm.substring(realm.indexOf(" ") + 1);
        }

        // Web address
        String address = "https://raider.io/api/v1/characters/profile?region=us&realm=" + realm
                + "&name=" + name + "&fields=mythic_plus_scores%2Cmythic_plus_weekly_highest_level_runs";

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
                JSONObject ch = jsonArray.getJSONObject(0);

                int score = ch.getJSONObject("mythic_plus_scores").getInt("all");

                int highest = 0;
                JSONArray arr = ch.getJSONArray("mythic_plus_weekly_highest_level_runs");
                if (arr.length() > 0) {
                    JSONObject obj = arr.getJSONObject(0);
                    highest = obj.getInt("mythic_level");
                }

                // Set the mythic score and highest mythic
                mToon.setMythicScore(score);
                mToon.setHighestMythic(highest);

                // Return the Toon to ToonListFragment.processRaider(Toon)
                mResponse.processRaider(mToon);
            }
            catch (JSONException e) {
                Log.e(TAG, e.toString());
            }
        }
    }
}

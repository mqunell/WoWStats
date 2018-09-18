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

public class RaiderioConnection extends AsyncTask<String, Void, String> {

    // The Toon whose score will be updated
    private Toon mToon;

    // Debugging tag
    private static final String TAG = "RaiderioConnection";

    public RaiderioConnection(Toon toon) {
        mToon = toon;
    }

    @Override
    protected String doInBackground(String... strings) {

        String name = mToon.getName();
        String realm = mToon.getRealm();
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

                Log.v(TAG, String.valueOf(score));

                // Return the Toon to ToonListFragment.processFinish(Toon)
                //mResponse.processFinish(new Toon(name, realm, faction, race, _class, level, itemLevel));
            }
            catch (JSONException e) {
                Log.e(TAG, e.toString());
            }
        }
        else {
            //mResponse.processFinish(null);
        }
    }
}

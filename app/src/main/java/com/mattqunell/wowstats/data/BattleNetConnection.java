package com.mattqunell.wowstats.data;

import android.content.Context;
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
import java.util.HashMap;
import java.util.Map;

public class BattleNetConnection extends AsyncTask<String, Void, String> {

    private static final String TAG = "BattleNetConnection";

    private Context mContext;

    public BattleNetConnection(Context context) {
        mContext = context;
    }

    @Override
    protected String doInBackground(String... strings) {

        String address = "https://us.api.battle.net/wow/character/" + strings[1] + "/" + strings[0]
                + "?fields=items&locale=en_US&apikey=kvh377u89xg3v3g3pnn8txydwc4ckdwd";

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

            JSONArray jsonArray = new JSONArray("[" + sb.toString() + "]");
            JSONObject ch = jsonArray.getJSONObject(0);

            String name = ch.getString("name");
            String realm = ch.getString("realm");
            int raceNum = ch.getInt("race");
            int classNum = ch.getInt("class");
            int level = ch.getInt("level");
            int itemLevel = ch.getJSONObject("items").getInt("averageItemLevel");

            //result = name + ", " + realm + ", " + level + ", " + RACES.get(raceNum) + ", " +
            //       CLASSES.get(classNum) + ", " + itemLevel;

            // Add the Toon to ToonDB
            ToonDB.get(mContext).addToon(new Toon(name, realm, raceNum, classNum, level, itemLevel));

            result = "Success";
        }
        catch (MalformedURLException e) {
            Log.e(TAG, e.toString());
            result = "URL error";
        }
        catch (IOException e) {
            Log.e(TAG, e.toString());
            result = "HTTP connection error";
        }
        catch (JSONException e) {
            Log.e(TAG, e.toString());
            result = "JSON error";
        }

        return result;
    }
}

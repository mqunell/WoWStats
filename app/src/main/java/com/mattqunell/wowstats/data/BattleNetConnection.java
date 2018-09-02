package com.mattqunell.wowstats.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mattqunell.wowstats.database.ToonDb;

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

/**
 * Accesses Battle.net's API to retrieve data about Toons.
 */
public class BattleNetConnection extends AsyncTask<String, Void, String> {

    private static final String TAG = "BattleNetConnection";

    // Battle.net stores each toon's race and class as ints. These Maps are used to convert these
    // ints to their respective Strings
    private static final Map<Integer, String> RACES;
    private static final Map<Integer, String> CLASSES;

    static {
        RACES = new HashMap<>();
        RACES.put(1, "Human");
        RACES.put(2, "Orc");
        RACES.put(3, "Dwarf");
        RACES.put(4, "Night Elf");
        RACES.put(5, "Undead");
        RACES.put(6, "Tauren");
        RACES.put(7, "Gnome");
        RACES.put(8, "Troll");
        RACES.put(9, "Goblin");
        RACES.put(10, "Blood Elf");
        RACES.put(11, "Draenei");
        RACES.put(22, "Worgen");
        RACES.put(25, "A. Pandaren");
        RACES.put(26, "H. Pandaren");

        CLASSES = new HashMap<>();
        CLASSES.put(1, "Warrior");
        CLASSES.put(2, "Paladin");
        CLASSES.put(3, "Hunter");
        CLASSES.put(4, "Rogue");
        CLASSES.put(5, "Priest");
        CLASSES.put(6, "Death Knight");
        CLASSES.put(7, "Shaman");
        CLASSES.put(8, "Mage");
        CLASSES.put(9, "Warlock");
        CLASSES.put(10, "Monk");
        CLASSES.put(11, "Druid");
        CLASSES.put(12, "Demon Hunter");
    }

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
            String race = RACES.get(ch.getInt("race"));
            String _class = CLASSES.get(ch.getInt("class"));
            int level = ch.getInt("level");
            int itemLevel = ch.getJSONObject("items").getInt("averageItemLevel");

            // Add the Toon to ToonDb
            ToonDb.get(mContext).addToon(new Toon(name, realm, race, _class, level, itemLevel));

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

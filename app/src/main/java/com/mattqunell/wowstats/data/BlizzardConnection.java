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
import java.util.HashMap;
import java.util.Map;

/**
 * Accesses Blizzard.com's API to retrieve general data about Toons.
 */
public class BlizzardConnection extends AsyncTask<String, Void, String> {

    public static final int MAX_LEVEL = 120;

    // The class that implements BlizzardAsyncResponse and listens for onPostExecute
    private BlizzardAsyncResponse mResponse;

    // Debugging tag
    private static final String TAG = "BlizzardConnection";

    // Blizzard stores each toon's race and class as ints. These Maps are used to convert these ints
    // to their respective Strings
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
        RACES.put(27, "Nightborne");
        RACES.put(28, "Highmountain Tauren");
        RACES.put(29, "Void Elf");
        RACES.put(30, "Lightforged Draenei");
        RACES.put(34, "Dark Iron Dwarf");
        RACES.put(36, "Mag'har Orc");

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

    public BlizzardConnection(BlizzardAsyncResponse response) {
        mResponse = response;
    }

    @Override
    protected String doInBackground(String... strings) {

        // Web address
        String fields = "items&";
        String address = "https://us.api.blizzard.com/wow/character/" + strings[1] + "/"
                + strings[0] + "?fields=" + fields + "locale=en_US&access_token="
                + Secret.BATTLE_TOKEN;

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

                String name = ch.getString("name");
                String realm = ch.getString("realm");
                int faction = ch.getInt("faction");
                String race = RACES.get(ch.getInt("race"));
                String _class = CLASSES.get(ch.getInt("class"));
                int level = ch.getInt("level");
                int itemLevel = ch.getJSONObject("items").getInt("averageItemLevel");

                // Return the Toon to ToonListFragment.processBlizzard(Toon)
                // Note: Mythic data (the 0, 0) is implemented separately in RaiderConnection
                mResponse.processBlizzard(new Toon(name, realm, faction, race, _class, level, itemLevel, 0, 0));
            }
            catch (JSONException e) {
                Log.e(TAG, e.toString());
            }
        }
        else {
            mResponse.processBlizzard(null);
        }
    }
}

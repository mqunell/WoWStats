package com.mattqunell.wowstats.data;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Accesses Raider.IO's API to retrieve Mythic Plus data about Toons.
 */
public class RaiderConnection extends AsyncTask<String, Void, String> {

    // The class that implements RaiderAsyncResponse and listens for onPostExecute
    private RaiderAsyncResponse mResponse;

    // The Toon being updated with mythic data
    private Toon mToon;

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

        // API address
        String address = "https://raider.io/api/v1/characters/profile?region=us&realm=" + realm
                + "&name=" + name + "&fields=mythic_plus_scores%2Cmythic_plus_weekly_highest_level_runs";

        return GenericHttpGet.get(address);
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
                Log.e("RaiderConnection", e.toString());
            }
        }
        // No else case because the passed-in Toon is modified directly
    }
}

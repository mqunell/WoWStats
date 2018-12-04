package com.mattqunell.wowstats.data;

import android.os.AsyncTask;
import android.util.Log;

import com.mattqunell.wowstats.Secret;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Accesses Blizzard.com's API to retrieve an access token.
 */
public class BlizzardAuthConnection extends AsyncTask<String, Void, String> {

    // The class that implements BlizzardAsyncResponse and listens for onPostExecute
    private BlizzardAuthAsyncResponse mResponse;

    // Debugging tag
    private static final String TAG = "BlizzardAuthConnection";

    public BlizzardAuthConnection(BlizzardAuthAsyncResponse response) {
        mResponse = response;
    }

    @Override
    protected String doInBackground(String... strings) {

        // OAuth address
        String address = "https://us.battle.net/oauth/token?grant_type=client_credentials" +
                "&client_id=" + Secret.CLIENT_ID +
                "&client_secret=" + Secret.CLIENT_SECRET;

        return GenericHttpGet.get(address);
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

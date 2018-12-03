package com.mattqunell.wowstats.data;

/**
 * Allows BlizzardAuthConnection to send data back to ToonListFragment. BlizzardConnection receives an
 * instance of this interface in its constructor and calls processBlizzardAuth from onPostExecute.
 * ToonListFragment implements this interface and overrides the method to receive data.
 */
public interface BlizzardAuthAsyncResponse {
    void processBlizzardAuth(String token);
}

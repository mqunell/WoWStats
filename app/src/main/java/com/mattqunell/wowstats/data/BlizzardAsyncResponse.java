package com.mattqunell.wowstats.data;

/**
 * Allows BlizzardConnection to send data back to ToonListFragment. BlizzardConnection receives an
 * instance of this interface in its constructor and calls processBlizzard from onPostExecute.
 * ToonListFragment implements this interface and overrides the method to receive data.
 */
public interface BlizzardAsyncResponse {
    void processBlizzard(Toon toon);
}

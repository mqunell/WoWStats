package com.mattqunell.wowstats.data;

/**
 * Allows RaiderConnection to send data back to ToonListFragment. RaiderConnection receives an
 * instance of this interface in its constructor and calls processRaider from onPostExecute.
 * ToonListFragment implements this interface and overrides the method to receive data.
 */
public interface RaiderAsyncResponse {
    void processRaiderio(Toon toon);
}

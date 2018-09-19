package com.mattqunell.wowstats.data;

/**
 * Allows RaiderioConnection to send data back to ToonListFragment. RaiderioConnection receives an
 * instance of this interface in its constructor and calls processRaiderio from onPostExecute.
 * ToonListFragment implements this interface and overrides the method to receive data.
 */
public interface RaiderioAsyncResponse {
    void processRaiderio(Toon toon);
}

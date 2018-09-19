package com.mattqunell.wowstats.data;

/**
 * Allows BattlenetConnection to send data back to ToonListFragment. BattlenetConnection receives an
 * instance of this interface in its constructor and calls processBattlenet from onPostExecute.
 * ToonListFragment implements this interface and overrides the method to receive data.
 */
public interface BattlenetAsyncResponse {
    void processBattlenet(Toon toon);
}

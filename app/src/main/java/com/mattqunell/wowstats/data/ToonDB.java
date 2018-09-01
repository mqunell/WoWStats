package com.mattqunell.wowstats.data;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

// SEE RECEIPTS - ReceiptBook for the actual DB implementation guidelines
public class ToonDB {

    // The one class to use throughout the app
    private static ToonDB sToonDB;

    private Context mContext;
    private static List<Toon> mToons;

    // Static getter that creates sToonDB if it doesn't exist and returns it
    public static ToonDB get(Context context) {
        if (sToonDB == null) {
            sToonDB = new ToonDB(context);
        }

        return sToonDB;
    }

    // Private constructor to limit instantiation
    private ToonDB(Context context) {
        mContext = context;
        mToons = new ArrayList<>();
    }

    // Adds a new Toon
    public static void addToon(Toon toon) {
        mToons.add(toon);
    }

    // Gets an ArrayList of all Toons
    public static List<Toon> getToons() {
        return mToons;
    }
}
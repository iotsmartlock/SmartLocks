package com.smartlockinc.smartlocks;

/**
 * Created by SunnySingh on 7/15/2015.
 */
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by SunnySingh on 7/8/2015.
 */
public class photourl {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "Smartlockgcm";
    private static final String IS_LOGIN = "photo";
    public static final String PHOTOURL = "";

    public photourl(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void photourl(String uri) {

        editor.putString(PHOTOURL, uri);
        editor.putBoolean(IS_LOGIN, true);

        editor.commit();
    }


    public String geturi() {
        return pref.getString(PHOTOURL, null);
    }
}

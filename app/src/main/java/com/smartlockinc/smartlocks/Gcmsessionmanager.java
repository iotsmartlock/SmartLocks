package com.smartlockinc.smartlocks;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by SunnySingh on 7/8/2015.
 */
public class Gcmsessionmanager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "Smartlockgcm";
    private static final String IS_LOGIN = "IsLoggedIngcm";
    public static final String KEY_REGID = "";

    public Gcmsessionmanager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void regidregis(String regid) {

        editor.putString(KEY_REGID, regid);
        editor.putBoolean(IS_LOGIN, true);

        editor.commit();
    }


    public boolean getregid() {
        if (pref.getString(KEY_REGID, null) != null) {
            return true;

        } else
            return false;
    }

    public String id()
    {
        return pref.getString(KEY_REGID,null);
    }

}
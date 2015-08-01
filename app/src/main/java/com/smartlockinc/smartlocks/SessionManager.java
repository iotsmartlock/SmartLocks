package com.smartlockinc.smartlocks;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;


/**
 * Created by SunnySingh on 7/5/2015.
 */
public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE=0;
    private static final String PREF_NAME = "Smartlock";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_Email = "";
    public static final String KEY_Password = "";
    public SessionManager(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void atlogin(String email,String password)
    {

        editor.putString(KEY_Email,email);
        editor.putString(KEY_Password,password);
        editor.putBoolean(IS_LOGIN, true);
        editor.commit();
    }





    public boolean checklogin()
    {
        if(pref.getString(KEY_Email,null)!=null&&pref.getString(KEY_Password,null)!=null)
        {
            return true;
        }
        else
            return false;


    }

    public void atlogout()
    {
        editor.clear();
        editor.commit();

    }

    public String Email()

    {
        return pref.getString(KEY_Email,null);
    }
    public String Password()

    {
        return pref.getString(KEY_Password,null);
    }


}

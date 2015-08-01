package com.smartlockinc.smartlocks;

import android.content.Context;
import android.content.Intent;

/**
 * Created by SunnySingh on 7/5/2015.
 */
public class CommonUtilities {
    static final String SERVER_URL = "server ip";
    static final String SENDER_ID =" 789373239000";
    static final String TAG = "Smartlock GCM";

    static final String DISPLAY_MESSAGE_ACTION ="smartlock.iot.android.smartlock.activity.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";


    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}

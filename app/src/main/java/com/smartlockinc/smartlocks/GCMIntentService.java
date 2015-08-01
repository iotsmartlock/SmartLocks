package com.smartlockinc.smartlocks;

/**
 * Created by SunnySingh on 7/5/2015.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import com.google.android.gcm.GCMBaseIntentService;
import com.smartlockinc.smartlocks.R;

import static com.smartlockinc.smartlocks.CommonUtilities.*;
/**
 * Created by SunnySingh on 6/28/2015.
 */
public class GCMIntentService extends GCMBaseIntentService {
    SessionManager session;
    DatabaseHandler db;
    Gcmsessionmanager gcmsession;
    public static String TAG = "GCMIntentService";

    public GCMIntentService(){
        super(" 789373239000");
    }
    @Override
    protected void onRegistered(Context context, String registrationid)
    {
        Log.i(TAG, "Device Registered: regid = " + registrationid);
        displayMessage(context, "Your device has been registered with GCM");
        Log.i(TAG, registrationid);
        gcmsession = new Gcmsessionmanager(context);
        gcmsession.regidregis(registrationid);
        //uncomment when server is functional
        //session = new SessionManager(context);
        //String Email = session.Email();
        //String Password = session.Password();
        //ServerUtilities.register(getApplicationContext(), Email, Password, registrationid);

    }
    @Override
    protected void onUnregistered(Context context, String registrationid)
    {
        Log.i(TAG, "Device  Unregistered: regid =" + registrationid);
        displayMessage(context, "Your device has been Unregistered with GCM");

        //uncomment when server is functional
        //ServerUtilities.unregister(context, registrationid);
    }
    @Override
    protected void onMessage(Context context, Intent intent)
    {
        Log.i(TAG, "Received message");
        String message = intent.getExtras().getString("message");
        Log.i(TAG, message);
        //displayMessage(context, message);
        //db = new DatabaseHandler(context);
        //db.addMessage(message);
        generateNotification(context, message);

    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        //displayMessage(context, getString(R.string.gcm_error, errorId));
    }
    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
       displayMessage(context, getString(R.string.gcm_recoverable_error, errorId));
        return super.onRecoverableError(context, errorId);
    }
    private static void generateNotification(Context context,String message)
    {
        int icon = R.mipmap.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager  notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String title = context.getString(R.string.app_name);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        Notification notification = new Notification.Builder(context)
                .setContentText(message)
                .setSmallIcon(icon)
                .setWhen(when)
                .setDefaults(Notification.DEFAULT_SOUND| Notification.DEFAULT_VIBRATE)
                .setContentTitle(title)
                .setContentIntent(intent)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, notification);



    }
}

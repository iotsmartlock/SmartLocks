package com.smartlockinc.smartlocks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by SunnySingh on 7/5/2015.
 */
public class AlertDialogueManager {
    public void ShowALert(Context context, String title, String message, Boolean status)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View alert = layoutInflater.inflate(R.layout.alertdialogue, null);

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setView(alert);
        alertDialog.setTitle(title);

        alertDialog.setMessage(message);

        if(status != null)
            alertDialog.setIcon((status) ? R.mipmap.success : R.mipmap.fail);//add icon success and fail in drawable

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();

    }
}

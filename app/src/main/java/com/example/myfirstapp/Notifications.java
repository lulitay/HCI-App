package com.example.myfirstapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings;

/**
 * Created by catalinavarela on 19/11/17.
 */

public class Notifications extends ContextWrapper{
    private NotificationManager mManager;
    private String message;
    private String title;
    private int id;
    private Notification notification;
    private static int instanceCounter = 0;

    public Notifications(String message, String title, Context base) {
        super(base);

        if (mManager == null) {
            mManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }

        this.message = message;
        this.title = title;
        id = instanceCounter;
        instanceCounter++;
    }

    public void build(){
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.smart_house_logo);

        notification = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setLargeIcon(largeIcon)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setVisibility (Notification.VISIBILITY_PRIVATE)
                .build();
    }

    public void addToLodge(){
        mManager.notify(id, notification);
    }
}

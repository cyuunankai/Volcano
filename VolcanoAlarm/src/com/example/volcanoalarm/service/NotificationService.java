package com.example.volcanoalarm.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.volcanoalarm.R;
import com.example.volcanoalarm.NotificationActivity;
import com.example.volcanoalarm.util.DateUtil;

public class NotificationService extends IntentService {
    
    Handler mMainThreadHandler = null;
    
    /** 
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public NotificationService() {
        super("NotificationService");
        
        mMainThreadHandler = new Handler();
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        mMainThreadHandler.post(new Runnable() {
              @Override
              public void run() {
                  
                  notificate(DateUtil.getSysTimeStr() + " 火山喷发预警!");
                  
              }
          });
    }
    
    public void notificate(String msg){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.abc_ab_bottom_solid_dark_holo)
                .setContentTitle("notification")
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setContentText(msg);
        
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, NotificationActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(NotificationActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());

    }
    
    

}

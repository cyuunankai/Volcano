package com.example.volcanoalarm.schedule;

import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.example.volcanoalarm.service.NotificationService;
import com.example.volcanoalarm.util.DateUtil;
import com.example.volcanoalarm.util.LogUtil;

public class NotificateBroadcastRecevier extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "NotificateBroadcastRecevier");
        // Acquire the lock
        wl.acquire();

        Intent i = new Intent(context, NotificationService.class);
        context.startService(i);

        LogUtil.appendLog(DateUtil.getSysTimeStr() + " execute 'notificate' pending event ");

        // Release the lock
        wl.release();

    }

    public void setAlarm(Context context, Date startDate) {
        
        startDate.setHours(8);
        
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 16);
        calendar.set(Calendar.MINUTE, 45);
        calendar.set(Calendar.SECOND, 0);
        
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificateBroadcastRecevier.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);

        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
        
        
        Calendar calendar1 = Calendar.getInstance();

        calendar1.set(Calendar.HOUR_OF_DAY, 16);
        calendar1.set(Calendar.MINUTE, 47);
        calendar1.set(Calendar.SECOND, 0);
        am.set(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), pi);

    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, NotificateBroadcastRecevier.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);

        LogUtil.appendLog("cancel 'notificate' pending event");
    }

}

package com.example.volcanoalarm.schedule;

import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.example.volcanoalarm.service.ChangeWallPaperService;
import com.example.volcanoalarm.util.DateUtil;
import com.example.volcanoalarm.util.LogUtil;

public class ChangeWallPaperBroadcastRecevier extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		 
		
		 PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
         PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ChangeWallPaperBroadcastRecevier");
         //Acquire the lock
         wl.acquire();
         
         Intent i = new Intent(context, ChangeWallPaperService.class);
         context.startService(i);
         
         LogUtil.appendLog(DateUtil.getSysTimeStr() + " execute 'change wallPaper' pending event ");
         
         
         //Release the lock
         wl.release();
         
         cancelAlarm(context);
	}
	
	public void setAlarm(Context context, Date date) {
		
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ChangeWallPaperBroadcastRecevier.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);

        am.set(AlarmManager.RTC_WAKEUP, date.getTime(), pi);
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, ChangeWallPaperBroadcastRecevier.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);

        LogUtil.appendLog(DateUtil.getSysTimeStr() + " cancel 'change wallPaper' pending event");
    }

}

package com.example.volcanoalarm.schedule;

import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.example.volcanoalarm.service.ReturnUserWallPaperService;

public class ReturnUserWallPaperBroadcastRecevier extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		 
		
		 PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
         PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ReturnUserWallPaperBroadcastRecevier");
         //Acquire the lock
         wl.acquire();
         
         Intent i = new Intent(context, ReturnUserWallPaperService.class);
         context.startService(i);
         
         //Release the lock
         wl.release();
         
         cancelAlarm(context);
	}
	
	public void setAlarm(Context context, Date returnToUserWpDate) {

		    
		    AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	        
		    Calendar calendar = Calendar.getInstance();
            calendar.setTime(returnToUserWpDate);
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            
            Intent intent = new Intent(context, ReturnUserWallPaperBroadcastRecevier.class);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
		    
	        
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, ReturnUserWallPaperBroadcastRecevier.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

}

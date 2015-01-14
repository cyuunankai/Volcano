package com.example.volcanoalarm.schedule;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.example.volcanoalarm.service.ReturnUserWallPaperService;
import com.example.volcanoalarm.util.DateUtil;
import com.example.volcanoalarm.util.LogUtil;

public class ReturnUserWallPaperBroadcastRecevier extends BroadcastReceiver {
	
	@Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.appendLog(DateUtil.getSysTimeStr() + " ReturnUserWallPaperBroadcastRecevier onReceive");

        PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ReturnUserWallPaperBroadcastRecevier");
        // Acquire the lock
        wl.acquire();

        Intent i = new Intent(context, ReturnUserWallPaperService.class);
        context.startService(i);

        // Release the lock
        wl.release();
    }
	
	public void setAlarm(Context context, List<Date> dateList) {
	    LogUtil.appendLog(DateUtil.getSysTimeStr() + " ReturnUserWallPaperBroadcastRecevier setAlarm ");
		    
	    AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        
	    int requstCode = 0;
        for(Date date : dateList) {
            LogUtil.appendLog(DateUtil.getSysTimeStr() + " ReturnUserWallPaperBroadcastRecevier setAlarm " + DateUtil.toDateString(date, DateUtil.DATE_FORMAT_YYYY_MM_DD_HYPHEN));
            
    	    Intent intent = new Intent(context, ReturnUserWallPaperBroadcastRecevier.class);
            PendingIntent pi = PendingIntent.getBroadcast(context, requstCode, intent, 0);
            
    	    Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 16);
            calendar.set(Calendar.MINUTE, 55);
            calendar.set(Calendar.SECOND, 0);
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
            
            requstCode++;
        }
    }

    public void cancelAlarm(Context context, List<Date> dateList) {
        LogUtil.appendLog(DateUtil.getSysTimeStr() + " ReturnUserWallPaperBroadcastRecevier cancelAlarm");
        
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        
        int requstCode = 0;
        for(Date date : dateList) {
            LogUtil.appendLog(DateUtil.getSysTimeStr() + " ReturnUserWallPaperBroadcastRecevier cancelAlarm " + DateUtil.toDateString(date, DateUtil.DATE_FORMAT_YYYY_MM_DD_HYPHEN));
            
            Intent intent = new Intent(context, ReturnUserWallPaperBroadcastRecevier.class);
            PendingIntent sender = PendingIntent.getBroadcast(context, requstCode, intent, 0);
            alarmManager.cancel(sender);
            
            requstCode ++;
        }
    }

}

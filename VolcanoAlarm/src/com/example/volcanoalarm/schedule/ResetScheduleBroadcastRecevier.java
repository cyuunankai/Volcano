package com.example.volcanoalarm.schedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.Toast;

import com.example.volcanoalarm.service.ResetScheduleService;
import com.example.volcanoalarm.util.DateUtil;
import com.example.volcanoalarm.util.LogUtil;

public class ResetScheduleBroadcastRecevier extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
	     LogUtil.appendLog(DateUtil.getSysTimeStr() + " ------ResetScheduleBroadcastRecevier onReceive");
		
		 PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
         PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ResetScheduleBroadcastRecevier");
         //Acquire the lock
         wl.acquire();
         Toast.makeText(context, "after reboot Recevier", Toast.LENGTH_LONG).show();
         Intent i = new Intent(context, ResetScheduleService.class);
         context.startService(i);
         
         //Release the lock
         wl.release();
	}
	
}

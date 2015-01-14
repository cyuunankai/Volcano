package com.example.volcanoalarm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.volcanoalarm.bean.AlarmDate;
import com.example.volcanoalarm.db.VolcanoDatabase;
import com.example.volcanoalarm.schedule.ChangeWallPaperBroadcastRecevier;
import com.example.volcanoalarm.schedule.NotificateBroadcastRecevier;
import com.example.volcanoalarm.util.DateUtil;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

public class ResetScheduleService extends IntentService {
    
    Handler mMainThreadHandler = null;
    
    /** 
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public ResetScheduleService() {
        super("ResetScheduleService");
        
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
                  Toast.makeText(getApplicationContext(), "after reboot service", Toast.LENGTH_LONG).show();
                  
                  List<Date> availableDateList = getAvailableDateList();
                  setSchedule(availableDateList);
              }

          });
    }
    
    private List<Date> getAvailableDateList() {
        VolcanoDatabase db = new VolcanoDatabase(getApplicationContext());
        List<AlarmDate> availableAdList = db.getAvalibleAlarmDates();
        List<Date> availableDateList = new ArrayList<Date>();
        for(AlarmDate aDate : availableAdList) {
            availableDateList.add(DateUtil.strToDate(aDate.getDate(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HYPHEN));
        }
        return availableDateList;
    }
    
    private void setSchedule(List<Date> availableDateList) {
        ChangeWallPaperBroadcastRecevier cwpbr = new ChangeWallPaperBroadcastRecevier();
        NotificateBroadcastRecevier nbr = new NotificateBroadcastRecevier();
        cwpbr.setAlarm(getApplicationContext(), availableDateList);
        nbr.setAlarm(getApplicationContext(), availableDateList);
    }

}

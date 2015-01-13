package com.example.volcanoalarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.volcanoalarm.bean.AlarmDate;
import com.example.volcanoalarm.bean.RelaxDate;
import com.example.volcanoalarm.db.VolcanoDatabase;
import com.example.volcanoalarm.schedule.ChangeWallPaperBroadcastRecevier;
import com.example.volcanoalarm.schedule.NotificateBroadcastRecevier;
import com.example.volcanoalarm.schedule.ReturnUserWallPaperBroadcastRecevier;
import com.example.volcanoalarm.util.DateUtil;
import com.example.volcanoalarm.util.LogUtil;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

public class MainActivity extends ActionBarActivity {

    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;
    private VolcanoDatabase db;
    private ChangeWallPaperBroadcastRecevier cwpbr;
    private ReturnUserWallPaperBroadcastRecevier ruwpbr;
    private NotificateBroadcastRecevier nbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        LogUtil.appendLog("==start==");
        // comment this if want to see db info
        setInvisible();
        
        // init db and broadcast receiver
        initDbAndBr();

        // Setup caldroid fragment
        caldroidFragment = new CaldroidFragment();

        // Setup arguments

        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            // Uncomment this to customize startDayOfWeek
            // args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
            // CaldroidFragment.TUESDAY); // Tuesday
            caldroidFragment.setArguments(args);
        }

        setCustomResourceForDates();

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                // check
                if (isBeforeToday(date)) {
                    Toast.makeText(getApplicationContext(), "不能设置今天以前的日期",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                
                String dateStr = DateUtil.toDateString(date, DateUtil.DATE_FORMAT_YYYY_MM_DD_HYPHEN);

                List<Date> beforeAvailableDateList = getAvailableDateList();
                
                // set alarm_date to db
                setAlarmDateToDb(dateStr);

                List<Date> afterAvailableDateList = getAvailableDateList();
                
                // set schedule
                setSchedule(beforeAvailableDateList, afterAvailableDateList);

                // refresh calendar color
                refreshCalendarColor(date, dateStr);

            }

            

            @Override
            public void onChangeMonth(int month, int year) {
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                
                String dateStr = DateUtil.toDateString(date, DateUtil.DATE_FORMAT_YYYY_MM_DD_HYPHEN);
                
                // set relax_date to db
                setRelaxDateToDb(dateStr);

                // refresh calendar color
                refreshCalendarColorOnLongClick(date, dateStr);
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);
    }

    private void setInvisible() {
        Button btn = (Button) findViewById(R.id.show_db_value_button);
        TextView tv = (TextView) findViewById(R.id.textview);
        btn.setVisibility(View.INVISIBLE);
        tv.setVisibility(View.INVISIBLE);
    }
    
    private void setCustomResourceForDates() {

        List<RelaxDate> rdList = db.getAllRelaxDates();
        List<AlarmDate> adList = db.getAllAlarmDates();

        if (caldroidFragment != null) {
            for (RelaxDate rd : rdList) {
                Date greenDate = DateUtil.strToDate(rd.getDate(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HYPHEN);
                setGreenColor(greenDate);
            }

            for (AlarmDate ad : adList) {
                Date redDate = DateUtil.strToDate(ad.getDate(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HYPHEN);
                setRedColor(redDate);
            }
        }
    }

    private void initDbAndBr() {
        db = new VolcanoDatabase(getApplicationContext());
        cwpbr = new ChangeWallPaperBroadcastRecevier();
        ruwpbr = new ReturnUserWallPaperBroadcastRecevier();
        nbr = new NotificateBroadcastRecevier();
    }
    
    private void refreshCalendarColor(Date date, String dateStr) {
        if (db.isSettedAlarmDate(dateStr)) {
            setRedColor(date);
        } else {
            setWhiteColor(date);
        }

        caldroidFragment.refreshView();
    }

    private void setWhiteColor(Date date) {
        caldroidFragment.setBackgroundResourceForDate(R.color.white,
                date);
        caldroidFragment.setTextColorForDate(R.color.black, date);
    }

    private void setRedColor(Date date) {
        caldroidFragment.setBackgroundResourceForDate(R.color.red,
                date);
        caldroidFragment.setTextColorForDate(R.color.white, date);
    }

    private void setSchedule(List<Date> beforeAvailableDateList, List<Date> afterAvailableDateList) {
        // notification alarm
        nbr.cancelAlarm(getBaseContext(), beforeAvailableDateList);
        nbr.setAlarm(getBaseContext(), afterAvailableDateList);
        
        // change to volcano wall paper alarm (00:00 per day in dateList)
        cwpbr.cancelAlarm(getBaseContext(), beforeAvailableDateList);
        cwpbr.setAlarm(getBaseContext(), afterAvailableDateList);
        
        // change to user wall paper alarm (23:29 per day in dateList)
        ruwpbr.cancelAlarm(getBaseContext(), beforeAvailableDateList);
        ruwpbr.setAlarm(getBaseContext(), afterAvailableDateList);
    }

    private void setAlarmDateToDb(String dateStr) {
        AlarmDate ad = new AlarmDate();
        ad.setDate(dateStr);
        db.deleteRelaxDate(dateStr);
        if (!db.isSettedAlarmDate(dateStr)) {
            db.addAlarmDate(ad);
        } else {
            db.deleteAlarmDate(dateStr);
        }
    }

    private List<Date> getAvailableDateList() {
        List<AlarmDate> availableAdList = db.getAvalibleAlarmDates();
        List<Date> availableDateList = new ArrayList<Date>();
        for(AlarmDate aDate : availableAdList) {
            availableDateList.add(DateUtil.strToDate(aDate.getDate(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HYPHEN));
        }
        return availableDateList;
    }

    private boolean isBeforeToday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        
        return cal.getTime().compareTo(new Date()) < 0;
    }
    
    
    
    private void refreshCalendarColorOnLongClick(Date date, String dateStr) {
        if (db.isSettedRelaxDate(dateStr)) {
            setGreenColor(date);
        } else {
            setWhiteColor(date);
        }
        caldroidFragment.refreshView();
    }



    private void setGreenColor(Date date) {
        caldroidFragment.setBackgroundResourceForDate(R.color.green,
                date);
        caldroidFragment.setTextColorForDate(R.color.white, date);
    }



    private void setRelaxDateToDb(String dateStr) {
        RelaxDate rd = new RelaxDate();
        rd.setDate(dateStr);
        db.deleteAlarmDate(dateStr);
        if (!db.isSettedRelaxDate(dateStr)) {
            db.addRelaxDate(rd);
        } else {
            db.deleteRelaxDate(dateStr);
        }
    }

    public void showDbValueBtnClickListener(View v) {
        final TextView textView = (TextView)findViewById(R.id.textview);
        List<RelaxDate> rdList = db.getAllRelaxDates();
        List<AlarmDate> avilableAdList = db.getAvalibleAlarmDates();
        List<AlarmDate> adList = db.getAllAlarmDates();

        String text = "";
        text += "relax date \n";
        for (RelaxDate rd : rdList) {
            text += rd.getDate() + "\n";
        }
        text += "avilble alarm date \n";
        for (AlarmDate ad : avilableAdList) {
            text += ad.getDate() + "\n";
        }
        text += "all alarm date \n";
        for (AlarmDate ad : adList) {
            text += ad.getDate() + "\n";
        }
        textView.setText(text);
    }

    /**
     * Save current states of the Caldroid here
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }

        if (dialogCaldroidFragment != null) {
            dialogCaldroidFragment.saveStatesToKey(outState,
                    "DIALOG_CALDROID_SAVED_STATE");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

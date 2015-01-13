package com.example.volcanoalarm;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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

    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;
    VolcanoDatabase db;
    ChangeWallPaperBroadcastRecevier cwpbr;
    ReturnUserWallPaperBroadcastRecevier ruwpbr;
    NotificateBroadcastRecevier nbr;

    private void setCustomResourceForDates() {

        List<RelaxDate> rdList = db.getAllRelaxDates();
        List<AlarmDate> adList = db.getAllAlarmDates();

        if (caldroidFragment != null) {
            for (RelaxDate rd : rdList) {
                Date greenDate = DateUtil.strToDate(rd.getDate(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HYPHEN);
                caldroidFragment.setBackgroundResourceForDate(R.color.green, greenDate);
                caldroidFragment.setTextColorForDate(R.color.white, greenDate);
            }

            for (AlarmDate ad : adList) {
                Date redDate = DateUtil.strToDate(ad.getDate(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HYPHEN);
                caldroidFragment.setBackgroundResourceForDate(R.color.red, redDate);
                caldroidFragment.setTextColorForDate(R.color.white, redDate);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        File logFile = new File(Environment.getExternalStorageDirectory() + "/volcanoLog.txt");
//        logFile.delete();
        
        LogUtil.appendLog("==start==");
        
        db = new VolcanoDatabase(getApplicationContext());
        cwpbr = new ChangeWallPaperBroadcastRecevier();
        ruwpbr = new ReturnUserWallPaperBroadcastRecevier();
        nbr = new NotificateBroadcastRecevier();

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****
        caldroidFragment = new CaldroidFragment();

        // //////////////////////////////////////////////////////////////////////
        // **** This is to show customized fragment. If you want customized
        // version, uncomment below line ****
        // caldroidFragment = new CaldroidSampleCustomFragment();

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
                
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                
                if(cal.getTime().compareTo(new Date()) < 0){
                    Toast.makeText(getApplicationContext(), "不能设置今天以前的日期",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                
                String dateStr = DateUtil.toDateString(date, DateUtil.DATE_FORMAT_YYYY_MM_DD_HYPHEN);

                List<AlarmDate> beforeAvailableAdList = db.getAvalibleAlarmDates();
                List<Date> beforeAvailableDateList = new ArrayList<Date>();
                for(AlarmDate aDate : beforeAvailableAdList) {
                    beforeAvailableDateList.add(DateUtil.strToDate(aDate.getDate(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HYPHEN));
                }
                
                // add alarm_date to db

                AlarmDate ad = new AlarmDate();
                ad.setDate(dateStr);
                db.deleteRelaxDate(dateStr);
                if (!db.isSettedAlarmDate(dateStr)) {
                    db.addAlarmDate(ad);
                } else {
                    db.deleteAlarmDate(dateStr);
                }

                // add alarm schedule
                List<AlarmDate> afterAvailableAdList = db.getAvalibleAlarmDates();
                List<Date> afterAvailableDateList = new ArrayList<Date>();
                for(AlarmDate aDate : afterAvailableAdList) {
                    afterAvailableDateList.add(DateUtil.strToDate(aDate.getDate(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HYPHEN));
                }
                
                // notification alarm
                nbr.cancelAlarm(getBaseContext(), beforeAvailableDateList);
                nbr.setAlarm(getBaseContext(), afterAvailableDateList);
                
                // change to volcano wall paper alarm (00:00 per day in dateList)
                cwpbr.cancelAlarm(getBaseContext(), beforeAvailableDateList);
                cwpbr.setAlarm(getBaseContext(), afterAvailableDateList);
                
                // change to user wall paper alarm (23:29 per day in dateList)
                ruwpbr.cancelAlarm(getBaseContext(), beforeAvailableDateList);
                ruwpbr.setAlarm(getBaseContext(), afterAvailableDateList);

                // reset calendar color
                if (db.isSettedAlarmDate(dateStr)) {
                    caldroidFragment.setBackgroundResourceForDate(R.color.red,
                            date);
                    caldroidFragment.setTextColorForDate(R.color.white, date);
                } else {
                    caldroidFragment.setBackgroundResourceForDate(R.color.white,
                            date);
                    caldroidFragment.setTextColorForDate(R.color.black, date);
                }

                caldroidFragment.refreshView();

            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                Toast.makeText(getApplicationContext(),
                        "Long click " + formatter.format(date),
                        Toast.LENGTH_SHORT).show();

                // add relax_date to db
                String dateStr = DateUtil.toDateString(date, DateUtil.DATE_FORMAT_YYYY_MM_DD_HYPHEN);
                RelaxDate rd = new RelaxDate();
                rd.setDate(dateStr);
                db.deleteAlarmDate(dateStr);
                if (!db.isSettedRelaxDate(dateStr)) {
                    db.addRelaxDate(rd);
                } else {
                    db.deleteRelaxDate(dateStr);
                }

                // reset calendar color
                if (db.isSettedRelaxDate(dateStr)) {
                    caldroidFragment.setBackgroundResourceForDate(R.color.green,
                            date);
                    caldroidFragment.setTextColorForDate(R.color.white, date);
                } else {
                    caldroidFragment.setBackgroundResourceForDate(R.color.white,
                            date);
                    caldroidFragment.setTextColorForDate(R.color.black, date);
                }
                caldroidFragment.refreshView();
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                    Toast.makeText(getApplicationContext(),
                            "Caldroid view is created", Toast.LENGTH_SHORT)
                            .show();
                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);

        final TextView textView = (TextView)findViewById(R.id.textview);

        final Button customizeButton = (Button)findViewById(R.id.customize_button);

        // Customize the calendar
        customizeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (undo) {
                    customizeButton.setText(getString(R.string.customize));
                    textView.setText("");

                    // Reset calendar
                    caldroidFragment.clearDisableDates();
                    caldroidFragment.clearSelectedDates();
                    caldroidFragment.setMinDate(null);
                    caldroidFragment.setMaxDate(null);
                    caldroidFragment.setShowNavigationArrows(true);
                    caldroidFragment.setEnableSwipe(true);
                    caldroidFragment.refreshView();
                    undo = false;
                    return;
                }

                // Else
                undo = true;
                customizeButton.setText(getString(R.string.undo));
                Calendar cal = Calendar.getInstance();

                // Min date is last 7 days
                cal.add(Calendar.DATE, -7);
                Date minDate = cal.getTime();

                // Max date is next 7 days
                cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 14);
                Date maxDate = cal.getTime();

                // Set selected dates
                // From Date
                cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 2);
                Date fromDate = cal.getTime();

                // To Date
                cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 3);
                Date toDate = cal.getTime();

                // Set disabled dates
                ArrayList<Date> disabledDates = new ArrayList<Date>();
                for (int i = 5; i < 8; i++) {
                    cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, i);
                    disabledDates.add(cal.getTime());
                }

                // Customize
                caldroidFragment.setMinDate(minDate);
                caldroidFragment.setMaxDate(maxDate);
                caldroidFragment.setDisableDates(disabledDates);
                caldroidFragment.setSelectedDates(fromDate, toDate);
                caldroidFragment.setShowNavigationArrows(false);
                caldroidFragment.setEnableSwipe(false);

                caldroidFragment.refreshView();

                // Move to date
                // cal = Calendar.getInstance();
                // cal.add(Calendar.MONTH, 12);
                // caldroidFragment.moveToDate(cal.getTime());

                String text = "Today: " + formatter.format(new Date()) + "\n";
                text += "Min Date: " + formatter.format(minDate) + "\n";
                text += "Max Date: " + formatter.format(maxDate) + "\n";
                text += "Select From Date: " + formatter.format(fromDate)
                        + "\n";
                text += "Select To Date: " + formatter.format(toDate) + "\n";
                for (Date date : disabledDates) {
                    text += "Disabled Date: " + formatter.format(date) + "\n";
                }

                textView.setText(text);
            }
        });

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

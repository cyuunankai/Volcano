package com.example.volcanoalarm.db;

import android.provider.BaseColumns;

public class VolcanoContract {

	public static abstract class RelaxDate implements BaseColumns {
        public static final String TABLE_NAME = "relax_dates";
        public static final String COLUMN_NAME_DATE = "date";
    }
	
	public static abstract class AlarmDate implements BaseColumns {
        public static final String TABLE_NAME = "alarm_dates";
        public static final String COLUMN_NAME_DATE = "date";
    }
}

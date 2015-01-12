package com.example.volcanoalarm.util;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 鏃ヤ粯闁㈤�銇儲銉笺儐銈ｃ儶銉嗐偅
 *
 * @author sugiyama_koki_gn
 *
 */
public class DateUtil {

    /** 銉偓銉�*/

    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy/MM/dd";

    public static final String DATE_FORMAT_YYYY_MM_DD_HYPHEN = "yyyy-MM-dd";

    public static final String DATE_FORMAT_YYYY_MM = "yyyy/MM";

    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy/MM/dd HH:mm:ss";

    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS_HYPHEN = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static final String DATE_FORMAT_HH_MM_SS = "HH:mm:ss";
    
    public static String getToday(){
    	return toDateString(new java.util.Date(), DATE_FORMAT_YYYY_MM_DD_HYPHEN);
    }
    
    public static String getSysTimeStr(){
    	return toDateString(new java.util.Date(), DATE_FORMAT_YYYY_MM_DD_HH_MM_SS_HYPHEN);
    }

    /**
     * 銈裤偆銉犮偣銈裤兂銉椼倰鏃ヤ粯銇枃瀛楀垪銇鎻�
     *
     * @param timestamp
     * @param format
     * @return
     */
    public static String toDateString(Timestamp timestamp, String format) {
        String dateTimeStr = new SimpleDateFormat(format).format(timestamp);

        return dateTimeStr;
    }

    /**
     * Time銈掓棩浠樸伄鏂囧瓧鍒椼伀澶夋彌
     *
     * @param time
     * @param format
     * @return
     */
    public static String toDateString(Time time, String format) {
        String dateTimeStr = new SimpleDateFormat(format).format(time);

        return dateTimeStr;
    }

    /**
     * java.sql.Date銈掓棩浠樸伄鏂囧瓧鍒椼伀澶夋彌
     *
     * @param date
     * @param format
     * @return
     */
    public static String toDateString(Date date, String format) {
        String dateTimeStr = new SimpleDateFormat(format).format(date);

        return dateTimeStr;
    }

    /**
     * java.util.Date銈掓棩浠樸伄鏂囧瓧鍒椼伀澶夋彌
     *
     * @param date
     * @param format
     * @return
     */
    public static String toDateString(java.util.Date date, String format) {
        String dateTimeStr = new SimpleDateFormat(format).format(date);

        return dateTimeStr;
    }

    /**
     * String鍨嬨伄鏃ヤ粯銉囥兗銈裤倰Timestamp鍨嬨伀澶夋彌
     *
     * @param str
     * @return
     */
    public static Timestamp strToTimestamp(String str, String format) {
        Timestamp time = null;
        try {
            time = new Timestamp(new SimpleDateFormat(format).parse(str).getTime());
        } catch (ParseException e) {
        }
        return time;
    }

    /**
     * String鍨嬨伄鏃ヤ粯銉囥兗銈裤倰java.util.Date鍨嬨伀澶夋彌
     *
     * @param str
     * @return
     */
    public static java.util.Date strToDate(String str, String format) {
        java.util.Date date = null;
        try {
            date = new java.util.Date(new SimpleDateFormat(format).parse(str).getTime());
        } catch (ParseException e) {
        }
        return date;
    }

    /**
     * 2銇ゃ伄鏈熼枔銇岄噸瑜囥仐銇︺亜銈嬨亱銈掋儊銈с儍銈�
     *
     * @param startDate1 1銇ょ洰銇湡闁撱伄闁嬪鏃�
     * @param endDate1 1銇ょ洰銇湡闁撱伄绲備簡鏃�
     * @param startDate2 锛掋仱鐩伄鏈熼枔銇枊濮嬫棩
     * @param endDate2 2銇ょ洰銇湡闁撱伄绲備簡鏃�
     * @return 閲嶈銇椼仸銇勩倠鍫村悎銇痶rue
     */
    public static boolean isDateDuplicate(Date startDate1, Date endDate1, Date startDate2, Date endDate2) {
        return (endDate2.compareTo(startDate1) > 0 && endDate1.compareTo(startDate2) > 0);
    }



    /**
     * 銇傘倠鏃ヤ粯銇綍鏃ュ緦銇棩浠樸倰瑷堢畻銆�
     *
     * @param n 鏃ユ暟
     * @param date 瀵捐薄鏃ヤ粯
     * @return 瑷堢畻銇椼仧寰屻伄鏃ヤ粯
     */
    public static java.util.Date nDaysAfter(int n, java.util.Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, n);
        return cal.getTime();
    }

    /**
     * 鏃ヤ粯銇瘮杓�
     */
    public static final boolean before(java.util.Date d1, java.util.Date d2) {
        return roundYMD(d1).before(roundYMD(d2));
    }

    /**
     * 鍚屻仒鏃ヤ粯銇垽瀹�
     */
    public static final boolean sameDate(java.util.Date d1, java.util.Date d2) {
        return roundYMD(d1).equals(roundYMD(d2));
    }

    /**
     * 鏃ヤ粯銇瘮杓�
     */
    public static final boolean beforeDate(java.util.Date d1, java.util.Date d2) {
        return before(d1, d2) || sameDate(d1, d2);
    }

    /**
     * 鏃ヤ粯銇瘮杓�
     */
    public static final boolean before(Calendar c1, Calendar c2) {
        return roundYMD(c1).before(roundYMD(c2));
    }

    /**
     * 骞存湀鏃ャ伄銇裤伄Date銇偑銉栥偢銈с偗銉堛伀瑷畾銇椼伨銇欍�
     *
     * @param date
     * @return
     */
    public static final java.util.Date roundYMD(java.util.Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return roundYMD(cal).getTime();
    }

    /**
     * 骞存湀鏃ャ伄銇裤伄Calendar銈儢銈搞偋銈儓銇ō瀹氥仐銇俱仚銆�
     *
     * @param date
     * @return
     */
    public static final Calendar roundYMD(Calendar cal) {
        Calendar newCal = Calendar.getInstance();
        newCal.clear();
        newCal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        newCal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        newCal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
        return newCal;
    }

    /**
     * 2銇ゃ伄鏃ヤ粯銇棩鏁板樊銈掕▓绠椼仚銈嬨�
     *
     * @param date1
     * @param date2
     * @return 鏃ユ暟銇�
     */
    public static final int diffDays(Date date1, Date date2) {
        long datetime1 = date1.getTime();
        long datetime2 = date2.getTime();
        long oneday = 1000 * 60 * 60 * 24;
        long diffdays = (datetime1 - datetime2) / oneday;
        return (int) diffdays;
    }

    /**
     * 鏈�鏃ャ倰鍙栧緱銇欍倠銆�
     *
     */
    public static String calendarToOneDigitMonthDay(Calendar c) {
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return "" + (month + 1) + "," + day;
    }

    /**
     * 2銇ゃ伄鏃ヤ粯銇棩鏁板樊銈掕▓绠椼仚銈嬨�
     *
     */
    public static Calendar strToCalendar(String dateStr, String format) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        java.util.Date date = sdf.parse(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 鏃ヤ粯銉曘偐銉笺優銉冦儓
     *
     */
    public static String calendarToStr(Calendar c, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(c.getTime());
    }

    /**
     * 2銇ゃ伄鏃ヤ粯銇棩鏁板樊銈掕▓绠椼仚銈嬨�
     *
     */
    public static int diffDays(Calendar start, Calendar end) {
        long sl = start.getTimeInMillis();
        long el = end.getTimeInMillis();

        long ei = el - sl;
        return (int) (ei / (1000 * 60 * 60 * 24));
    }

    /**
     * 銉嶃儍銉堛儻銉笺偗銇敞鏂囥伄闁嬪鍙兘銇ㄧ祩浜嗘棩銈掑彇寰椼仚銈�
     * @param orderStopDate
     * @param airtimeMaxDays
     * @return
     */
    public static java.util.Date[] getNormalOrderStartEndDates(int orderStopDate, int airtimeMaxDays) {
        Calendar todayCal = Calendar.getInstance();
        int todayYear = todayCal.get(Calendar.YEAR);
        int todayMon = todayCal.get(Calendar.MONTH);
        int todayDate = todayCal.get(Calendar.DATE);
        todayCal.set(todayYear, todayMon, todayDate, 0, 0, 0);
        // 娉ㄦ枃绶犲垏鏃�1鏃ャ亱銈夋敞鏂囧彲鑳�
        todayCal.add(Calendar.DATE, (orderStopDate + 1));
        java.util.Date orderStartDay = todayCal.getTime();

        // 娉ㄦ枃闁嬪鏃ャ亱銈�3鏃ラ枔銇屾敞鏂囧彲鑳芥湡闁�
        Calendar endCal = Calendar.getInstance();
        endCal.clear();
        endCal.setTime(orderStartDay);
        endCal.add(Calendar.DATE, (airtimeMaxDays - 1));
        java.util.Date endOrderDate = endCal.getTime();

        return new java.util.Date[]{orderStartDay, endOrderDate};
    }

    /**
     * String[][] 鏃ヤ粯 => String[] 鏃ヤ粯
     *
     * @param periodArr
     * @return
     * @throws Exception
     */
    public static String[] getFlattenDates(String[][] periodArr) throws Exception {
        return getFlattenDates(periodArr, DATE_FORMAT_YYYY_MM_DD);
    }

    /**
     * String[][] 鏃ヤ粯 => String[] 鏃ヤ粯
     *
     * @param periodArr
     * @param dateFormat
     * @return
     * @throws Exception
     */
    public static String[] getFlattenDates(String[][] periodArr, String dateFormat) throws Exception {

        // dates is null or empty, return empty String[]
        if (periodArr == null || periodArr.length == 0) {
            return new String[] {};
        }

        List<String> resultList = new ArrayList<String>();

        for (int i = 0; i < periodArr.length; i++) {

            // one date only
            if (periodArr[i].length == 1) {
                resultList.add(periodArr[i][0]);
            }

            // date is two
            if (periodArr[i].length != 1) {

                List<String> periodList = getPeroidDates(periodArr[i][0], periodArr[i][1], dateFormat);
                resultList.addAll(periodList);
            }
        }

        // change list to array
        String[] resultArr = new String[resultList.size()];
        resultList.toArray(resultArr);
        return resultArr;
    }

    /**
     * String[] 鏃ヤ粯 => String[][] 鏃ヤ粯
     *
     * @param dates
     * @param dateFormat
     * @return
     * @throws Exception
     */
    public static String[][] getStereoDates(String[] dates) throws Exception {
        return getStereoDates(dates, DATE_FORMAT_YYYY_MM_DD);
    }

    /**
     * String[] 鏃ヤ粯 => String[][] 鏃ヤ粯
     *
     * @param dates
     * @param dateFormat
     * @return
     * @throws Exception
     */
    public static String[][] getStereoDates(String[] dates, String dateFormat) throws Exception {

        // dates is null or empty, return empty String[][]
        if (dates == null || dates.length == 0) {
            return new String[][] {};
        }

        // chang string array to calendar array
        Calendar[] cdates = new Calendar[dates.length];
        for (int i = 0; i < dates.length; i++) {
            Calendar calendarTime = strToCalendar(dates[i], dateFormat);
            cdates[i] = calendarTime;
        }

        // one date only ,return one day String[]
        if (cdates.length == 1) {
            return new String[][] {dates};
        }

        // date is more than one day
        // compare
        Calendar compareSource = null;
        Calendar compareTarget = null;
        List<Calendar> continueDateList = new ArrayList<Calendar>();
        List<String[]> resultList = new ArrayList<String[]>();

        continueDateList.add(cdates[0]);
        for (int i = 0; i < cdates.length - 1; i++) {
            compareSource = cdates[i];
            compareTarget = cdates[i + 1];

            if (diffDays(compareSource, compareTarget) == 1) {
                continueDateList.add(compareTarget);
            } else {
                setResultByContinueList(continueDateList, resultList);

                // reinit the continueDateList, add "compareTarget" as first
                // element
                continueDateList = new ArrayList<Calendar>();
                continueDateList.add(compareTarget);
            }
        }

        // last continue date
        setResultByContinueList(continueDateList, resultList);

        // change list to array
        String[][] resultArr = new String[resultList.size()][];
        resultList.toArray(resultArr);

        return resultArr;
    }

    /**
     * 鏈熼枔鏃ヤ粯銈掑彇寰椼仚銈�
     *
     */
    public static List<String> getPeroidDates(String startDateStr, String endDateStr) throws Exception {
        return getPeroidDates(startDateStr, endDateStr, DATE_FORMAT_YYYY_MM_DD);
    }

    /**
     * 鏈熼枔鏃ヤ粯銈掑彇寰椼仚銈�
     *
     */
    public static List<String> getPeroidDates(String startDateStr, String endDateStr, String dateFormat)
            throws Exception {
        List<String> resultList = new ArrayList<String>();

        Calendar periodStartTime = strToCalendar(startDateStr, dateFormat);
        Calendar periodEndTime = strToCalendar(endDateStr, dateFormat);
        int days = diffDays(periodStartTime, periodEndTime);

        for (int i = 0; i <= days; i++) {
            Calendar tmpPeriod = strToCalendar(startDateStr, dateFormat);
            tmpPeriod.add(Calendar.DAY_OF_WEEK, i);
            resultList.add(calendarToStr(tmpPeriod, DATE_FORMAT_YYYY_MM_DD));
        }
        return resultList;
    }

    /**
     * 鎸囧畾鏈熼枔鍐呫伄鏃ヤ粯銈掑垽瀹氥仚銈�
     *
     */
    public static boolean isDateIn(String dateStr, String startDateStr, String endDateStr) {
        java.util.Date startDate = strToDate(startDateStr, DATE_FORMAT_YYYY_MM_DD);
        java.util.Date endDate = strToDate(endDateStr, DATE_FORMAT_YYYY_MM_DD);
        java.util.Date sourceDate = strToDate(dateStr, DATE_FORMAT_YYYY_MM_DD);
        if (sourceDate.compareTo(startDate) >= 0 && sourceDate.compareTo(endDate) <= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 鎸囧畾鏈熼枔鍐呫伄鏃ヤ粯銈掑垽瀹氥仚銈�
     *
     */
    public static boolean isDateIn(String dateStr, java.util.Date startDate, java.util.Date endDate) {
        java.util.Date sourceDate = strToDate(dateStr, DATE_FORMAT_YYYY_MM_DD);
        if (sourceDate.compareTo(startDate) >= 0 && sourceDate.compareTo(endDate) <= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 鎸囧畾鏈熼枔鍐呫伄鏃ヤ粯銈掑垽瀹氥仚銈�
     * 
     */
    public static boolean isDateIn(java.util.Date sourceDate, java.util.Date startDate, java.util.Date endDate) {
        if (sourceDate.compareTo(startDate) >= 0 && sourceDate.compareTo(endDate) <= 0) {
            return true;
        } else {
            return false;
        }
    }



    /**
     * 鏈熼枔闁嬪鏃ャ仺绲備簡鏃ャ倰鍙栧緱銇欍倠
     *
     */
    private static void setResultByContinueList(List<Calendar> continueDateList, List<String[]> resultList)
            throws Exception {
        if (continueDateList.size() == 1) {
            // no continue dates, one alone day
            String result = calendarToStr(continueDateList.get(0), DATE_FORMAT_YYYY_MM_DD);
            resultList.add(new String[] {result});
        } else {
            // more than two day continued
            String start = calendarToStr(continueDateList.get(0), DATE_FORMAT_YYYY_MM_DD);
            String end = calendarToStr(continueDateList.get(continueDateList.size() - 1), DATE_FORMAT_YYYY_MM_DD);
            resultList.add(new String[] {start, end});
        }
    }



}

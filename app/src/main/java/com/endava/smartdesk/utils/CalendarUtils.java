package com.endava.smartdesk.utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtils {

    private String mCalendarValue;

    public Date getDateAndTimeFromString(String dateAndTime) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        try {
            return dateFormat.parse(dateAndTime);
        } catch (ParseException e) {
            Log.e("getDate", "Unable to format the Date");
        }
        return null;
    }

    public Date getTimeFromString(String time) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        try {
            return timeFormat.parse(time);
        } catch (ParseException e) {
            Log.e("getDate", "Unable to format the time");
        }
        return null;
    }

    public void getDateAndTime(Context context, EditText dateField) {
        mCalendarValue = "";
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        DatePickerDialog picker = new DatePickerDialog(context,
                (datePickerView, newYear, newMonth, newDay) -> {
                    mCalendarValue = newDay + "/" + (newMonth + 1) + "/" + newYear;
                    @SuppressLint("DefaultLocale") TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                            (timePickerView, newHour, newMinute) -> {
                                mCalendarValue = mCalendarValue + " " + String.format("%02d:%02d", newHour, newMinute);
                                dateField.setText(mCalendarValue);
                            }, hour, minute, false);
                    timePickerDialog.show();
                }, year, month, day);
        picker.show();
    }

    public void getTime(Context context, EditText timeField) {
        mCalendarValue = "";
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        @SuppressLint("DefaultLocale") TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                (timePickerView, newHour, newMinute) -> {
                    mCalendarValue = String.format("%02d:%02d", newHour, newMinute);
                    timeField.setText(mCalendarValue);
                }, hour, minute, false);
        timePickerDialog.show();
    }

    @SuppressLint("DefaultLocale")
    public String setDateAndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.DAY_OF_MONTH) + " "
                + String.format("%02d:%02d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
    }

    @SuppressLint("DefaultLocale")
    public String setTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return String.format("%02d:%02d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
    }
}

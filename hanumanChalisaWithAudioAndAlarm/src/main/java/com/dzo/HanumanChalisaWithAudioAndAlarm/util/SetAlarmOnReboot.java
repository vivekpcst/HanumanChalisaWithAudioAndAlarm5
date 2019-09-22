package com.dzo.HanumanChalisaWithAudioAndAlarm.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dzo.HanumanChalisaWithAudioAndAlarm.receiver.AlarmReceiver;

public class SetAlarmOnReboot 
{
	Context ctx;
	String strAmPM;
	boolean alarmEnabled;
	public SetAlarmOnReboot(Context ctx)
	{
		this.ctx = ctx;
	}//Constructor
	/**
	 * Nitish 13 Dec, 2012
	 * Utility to reset alarm on reboot/snooze
	 * */
	public void setDailyAlarmAfterReboot(int reqId, String time, String repeatStatus) 
    {
		PendingIntent Sender = null;
		Intent AlarmIntent = new Intent(ctx, AlarmReceiver.class);
        AlarmManager AlmMgr = (AlarmManager)ctx.getSystemService(Context.ALARM_SERVICE);
        
        //Build Calendar objects for setting alarm
        Calendar curCalendar = Calendar.getInstance();
        Calendar alarmCalendar = Calendar.getInstance();
        curCalendar.set(Calendar.SECOND, 0);
        curCalendar.set(Calendar.MILLISECOND, 0);    		
        alarmCalendar.set(Calendar.SECOND, 0);
        alarmCalendar.set(Calendar.MILLISECOND, 0);
        
        RescheduleAlarmTime rescheduleTime = formatTime(time);
        
        if(repeatStatus != null && !repeatStatus.equals("") 
        		&& !repeatStatus.equals("null"))
        {
        	if(repeatStatus.equals("Daily"))
        	{
        		alarmCalendar.set(Calendar.HOUR_OF_DAY, rescheduleTime.hour);
        	    alarmCalendar.set(Calendar.MINUTE, rescheduleTime.min);
        	        	
        	    if (alarmCalendar.getTimeInMillis() <= curCalendar.getTimeInMillis()) 
        	    {
        	     	alarmCalendar.add(Calendar.HOUR, 24);
        	    }//if
        	    alarmEnabled = true;
        		AlarmIntent.putExtra("REQUEST CODE", reqId);
        		AlarmIntent.putExtra("ALARM_ENABLED", alarmEnabled);
                
        		Log.v("HCMultipleAlarmBoot", ""+reqId);
                
                Sender = PendingIntent.getBroadcast(ctx, reqId, AlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                
                AlmMgr.setRepeating(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), 
                			24 * 60 * 60 * 1000, Sender);
        	}//if
        }//if
        Log.v("HCSetAlarmonReboot", "daily alarm set");
    }//setDailyAlarmAfterReboot 
	
	public void setSundayAlarmAfterReboot(String time, String repeatStatus, String sundayRepeatId) 
	{
		PendingIntent Sender = null;
		Intent AlarmIntent = new Intent(ctx, AlarmReceiver.class);
        AlarmManager AlmMgr = (AlarmManager)ctx.getSystemService(Context.ALARM_SERVICE);
        
        //Build Calendar objects for setting alarm
        Calendar curCalendar = Calendar.getInstance();
        Calendar alarmCalendar = Calendar.getInstance();
        curCalendar.set(Calendar.SECOND, 0);
        curCalendar.set(Calendar.MILLISECOND, 0);    		
        alarmCalendar.set(Calendar.SECOND, 0);
        alarmCalendar.set(Calendar.MILLISECOND, 0);
        
        RescheduleAlarmTime rescheduleTime = formatTime(time);
        
        if(repeatStatus != null && !repeatStatus.equals("") 
        		&& !repeatStatus.equals("null"))
        {
        	if(sundayRepeatId != null && !sundayRepeatId.equals("") 
        			&& !sundayRepeatId.equals("null"))
        	{
        		alarmCalendar.set(Calendar.DAY_OF_WEEK, 1);
            	alarmCalendar.set(Calendar.HOUR_OF_DAY, rescheduleTime.hour);
            	alarmCalendar.set(Calendar.MINUTE, rescheduleTime.min);
        		int sundayRepId = Integer.parseInt(sundayRepeatId);
        		
        		alarmEnabled = true;
        		AlarmIntent.putExtra("REQUEST CODE", sundayRepeatId);
        		AlarmIntent.putExtra("ALARM_ENABLED", alarmEnabled);
        		
        		if(alarmCalendar.before(curCalendar))
            	{
            		alarmCalendar.add(Calendar.WEEK_OF_YEAR, 1);
            		Log.v("HCSetAlarmOnReboot", "alarm set for next week sunday");
            	}//if
        		
        		Sender = PendingIntent.getBroadcast(ctx, sundayRepId, AlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        		AlmMgr.setRepeating(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), 
            			AlarmManager.INTERVAL_DAY * 7, Sender);
        		Log.v("HCSetAlarmOnReboot", "alarm set for sunday");
        	}//if
        }//if
	}//setSundayAlarmAfterReboot
	
	public void setMondayAlarmAfterReboot(String time, String repeatStatus, String mondayRepeatId) 
	{
		PendingIntent Sender = null;
		Intent AlarmIntent = new Intent(ctx, AlarmReceiver.class);
        AlarmManager AlmMgr = (AlarmManager)ctx.getSystemService(Context.ALARM_SERVICE);
        
        //Build Calendar objects for setting alarm
        Calendar curCalendar = Calendar.getInstance();
        Calendar alarmCalendar = Calendar.getInstance();
        curCalendar.set(Calendar.SECOND, 0);
        curCalendar.set(Calendar.MILLISECOND, 0);    		
        alarmCalendar.set(Calendar.SECOND, 0);
        alarmCalendar.set(Calendar.MILLISECOND, 0);
        
        RescheduleAlarmTime rescheduleTime = formatTime(time);
        
        if(repeatStatus != null && !repeatStatus.equals("") 
        		&& !repeatStatus.equals("null"))
        {
        	if(mondayRepeatId != null && !mondayRepeatId.equals("") 
        			&& !mondayRepeatId.equals("null"))
        	{
        		alarmCalendar.set(Calendar.DAY_OF_WEEK, 2);
            	alarmCalendar.set(Calendar.HOUR_OF_DAY, rescheduleTime.hour);
            	alarmCalendar.set(Calendar.MINUTE, rescheduleTime.min);
        		int mondayRepId = Integer.parseInt(mondayRepeatId);
        		
        		alarmEnabled = true;
        		AlarmIntent.putExtra("REQUEST CODE", mondayRepId);
        		AlarmIntent.putExtra("ALARM_ENABLED", alarmEnabled);
        		
        		if(alarmCalendar.before(curCalendar))
            	{
            		alarmCalendar.add(Calendar.WEEK_OF_YEAR, 1);
            		Log.v("HCSetAlarmOnReboot", "alarm set for next week monday");
            	}//if
        		
        		Sender = PendingIntent.getBroadcast(ctx, mondayRepId, AlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        		AlmMgr.setRepeating(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), 
            			AlarmManager.INTERVAL_DAY * 7, Sender);
        		Log.v("HCSetAlarmOnReboot", "alarm set for monday");
        	}//if
        }//if
	}//setMondayAlarmAfterReboot
	
	public void setTuesdayAlarmAfterReboot(String time, String repeatStatus, String tuesdayRepeatId) 
	{
		PendingIntent Sender = null;
		Intent AlarmIntent = new Intent(ctx, AlarmReceiver.class);
        AlarmManager AlmMgr = (AlarmManager)ctx.getSystemService(Context.ALARM_SERVICE);
        
        //Build Calendar objects for setting alarm
        Calendar curCalendar = Calendar.getInstance();
        Calendar alarmCalendar = Calendar.getInstance();
        curCalendar.set(Calendar.SECOND, 0);
        curCalendar.set(Calendar.MILLISECOND, 0);    		
        alarmCalendar.set(Calendar.SECOND, 0);
        alarmCalendar.set(Calendar.MILLISECOND, 0);
        
        RescheduleAlarmTime rescheduleTime = formatTime(time);
        
        if(repeatStatus != null && !repeatStatus.equals("") 
        		&& !repeatStatus.equals("null"))
        {
        	if(tuesdayRepeatId != null && !tuesdayRepeatId.equals("") 
        			&& !tuesdayRepeatId.equals("null"))
        	{
        		alarmCalendar.set(Calendar.DAY_OF_WEEK, 3);
            	alarmCalendar.set(Calendar.HOUR_OF_DAY, rescheduleTime.hour);
            	alarmCalendar.set(Calendar.MINUTE, rescheduleTime.min);
        		int tuesdayRepId = Integer.parseInt(tuesdayRepeatId);
        		
        		alarmEnabled = true;
        		AlarmIntent.putExtra("REQUEST CODE", tuesdayRepId);
        		AlarmIntent.putExtra("ALARM_ENABLED", alarmEnabled);
        		
        		if(alarmCalendar.before(curCalendar))
            	{
            		alarmCalendar.add(Calendar.WEEK_OF_YEAR, 1);
            		Log.v("HCSetAlarmOnReboot", "alarm set for next week tuesday");
            	}//if
        		
        		Sender = PendingIntent.getBroadcast(ctx, tuesdayRepId, AlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        		AlmMgr.setRepeating(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), 
            			AlarmManager.INTERVAL_DAY * 7, Sender);
        		Log.v("HCSetAlarmOnReboot", "alarm set for tuesday");
        	}//if
        }//if
	}//setTuesdayAlarmAfterReboot
	
	public void setWednesdayAlarmAfterReboot(String time, String repeatStatus, String wednesdayRepeatId) 
	{
		PendingIntent Sender = null;
		Intent AlarmIntent = new Intent(ctx, AlarmReceiver.class);
        AlarmManager AlmMgr = (AlarmManager)ctx.getSystemService(Context.ALARM_SERVICE);
        
        //Build Calendar objects for setting alarm
        Calendar curCalendar = Calendar.getInstance();
        Calendar alarmCalendar = Calendar.getInstance();
        curCalendar.set(Calendar.SECOND, 0);
        curCalendar.set(Calendar.MILLISECOND, 0);    		
        alarmCalendar.set(Calendar.SECOND, 0);
        alarmCalendar.set(Calendar.MILLISECOND, 0);
        
        RescheduleAlarmTime rescheduleTime = formatTime(time);
        
        if(repeatStatus != null && !repeatStatus.equals("") 
        		&& !repeatStatus.equals("null"))
        {
        	if(wednesdayRepeatId != null && !wednesdayRepeatId.equals("") 
        			&& !wednesdayRepeatId.equals("null"))
        	{
        		alarmCalendar.set(Calendar.DAY_OF_WEEK, 4);
            	alarmCalendar.set(Calendar.HOUR_OF_DAY, rescheduleTime.hour);
            	alarmCalendar.set(Calendar.MINUTE, rescheduleTime.min);
        		int wednesdayRepId = Integer.parseInt(wednesdayRepeatId);
        		
        		alarmEnabled = true;
        		AlarmIntent.putExtra("REQUEST CODE", wednesdayRepId);
        		AlarmIntent.putExtra("ALARM_ENABLED", alarmEnabled);
        		
        		if(alarmCalendar.before(curCalendar))
            	{
            		alarmCalendar.add(Calendar.WEEK_OF_YEAR, 1);
            		Log.v("HCSetAlarmOnReboot", "alarm set for next week wednesday");
            	}//if
        		
        		Sender = PendingIntent.getBroadcast(ctx, wednesdayRepId, AlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        		AlmMgr.setRepeating(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), 
            			AlarmManager.INTERVAL_DAY * 7, Sender);
        		Log.v("HCSetAlarmOnReboot", "alarm set for wednesday");
        	}//if
        }//if
	}//setWednesdayAlarmAfterReboot
	
	public void setThursdayAlarmAfterReboot(String time, String repeatStatus, String thursdayRepeatId) 
	{
		PendingIntent Sender = null;
		Intent AlarmIntent = new Intent(ctx, AlarmReceiver.class);
        AlarmManager AlmMgr = (AlarmManager)ctx.getSystemService(Context.ALARM_SERVICE);
        
        //Build Calendar objects for setting alarm
        Calendar curCalendar = Calendar.getInstance();
        Calendar alarmCalendar = Calendar.getInstance();
        curCalendar.set(Calendar.SECOND, 0);
        curCalendar.set(Calendar.MILLISECOND, 0);    		
        alarmCalendar.set(Calendar.SECOND, 0);
        alarmCalendar.set(Calendar.MILLISECOND, 0);
        
        RescheduleAlarmTime rescheduleTime = formatTime(time);
        
        if(repeatStatus != null && !repeatStatus.equals("") 
        		&& !repeatStatus.equals("null"))
        {
        	if(thursdayRepeatId != null && !thursdayRepeatId.equals("") 
        			&& !thursdayRepeatId.equals("null"))
        	{
        		alarmCalendar.set(Calendar.DAY_OF_WEEK, 5);
            	alarmCalendar.set(Calendar.HOUR_OF_DAY, rescheduleTime.hour);
            	alarmCalendar.set(Calendar.MINUTE, rescheduleTime.min);
        		int thursdayRepId = Integer.parseInt(thursdayRepeatId);
        		
        		alarmEnabled = true;
        		AlarmIntent.putExtra("REQUEST CODE", thursdayRepId);
        		AlarmIntent.putExtra("ALARM_ENABLED", alarmEnabled);
        		
        		if(alarmCalendar.before(curCalendar))
            	{
            		alarmCalendar.add(Calendar.WEEK_OF_YEAR, 1);
            		Log.v("HCSetAlarmOnReboot", "alarm set for next week thursday");
            	}//if
        		
        		Sender = PendingIntent.getBroadcast(ctx, thursdayRepId, AlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        		AlmMgr.setRepeating(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), 
            			AlarmManager.INTERVAL_DAY * 7, Sender);
        		Log.v("HCSetAlarmOnReboot", "alarm set for thursday");
        	}//if
        }//if
	}//setThursdayAlarmAfterReboot
	
	public void setFridayAlarmAfterReboot(String time, String repeatStatus, String fridayRepeatId) 
	{
		PendingIntent Sender = null;
		Intent AlarmIntent = new Intent(ctx, AlarmReceiver.class);
        AlarmManager AlmMgr = (AlarmManager)ctx.getSystemService(Context.ALARM_SERVICE);
        
        //Build Calendar objects for setting alarm
        Calendar curCalendar = Calendar.getInstance();
        Calendar alarmCalendar = Calendar.getInstance();
        curCalendar.set(Calendar.SECOND, 0);
        curCalendar.set(Calendar.MILLISECOND, 0);    		
        alarmCalendar.set(Calendar.SECOND, 0);
        alarmCalendar.set(Calendar.MILLISECOND, 0);
        
        RescheduleAlarmTime rescheduleTime = formatTime(time);
        
        if(repeatStatus != null && !repeatStatus.equals("") 
        		&& !repeatStatus.equals("null"))
        {
        	if(fridayRepeatId != null && !fridayRepeatId.equals("") 
        			&& !fridayRepeatId.equals("null"))
        	{
        		alarmCalendar.set(Calendar.DAY_OF_WEEK, 6);
            	alarmCalendar.set(Calendar.HOUR_OF_DAY, rescheduleTime.hour);
            	alarmCalendar.set(Calendar.MINUTE, rescheduleTime.min);
        		int fridayRepId = Integer.parseInt(fridayRepeatId);
        		
        		alarmEnabled = true;
        		AlarmIntent.putExtra("REQUEST CODE", fridayRepId);
        		AlarmIntent.putExtra("ALARM_ENABLED", alarmEnabled);
        		
        		if(alarmCalendar.before(curCalendar))
            	{
            		alarmCalendar.add(Calendar.WEEK_OF_YEAR, 1);
            		Log.v("HCSetAlarmOnReboot", "alarm set for next week friday");
            	}//if
        		
        		Sender = PendingIntent.getBroadcast(ctx, fridayRepId, AlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        		AlmMgr.setRepeating(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), 
            			AlarmManager.INTERVAL_DAY * 7, Sender);
        		Log.v("HCSetAlarmOnReboot", "alarm set for friday");
        	}//if
        }//if
	}//setFridayAlarmAfterReboot
	
	public void setSaturdayAlarmAfterReboot(String time, String repeatStatus, String saturdayRepeatId) 
	{
		PendingIntent Sender = null;
		Intent AlarmIntent = new Intent(ctx, AlarmReceiver.class);
        AlarmManager AlmMgr = (AlarmManager)ctx.getSystemService(Context.ALARM_SERVICE);
        
        //Build Calendar objects for setting alarm
        Calendar curCalendar = Calendar.getInstance();
        Calendar alarmCalendar = Calendar.getInstance();
        curCalendar.set(Calendar.SECOND, 0);
        curCalendar.set(Calendar.MILLISECOND, 0);    		
        alarmCalendar.set(Calendar.SECOND, 0);
        alarmCalendar.set(Calendar.MILLISECOND, 0);
        
        RescheduleAlarmTime rescheduleTime = formatTime(time);
        
        if(repeatStatus != null && !repeatStatus.equals("") 
        		&& !repeatStatus.equals("null"))
        {
        	if(saturdayRepeatId != null && !saturdayRepeatId.equals("") 
        			&& !saturdayRepeatId.equals("null"))
        	{
        		alarmCalendar.set(Calendar.DAY_OF_WEEK, 7);
            	alarmCalendar.set(Calendar.HOUR_OF_DAY, rescheduleTime.hour);
            	alarmCalendar.set(Calendar.MINUTE, rescheduleTime.min);
        		int saturdayRepId = Integer.parseInt(saturdayRepeatId);
        		
        		alarmEnabled = true;
        		AlarmIntent.putExtra("REQUEST CODE", saturdayRepId);
        		AlarmIntent.putExtra("ALARM_ENABLED", alarmEnabled);
        		
        		if(alarmCalendar.before(curCalendar))
            	{
            		alarmCalendar.add(Calendar.WEEK_OF_YEAR, 7);
            		Log.v("HCSetAlarmOnReboot", "alarm set for next week saturday");
            	}//if
        		
        		Sender = PendingIntent.getBroadcast(ctx, saturdayRepId, AlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        		AlmMgr.setRepeating(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), 
            			AlarmManager.INTERVAL_DAY * 7, Sender);
        		Log.v("HCSetAlarmOnReboot", "alarm set for saturday");
        	}//if
        }//if
	}//setSaturdayAlarmAfterReboot
	
	private RescheduleAlarmTime formatTime(String timeToFormat) 
	{
		RescheduleAlarmTime rescheduleTime = new RescheduleAlarmTime();
		String delimiter = ":";
		String[] timeArray = timeToFormat.split(delimiter);
		
		for(int i=0; i < timeArray.length; i++)
		{
			String timeData = timeArray[i];
			Log.v("HCRescheduleAlarmTime", timeData);
		}//for
	
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		Date date = null;
	
			try {
				date = sdf.parse(timeToFormat);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		
			}

		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
	
		if(timeToFormat.contains("AM"))
		{
			rescheduleTime.hour = calendar.get(Calendar.HOUR);
			rescheduleTime.min = calendar.get(Calendar.MINUTE);
		}//if
		else if(timeToFormat.contains("PM"))
		{
			rescheduleTime.hour = calendar.get(Calendar.HOUR) + 12;
			rescheduleTime.min = calendar.get(Calendar.MINUTE);
		}//else
		Log.v("HCSetAlarmOnReboot", ""+rescheduleTime.hour);
		Log.v("HCAlarmTimeDetail", ""+rescheduleTime.min);
		return rescheduleTime;
	}//formatTime

	class RescheduleAlarmTime
	{
		int	hour, min; 
	}//RescheduleAlarmTime
}//SetAlarmOnReboot
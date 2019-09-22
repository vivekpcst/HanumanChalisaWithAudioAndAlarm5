/**
 * Nitish Kumar 09 Dec, 2012
 * 
 * Broadcastreceiver will be called when the alarm 
 * fires
 * */

package com.dzo.HanumanChalisaWithAudioAndAlarm.receiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dzo.HanumanChalisaWithAudioAndAlarm.SnoozeActivity;
import com.dzo.HanumanChalisaWithAudioAndAlarm.dao.AlarmTimeDAO;
import com.dzo.HanumanChalisaWithAudioAndAlarm.database.AlarmDBHelper;
import com.dzo.HanumanChalisaWithAudioAndAlarm.util.SetAlarmOnReboot;

/** Class for receiving broadcast when the Alarm occurs */
public class AlarmReceiver extends BroadcastReceiver
{
    public static final String ALARM_ALERT_ACTION = "com.android.alarmclock.ALARM_ALERT";
    public static final String ALARM_INTENT_EXTRA = "intent.extra.alarm";
    AlarmDBHelper alarmHelper;
    List<AlarmTimeDAO> mAlarmList;
    int mAlarmHour = 0, mAlarmMin = 0;
    boolean alarmEnabled;
    int reqIdFromSnoozeActivity; 
    
    /**
     * Nitish Kumar 09 Dec, 2012
     * 
     * Fetch all results from database, check for alarm time and 
     * day on which alarm should be fired
     * */
    @Override
    public void onReceive(Context context, Intent intent) 
    { 
    	alarmHelper = new AlarmDBHelper(context);
    	mAlarmList = alarmHelper.getAllAlarmTimeResults();
    	alarmEnabled = intent.getBooleanExtra("ALARM_ENABLED", false);
    	
    	if((mAlarmList == null) || (mAlarmList.size() == 0))
    	{
    		Log.v("AlarmReceiver ", "no data found");
    	}//if
    	else
    	{
    		Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            String today = null;
            if (day == 2) 
            {
                today = "Monday";
            }//if
            else if (day == 3) 
            {
                today = "Tuesday";
            }//else if
            else if (day == 4) 
            {
                today = "Wednesday";
            }//else if
            else if (day == 5) 
            {
                today = "Thursday";
            }//else if
            else if (day == 6) 
            {
                today = "Friday";
            }//else if
            else if (day == 7) 
            {
                today = "Saturday";
            }//else if
            else if (day == 1) 
            {
                today = "Sunday";
            }//else if
            Log.v("Alarmreceiver", "Today is:- " + today);
            
			int system_hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int system_minute = Calendar.getInstance().get(Calendar.MINUTE);
           
            Log.v("Current Time: ", system_hour+":"+system_minute);
            
            for(int i = 0; i < mAlarmList.size(); i++)
            {
            	AlarmTimeDAO alarmDAO = mAlarmList.get(i);
            	
            	if(alarmDAO != null)
            	{
            		String alarmTime = alarmDAO.getAlarmTime();
            		String daysToRepeatAlarm = alarmDAO.getDaysToRepeatAlarm();
            		
            		if((alarmTime != null) && (!alarmTime.equals("null")) 
            				&& (!alarmTime.equals("")))
            		{
            			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            			Date date = null;
            			try 
            			{
            				date = sdf.parse(alarmTime);
            			}//try 
            			catch (ParseException e) 
            			{
            				e.printStackTrace();
            			}//catch
            			
            			final Calendar mCalendar = Calendar.getInstance();
            			mCalendar.setTime(date);
            		
            			if(alarmTime.contains("AM"))
            			{
            				mAlarmHour = mCalendar.get(Calendar.HOUR);
            				mAlarmMin = mCalendar.get(Calendar.MINUTE);
            				Log.v("AlarmReceiver if AM", "time contains AM");
            			}//if
            			else if(alarmTime.contains("PM"))
            			{
            				mAlarmHour = mCalendar.get(Calendar.HOUR)+12;
            				mAlarmMin = mCalendar.get(Calendar.MINUTE);
            				Log.v("AlarmReceiver else PM", "time contains PM");
            			}//else
            			
            			String daoAlarmTime = mAlarmHour+":"+mAlarmMin;
            			Log.v("AlarmReceiver",daoAlarmTime);
            			
            			if(daoAlarmTime.equals(system_hour+":"+system_minute) 
            					&& daysToRepeatAlarm.equals("Daily"))
            			{
            				Log.v("HCAlarmReceiver matched", "daily alarm");
            				Intent in = new Intent(Intent.ACTION_MAIN);
            				in.setClass(context, SnoozeActivity.class);
            				int reqCode = intent.getIntExtra("REQUEST CODE", 0);
            				Log.v("HCAlarmReceiver ", ""+reqCode);
            				in.putExtra("REQUEST CODE", intent.getIntExtra("REQUEST CODE", 0));
            				in.putExtra("ALARM_ENABLED", alarmEnabled);
            				in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            				context.startActivity(in);
            			}//if
            			else if(daoAlarmTime.equals(system_hour+":"+system_minute)
            					&& daysToRepeatAlarm.contains(today))
            			{
            				Log.v("HCAlarmReceiver matched", "weekday alarm");
            				Intent in = new Intent(Intent.ACTION_MAIN);
            				in.setClass(context, SnoozeActivity.class);
            				Log.v("HCAlarmReceiver ", ""+intent.getIntExtra("REQUEST CODE", 0));
            				in.putExtra("REQUEST CODE", intent.getIntExtra("REQUEST CODE", 0));
            				in.putExtra("ALARM_ENABLED", alarmEnabled);
            				in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            				context.startActivity(in);
            			}//else if
            			else
            			{
            				Log.v("AlarmReceiver ", "no alarm matched");
            			}//else
            		}//if
            	}
            }//for
    	}//else
    	if(SnoozeActivity.snoozed)
		{
			Log.v("HCAlarmReceiver matched", "snoozed alarm");
			Intent in = new Intent(Intent.ACTION_MAIN);
	    	in.setClass(context, SnoozeActivity.class);
	    	in.putExtra("ALARM_SNOOZED", SnoozeActivity.snoozed);
	    	in.putExtra("ALARM_ENABLED", alarmEnabled);
	    	reqIdFromSnoozeActivity = intent.getIntExtra("REQUEST CODE", 0);
	    	Log.v("HCAlarmReceiver ", ""+reqIdFromSnoozeActivity);
	    	in.putExtra("REQUEST CODE", reqIdFromSnoozeActivity);
	    	in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	context.startActivity(in);
	    	SetAlarmOnReboot setOnReboot = new SetAlarmOnReboot(context);
	    	for(int j = 0; j < mAlarmList.size(); j++)
    		{
	    		if(mAlarmList.get(j).getId() == reqIdFromSnoozeActivity)
	    		{
	    			if(mAlarmList.get(j).getDaysToRepeatAlarm() != null
	    					&& mAlarmList.get(j).getDaysToRepeatAlarm().contains("Daily"))
	    			{    				
	    				String alarmTimeFromDB = mAlarmList.get(j).getAlarmTime();
	    				
	    				if(alarmTimeFromDB != null && !alarmTimeFromDB.equals("null")
	    						&& !alarmTimeFromDB.equals(""))
	    				{
	    					setOnReboot.setDailyAlarmAfterReboot(reqIdFromSnoozeActivity, 
	    							alarmTimeFromDB, mAlarmList.get(j).getDaysToRepeatAlarm());
	    				}//if
	    				Log.v("HCAlarmReceiver", "Alarmreset after snooze for daily");
	    			}//if
	    		}//if
	    		else if(mAlarmList.get(j).getSundayRepeatId() != null 
	    				&& !mAlarmList.get(j).getSundayRepeatId().equals("")
	    				&& !mAlarmList.get(j).getSundayRepeatId().equals("null")
	    				&& reqIdFromSnoozeActivity 
	    				== Integer.parseInt(mAlarmList.get(j).getSundayRepeatId()))
	    		{
	    			String alarmTimeFromDB = mAlarmList.get(j).getAlarmTime();
	    			
	    			if(alarmTimeFromDB != null && !alarmTimeFromDB.equals("null")
    						&& !alarmTimeFromDB.equals(""))
	    			{
	    				setOnReboot.setSundayAlarmAfterReboot(alarmTimeFromDB, 
	    					mAlarmList.get(j).getDaysToRepeatAlarm(), mAlarmList.get(j).getSundayRepeatId());
	    				Log.v("HCAlarmReceiver", "Alarmreset after snooze for sunday");
	    			}
	    		}//else if
	    		else if(mAlarmList.get(j).getMondayRepeatId() != null 
	    				&& !mAlarmList.get(j).getMondayRepeatId().equals("")
	    				&& !mAlarmList.get(j).getMondayRepeatId().equals("null")
	    				&& reqIdFromSnoozeActivity 
	    				== Integer.parseInt(mAlarmList.get(j).getMondayRepeatId()))
	    		{
	    			String alarmTimeFromDB = mAlarmList.get(j).getAlarmTime();
	    			
	    			if(alarmTimeFromDB != null && !alarmTimeFromDB.equals("null")
    						&& !alarmTimeFromDB.equals(""))
	    			{
	    				setOnReboot.setMondayAlarmAfterReboot(alarmTimeFromDB, 
	    					mAlarmList.get(j).getDaysToRepeatAlarm(), mAlarmList.get(j).getMondayRepeatId());
	    				Log.v("HCAlarmReceiver", "Alarmreset after snooze for monday");
	    			}
	    		}//else if
	    		else if(mAlarmList.get(j).getTuesdayRepeatId() != null 
	    				&& !mAlarmList.get(j).getTuesdayRepeatId().equals("")
	    				&& !mAlarmList.get(j).getTuesdayRepeatId().equals("null")
	    				&& reqIdFromSnoozeActivity 
	    				== Integer.parseInt(mAlarmList.get(j).getTuesdayRepeatId()))
	    		{
	    			String alarmTimeFromDB = mAlarmList.get(j).getAlarmTime();
	    			
	    			if(alarmTimeFromDB != null && !alarmTimeFromDB.equals("null")
    						&& !alarmTimeFromDB.equals(""))
	    			{
	    				setOnReboot.setTuesdayAlarmAfterReboot(alarmTimeFromDB, 
	    					mAlarmList.get(j).getDaysToRepeatAlarm(), mAlarmList.get(j).getTuesdayRepeatId());
	    				Log.v("HCAlarmReceiver", "Alarmreset after snooze for tuesday");
	    			}
	    		}//else if
	    		else if(mAlarmList.get(j).getWednesdayRepeatId() != null 
	    				&& !mAlarmList.get(j).getWednesdayRepeatId().equals("")
	    				&& !mAlarmList.get(j).getWednesdayRepeatId().equals("null")
	    				&& reqIdFromSnoozeActivity 
	    				== Integer.parseInt(mAlarmList.get(j).getWednesdayRepeatId()))
	    		{
	    			String alarmTimeFromDB = mAlarmList.get(j).getAlarmTime();
	    			
	    			if(alarmTimeFromDB != null && !alarmTimeFromDB.equals("null")
    						&& !alarmTimeFromDB.equals(""))
	    			{
	    				setOnReboot.setWednesdayAlarmAfterReboot(alarmTimeFromDB, 
	    					mAlarmList.get(j).getDaysToRepeatAlarm(), mAlarmList.get(j).getWednesdayRepeatId());
	    				Log.v("HCAlarmReceiver", "Alarmreset after snooze for wednesday");
	    			}
	    		}//else if
	    		else if(mAlarmList.get(j).getThursdayRepeatId() != null
	    				&& !mAlarmList.get(j).getThursdayRepeatId().equals("")
	    				&& !mAlarmList.get(j).getThursdayRepeatId().equals("null")
	    				&& reqIdFromSnoozeActivity 
	    				== Integer.parseInt(mAlarmList.get(j).getThursdayRepeatId()))
	    		{
	    			String alarmTimeFromDB = mAlarmList.get(j).getAlarmTime();
	    			
	    			if(alarmTimeFromDB != null && !alarmTimeFromDB.equals("null")
    						&& !alarmTimeFromDB.equals(""))
	    			{
	    				setOnReboot.setThursdayAlarmAfterReboot(alarmTimeFromDB, 
	    					mAlarmList.get(j).getDaysToRepeatAlarm(), mAlarmList.get(j).getThursdayRepeatId());
	    				Log.v("HCAlarmReceiver", "Alarmreset after snooze for thursday");
	    			}//if
	    		}//else if
	    		else if(mAlarmList.get(j).getFridayRepeatId() != null
	    				&& !mAlarmList.get(j).getFridayRepeatId().equals("")
	    				&& !mAlarmList.get(j).getFridayRepeatId().equals("null")
	    				&& reqIdFromSnoozeActivity 
	    				== Integer.parseInt(mAlarmList.get(j).getFridayRepeatId()))
	    		{
	    			String alarmTimeFromDB = mAlarmList.get(j).getAlarmTime();
	    			
	    			if(alarmTimeFromDB != null && !alarmTimeFromDB.equals("null")
    						&& !alarmTimeFromDB.equals(""))
	    			{
	    				setOnReboot.setFridayAlarmAfterReboot(alarmTimeFromDB, 
	    					mAlarmList.get(j).getDaysToRepeatAlarm(), mAlarmList.get(j).getFridayRepeatId());
	    				Log.v("HCAlarmReceiver", "Alarmreset after snooze for friday");
	    			}
	    		}//else if
	    		else if(mAlarmList.get(j).getSaturdayRepeatId() != null
	    				&& !mAlarmList.get(j).getSaturdayRepeatId().equals("")
	    				&& !mAlarmList.get(j).getSaturdayRepeatId().equals("null")
	    				&& reqIdFromSnoozeActivity 
	    				== Integer.parseInt(mAlarmList.get(j).getSaturdayRepeatId()))
	    		{
	    			String alarmTimeFromDB = mAlarmList.get(j).getAlarmTime();
	    			
	    			if(alarmTimeFromDB != null && !alarmTimeFromDB.equals("null")
    						&& !alarmTimeFromDB.equals(""))
	    			{
	    				setOnReboot.setSaturdayAlarmAfterReboot(alarmTimeFromDB, 
	    					mAlarmList.get(j).getDaysToRepeatAlarm(), mAlarmList.get(j).getSaturdayRepeatId());
	    				Log.v("HCAlarmReceiver", "Alarmreset after snooze for saturday");
	    			}
	    		}//else if
    		}//for
		}//else
    }//onReceive
}//AlarmReceiver

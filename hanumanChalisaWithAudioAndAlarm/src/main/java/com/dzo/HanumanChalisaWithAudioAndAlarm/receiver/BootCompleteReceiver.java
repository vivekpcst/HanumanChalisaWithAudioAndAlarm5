/**
 * Nitish 10 Dec, 2012
 * 
 * This broadcastreceiver will be called after the device 
 * reboots for resetting alarm
 * */
package com.dzo.HanumanChalisaWithAudioAndAlarm.receiver;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.dzo.HanumanChalisaWithAudioAndAlarm.NotificatioAlarmReceiver;
import com.dzo.HanumanChalisaWithAudioAndAlarm.StartManualActivity;
import com.dzo.HanumanChalisaWithAudioAndAlarm.dao.AlarmTimeDAO;
import com.dzo.HanumanChalisaWithAudioAndAlarm.database.AlarmDBHelper;
import com.dzo.HanumanChalisaWithAudioAndAlarm.util.SetAlarmOnReboot;

public class BootCompleteReceiver extends BroadcastReceiver 
{
	AlarmDBHelper alarmHelper;
    List<AlarmTimeDAO> mAlarmList;
    int mAlarmHour = 0, mAlarmMin = 0;
    boolean alarmEnabled;
    Context context;
	public void onReceive(Context context, Intent intent)
	{
		this.context=context;
		if (intent.getAction() != null) {
			switch((intent.getAction())){
				case Intent.ACTION_BOOT_COMPLETED:
					setAlarm();
					setNotification();
//					setAlarmNotificationOnSaturday();
//					setAlarmNotificationOnTuesday();
					break;
			}
		}
		setAlarm();
	}//onReceive

	private void setNotification() {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent alarmIntent = new Intent(context, NotificatioAlarmReceiver.class);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);
		alarmIntent.setData((Uri.parse("custom://"+System.currentTimeMillis())));
		alarmManager.cancel(pendingIntent);

		Calendar alarmStartTime = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		alarmStartTime.set(Calendar.HOUR_OF_DAY, 8);
		alarmStartTime.set(Calendar.MINUTE, 00);
		alarmStartTime.set(Calendar.SECOND, 0);
		if (now.after(alarmStartTime)) {
			Log.d("Hey","Added a day");
			alarmStartTime.add(Calendar.DAY_OF_MONTH, 1);
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,alarmStartTime.getTimeInMillis(),pendingIntent);
		}else {
			alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
		}

//		alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

	}

	private void setAlarm() {
		alarmHelper = new AlarmDBHelper(context);
		mAlarmList = alarmHelper.getAllAlarmTimeResults();


		if((mAlarmList == null) || (mAlarmList.size() == 0))
		{
			Log.v("HCBootCompleteReceiver", "no data found");
		}//if
		else
		{
			for(int i = 0; i < mAlarmList.size(); i++)
			{
				AlarmTimeDAO alarmTimeDAO = mAlarmList.get(i);
				int alarmId = alarmTimeDAO.getId();
				String alarmTime = alarmTimeDAO.getAlarmTime();
				String daysToRepeatAlarm = alarmTimeDAO.getDaysToRepeatAlarm();
				String sundayRepeatId = alarmTimeDAO.getSundayRepeatId();
				String mondayRepeatId = alarmTimeDAO.getMondayRepeatId();
				String tuesdayRepeatId = alarmTimeDAO.getTuesdayRepeatId();
				String wednesdayRepeatId = alarmTimeDAO.getWednesdayRepeatId();
				String thursdayRepeatId = alarmTimeDAO.getThursdayRepeatId();
				String fridayRepeatId = alarmTimeDAO.getFridayRepeatId();
				String saturdayRepeatId = alarmTimeDAO.getSaturdayRepeatId();
				SetAlarmOnReboot setAlarmOnReboot = new SetAlarmOnReboot(context);

				if(alarmTimeDAO.getDaysToRepeatAlarm().contains("Daily"))
				{
					setAlarmOnReboot.setDailyAlarmAfterReboot(alarmId, alarmTime,
							daysToRepeatAlarm);
					Log.v("HCBootCompleteReceiver", "daily alarm set on reboot");
				}//if

				if(alarmTimeDAO.getDaysToRepeatAlarm().contains("Sunday"))
				{
					setAlarmOnReboot.setSundayAlarmAfterReboot(alarmTime,
							daysToRepeatAlarm, sundayRepeatId);
					Log.v("HCBootCompleteReceiver", "sunday alarm set on reboot");
				}//if

				if(alarmTimeDAO.getDaysToRepeatAlarm().contains("Monday"))
				{
					setAlarmOnReboot.setMondayAlarmAfterReboot(alarmTime,
							daysToRepeatAlarm, mondayRepeatId);
					Log.v("HCBootCompleteReceiver", "monday alarm set on reboot");
				}//if

				if(alarmTimeDAO.getDaysToRepeatAlarm().contains("Tuesday"))
				{
					setAlarmOnReboot.setTuesdayAlarmAfterReboot(alarmTime,
							daysToRepeatAlarm, tuesdayRepeatId);
					Log.v("HCBootCompleteReceiver", "tuesday alarm set on reboot");
				}//if

				if(alarmTimeDAO.getDaysToRepeatAlarm().contains("Wednesday"))
				{
					setAlarmOnReboot.setWednesdayAlarmAfterReboot(alarmTime,
							daysToRepeatAlarm, wednesdayRepeatId);
					Log.v("HCBootCompleteReceiver", "wednesday alarm set on reboot");
				}//if

				if(alarmTimeDAO.getDaysToRepeatAlarm().contains("Thursday"))
				{
					setAlarmOnReboot.setThursdayAlarmAfterReboot(alarmTime,
							daysToRepeatAlarm, thursdayRepeatId);
					Log.v("HCBootCompleteReceiver", "thursday alarm set on reboot");
				}//if

				if(alarmTimeDAO.getDaysToRepeatAlarm().contains("Friday"))
				{
					setAlarmOnReboot.setFridayAlarmAfterReboot(alarmTime,
							daysToRepeatAlarm, fridayRepeatId);
					Log.v("HCBootCompleteReceiver", "friday alarm set on reboot");
				}//if

				if(alarmTimeDAO.getDaysToRepeatAlarm().contains("Saturday"))
				{
					setAlarmOnReboot.setSaturdayAlarmAfterReboot(alarmTime,
							daysToRepeatAlarm, saturdayRepeatId);
					Log.v("HCBootCompleteReceiver", "saturday alarm set on reboot");
				}//if
				Log.v("HCBootCompleteReceiver", "mAlarmList not null in for loop");
			}//for
			Log.v("HCBootCompleteReceiver", "mAlarmList not null");
		}//else
		Log.v("HCBootCompleteReceiver", "onReceive called");
	}

	private void setAlarmNotificationOnSaturday() {
		Calendar   alarmStartTime = Calendar.getInstance();

		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent alarmIntent = new Intent(context, NotificatioAlarmReceiver.class);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);
		alarmIntent.setData((Uri.parse("custom://"+System.currentTimeMillis())));
		alarmManager.cancel(pendingIntent);

		Calendar now = Calendar.getInstance();
		alarmStartTime.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
		alarmStartTime.set(Calendar.HOUR_OF_DAY, 8);
		alarmStartTime.set(Calendar.MINUTE, 30);
		alarmStartTime.set(Calendar.SECOND, 0);
		if (now.after(alarmStartTime)) {
			alarmStartTime.add(Calendar.DAY_OF_MONTH, 7);
		}
		alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), 7*24*60*60*1000, pendingIntent);
	}


	private void setAlarmNotificationOnTuesday() {
		Calendar   alarmStartTime = Calendar.getInstance();

		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent alarmIntent = new Intent(context, NotificatioAlarmReceiver.class);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);
		alarmIntent.setData((Uri.parse("custom://"+System.currentTimeMillis())));
		alarmManager.cancel(pendingIntent);

		Calendar now = Calendar.getInstance();
		alarmStartTime.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY);
		alarmStartTime.set(Calendar.HOUR_OF_DAY, 8);
		alarmStartTime.set(Calendar.MINUTE, 00);
		alarmStartTime.set(Calendar.SECOND, 0);
		if (now.after(alarmStartTime)) {
			alarmStartTime.add(Calendar.DAY_OF_MONTH, 7);
		}
		alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), 7*24*60*60*1000, pendingIntent);

	}

}//BootCompleteReceiver

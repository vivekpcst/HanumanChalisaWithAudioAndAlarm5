package com.dzo.HanumanChalisaWithAudioAndAlarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dzo.HanumanChalisaWithAudioAndAlarm.dao.AlarmTimeDAO;
import com.dzo.HanumanChalisaWithAudioAndAlarm.database.AlarmDBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmTimeDetail extends BottomNav implements
OnClickListener
{
	TextView txtTimeSet;
	LinearLayout linAlarmTime;
	int reqCode, alarmDAOPosition;
	String strAmPm;
	boolean alarmEnabled;
	int mHour, mMinute;
	AlarmTimeDAO alarmTimeDaoToEdit;
	Button btnDone,  btnDelete;
	AlarmDBHelper alarmDBHelper;
	ImageView chkEveryday, chkSunday, chkMonday, chkTuesday, 
			chkWednesday, chkThursday, chkFriday, chkSaturday;

	@Override
	public int getContentViewId() {
		return R.layout.activity_alarm_detail;
	}

	@Override
	public int getNavigationMenuItemId() {
		return R.id.navigation_alarm;
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_detail);
		
		alarmDBHelper = new AlarmDBHelper(AlarmTimeDetail.this);
		
		alarmTimeDaoToEdit = (AlarmTimeDAO)getIntent().getSerializableExtra("AlarmDAOToEdit");
		
		alarmDAOPosition = getIntent().getIntExtra("AlarmDAOPosition", 0);
		reqCode = alarmTimeDaoToEdit.getId();
		
		Log.v("AlarmTimeDetail ", ""+reqCode);
		
		Log.v("AlarmDAO ID",""+alarmTimeDaoToEdit.getId());
		
		alarmEnabled = getIntent().getBooleanExtra("ALARM_ENABLED", false);
		Log.v("AlarmTimeDetail", ""+alarmEnabled);
		
		linAlarmTime = (LinearLayout)findViewById(R.id.linAlarmTime);
		
		txtTimeSet = (TextView)findViewById(R.id.txtTimeSet);
		txtTimeSet.setText(alarmTimeDaoToEdit.getAlarmTime());
		
		btnDone = (Button)findViewById(R.id.btnDone);
		btnDelete = (Button)findViewById(R.id.btnDelete);
		
		chkEveryday = (ImageView)findViewById(R.id.imgchkEveryday);
		chkSunday = (ImageView)findViewById(R.id.imgchkSunday);
		chkMonday = (ImageView)findViewById(R.id.imgchkMonday);
		chkTuesday = (ImageView)findViewById(R.id.imgchkTuesday);
		chkWednesday = (ImageView)findViewById(R.id.imgchkWednesday);
		chkThursday = (ImageView)findViewById(R.id.imgchkThursday);
		chkFriday = (ImageView)findViewById(R.id.imgchkFriday);
		chkSaturday = (ImageView)findViewById(R.id.imgchkSaturday);
		
		btnDone.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		txtTimeSet.setOnClickListener(this);
		chkEveryday.setOnClickListener(this);
		chkSunday.setOnClickListener(this);
		chkMonday.setOnClickListener(this);
		chkTuesday.setOnClickListener(this);
		chkWednesday.setOnClickListener(this);
		chkThursday.setOnClickListener(this);
		chkFriday.setOnClickListener(this);
		chkSaturday.setOnClickListener(this);
//		getSupportActionBar().setBackgroundDrawable(
//                getResources().getDrawable(R.drawable.header));
	}//onCreate

	public void onClick(View v) 
	{
		if(v.equals(txtTimeSet))
		{
			Intent editAlarmIntent = new Intent(AlarmTimeDetail.this, SetAlarmActivity.class);
			editAlarmIntent.putExtra("ALARM_ENABLED", alarmEnabled);
			editAlarmIntent.putExtra("AlarmDAOToEdit", alarmTimeDaoToEdit);
			editAlarmIntent.putExtra("FROM_CLASS", "AlarmTimeDetail");
			startActivity(editAlarmIntent);
			finish();
		}//else if
		else if (v.equals(btnDone)) 
		{
			Intent in = new Intent(AlarmTimeDetail.this, MultipleAlarmActivity.class);
			in.putExtra("ALARM_ENABLED", alarmEnabled);
			startActivity(in);
			finish();
		}//else if
		else if(v.equals(btnDelete))
		{
			alarmDBHelper.deleteAlarm(alarmTimeDaoToEdit);
			cancelAllAlarms();
			MultipleAlarmActivity.alarmDAOList.remove(alarmDAOPosition);
			finish();
			Intent UpdateDisplayIntent = new Intent(AlarmTimeDetail.this, MultipleAlarmActivity.class);
			startActivity(UpdateDisplayIntent);
		}//else if
		else if(v.equals(chkEveryday))
		{
			if(alarmTimeDaoToEdit.getDaysToRepeatAlarm() != null)
			{
				if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().equals("Daily"))
				{
					dismissDailyAlarm();
					chkEveryday.setImageResource(R.drawable.checkboxunchecked);
					Log.v("AlarmTimeDetail ", "dismiss daily alarm");
				}//if
				else if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().equals("null") 
						|| alarmTimeDaoToEdit.getDaysToRepeatAlarm().equals(""))
				{
					setDailyAlarm();
					chkEveryday.setImageResource(R.drawable.checkboxchecked);
					Log.v("HCAlarmTimeDetail ", "daysToRepeat was null or empty");
				}//else if
				else
				{
					dismissDayWiseAlarm(1);
					dismissDayWiseAlarm(2);
					dismissDayWiseAlarm(3);
					dismissDayWiseAlarm(4);
					dismissDayWiseAlarm(5);
					dismissDayWiseAlarm(6);
					dismissDayWiseAlarm(7);
					chkSunday.setImageResource(R.drawable.checkboxunchecked);
					chkMonday.setImageResource(R.drawable.checkboxunchecked);
					chkTuesday.setImageResource(R.drawable.checkboxunchecked);
					chkWednesday.setImageResource(R.drawable.checkboxunchecked);
					chkThursday.setImageResource(R.drawable.checkboxunchecked);
					chkFriday.setImageResource(R.drawable.checkboxunchecked);
					chkSaturday.setImageResource(R.drawable.checkboxunchecked);
					chkEveryday.setImageResource(R.drawable.checkboxchecked);
					setDailyAlarm();
					Log.v("HCAlarmTimeDetail", "daily was not present");
				}//else
				Log.v("AlarmTimeDetail ", "chkEveryday checked");
			}//if
			
			Log.v("AlarmTimeDetail ", "in dailyChecked");
			Log.v("AlarmTimeDetail ", ""+alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Daily"));
		}//else if
		
		else if(v.equals(chkSunday))
		{
			if(alarmTimeDaoToEdit.getDaysToRepeatAlarm() != null)
			{
				if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().equals("Daily"))
				{
					dismissDailyAlarm();
					chkEveryday.setImageResource(R.drawable.checkboxunchecked);
					setDayWiseAlarm(1);
					chkSunday.setImageResource(R.drawable.checkboxchecked);
					Log.v("AlarmTimeDetail ", "dismiss daily alarm");
				}//if
				else if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().equals("null") 
						|| alarmTimeDaoToEdit.getDaysToRepeatAlarm().equals(""))
				{
					setDayWiseAlarm(1);
					chkSunday.setImageResource(R.drawable.checkboxchecked);
					Log.v("HCAlarmTimeDetail ", "daysToRepeat was null or empty");
				}//else if
				else if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Sunday") == true)
				{
					dismissDayWiseAlarm(1);
					Log.v("AlarmTimeDetail ", "chkSunday unchecked");
					chkSunday.setImageResource(R.drawable.checkboxunchecked);
				}//else if
				else
				{
					setDayWiseAlarm(1);
					chkSunday.setImageResource(R.drawable.checkboxchecked);
					Log.v("HCAlarmTimeDetail ", "sunday was not present");
				}//else
				Log.v("AlarmTimeDetail ", "chkSunday checked");
			}//if
			
			Log.v("AlarmTimeDetail ", "in sundayChecked");
			Log.v("AlarmTimeDetail ", ""+alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Sunday"));
		}//else if

		else if(v.equals(chkMonday))
		{
			if(alarmTimeDaoToEdit.getDaysToRepeatAlarm() != null)
			{
				if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().equals("Daily"))
				{
					dismissDailyAlarm();
					chkEveryday.setImageResource(R.drawable.checkboxunchecked);
					setDayWiseAlarm(2);
					chkMonday.setImageResource(R.drawable.checkboxchecked);
					Log.v("AlarmTimeDetail ", "dismiss daily alarm");
				}//if
				else if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().equals("null") 
						|| alarmTimeDaoToEdit.getDaysToRepeatAlarm().equals(""))
				{
					setDayWiseAlarm(2);
					chkMonday.setImageResource(R.drawable.checkboxchecked);
					Log.v("HCAlarmTimeDetail ", "daysToRepeat was null or empty");
				}//else if
				else if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Monday") == true)
				{
					dismissDayWiseAlarm(2);
					Log.v("AlarmTimeDetail ", "chkMonday unchecked");
					chkMonday.setImageResource(R.drawable.checkboxunchecked);
				}//else if
				else
				{
					setDayWiseAlarm(2);
					chkMonday.setImageResource(R.drawable.checkboxchecked);
					Log.v("HCAlarmTimeDetail", "monday was not present");
				}//else
				Log.v("AlarmTimeDetail ", "chkMonday checked");
			}//if
			
			Log.v("AlarmTimeDetail", "in mondayChecked");
			Log.v("AlarmTimeDetail", ""+alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Monday"));
		}//else if
		
		else if(v.equals(chkTuesday))
		{
			if(alarmTimeDaoToEdit.getDaysToRepeatAlarm() != null)
			{
				if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().equals("Daily"))
				{
					dismissDailyAlarm();
					chkEveryday.setImageResource(R.drawable.checkboxunchecked);
					setDayWiseAlarm(3);
					chkTuesday.setImageResource(R.drawable.checkboxchecked);
					Log.v("AlarmTimeDetail", "dismiss daily alarm");
				}//if
				else if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().equals("null") 
						|| alarmTimeDaoToEdit.getDaysToRepeatAlarm().equals(""))
				{
					setDayWiseAlarm(3);
					chkTuesday.setImageResource(R.drawable.checkboxchecked);
					Log.v("HCAlarmTimeDetail", "daysToRepeat was null or empty");
				}//else if
				else if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Tuesday") == true)
				{
					dismissDayWiseAlarm(3);
					Log.v("AlarmTimeDetail ", "chkTuesday unchecked");
					chkTuesday.setImageResource(R.drawable.checkboxunchecked);
				}//else if
				else
				{
					setDayWiseAlarm(3);
					chkTuesday.setImageResource(R.drawable.checkboxchecked);
					Log.v("HCAlarmTimeDetail", "tuesday was not present");
				}//else
				Log.v("AlarmTimeDetail", "chkTuesday checked");
			}//if
			
			Log.v("AlarmTimeDetail", "in tuesdayChecked");
			Log.v("AlarmTimeDetail", ""+alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Tuesday"));
		}//else if
		
		else if(v.equals(chkWednesday))
		{
			if(alarmTimeDaoToEdit.getDaysToRepeatAlarm() != null)
			{
				if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().equals("Daily"))
				{
					dismissDailyAlarm();
					chkEveryday.setImageResource(R.drawable.checkboxunchecked);
					setDayWiseAlarm(4);
					chkWednesday.setImageResource(R.drawable.checkboxchecked);
					Log.v("AlarmTimeDetail", "dismiss daily alarm");
				}//if
				else if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().equals("null") 
						|| alarmTimeDaoToEdit.getDaysToRepeatAlarm().equals(""))
				{
					setDayWiseAlarm(4);
					chkWednesday.setImageResource(R.drawable.checkboxchecked);
					Log.v("HCAlarmTimeDetail", "daysToRepeat was null or empty");
				}//else if
				else if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Wednesday") == true)
				{
					dismissDayWiseAlarm(4);
					Log.v("AlarmTimeDetail", "chkWednesday unchecked");
					chkWednesday.setImageResource(R.drawable.checkboxunchecked);
				}//else if
				else
				{
					setDayWiseAlarm(4);
					chkWednesday.setImageResource(R.drawable.checkboxchecked);
					Log.v("HCAlarmTimeDetail", "chkWednesday was not present");
				}//else
				Log.v("AlarmTimeDetail", "chkWednesday checked");
			}//if
			
			Log.v("AlarmTimeDetail", "in chkWednesday");
			Log.v("AlarmTimeDetail ", ""+alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Wednesday"));
		}//else if
		
		else if(v.equals(chkThursday))
		{
			if(alarmTimeDaoToEdit.getDaysToRepeatAlarm() != null)
			{
				if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().equals("Daily"))
				{
					dismissDailyAlarm();
					chkEveryday.setImageResource(R.drawable.checkboxunchecked);
					setDayWiseAlarm(5);
					chkThursday.setImageResource(R.drawable.checkboxchecked);
					Log.v("AlarmTimeDetail", "dismiss daily alarm");
				}//if
				else if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().equals("null") 
						|| alarmTimeDaoToEdit.getDaysToRepeatAlarm().equals(""))
				{
					setDayWiseAlarm(5);
					chkThursday.setImageResource(R.drawable.checkboxchecked);
					Log.v("HCAlarmTimeDetail", "daysToRepeat was null or empty");
				}//else if
				else if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Thursday") == true)
				{
					dismissDayWiseAlarm(5);
					Log.v("AlarmTimeDetail", "chkThursday unchecked");
					chkThursday.setImageResource(R.drawable.checkboxunchecked);
				}//else if
				else
				{
					setDayWiseAlarm(5);
					chkThursday.setImageResource(R.drawable.checkboxchecked);
					Log.v("HCAlarmTimeDetail", "chkThursday was not present");
				}//else
				Log.v("AlarmTimeDetail", "chkThursday checked");
			}//if
			
			Log.v("AlarmTimeDetail", "in chkThursday");
			Log.v("AlarmTimeDetail ", ""+alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Thursday"));
		}//else if
		
		else if(v.equals(chkFriday))
		{
			if(alarmTimeDaoToEdit.getDaysToRepeatAlarm() != null)
			{
				if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().equals("Daily"))
				{
					dismissDailyAlarm();
					chkEveryday.setImageResource(R.drawable.checkboxunchecked);
					setDayWiseAlarm(6);
					chkFriday.setImageResource(R.drawable.checkboxchecked);
					Log.v("AlarmTimeDetail", "dismiss daily alarm");
				}//if
				else if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().equals("null") 
						|| alarmTimeDaoToEdit.getDaysToRepeatAlarm().equals(""))
				{
					setDayWiseAlarm(6);
					chkFriday.setImageResource(R.drawable.checkboxchecked);
					Log.v("HCAlarmTimeDetail", "daysToRepeat was null or empty");
				}//else if
				else if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Friday") == true)
				{
					dismissDayWiseAlarm(6);
					Log.v("AlarmTimeDetail ", "chkFriday unchecked");
					chkFriday.setImageResource(R.drawable.checkboxunchecked);
				}//else if
				else
				{
					setDayWiseAlarm(6);
					chkFriday.setImageResource(R.drawable.checkboxchecked);
					Log.v("HCAlarmTimeDetail", "chkFriday was not present");
				}//else
				Log.v("AlarmTimeDetail", "chkFriday checked");
			}//if
			
			Log.v("AlarmTimeDetail", "in chkFriday");
			Log.v("AlarmTimeDetail ", ""+alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Friday"));
		}//else if
	
		else if(v.equals(chkSaturday))
		{
			if(alarmTimeDaoToEdit.getDaysToRepeatAlarm() != null)
			{
				if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().equals("Daily"))
				{
					dismissDailyAlarm();
					chkEveryday.setImageResource(R.drawable.checkboxunchecked);
					setDayWiseAlarm(7);
					chkWednesday.setImageResource(R.drawable.checkboxchecked);
					Log.v("AlarmTimeDetail", "dismiss daily alarm");
				}//if
				else if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().equals("null") 
						|| alarmTimeDaoToEdit.getDaysToRepeatAlarm().equals(""))
				{
					setDayWiseAlarm(7);
					chkSaturday.setImageResource(R.drawable.checkboxchecked);
					Log.v("HCAlarmTimeDetail", "daysToRepeat was null or empty");
				}//else if
				else if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Saturday") == true)
				{
					dismissDayWiseAlarm(7);
					Log.v("AlarmTimeDetail ", "chkSaturday unchecked");
					chkSaturday.setImageResource(R.drawable.checkboxunchecked);
				}//else if
				else
				{
					setDayWiseAlarm(7);
					chkSaturday.setImageResource(R.drawable.checkboxchecked);
					Log.v("HCAlarmTimeDetail ", "chkSaturday was not present");
				}//else
				Log.v("AlarmTimeDetail ", "chkSaturday checked");
			}//if
			
			Log.v("AlarmTimeDetail ", "in chkSaturday");
			Log.v("AlarmTimeDetail ", ""+alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Saturday"));
		}//else if
	}//onClick
	/**
	 * Nitish 13 Dec 2012
	 * 
	 * Method to set day wise alarm
	 * */
	private void setDayWiseAlarm(int checkNo)
	{
		Intent AlarmIntent = new Intent("com.dzo.HanumanChalisaWithAudioAndAlarm.RECEIVEALARM");
		int repeatId = checkNo + reqCode;
		
		Log.v("AlarmTimeDetail", reqCode+" "+checkNo+" "+repeatId);
		AlarmManager AlmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		
		Log.v("AlarmTimeDetail", ""+checkNo);
		Calendar curCalendar = Calendar.getInstance();
		Calendar alarmCalendar = Calendar.getInstance();
		
		curCalendar.set(Calendar.SECOND, 0);
    	curCalendar.set(Calendar.MILLISECOND, 0);    		
    	alarmCalendar.set(Calendar.SECOND, 0);
    	alarmCalendar.set(Calendar.MILLISECOND, 0);
    	
    	RepeatAlarmTime repTime = formatTime();
    	alarmCalendar.set(Calendar.DAY_OF_WEEK, checkNo);
    	alarmCalendar.set(Calendar.HOUR_OF_DAY, repTime.hour);
    	alarmCalendar.set(Calendar.MINUTE, repTime.min);
    	
    	if(alarmCalendar.before(curCalendar))
    	{
    		alarmCalendar.add(Calendar.WEEK_OF_YEAR, 1);
    		Log.v("AlarmTimeDetail ", "alarm set for next week");
    	}//if
    	
    	if(checkNo == 1)
    	{
    		String daysToRepeat = alarmTimeDaoToEdit.getDaysToRepeatAlarm();
    		Log.v("HCAlarmTimeDetail", "1 daysToRepeat before edit "+daysToRepeat);
    		
    		if(!daysToRepeat.equals(null))
    		{
    			if(daysToRepeat.equals(""))
    			{
    				daysToRepeat = "Sunday";
    				Log.v("HCAlarmTimeDetail ", daysToRepeat);
    			}//if
    			else if(daysToRepeat.equals("Daily") || daysToRepeat.equals("null"))
    			{
    				daysToRepeat = "Sunday";
    				Log.v("HCAlarmTimeDetail ", daysToRepeat);
    			}//if
    			else
    			{
    				daysToRepeat = daysToRepeat + " Sunday";
    				Log.v("HCAlarmTimeDetail", daysToRepeat);
    			}//else
    			Log.v("AlarmTimeDetail ", "daysToRepeatNotNull");
    		}//if
    		alarmTimeDaoToEdit.setSundayRepeatId(Integer.toString(repeatId));
    		alarmTimeDaoToEdit.setDaysToRepeatAlarm(daysToRepeat);
    		Log.v("HCAlarmTimeDetail", "Alarm set for sunday");
    		Log.v("HCAlarmTimeDetail", "1 daysToRepeat after edit "+daysToRepeat);
    	}//if
    	
    	if(checkNo == 2)
    	{
    		String daysToRepeat = alarmTimeDaoToEdit.getDaysToRepeatAlarm();
    		Log.v("HCAlarmTimeDetail", "2 daysToRepeat before edit "+daysToRepeat);
    		
    		if(!daysToRepeat.equals(null))
    		{
    			if(daysToRepeat.equals(""))
    			{
    				daysToRepeat = "Monday";
    				Log.v("HCAlarmTimeDetail ", daysToRepeat);
    			}//if
    			else if(daysToRepeat.equals("Daily") || daysToRepeat.equals("null"))
    			{
    				daysToRepeat = "Monday";
    				Log.v("HCAlarmTimeDetail ", daysToRepeat);
    			}//if
    			else
    			{
    				daysToRepeat = daysToRepeat + " Monday";
    				Log.v("HCAlarmTimeDetail ", daysToRepeat);
    			}//else
    			Log.v("AlarmTimeDetail ", "daysToRepeatNotNull");
    		}//if
    		alarmTimeDaoToEdit.setMondayRepeatId(Integer.toString(repeatId));
    		alarmTimeDaoToEdit.setDaysToRepeatAlarm(daysToRepeat);
    		Log.v("HCAlarmTimeDetail", "Alarm set for monday");
    		Log.v("HCAlarmTimeDetail", "2 daysToRepeat after edit "+daysToRepeat);
    	}//if
    	
    	if(checkNo == 3)
    	{
    		String daysToRepeat = alarmTimeDaoToEdit.getDaysToRepeatAlarm();
    		Log.v("HCAlarmTimeDetail", "3 daysToRepeat before edit "+daysToRepeat);
    		
    		if(!daysToRepeat.equals(null))
    		{
    			if(daysToRepeat.equals(""))
    			{
    				daysToRepeat = "Tuesday";
    				Log.v("HCAlarmTimeDetail ", daysToRepeat);
    			}//if
    			else if(daysToRepeat.equals("Daily") || daysToRepeat.equals("null"))
    			{
    				daysToRepeat = "Tuesday";
    				Log.v("HCAlarmTimeDetail ", daysToRepeat);
    			}//if
    			else
    			{
    				daysToRepeat = daysToRepeat + " Tuesday";
    				Log.v("HCAlarmTimeDetail ", daysToRepeat);
    			}//else
    			Log.v("AlarmTimeDetail ", "daysToRepeatNotNull");
    		}//if
    		alarmTimeDaoToEdit.setTuesdayRepeatId(Integer.toString(repeatId));
    		alarmTimeDaoToEdit.setDaysToRepeatAlarm(daysToRepeat);
    		Log.v("HCAlarmTimeDetail", "Alarm set for tuesday");
    		Log.v("HCAlarmTimeDetail", "2 daysToRepeat after edit "+daysToRepeat);
    	}//if
    	
    	if(checkNo == 4)
    	{
    		String daysToRepeat = alarmTimeDaoToEdit.getDaysToRepeatAlarm();
    		Log.v("HCAlarmTimeDetail", "4 daysToRepeat before edit "+daysToRepeat);
    		
    		if(!daysToRepeat.equals(null))
    		{
    			if(daysToRepeat.equals(""))
    			{
    				daysToRepeat = "Wednesday";
    				Log.v("HCAlarmTimeDetail", daysToRepeat);
    			}//if
    			else if(daysToRepeat.equals("Daily") || daysToRepeat.equals("null"))
    			{
    				daysToRepeat = "Wednesday";
    				Log.v("HCAlarmTimeDetail ", daysToRepeat);
    			}//if
    			else
    			{
    				daysToRepeat = daysToRepeat + " Wednesday";
    				Log.v("HCAlarmTimeDetail ", daysToRepeat);
    			}//else
    			Log.v("AlarmTimeDetail ", "daysToRepeatNotNull");
    		}//if
    		alarmTimeDaoToEdit.setWednesdayRepeatId(Integer.toString(repeatId));
    		alarmTimeDaoToEdit.setDaysToRepeatAlarm(daysToRepeat);
    		Log.v("HCAlarmTimeDetail", "Alarm set for wednesday");
    		Log.v("HCAlarmTimeDetail", "4 daysToRepeat after edit "+daysToRepeat);
    	}//if
    	
    	if(checkNo == 5)
    	{
    		String daysToRepeat = alarmTimeDaoToEdit.getDaysToRepeatAlarm();
    		Log.v("HCAlarmTimeDetail", "5 daysToRepeat before edit "+daysToRepeat);
    		
    		if(!daysToRepeat.equals(null))
    		{
    			if(daysToRepeat.equals(""))
    			{
    				daysToRepeat = "Thursday";
    				Log.v("HCAlarmTimeDetail ", daysToRepeat);
    			}//if
    			else if(daysToRepeat.equals("Daily") || daysToRepeat.equals("null"))
    			{
    				daysToRepeat = "Thursday";
    				Log.v("HCAlarmTimeDetail ", daysToRepeat);
    			}//if
    			else
    			{
    				daysToRepeat = daysToRepeat + " Thursday";
    				Log.v("HCAlarmTimeDetail ", daysToRepeat);
    			}//else
    			Log.v("AlarmTimeDetail ", "daysToRepeatNotNull");
    		}//if
    		alarmTimeDaoToEdit.setThursdayRepeatId(Integer.toString(repeatId));
    		alarmTimeDaoToEdit.setDaysToRepeatAlarm(daysToRepeat);
    		Log.v("HCAlarmTimeDetail", "Alarm set for thursday");
    		Log.v("HCAlarmTimeDetail", "5 daysToRepeat after edit "+daysToRepeat);
    	}//if
    	
    	if(checkNo == 6)
    	{
    		String daysToRepeat = alarmTimeDaoToEdit.getDaysToRepeatAlarm();
    		Log.v("HCAlarmTimeDetail", "6 daysToRepeat before edit "+daysToRepeat);
    		
    		if(!daysToRepeat.equals(null))
    		{
    			if(daysToRepeat.equals(""))
    			{
    				daysToRepeat = "Friday";
    				Log.v("HCAlarmTimeDetail ", daysToRepeat);
    			}//if
    			else if(daysToRepeat.equals("Daily") || daysToRepeat.equals("null"))
    			{
    				daysToRepeat = "Friday";
    				Log.v("HCAlarmTimeDetail ", daysToRepeat);
    			}//if
    			else
    			{
    				daysToRepeat = daysToRepeat + " Friday";
    				Log.v("HCAlarmTimeDetail ", daysToRepeat);
    			}//else
    			Log.v("AlarmTimeDetail ", "daysToRepeatNotNull");
    		}//if
    		alarmTimeDaoToEdit.setFridayRepeatId(Integer.toString(repeatId));
    		alarmTimeDaoToEdit.setDaysToRepeatAlarm(daysToRepeat);
    		Log.v("HCAlarmTimeDetail", "Alarm set for friday");
    		Log.v("HCAlarmTimeDetail", "6 daysToRepeat after edit "+daysToRepeat);
    	}//if
    	
    	if(checkNo == 7)
    	{
    		String daysToRepeat = alarmTimeDaoToEdit.getDaysToRepeatAlarm();
    		Log.v("HCAlarmTimeDetail", "7 daysToRepeat before edit "+daysToRepeat);
    		
    		if(!daysToRepeat.equals(null))
    		{
    			if(daysToRepeat.equals(""))
    			{
    				daysToRepeat = "Saturday";
    				Log.v("HCAlarmTimeDetail ", daysToRepeat);
    			}//if
    			else if(daysToRepeat.equals("Daily") || daysToRepeat.equals("null"))
    			{
    				daysToRepeat = "Saturday";
    				Log.v("HCAlarmTimeDetail", daysToRepeat);
    			}//if
    			else
    			{
    				daysToRepeat = daysToRepeat + " Saturday";
    				Log.v("HCAlarmTimeDetail ", daysToRepeat);
    			}//else
    			Log.v("AlarmTimeDetail ", "daysToRepeatNotNull");
    		}//if
    		alarmTimeDaoToEdit.setSaturdayRepeatId(Integer.toString(repeatId));
    		alarmTimeDaoToEdit.setDaysToRepeatAlarm(daysToRepeat);
    		Log.v("HCAlarmTimeDetail", "Alarm set for Saturday");
    		Log.v("HCAlarmTimeDetail", "7 daysToRepeat after edit "+daysToRepeat);
    	}//if
    	
    	if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().equals("Sunday Monday Tuesday Wednesday " +
    			"Thursday Friday Saturday"))
    	{
    		alarmEnabled = true;
            alarmTimeDaoToEdit.setDaysToRepeatAlarm("Daily");
    		Log.v("AlarmTimeDetail", "Alarm set for everyday");
    		dismissDayWiseAlarm(1);
    		dismissDayWiseAlarm(2);
    		dismissDayWiseAlarm(3);
    		dismissDayWiseAlarm(4);
    		dismissDayWiseAlarm(5);
    		dismissDayWiseAlarm(6);
    		dismissDayWiseAlarm(7);
    		setDailyAlarm();
    	}//if
    	else
    	{
    		alarmEnabled = true;
    		AlarmIntent.putExtra("REQUEST CODE", repeatId);
    		AlarmIntent.putExtra("ALARM_ENABLED", alarmEnabled);
    		alarmDBHelper.updateAlarmTime(alarmTimeDaoToEdit);
    		PendingIntent Sender = PendingIntent.getBroadcast(AlarmTimeDetail.this, repeatId
        		, AlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    	
    		AlmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(),
    			AlarmManager.INTERVAL_DAY * 7, Sender);
    	}//else
    }//setDayAlarm
	
	/**
	 * Nitish 13 Dec 2012
	 * 
	 * Method to format alarm time
	 * */
	private RepeatAlarmTime formatTime() 
	{
		RepeatAlarmTime repAlmTime = new RepeatAlarmTime();
		String setTime = txtTimeSet.getText().toString();
		
		if((setTime != null) && (!setTime.equals("null")) && (!setTime.equals("")))
		{
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
			Date date = null;
			
			try 
			{
				date = sdf.parse(setTime);
			}//try 
			catch (ParseException e) 
			{
				e.printStackTrace();
			}//catch
	
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			
			if(setTime.contains("AM"))
			{
				repAlmTime.hour = calendar.get(Calendar.HOUR);
				repAlmTime.min = calendar.get(Calendar.MINUTE);
			}//if
			else if(setTime.contains("PM"))
			{
				repAlmTime.hour = calendar.get(Calendar.HOUR) + 12;
				repAlmTime.min = calendar.get(Calendar.MINUTE);
			}//else
			Log.v("AlarmTimeDetail ", ""+repAlmTime.hour);
			Log.v("AlarmTimeDetail ", ""+repAlmTime.min);
		}
		return repAlmTime;
	}//formatTime
	
	/**
	 * Nitish 13 Dec 2012
	 * 
	 * Method to set daily alarm
	 * */
	public void setDailyAlarm() 
    {
		Intent AlarmIntent = new Intent("com.dzo.HanumanChalisaWithAudioAndAlarm.RECEIVEALARM");
		
		Log.v("HCAlarmTimeDetail", ""+reqCode);
		AlarmManager AlmMgr = (AlarmManager)getSystemService(ALARM_SERVICE);
		
		Calendar curCalendar = Calendar.getInstance();
		Calendar alarmCalendar = Calendar.getInstance();
		
		curCalendar.set(Calendar.SECOND, 0);
    	curCalendar.set(Calendar.MILLISECOND, 0);    		
    	alarmCalendar.set(Calendar.SECOND, 0);
    	alarmCalendar.set(Calendar.MILLISECOND, 0);
    	
    	RepeatAlarmTime repTime = formatTime();
    	alarmCalendar.set(Calendar.HOUR_OF_DAY, repTime.hour);
    	alarmCalendar.set(Calendar.MINUTE, repTime.min);
    	
    	if (alarmCalendar.getTimeInMillis() <= curCalendar.getTimeInMillis()) 
    	{
    		alarmCalendar.add(Calendar.HOUR, 24);
    	}//if
    	
    	alarmEnabled = true;
    	AlarmIntent.putExtra("REQUEST CODE", reqCode);
    	AlarmIntent.putExtra("ALARM_ENABLED", alarmEnabled);
    	Log.v("AlarmTimeDetail ", " days to repeat before edit "+alarmTimeDaoToEdit.getDaysToRepeatAlarm());
    	alarmTimeDaoToEdit.setDaysToRepeatAlarm("Daily");
    	alarmDBHelper.updateAlarmTime(alarmTimeDaoToEdit);
    	Log.v("AlarmTimeDetail ", " days to repeat after edit "+alarmTimeDaoToEdit.getDaysToRepeatAlarm());
    	PendingIntent Sender = PendingIntent.getBroadcast(AlarmTimeDetail.this, reqCode
        		, AlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    	AlmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(),
    			24 * 60 * 60 * 1000, Sender);
    }//setDailyAlarm
	
	/**
	 * Nitish 13 Dec 2012
	 * 
	 * Method to dismiss day wise alarm
	 * */
	public void dismissDayWiseAlarm(int checkNo)
    {
		alarmEnabled = false;
		int repeatId = checkNo + reqCode;
		Log.v("AlarmTimeDetail : ", ""+checkNo);
    	Intent AlarmIntent = new Intent("com.dzo.HanumanChalisaWithAudioAndAlarm.RECEIVEALARM");
		AlarmManager AlmMgr = (AlarmManager)getSystemService(ALARM_SERVICE);
		String daysToRepeat = alarmTimeDaoToEdit.getDaysToRepeatAlarm();
		
		if(checkNo == 1)
		{
			if(daysToRepeat.contains("Sunday"))
			{
				daysToRepeat = daysToRepeat.replace("Sunday", "");
				alarmTimeDaoToEdit.setSundayRepeatId("");
			}//if
		}//if
		
		if(checkNo == 2)
		{
			if(daysToRepeat.contains("Monday"))
			{
				daysToRepeat = daysToRepeat.replace("Monday", "");
				alarmTimeDaoToEdit.setMondayRepeatId("");
			}//if
		}//if
		
		if(checkNo == 3)
		{
			if(daysToRepeat.contains("Tuesday"))
			{
				daysToRepeat = daysToRepeat.replace("Tuesday", "");
				alarmTimeDaoToEdit.setTuesdayRepeatId("");
			}//if
		}//if
		
		if(checkNo == 4)
		{
			if(daysToRepeat.contains("Wednesday"))
			{
				daysToRepeat = daysToRepeat.replace("Wednesday", "");
				alarmTimeDaoToEdit.setWednesdayRepeatId("");
			}//if
		}//if
		
		if(checkNo == 5)
		{
			if(daysToRepeat.contains("Thursday"))
			{
				daysToRepeat = daysToRepeat.replace("Thursday", "");
				alarmTimeDaoToEdit.setThursdayRepeatId("");
			}//if
		}//if
		
		if(checkNo == 6)
		{
			if(daysToRepeat.contains("Friday"))
			{
				daysToRepeat = daysToRepeat.replace("Friday", "");
				alarmTimeDaoToEdit.setFridayRepeatId("");
			}//if
		}//if
		
		if(checkNo == 7)
		{
			if(daysToRepeat.contains("Saturday"))
			{
				daysToRepeat = daysToRepeat.replace("Saturday", "");
				alarmTimeDaoToEdit.setSaturdayRepeatId("");
			}//if
		}//if
		
		alarmTimeDaoToEdit.setDaysToRepeatAlarm(daysToRepeat);
		alarmDBHelper.updateAlarmTime(alarmTimeDaoToEdit);
		AlarmIntent.putExtra("REQUEST CODE", repeatId);
		AlarmIntent.putExtra("ALARM_ENABLED", alarmEnabled);
		PendingIntent Sender = PendingIntent.getBroadcast(AlarmTimeDetail.this, repeatId, 
				AlarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);  
    	AlmMgr.cancel(Sender);
    	Log.v("Alarm disabled for", "day "+checkNo+" REQ_CODE "+reqCode);
    }//dismissAlarm
	
	/**
	 * Nitish 13 Dec 2012
	 * 
	 * Cancels all alarms on deleting alarm time
	 * */
	public void cancelAllAlarms()
    {
    	if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Daily"))
    	{
    		dismissDailyAlarm();
    	}//if

    	else if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Sunday"))
    	{
    		dismissDayWiseAlarm(1);
    	}//else if
    	
    	else if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Monday"))
    	{
    		dismissDayWiseAlarm(2);
    	}//else if
    	
    	else if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Tuesday"))
    	{
    		dismissDayWiseAlarm(3);
    	}//else if
    	
    	else if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Wednesday"))
    	{
    		dismissDayWiseAlarm(4);
    	}//else if
    	
    	else if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Thursday"))
    	{
    		dismissDayWiseAlarm(5);
    	}//else if
    	
    	else if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Friday"))
    	{
    		dismissDayWiseAlarm(6);
    	}//else if
    	
    	else if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Saturday"))
    	{
    		dismissDayWiseAlarm(7);
    	}//else if
    }//cancelAlarm
	
	/**
	 * Nitish 13 Dec 2012
	 * 
	 * Method to dismiss daily alarm
	 * */
	public void dismissDailyAlarm()
    {
		alarmEnabled = false;
		Log.v("HCAlarmTimeDetail : ", ""+reqCode);
		Intent AlarmIntent = new Intent("com.dzo.HanumanChalisaWithAudioAndAlarm.RECEIVEALARM");
		AlarmManager AlmMgr = (AlarmManager)getSystemService(ALARM_SERVICE);
		alarmTimeDaoToEdit.setDaysToRepeatAlarm("null");
		alarmDBHelper.updateAlarmTime(alarmTimeDaoToEdit);
		AlarmIntent.putExtra("REQUEST CODE", reqCode);
		AlarmIntent.putExtra("ALARM_ENABLED", alarmEnabled);
		PendingIntent Sender = PendingIntent.getBroadcast(AlarmTimeDetail.this, reqCode, 
				AlarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);  
    	AlmMgr.cancel(Sender);
    	Log.v("HCAlarm disabled", "with request code "+reqCode);
    }//dismissDailyAlarm
	
	class RepeatAlarmTime
	{
		int hour, min;
	}//RepeatAlarmTime
	
	/**
	 * Nitish 13 Dec 2012
	 * 
	 * Show selected days on which alarm will fire
	 * */
	@Override
	protected void onResume() 
	{
		if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().equals("Daily"))
		{
			chkEveryday.setImageResource(R.drawable.checkboxchecked);
		}//if
		
		if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Sunday"))
		{
			chkSunday.setImageResource(R.drawable.checkboxchecked);
		}//if
		
		if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Monday"))
		{
			chkMonday.setImageResource(R.drawable.checkboxchecked);
		}//if
		
		if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Tuesday"))
		{
			chkTuesday.setImageResource(R.drawable.checkboxchecked);
		}//if
		
		if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Wednesday"))
		{
			chkWednesday.setImageResource(R.drawable.checkboxchecked);
		}//if
		
		if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Thursday"))
		{
			chkThursday.setImageResource(R.drawable.checkboxchecked);
		}//if
		
		if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Friday"))
		{
			chkFriday.setImageResource(R.drawable.checkboxchecked);
		}//if
		
		if(alarmTimeDaoToEdit.getDaysToRepeatAlarm().contains("Saturday"))
		{
			chkSaturday.setImageResource(R.drawable.checkboxchecked);
		}//if
		super.onResume();
		Log.v("AlarmTimeDetail", "onResume called");
	}//onResume
	
	@Override
	public void onBackPressed() 
	{
		super.onBackPressed();
		finish();
		
	}//onBackPressed
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		if(alarmDBHelper != null)
		{
			Intent AlarmIntent = new Intent("com.dzo.HanumanChalisaWithAudioAndAlarm.RECEIVEALARM");
			Log.v("HCAlarmTimeDetail", ""+reqCode);
			AlarmManager AlmMgr = (AlarmManager)getSystemService(ALARM_SERVICE);

			Calendar curCalendar = Calendar.getInstance();
			Calendar alarmCalendar = Calendar.getInstance();

			curCalendar.set(Calendar.SECOND, 0);
			curCalendar.set(Calendar.MILLISECOND, 0);
			alarmCalendar.set(Calendar.SECOND, 0);
			alarmCalendar.set(Calendar.MILLISECOND, 0);

			RepeatAlarmTime repTime = formatTime();
			alarmCalendar.set(Calendar.HOUR_OF_DAY, repTime.hour);
			alarmCalendar.set(Calendar.MINUTE, repTime.min);

			if (alarmCalendar.getTimeInMillis() <= curCalendar.getTimeInMillis())
			{
				alarmCalendar.add(Calendar.HOUR, 24);
			}//if

			alarmEnabled = true;
			AlarmIntent.putExtra("REQUEST CODE", reqCode);
			AlarmIntent.putExtra("ALARM_ENABLED", alarmEnabled);
			Log.v("AlarmTimeDetail ", " days to repeat before edit "+alarmTimeDaoToEdit.getDaysToRepeatAlarm());
			alarmTimeDaoToEdit.setDaysToRepeatAlarm("Daily");
			alarmDBHelper.updateAlarmTime(alarmTimeDaoToEdit);
			Log.v("AlarmTimeDetail ", " days to repeat after edit "+alarmTimeDaoToEdit.getDaysToRepeatAlarm());
			PendingIntent Sender = PendingIntent.getBroadcast(AlarmTimeDetail.this, reqCode
					, AlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			AlmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(),
					24 * 60 * 60 * 1000, Sender);

			alarmDBHelper.close();
		}//if
	}//onDestroy
}//AlarmTimeDetail

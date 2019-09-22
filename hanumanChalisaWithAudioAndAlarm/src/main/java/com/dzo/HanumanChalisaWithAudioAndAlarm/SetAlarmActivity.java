package com.dzo.HanumanChalisaWithAudioAndAlarm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dzo.HanumanChalisaWithAudioAndAlarm.dao.AlarmTimeDAO;
import com.dzo.HanumanChalisaWithAudioAndAlarm.database.AlarmDBHelper;
import com.dzo.HanumanChalisaWithAudioAndAlarm.receiver.AlarmReceiver;

public class SetAlarmActivity extends BottomNav
{
	LinearLayout linAlarmTimePicker;
	Button btnOKTimePicker, btnCancelTimePicker;
	int mHour, mMinute, randomDiff;
	String ampm;
	boolean alarmEnabled;
	TimePicker alarmTimePicker;
	int _id, reqCodeForEdit;
	AlarmDBHelper alarmDBHelper;
	String strAmPM, intentFromClass;
	String timeToEdit;
	AlarmTimeDAO alarmDaoToEdit;
	AlarmTimeDAO mAlarmDAO;

	@Override
	public int getContentViewId() {
		return R.layout.set_alarm;
	}

	@Override
	public int getNavigationMenuItemId() {
		return R.id.navigation_alarm;
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.set_alarm);
		getLayoutInflater().inflate(R.layout.set_alarm,content_frame);

		alarmEnabled = getIntent().getBooleanExtra("ALARM_ENABLED", false);
		Log.v("HanumanChalisaSetAlarm", ""+alarmEnabled);
		
		intentFromClass = getIntent().getStringExtra("FROM_CLASS");
		Log.v("HanumanChalisaSetAlarm", ""+reqCodeForEdit);
	//	Toast.makeText(getApplicationContext(), "SerAlarmActivity", Toast.LENGTH_LONG).show();
		alarmDaoToEdit = (AlarmTimeDAO)getIntent().getSerializableExtra("AlarmDAOToEdit");
		
		if(alarmDaoToEdit != null)
		{
			timeToEdit = alarmDaoToEdit.getAlarmTime();
			reqCodeForEdit = alarmDaoToEdit.getId();
		}//if
		
		alarmDBHelper = new AlarmDBHelper(SetAlarmActivity.this);
		alarmTimePicker = (TimePicker)findViewById(R.id.alarmTimePicker);
        linAlarmTimePicker = (LinearLayout)findViewById(R.id.linAlarmTimePicker);
        btnOKTimePicker = (Button)findViewById(R.id.btnOKTimePicker);
        btnCancelTimePicker = (Button)findViewById(R.id.btnCancelTimePicker);
        btnOKTimePicker.setBackgroundColor(getResources().getColor(R.color.orangelight));
		btnCancelTimePicker.setBackgroundColor(getResources().getColor(R.color.orangelight));

		if(intentFromClass.equalsIgnoreCase("AlarmTimeDetail"))
		{
			showFormattedTimePicker(timeToEdit);
		}//if
		
		btnOKTimePicker.setOnClickListener(new View.OnClickListener() 
        {
			public void onClick(View v) 
			{
				linAlarmTimePicker.setVisibility(View.GONE);
				setAlarm(alarmTimePicker.getCurrentHour(), alarmTimePicker.getCurrentMinute());
				Log.v("HanumanChalisaTime", ""+alarmTimePicker.getCurrentHour());
				Log.v("HanumanChalisaTime", ""+alarmTimePicker.getCurrentMinute());
				Log.v("HanumanChalisabtnOk", "onClick of time picker");
			}//onClick
		});
        
        btnCancelTimePicker.setOnClickListener(new View.OnClickListener()
        {
			public void onClick(View v) 
			{
				linAlarmTimePicker.setVisibility(View.GONE);
				updateDisplay();
				finish();
			}//onClick
		});
	}//onCreate
		
	public void setAlarm(int AlarmHour, int AlarmMin) 
    {
		PendingIntent Sender = null;
		Intent AlarmIntent = new Intent(this, AlarmReceiver.class);//"com.dzo.HanumanChalisaWithAudioAndAlarm.RECEIVEALARM");
        AlarmManager AlmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
       
        if(intentFromClass.equalsIgnoreCase("MultipleAlarmActivity"))
        {
        	Calendar curCalendar = Calendar.getInstance();
        	Calendar alarmCalendar = Calendar.getInstance();
        		
        	curCalendar.set(Calendar.SECOND, 0);
        	curCalendar.set(Calendar.MILLISECOND, 0);    		
        	alarmCalendar.set(Calendar.SECOND, 0);
        	alarmCalendar.set(Calendar.MILLISECOND, 0);

        	alarmCalendar.set(Calendar.HOUR_OF_DAY, AlarmHour);
        	alarmCalendar.set(Calendar.MINUTE, AlarmMin);
        	
        	if (alarmCalendar.getTimeInMillis() <= curCalendar.getTimeInMillis()) 
        	{
        		alarmCalendar.add(Calendar.HOUR, 24);
        	}//if
        	
        	String CalendarHourStr;
        	if (AlarmHour > 12) 
        	{
        		CalendarHourStr = Integer.toString(AlarmHour - 12);
        	}//if 
        	else 
        	{
        		CalendarHourStr = Integer.toString(AlarmHour);
        	}//else
        		
        	String CalendarMinStr = Integer.toString(AlarmMin);
        	if (AlarmMin < 10) 
        	{
        		CalendarMinStr = "0" + CalendarMinStr;
        	}//if
        		
        	if (AlarmHour < 12) 
        	{
        		strAmPM = "AM";
        	}//if
        	else 
        	{
        		strAmPM = "PM";
        	}//else
        	
        	alarmEnabled = true;
        	_id = (int)System.currentTimeMillis();
        	AlarmIntent.putExtra("REQUEST CODE", _id);
        	AlarmIntent.putExtra("ALARM_ENABLED", alarmEnabled);
        	Log.v("HanumanChalisaMultiple", ""+_id);
        	
        	Sender = PendingIntent.getBroadcast(SetAlarmActivity.this, _id, AlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        	
        	AlmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(),
        			24 * 60 * 60 * 1000, Sender);
        	
        	String alarmTime = CalendarHourStr + ":" + CalendarMinStr+ " " + strAmPM;
        	Log.v("HanumanChalisaSet", alarmTime);
        	
        	mAlarmDAO = new AlarmTimeDAO();
        	mAlarmDAO.setId(_id);
        	Log.v("HanumanChalisa", ""+mAlarmDAO.getId());
        	
        	mAlarmDAO.setAlarmTime(alarmTime);
        	mAlarmDAO.setPeriod(strAmPM);
        	mAlarmDAO.setDaysToRepeatAlarm("Daily");
        	
        	alarmDBHelper.addAlarmTime(mAlarmDAO);
            
        	updateDisplay();
              
        	Toast.makeText(this, "Alarm Set For " + alarmTime, Toast.LENGTH_LONG).show();    	
        	Log.v("HanumanChalisa", "setAlarm called");
        }//if
        else if(intentFromClass.equalsIgnoreCase("AlarmTimeDetail"))
        {
        	Calendar curCalendar = Calendar.getInstance();
        	Calendar alarmCalendar = Calendar.getInstance();
        		
        	curCalendar.set(Calendar.SECOND, 0);
        	curCalendar.set(Calendar.MILLISECOND, 0);    		
        	alarmCalendar.set(Calendar.SECOND, 0);
        	alarmCalendar.set(Calendar.MILLISECOND, 0);

        	alarmCalendar.set(Calendar.HOUR_OF_DAY, AlarmHour);
        	alarmCalendar.set(Calendar.MINUTE, AlarmMin);
        	
        	if (alarmCalendar.getTimeInMillis() <= curCalendar.getTimeInMillis()) 
        	{
        		alarmCalendar.add(Calendar.HOUR, 24);
        	}
        	
        	String CalendarHourStr;
        	if (AlarmHour > 12) 
        	{
        		CalendarHourStr = Integer.toString(AlarmHour - 12);
        	}//if 
        	else 
        	{
        		CalendarHourStr = Integer.toString(AlarmHour);
        	}//else
        		
        	String CalendarMinStr = Integer.toString(AlarmMin);
        	if (AlarmMin < 10) 
        	{
        		CalendarMinStr = "0" + CalendarMinStr;
        	}//if
        		
        	if (AlarmHour < 12) 
        	{
        		strAmPM = "AM";
        	}//if
        	else 
        	{
        		strAmPM = "PM";
        	}//else
        	alarmEnabled = true;
        	String alarmTime = CalendarHourStr + ":" + CalendarMinStr + " " + strAmPM;
        	Log.v("HanumanChalisaSetAlarm", alarmTime);
        	
        	alarmDaoToEdit.setAlarmTime(alarmTime);
        	alarmDaoToEdit.setPeriod(strAmPM);
        	alarmDaoToEdit.setDaysToRepeatAlarm("Daily");
        	alarmDBHelper.updateAlarmTime(alarmDaoToEdit);
        	AlarmIntent.putExtra("REQUEST CODE", reqCodeForEdit);
        	AlarmIntent.putExtra("ALARM_ENABLED", alarmEnabled);
        	Log.v("HanumanChalisaMultiple", ""+reqCodeForEdit);
        	
        	Sender = PendingIntent.getBroadcast(SetAlarmActivity.this, reqCodeForEdit, 
        			AlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        	AlmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(),
        			24 * 60 * 60 * 1000, Sender);

        	updateDisplay();
            Toast.makeText(this, "Alarm Set For " + alarmTime, Toast.LENGTH_LONG).show();    	
        	Log.v("HanumanChalisa", "setAlarm called");
        }//else if       
    }//setAlarm 
	
	@Override
	public void onBackPressed() {

		
			super.onBackPressed();
			finish();
		

	}
    
    public void onPause() 
    {
    	super.onPause();
    	Log.v("HanumanChalisa", "onPause called");
    }//onPause
    
    private void updateDisplay()
    {
    	if(intentFromClass.equalsIgnoreCase("MultipleAlarmActivity"))
    	{
    		Intent alarmListIntent = new Intent(SetAlarmActivity.this, MultipleAlarmActivity.class);
    		alarmListIntent.putExtra("REQUEST CODE", _id);
    		alarmListIntent.putExtra("ALARM_ENABLED", alarmEnabled);
    		startActivity(alarmListIntent);
    		overridePendingTransition(R.anim.fed_in,R.anim.fed_out);
    		finish();
    	}//if
    	else if(intentFromClass.equalsIgnoreCase("AlarmTimeDetail"))
    	{
    		Intent alarmListIntent = new Intent(SetAlarmActivity.this, MultipleAlarmActivity.class);
    		alarmListIntent.putExtra("REQUEST CODE", reqCodeForEdit);
    		alarmListIntent.putExtra("ALARM_ENABLED", alarmEnabled);
    		startActivity(alarmListIntent);
			overridePendingTransition(R.anim.fed_in,R.anim.fed_out);
			finish();
    	}//else
    	Log.v("HanumanChalisa", "updateDisplay called");
	}//updateDisplay
    
    private void showFormattedTimePicker(String timeToEdit) 
    {
    	String delimiter = ":";
		String[] editTimeArray = timeToEdit.split(delimiter);
		
		for(int i=0; i < editTimeArray.length; i++)
		{
			String timeData = editTimeArray[i];
			Log.v("HanumanChalisa", timeData);
		}//for
		
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		Date date = null;
		try 
		{
			date = sdf.parse(timeToEdit);
		}//try 
		catch (ParseException e) 
		{
			e.printStackTrace();
		}//catch
		
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		if(timeToEdit.contains("AM"))
		{
			alarmTimePicker.setCurrentHour(calendar.get(Calendar.HOUR));
			alarmTimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
			Log.v("HanumanChalisa", "time to edit contains AM");
		}//if
		else if(timeToEdit.contains("PM"))
		{
			alarmTimePicker.setCurrentHour(calendar.get(Calendar.HOUR)+12);
			alarmTimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
			Log.v("HanumanChalisa", "time to edit contains PM");
 		}//else
	}//showFormattedTimePicker
    
    @Override
    protected void onDestroy() 
    {
    	super.onDestroy();
    //	Toast.makeText(getApplicationContext(), "destroyActivity", Toast.LENGTH_LONG).show();
    	if(alarmDBHelper != null)
    	{
    		alarmDBHelper.close();
    	}//if
    	Log.v("HanumanChalisa", "onDestroy called");
    }//onDestroy
}//SetAlarmActivity

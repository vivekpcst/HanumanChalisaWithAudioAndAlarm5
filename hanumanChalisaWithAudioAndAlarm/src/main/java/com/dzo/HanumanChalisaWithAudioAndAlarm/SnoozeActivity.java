package com.dzo.HanumanChalisaWithAudioAndAlarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.dzo.HanumanChalisaWithAudioAndAlarm.database.AlarmDBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/** Class for managing the "Snooze"(AlarmActivity) Activity */
public class SnoozeActivity extends Activity implements OnPreparedListener
{	
	public int snoozeMin;
	public int SNOOZE_MIN = 5;
	public MediaPlayer mMediaPlayer;
	public boolean alarmEnabled;
	public static boolean snoozed;
	private int mHour;
	long playTime;
	CountDownTimer timer;
	String ampm;
	Date date;
	Calendar calendar;
	DateFormat dateFormat;
	int req_code;
	AlarmDBHelper mAlarmHelper;
	
	/** Called when the activity is first created. */
	@Override 
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    
	    snoozed = getIntent().getBooleanExtra("ALARM_SNOOZED", false);
        Window window = getWindow();
        
        /**
         * Nitish
         * 06 Oct 12 PM
         * 
         * Flag to disable the keyguard on alarm.
         * The keyguard will be disable only, 
         * if it is not a secured keyguard
         * */
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
	    
        /**
         * Nitish
         * 06 Oct 12 PM
         * 
         * Flag to turn on the screen when alarm rings.
         * */
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        mAlarmHelper = new AlarmDBHelper(SnoozeActivity.this);
        playTime = System.currentTimeMillis();
        
        //Set window features to hide the notification bar and make the UI full screen before assigning the layout
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);        
        setContentView(R.layout.alarmsnooze);
        req_code = getIntent().getIntExtra("REQUEST CODE", 0);
        Log.v("SnoozeActivity", ""+getIntent().getIntExtra("REQUEST CODE", 0));
        alarmEnabled = getIntent().getBooleanExtra("ALARM_ENABLED", false);
        Log.v("SnoozeActivity", ""+alarmEnabled);
        //Get Last used Snooze Value and use as default
        
        if (mHour == 12)
        {
    		ampm = "PM";
    	}//if
    	
    	if (mHour == 0)
    	{
    		mHour = mHour + 12;    		
    	}//if
    	
    	if (mHour > 12)
    	{
    		mHour = mHour - 12;
    		ampm = "PM";
    	}//if
    	//date = new Date(System.currentTimeMillis());
    	
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        Date date = new Date();
		
    	// Set the current time here
        TextView txtTime = (TextView) findViewById(R.id.txtTime);
        
        txtTime.setText(sdf.format(date));

        //Assign UI components to local variables and define Click Event Listeners
        Button btnSnooze = (Button)this.findViewById(R.id.btnSnooze);
        btnSnooze.setOnClickListener(new MySnoozeListener());
        
        
        //Create Media Player for sounding the system alarm
        Uri ringUri = Uri.parse("android.resource://com.dzo.HanumanChalisaWithAudioAndAlarm/raw/bell");
        mMediaPlayer = new MediaPlayer();
        
        try 
        {
        	mMediaPlayer.setDataSource(this, ringUri);
        	mMediaPlayer.prepare();
        	mMediaPlayer.setOnPreparedListener(this);
        }//try
        catch(Exception e) 
        {
        	//TODO : Implement Error Checking
        }//catch
        
        Button btnDismiss=(Button)findViewById(R.id.btnDismiss);
        btnDismiss.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) 
			{
				snoozed = false;
				cancelTimer();
//				Intent in = new Intent(Intent.ACTION_MAIN);
				Intent in= new Intent(getApplicationContext(),MultipleAlarmActivity.class);
				in.addCategory(Intent.CATEGORY_HOME);
				in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(in);
//				finish();
			}//onClick
		});
	}//onCreate
	
	public void cancelTimer() 
	{
		timer.cancel();
		mMediaPlayer.stop();
		mMediaPlayer.release();
		Log.v("MultipleAlarm", "cancelTimer called");
	}//cancelTimer
	
    @Override
    public void onPause() 
    {
		super.onPause();
        Log.v("MultipleAlarm", "onPause called");
    }//onPause

	//Class for implementing the Click Event listener for the Snooze Button
    public class MySnoozeListener implements Button.OnClickListener 
    {
    	public void onClick(View v) 
    	{  
    		//Stop Alarm Sound
            try 
            {
	      	  	mMediaPlayer.stop();
	      	  	timer.cancel();
	      	  	Log.v("MultipleAlarm ", "mediaPlayer stopped");
            }
            catch(Exception e) 
            {
            	Log.v("MultipleAlarm ", e.toString());
            }
            
            Log.v("MultipleAlarm ", ""+alarmEnabled);
        	//Check Alarm Enabled Preference: Enabled = Snooze Alarm, Disabled = Disarm Alarm
            
            if (alarmEnabled) 
            {        	
	        	Calendar calendar = Calendar.getInstance();
	        	calendar.add(Calendar.MINUTE, SNOOZE_MIN);
	    		snoozed = true;
	    		Log.v("HCMultipleAlarm ", "alarm snoozed snoozed = "+ snoozed);
	      	  	long snoozeTime = calendar.getTimeInMillis();
	      	  	Intent AlarmIntent = new Intent("com.dzo.HanumanChalisaWithAudioAndAlarm.RECEIVEALARM");
	        	//AlarmIntent.putExtra("ALARM_SNOOZED", snoozed);
	      	  	AlarmManager AlmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
	        	AlarmIntent.putExtra("REQUEST CODE", req_code);
	        	AlarmIntent.putExtra("ALARM_ENABLED", alarmEnabled);
	        	PendingIntent Sender = PendingIntent.getBroadcast(SnoozeActivity.this, req_code, AlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);   
	    		AlmMgr.set(AlarmManager.RTC_WAKEUP, snoozeTime, Sender);
	    		Log.v("MultipleAlarm ", "in if loop alarm snoozed");
            }//if
//    		Intent in = new Intent(Intent.ACTION_MAIN);
			Intent in= new Intent(getApplicationContext(),MultipleAlarmActivity.class);
			in.addCategory(Intent.CATEGORY_HOME);
			in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(in);
            finish();  
            Log.v("MultipleAlarm", "Snooze button onClick called");
    	}//onClick
    }//MySnoozeListener

    /** Destructor routine to ensure media player is cleaned up after */
    @Override
    protected void onDestroy() 
    {
        super.onDestroy();
        if(mAlarmHelper != null)
        {
        	mAlarmHelper.close();
        }//if
        Log.v("MultipleAlarm onDestroy", "onDestroy called");
    }//onDestroy

    public void onPrepared(final MediaPlayer mp) 
	{
		timer = new CountDownTimer(60000, 1000)
		{
			@Override
			public void onTick(long millisUntilFinished) 
			{
				mp.start();
				Log.v("MultipleAlarm", "onTick called");
			}//onTick
			
			@Override
			public void onFinish()
			{
				mp.stop();
				mp.release();
				finish();
				Log.v("MultipleAlarmSnooze", "onFinish called");
			}//onFinish
		};
		timer.start();
	}//onPrepared
}//SnoozeActivity

package com.dzo.HanumanChalisaWithAudioAndAlarm;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dzo.HanumanChalisaWithAudioAndAlarm.util.Apputils;
import com.dzo.HanumanChalisaWithAudioAndAlarm.util.Prefs;
//import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.Locale;

/** Main Class for setting the Alarm */
public class HanuAlarm extends AppCompatActivity
{
	public static final String UPDATE_UI_ACTION = "com.dzo.HanumanChalisaWithAudioAndAlarm.UPDATE_UI";
	SharedPreferences languagePrefs;
	static String myPrefs;
	String ampm;
	
	static final String prefHindi = "Select Hindi";
	static final String prefEnglish = "Select English";
	
	static String LOCALE_HINDI = "hi";
	static String LOCALE_ENGLISH = Locale.ENGLISH.toString();
	boolean showOnce;
	
	String SHOW_RATING_DIALOG = "showRatingDialog";
	
	boolean alarmEnabled;
	TimePicker AlarmTimePicker;
	
	public static boolean chalisaPlaying = false;
    private ImageButton btnSetTime;
//	private GoogleCloudMessaging gcm;
    int randomDiff;
    
    static ImageButton btn_Play;
    TextView txtPlay;
    TextView txtRead, txtAlarm, txtInfo, txtExit; 
    Intent in;
    static Locale[] mLocaleArray;
    Locale hindi;
    LinearLayout linStartbtn, linAudiobtn, linAlarmbtn, linAboutbtn, linExitbtn;
    static final int TIME_DIALOG_ID = 0;
    private String regid;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        //set Layout
        setContentView(R.layout.alarmclock);
        
//    	gcm = GoogleCloudMessaging.getInstance(this);
		regid = getRegistrationId(this);
		registerInBackground();
        in = new Intent(HanuAlarm.this, ChalisaService.class);
		//Log.i("HanuAlarm play button if intent", ""+in);
		
        txtRead = (TextView)findViewById(R.id.txtRead);
		txtPlay = (TextView)findViewById(R.id.txtPlay);
		txtAlarm = (TextView)findViewById(R.id.txtAlarm);
		txtInfo = (TextView)findViewById(R.id.txtInfo);
		txtExit = (TextView)findViewById(R.id.txtExit);
        
		btn_Play = (ImageButton)findViewById(R.id.btn_Play);
        btn_Play.setOnClickListener(new View.OnClickListener() 
        {
			public void onClick(View v) 
			{
				if(ChalisaService.playerFlag == 0)
				{
					startService(in);
					ChalisaService.playerFlag = 1;
					//Log.v("HanuAlarm play button if", "in if loop");
					txtPlay.setText("Pause");
					txtPlay.setTextColor(getResources().getColor(R.color.redwine));
					btn_Play.setBackgroundResource(R.drawable.btnpause);
				}//if
				else if(ChalisaService.playerFlag == 1)
				{
					ChalisaService.mediaPlayer.pause();
					ChalisaService.playerFlag = 0;
					//Log.v("HanuAlarm play button else", "in else loop");
					txtPlay.setText("Play");
					txtPlay.setTextColor(getResources().getColor(R.color.white));
					btn_Play.setBackgroundResource(R.drawable.btnplay_a);
				}//else if
			}//onClick
		});
        
        ImageButton start = (ImageButton) findViewById(R.id.btn_Start);
		start.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) 
			{
				txtRead.setTextColor(getResources().getColor(R.color.redwine));
				if(Build.MANUFACTURER.equalsIgnoreCase("samsung"))
				{
					Intent intent = new Intent(HanuAlarm.this, StartManualActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}//if
				else
				{
					Intent intent = new Intent(HanuAlarm.this, English.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					Toast.makeText(HanuAlarm.this, 
							"Sorry, Devanagari Script is not supported by your device", Toast.LENGTH_LONG).show();
				}//else
			}//onClick
		});

		ImageButton about = (ImageButton) findViewById(R.id.btn_About);
		about.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				txtInfo.setTextColor(getResources().getColor(R.color.redwine));
				Intent intent = new Intent(HanuAlarm.this, About.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		
		ImageButton exit = (ImageButton) findViewById(R.id.btn_Exit);
		exit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				txtExit.setTextColor(getResources().getColor(R.color.redwine));
				showOnce = Prefs.getBoolean(HanuAlarm.this, SHOW_RATING_DIALOG);
				if(showOnce == false)
				{
					showDialog();
					Prefs.setBoolean(HanuAlarm.this, SHOW_RATING_DIALOG, true);
				}//if
				else
				{
					Intent in = new Intent(Intent.ACTION_MAIN);
					in.addCategory(Intent.CATEGORY_HOME);
					in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(in);
					finish();
				}//else
			}
		});
        
        btnSetTime=(ImageButton)findViewById(R.id.btnSetTime);
        btnSetTime.setOnClickListener(new OnClickListener() {
			public void onClick(View v) 
			{
				Intent setAlarmIntent = new Intent(HanuAlarm.this, MultipleAlarmActivity.class);
				setAlarmIntent.putExtra("FROM_CLASS", "HanuAlarm");
				startActivity(setAlarmIntent);
				Log.v("HanumanChalisa", "onClick called");
			}//onClick
		});
        registerReceiver(mUpdateUIReceiver, new IntentFilter(UPDATE_UI_ACTION));
    }//onCreate 	

	@Override
    public void onPause()
    {
        super.onPause();
        Log.v("HanuAlarm", "onPause called");
    }//onPause
    
    public void refresh()
    {
    	Intent intent = new Intent(HanuAlarm.this, HanuAlarm.class);
    	startActivity(intent);
		this.finish();
    }
     
    @Override
    protected void onDestroy() 
    {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	unregisterReceiver(mUpdateUIReceiver);
    }//onDestroy
    
    protected void onResume() 
    {
    	if(ChalisaService.playerFlag == 0)
    	{	
    		txtPlay.setText("Play");
    		txtPlay.setTextColor(getResources().getColor(R.color.white));
			btn_Play.setBackgroundResource(R.drawable.btnplay_a);
			//Log.i("HanuAlarm", "onResume if");
    	}//if
    	else if(ChalisaService.playerFlag == 1)
    	{
    		txtPlay.setText("Pause");
    		txtPlay.setTextColor(getResources().getColor(R.color.redwine));
			btn_Play.setBackgroundResource(R.drawable.btnpause);
			//Log.i("HanuAlarm", "onResume else");
    	}//else
    	super.onResume();
    }//onResume
     
    private void showDialog() 
	{
		AlertDialog.Builder ad = new AlertDialog.Builder(HanuAlarm.this);
		ad.setIcon(R.drawable.icon);
		ad.setTitle("Rate The App");
		ad.setMessage("Would you like to rate this app?");
		ad.setPositiveButton("Now", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				Intent intent = new Intent(Intent.ACTION_VIEW); 
				intent.setData(Uri.parse("market://details?id="+getPackageName())); 
				try 
				{
				   startActivity(intent);
				}//try 
				catch (android.content.ActivityNotFoundException ex) 
				{
				      Toast.makeText(HanuAlarm.this, "Google Play Not Available on this Device", Toast.LENGTH_SHORT).show();
				}//catch
			}//onClick
		});
			
		ad.setNegativeButton("Later", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				Intent in = new Intent(Intent.ACTION_MAIN);
				in.addCategory(Intent.CATEGORY_HOME);
				in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(in);
				finish();
			}//onClick
		});
		ad.show();
	}//showDialog
     
    private final BroadcastReceiver mUpdateUIReceiver = new BroadcastReceiver() 
    {
    	@Override
 		public void onReceive(Context context, Intent intent) 
    	{
 			int playerFlagValue = intent.getIntExtra("Player_FLAG_VALUE", 0);
 			if(playerFlagValue == 0)
 			{
 				txtPlay.setText("Play");
				txtPlay.setTextColor(getResources().getColor(R.color.white));
				btn_Play.setBackgroundResource(R.drawable.play);
 			}//if
 			else
 			{
 				txtPlay.setText("Pause");
				txtPlay.setTextColor(getResources().getColor(R.color.redwine));
				btn_Play.setBackgroundResource(R.drawable.btnpause);
 			}//else
 		}
 	};
 	private String getRegistrationId(Context context) {
		String registrationId = Prefs.getGCMDeviceRegistrationID(
				context, Apputils.GCM_DEVICE_REG_ID);

		
	
		int registeredVersion = Prefs.getAppVersion(context,
				Apputils.PROPERTY_APP_VERSION);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			return "";
		}
		return registrationId;
	}
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}
	private void storeRegistrationId(Context context, String regId) {
		int appVersion = getAppVersion(context);
		Prefs.saveGcmDeviceRegistrationID(context,
				Apputils.GCM_DEVICE_REG_ID, regId);
		Prefs.saveAppVersion(context, Apputils.PROPERTY_APP_VERSION,
				appVersion);
	}
	private void registerInBackground() {/*
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(HanuAlarm.this);
					}
					regid = gcm.register(Apputils.GCM_SENDER_ID);
					msg = "Device registered, registration ID=" + regid;
					Log.e("regi id",regid);
					storeRegistrationId(getApplicationContext(), regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
		
				}
				return msg;
			}

			protected void onPostExecute(String result) {
				//Log.e(TAG, "Device RegID: " + result);
			}
		}.execute(null, null, null);
	
	
	*/
	}
	
	
	
}


package com.dzo.HanumanChalisaWithAudioAndAlarm;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StartManualActivity extends BottomNav {

	private PagerWithPageControl mPager;
	 static ImageButton btnPlaying, btnHindi, btnEnglish, btnLogo; 
	 LinearLayout linHomebtn, linHindibtn, linEnglishbtn, linAudiobtn, linAboutbtn, linEnglishToggle;
	 String LOCALE_HINDI = "hi";
	 String LOCALE_ENGLISH = Locale.ENGLISH.toString();
	 TextView txtHome, txtHindi, txtPlay, txtinfo, txtExit;
	 Intent in;
	 Locale mLocale, defaultLocale;
	 Configuration config;
	 ImageButton btnLang, btn_Exit, bottomsheetDialog;
	 boolean playerStarted, playerStoppedInOnStop;
	 BroadcastReceiver screenoffReceiver;
	 String myPref;
	 static SharedPreferences languagePref;
	 //boolean chalisaPlaying = false;
	 String SHOW_RATING_DIALOG = "showRatingDialog";
	 boolean showOnce;
	 public static TextView pageCounter;
	 public static final String UPDATE_UI_ACTION = "com.dzo.HanumanChalisaWithAudioAndAlarm.UPDATE_UI";


	String DB_PATH;
	final Context context=this;
	private SQLiteDatabase mDataBase;
	private static String DB_NAME = "HcEvents_1.db";



	@Override
    public int getContentViewId() {
        return R.layout.hanuman_hindi_read;
    }

	@Override
	protected void onStart() {
		super.onStart();
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		Configuration config = getBaseContext().getResources().getConfiguration();
		String lang = settings.getString("LANG", "");
		if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang)) {
			Locale locale = new Locale(lang);
			Locale.setDefault(locale);
			config.locale = locale;
			getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
		}
	}

	@Override
    public int getNavigationMenuItemId() {
        return R.id.nav_read_lang;
    }
    @SuppressLint("WrongConstant")
	 public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getLayoutInflater().inflate(R.layout.hanuman_hindi_read, content_frame);
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		Configuration config = getBaseContext().getResources().getConfiguration();
		String lang = settings.getString("LANG", "");
		if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang)) {
			Locale locale = new Locale(lang);
			Locale.setDefault(locale);
			config.locale = locale;
			getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
		}
		pageCounter=findViewById(R.id.pageCounter);
		mPager = (PagerWithPageControl) findViewById(R.id.horizontal_pager);
        mPager.addPagerControl();
//		registerReceiver(mUpdateUIReceiver, new IntentFilter(UPDATE_UI_ACTION));


		setNotificationReminder();


		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		if((width*height)>=(1080*2160)) {
//			about.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
//			about_1.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
			((TextView)findViewById(R.id.vsk_h1)).setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
			((TextView)findViewById(R.id.vsk_h2)).setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
			((TextView)findViewById(R.id.vsk_h3)).setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
			((TextView)findViewById(R.id.vsk_h4)).setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
			((TextView)findViewById(R.id.vsk_h5)).setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
			((TextView)findViewById(R.id.vsk_h6)).setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
			((TextView)findViewById(R.id.vsk_h7)).setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
			((TextView)findViewById(R.id.vsk_h8)).setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
			((TextView)findViewById(R.id.vsk_h9)).setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
			((TextView)findViewById(R.id.vsk_h10)).setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
			((TextView)findViewById(R.id.vsk_h11)).setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
			((TextView)findViewById(R.id.vsk_h12)).setTextSize(TypedValue.COMPLEX_UNIT_SP,23);

		}


/*


		Date cal = Calendar.getInstance().getTime();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
		String formattedDate = df.format(cal);


		DatabaseMain dbMain=new DatabaseMain(this);
		try{
			dbMain.createDatabase();
		} catch (IOException e) {
			throw new Error("Database not created...");
//			e.printStackTrace();
		}
		try{
			dbMain.openDB();
		}catch (SQLException s){
			throw s;
		}
		SQLiteDatabase db1;
		db1=openOrCreateDatabase("HcEvents",SQLiteDatabase.CREATE_IF_NECESSARY,null);
		Cursor c= db1.rawQuery("SELECT * FROM EventList",null);
		c.moveToFirst();
		String temp="";
		while(! c.isAfterLast())
		{
			String s2=c.getString(0);
			String s3=c.getString(1);
			temp=temp+"\n event_date:"+s2+"\tevent_name:"+s3;
			c.moveToNext();
		}
		c.close();
//		String query = "SELECT * FROM EventList WHERE event_date='"+formattedDate+"'";
		String query="select * from EventList where event_date='"+formattedDate+"'";
		SQLiteDatabase db = dbMain.getWritableDatabase();
//		db1.query("EventList", null, "event_date = ?", new String[]{"event_date"}, null, null, null);
		String eventName=null;
		Cursor cur= db.rawQuery(query,null);
		if( cur != null && cur.moveToFirst() ){
			String s2=cur.getString(0);
			eventName=cur.getString(1);
			cur.close();
			db.close();
		}
		if(eventName!=null){
			showNotification(eventName);
		}*/
//		bottomsheetDialog=findViewById(R.id.imageButton2);
//		initBottomSheet();
//		bottomsheetDialog.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				showEvents();
//			}
//		});
		notifyUser();

	}

	/*private void showEvents() {
		if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
			sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
		} else {
			sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
		}
	}*/

	/*ConstraintLayout layoutBottomSheet;
	BottomSheetBehavior sheetBehavior;

	private void initBottomSheet() {
		layoutBottomSheet=findViewById(R.id.bottom_sheet);
		sheetBehavior=BottomSheetBehavior.from(layoutBottomSheet);
		sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
			@Override
			public void onStateChanged(@NonNull View bottomSheet, int newState) {
				switch (newState) {
					case BottomSheetBehavior.STATE_HIDDEN:
						break;
					case BottomSheetBehavior.STATE_EXPANDED: {
					}
					break;
					case BottomSheetBehavior.STATE_COLLAPSED: {

					}
					break;
					case BottomSheetBehavior.STATE_DRAGGING:
						break;
					case BottomSheetBehavior.STATE_SETTLING:
						break;
				}
			}

			@Override
			public void onSlide(@NonNull View bottomSheet, float slideOffset) {

			}
		});
	}



*/
	private void setNotificationReminder() {
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent alarmIntent = new Intent(this, NotificatioAlarmReceiver.class);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);
		alarmIntent.setData((Uri.parse("custom://"+System.currentTimeMillis())));
		alarmManager.cancel(pendingIntent);

		Calendar alarmStartTime = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		alarmStartTime.set(Calendar.HOUR_OF_DAY, 15);
		alarmStartTime.set(Calendar.MINUTE, 18);
		alarmStartTime.set(Calendar.SECOND, 0);
		if (now.after(alarmStartTime)) {
			Log.d("Hey","Added a day");
			alarmStartTime.add(Calendar.DAY_OF_MONTH, 1);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			alarmManager.setExact(AlarmManager.RTC_WAKEUP,alarmStartTime.getTimeInMillis(),pendingIntent);
//			alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,alarmStartTime.getTimeInMillis(),pendingIntent);
		}else {
			alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
		}


//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

	/*	Intent myIntent1 = new Intent(StartManualActivity.this,NotificatioAlarmReceiver.class);
		PendingIntent pendingintent2 = PendingIntent.getService(StartManualActivity.this, 1,myIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager1 = (AlarmManager) getSystemService(ALARM_SERVICE);
		Calendar calendar1Notify = Calendar.getInstance();
		calendar1Notify.setTimeInMillis(System.currentTimeMillis());
		calendar1Notify.set(Calendar.HOUR_OF_DAY, 10);
		calendar1Notify.set(Calendar.MINUTE, 03);

		alarmManager1.set(AlarmManager.RTC_WAKEUP,calendar1Notify.getTimeInMillis(), pendingintent2);

		long time24h = 24*60*60*1000;

		alarmManager1.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar1Notify.getTimeInMillis(),time24h,pendingintent2);

*/

	}

	/*
    private void showNotification(String eventName) {
		Bitmap icon= BitmapFactory.decodeResource(this.getResources(),R.drawable.icon);
		NotificationCompat.Builder builder=new NotificationCompat.Builder(this,CHANNEL_ID);
		builder.setContentTitle("Hanuman chalisa");
		builder.setContentText(eventName);
		builder.setVibrate(new long[]{500,500,1000,1000,500});
		builder.setDefaults(Notification.DEFAULT_SOUND);
		builder.setChannelId(CHANNEL_ID);
		builder.setLargeIcon(icon);
		builder.setAutoCancel(true);
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
			builder.setSmallIcon(R.drawable.ic_icon_trans);
			builder.setColor(getResources().getColor(R.color.orangelight));
		}else {
			builder.setSmallIcon(R.drawable.icon);
		}
		NotificationManager notificationManager= (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
			CharSequence name ="" +
					"Hanuman chalisa";
			int importance=NotificationManager.IMPORTANCE_DEFAULT;
			NotificationChannel channel=new NotificationChannel(CHANNEL_ID,name,importance);
			notificationManager.createNotificationChannel(channel);
		}
		notificationManager.notify(0,builder.build());


	}*/
	protected void onPause(){
	    	super.onPause();
		}
	protected void onStop() {
		super.onStop();
	}
		protected void onResume()
		{
			super.onResume();
	    }
		protected void onDestroy()
		{
			super.onDestroy();
//			unregisterReceiver(mUpdateUIReceiver);
		}
		
		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			super.onBackPressed();
		}
		@Override
		protected void onRestart() 
		{
			super.onRestart();
		}
		private void showDialog() 
		 {
				AlertDialog.Builder ad = new AlertDialog.Builder(StartManualActivity.this);
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
						      Toast.makeText(StartManualActivity.this, "Android Market Not Availbale at this Device", Toast.LENGTH_SHORT).show();
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
	 		public void onReceive(Context context, Intent intent) {
	 			//updateStatus();
	 			int playerFlagValue = intent.getIntExtra("Player_FLAG_VALUE", 0);
	 			if(playerFlagValue == 0)
	 			{
	 				txtPlay.setText("Play");
					txtPlay.setTextColor(getResources().getColor(R.color.white));
					btnPlaying.setBackgroundResource(R.drawable.play);
	 			}//if
	 			else
	 			{
	 				txtPlay.setText("Pause");
					txtPlay.setTextColor(getResources().getColor(R.color.redwine));
					btnPlaying.setBackgroundResource(R.drawable.btnpause);
	 			}//else
	 		}
	 	};

	List<String> eventList= new ArrayList<>();
	private void notifyUser() {
		eventList.clear();
		Date cal = Calendar.getInstance().getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		String formattedDate = df.format(cal);


		DatabaseMain dbMain=new DatabaseMain(context);
		try{
			dbMain.createDatabase();
		} catch (IOException e) {
			throw new Error("Database not created...");
//			e.printStackTrace();
		}
		try{
			dbMain.openDB();
		}catch (SQLException s){
			throw s;
		}
		SQLiteDatabase db1;
		db1=context.openOrCreateDatabase(/*"HcEvents_1"*/"vskingEdu",0,null);
		Cursor c= db1.rawQuery("SELECT * FROM Events",null);
		c.moveToFirst();
		String temp="";
		while(! c.isAfterLast())
		{
			String s2=c.getString(0);
			String s3=c.getString(1);
			temp=temp+"\n event_date:"+s2+"\tevent_name:"+s3;
			eventList.add(s3+"\n"+s2);
			c.moveToNext();
		}
		c.close();
//		String query = "SELECT * FROM EventList WHERE event_date='"+formattedDate+"'";
		String query="select * from Events where event_date='"+formattedDate+"'";
		SQLiteDatabase db = dbMain.getWritableDatabase();
//		db1.query("EventList", null, "event_date = ?", new String[]{"event_date"}, null, null, null);
		String eventName=null;
		Cursor cur= db.rawQuery(query,null);
		if( cur != null && cur.moveToFirst() ){
			String s2=cur.getString(0);
			eventName=cur.getString(1);
			cur.close();
			db.close();
			Intent service1 = new Intent(context, NotificationService.class);
			service1.putExtra("eventName",eventName);
			service1.setData((Uri.parse("custom://"+System.currentTimeMillis())));
			context.startService(service1);
		}



	}


}

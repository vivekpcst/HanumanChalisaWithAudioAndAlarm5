package com.dzo.HanumanChalisaWithAudioAndAlarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_TIME = 2000;  /* 2 seconds */
	public static final String TAG = "Hanuman_Chalisa_Alarm_Wake_Tag";
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
		/*SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor vEditor=sharedPreferences.edit();
		vEditor.putBoolean("isNotNull",false);
		vEditor.apply();*/
		new Handler().postDelayed(new Runnable() {
			public void run() {
//				English.isNotNull=false;


				/* Create an intent that will start the main activity. */
				Intent mainIntent = new Intent(SplashActivity.this, StartManualActivity.class);
				SplashActivity.this.startActivity(mainIntent);
				SplashActivity.this.finish();				
				overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
			}
		}, SPLASH_DISPLAY_TIME);
    }
}
package com.dzo.HanumanChalisaWithAudioAndAlarm;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dzo.HanumanChalisaWithAudioAndAlarm.dao.HanuDrawerItem;

import java.util.ArrayList;
import java.util.Locale;

public class English extends BottomNav {
	
	private ArrayList<HanuDrawerItem> mDrawerItem;
	private PagerWithPageControl mPager;
	 //MediaPlayer mp = null;
	 ImageButton btnPlaying, btn_Exit; 
	 String LOCALE_HINDI = "hi";
	 ImageButton btnHindi, btnEnglish, btnLogo;
	 boolean playerStarted, playerStoppedInOnStop;
	 BroadcastReceiver screenoffReceiver;
	 String LOCALE_ENGLISH = Locale.ENGLISH.toString();
	 TextView txtHome, txtEnglish, txtPlay, txtInfo, txtExit;
	 boolean chalisaPlaying = false;
	 boolean showOnce;
	 String SHOW_RATING_DIALOG = "showRatingDialog";
	 Intent in;
	 public static TextView Pcounter;
	 public static boolean isNotNull=false;

	 LinearLayout linHomebtn, linHindibtn, linEnglishbtn, linAudiobtn, linAboutbtn, linHindiToggle;
	 public static final String UPDATE_UI_ACTION = "com.dzo.HanumanChalisaWithAudioAndAlarm.UPDATE_UI";

	public English() {
	}

	@Override
    public int getContentViewId() {
        return R.layout.hanuman_english_read;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.nav_read_lang;
    }

    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        /*SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor vEditor=sharedPreferences.edit();
        vEditor.putBoolean("isNotNull",true);
        vEditor.apply();*/
        initView();


		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		Configuration config = getBaseContext().getResources().getConfiguration();

		String lang = settings.getString("LANG", "");
		if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang)) {
			Locale locale = new Locale(lang);
			Locale.setDefault(locale);
			config.locale = locale;
			getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
		}



		in = new Intent(English.this, ChalisaService.class);

		mPager = (PagerWithPageControl) findViewById(R.id.horizontal_pager);
        mPager.addPagerControl();

	}

	private void initView() {
				getLayoutInflater().inflate(R.layout.hanuman_english_read, content_frame);
		Pcounter=findViewById(R.id.Pcounter);
		isNotNull=true;

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		if((width*height)>=(1080*2160)) {
//			about.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
//			about_1.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
			((TextView)findViewById(R.id.vk_h1)).setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
			((TextView)findViewById(R.id.vk_h2)).setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
			((TextView)findViewById(R.id.vk_h3)).setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
			((TextView)findViewById(R.id.vk_h4)).setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
			((TextView)findViewById(R.id.vk_h5)).setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
			((TextView)findViewById(R.id.vk_h6)).setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
			((TextView)findViewById(R.id.vk_h7)).setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
			((TextView)findViewById(R.id.vk_h8)).setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
			((TextView)findViewById(R.id.vk_h9)).setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
			((TextView)findViewById(R.id.vk_h10)).setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
			((TextView)findViewById(R.id.vk_h11)).setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
			((TextView)findViewById(R.id.vk_h12)).setTextSize(TypedValue.COMPLEX_UNIT_SP,23);

		}

	}
		protected void onDestroy()
		{
			super.onDestroy();
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

	private void showDialog()
		{
			AlertDialog.Builder ad = new AlertDialog.Builder(English.this);
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
					      Toast.makeText(English.this, "Android Market Not Availbale at this Device", Toast.LENGTH_SHORT).show();
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
		}
}

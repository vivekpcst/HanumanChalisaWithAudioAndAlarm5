package com.dzo.HanumanChalisaWithAudioAndAlarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.dzo.HanumanChalisaWithAudioAndAlarm.util.Apputils;
import com.dzo.HanumanChalisaWithAudioAndAlarm.util.Prefs;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


import static com.dzo.HanumanChalisaWithAudioAndAlarm.ChalisaService.mediaPlayer;

public class Play extends BottomNav implements SeekBar.OnSeekBarChangeListener, AudioManager.OnAudioFocusChangeListener {
	TextView tvMusicProgres,tvMaxDuration;
	Button btnPlay;
	Intent	in ;
	int off=1;
	int on=0;
	int pause=2;
	SeekBar mSeekBar;
	private Handler mHandler = new Handler();
	Runnable runnable;
	ImageView imageView;
	AudioManager audioManager;
	@Override
	public int getContentViewId() {
		return R.layout.play_new;
	}
	@Override
	public int getNavigationMenuItemId() {
		return R.id.navigation_play;
	}


	private int position=0;
	private int[] img = {R.drawable.h, R.drawable.hh, R.drawable.hhh, R.drawable.hhhh,R.drawable.hhhhh};
	private Timer timer=null;
	private void swithImage(){
		timer= new Timer();
		//Set the schedule function and rate
		timer.scheduleAtFixedRate(new TimerTask() {

			public void run() {
				//Called each time when 1000 milliseconds (1 second) (the period parameter)

				// If index reaches maximum reset it

				runOnUiThread(new Runnable() {

					public void run() {
						imgSw.setImageResource(img[position]);
						position++;
						if(position == 5){
							position=0;

						}
					}
				});
			}

		},0,5000);
	}
	ImageSwitcher imgSw;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set Layout
		//setContentView(R.layout.play);
//		getSupportActionBar().setBackgroundDrawable(
//                getResources().getDrawable(R.drawable.header));

        getLayoutInflater().inflate(R.layout.play_new, content_frame);

		imgSw=(ImageSwitcher) findViewById(R.id.imageView);


		HashMap<String,String> url_maps = new HashMap<String, String>();
		url_maps.put("h", "https://www.hindugallery.com/images/Lord-Hanuman-wallpaper-ima.jpg");
		url_maps.put("hh", "https://c8.alamy.com/comp/DE1PWT/goddess-hanuman-DE1PWT.jpg");
		url_maps.put("hhh", "https://theharekrishnamovement.files.wordpress.com/2014/11/rama-hanuman.jpg");
		url_maps.put("hhhh", "https://i1.wp.com/haistatus.com/wp-content/uploads/2017/08/hanuman-images-25.jpg");

		imgSw.setFactory(new ViewSwitcher.ViewFactory() {
			@Override
			public View makeView() {
				imageView=new ImageView(getApplicationContext());
				imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
				imageView.setLayoutParams(new ImageSwitcher.LayoutParams(SlidingUpPanelLayout.LayoutParams.MATCH_PARENT, SlidingUpPanelLayout.LayoutParams.MATCH_PARENT));
				imageView.setImageResource(img[position]);
				swithImage();
				return imageView;
			}
		});

		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
		mSeekBar=findViewById(R.id.mSeekBar);
		tvMaxDuration=findViewById(R.id.tvMaxDuration);
		tvMusicProgres=findViewById(R.id.tvMusicProgres);
		mSeekBar.setOnSeekBarChangeListener(this);
		in = new Intent(Play.this, ChalisaService.class);

		btnPlay=(Button)findViewById(R.id.btnPlay);
		if(mediaPlayer==null) {
			mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.chalisa);
			mediaPlayer.setVolume(100, 100);
			mediaPlayer.setLooping(true);
			mSeekBar.setMax(mediaPlayer.getDuration());
//			UpdateSongTime.run();
			/*startService(new Intent(Apputils.PAUSE_MUSIC)
					.putExtra("AUDIO_PAUSE_REASON",
							"user"));
			*/

			/*in.setPackage("com.dzo.HanumanChalisaWithAudioAndAlarm");
			in.setAction(Apputils.PLAY_MUSIC).putExtra("AUDIO_PAUSE_REASON","user");

			startService(in);*/
			in.setAction(Apputils.PLAY_MUSIC);
			startService(in);
			ChalisaService.playerFlag = 1;
			Prefs.setMusicOff(getApplicationContext(),
					Apputils.PAUSE_MUSIC, 0);
			Log.i("Abhishek", "serviceoff");
						/*txtPlay.setText("Pause");
						txtPlay.setTextColor(getResources().getColor(R.color.redwine));*/
			btnPlay.setBackgroundResource(R.drawable.pause);

//                        tvMaxDuration.setText(mediaPlayer.getDuration());
//                                playCycle();
			UpdateSongTime.run();

		}else{
			mSeekBar.setMax(mediaPlayer.getDuration());
			mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
			UpdateSongTime.run();
		}

		/*ChalisaService.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mSeekBar.setMax(ChalisaService.mediaPlayer.getDuration());
				playCycle();
				mp.start();

			}
		});*/

		/**
		 * Setting title and itemChecked
		 */
//		mDrawerList.setItemChecked(position, true);
		//setTitle(mDrawerMenuList[position]);

		btnPlay.setOnClickListener(new OnClickListener() {
	          public void onClick(View v) {/*

	        	  if((Prefs.getMusicOff(getApplicationContext(), Apputils.PAUSE_MUSIC))==pause)
	        	  {
					  in.setAction(Apputils.PLAY_MUSIC);
					  startService(in);
					  mediaPlayer.pause();
	        		  ChalisaService.playerFlag=0;
	        		  btnPlay.setBackgroundResource(R.drawable.music);
						Prefs.setMusicOff(getApplicationContext(),
								Apputils.PAUSE_MUSIC, 1);
	        	  }
      	  
	        	// If !started, start & change the button	        	  
	        	  else  if(ChalisaService.playerFlag == 0)
					{
						mediaPlayer.start();
						in.setPackage("com.dzo.HanumanChalisaWithAudioAndAlarm");
						in.setAction(Apputils.PAUSE_MUSIC).putExtra("AUDIO_PAUSE_REASON","user");
						startService(in);
						ChalisaService.playerFlag = 1;
						Prefs.setMusicOff(getApplicationContext(),
								Apputils.PAUSE_MUSIC, 0);
						Log.i("Abhishek", "serviceoff");
						*//*txtPlay.setText("Pause");
						txtPlay.setTextColor(getResources().getColor(R.color.redwine));*//*
						btnPlay.setBackgroundResource(R.drawable.pause);

//                        tvMaxDuration.setText(mediaPlayer.getDuration());
//                                playCycle();
                                UpdateSongTime.run();
					}//if
					else if(ChalisaService.playerFlag == 1)
					{
						in.setPackage("com.dzo.HanumanChalisaWithAudioAndAlarm");
						in.setAction(Apputils.PAUSE_MUSIC).putExtra("AUDIO_PAUSE_REASON","user");
						startService(in);
						mediaPlayer.pause();
						ChalisaService.playerFlag = 0;
						btnPlay.setBackgroundResource(R.drawable.music);
						Prefs.setMusicOff(getApplicationContext(),
								Apputils.PAUSE_MUSIC, 1);
						Log.i("Verma", "serviceOn");
//						txtPlay.setText("Play");
//						txtPlay.setTextColor(getResources().getColor(R.color.white));

					}//else*/

				  if((Prefs.getMusicOff(getApplicationContext(), Apputils.PAUSE_MUSIC))==pause)
				  {
					  mediaPlayer.pause();
					  ChalisaService.playerFlag=0;
					  btnPlay.setBackgroundResource(R.drawable.music);
					  Prefs.setMusicOff(getApplicationContext(),
							  Apputils.PAUSE_MUSIC, 1);
				  }

				  // If !started, start & change the button
				  else  if(ChalisaService.playerFlag == 0)
				  {
					  startService(in);
					  ChalisaService.playerFlag = 1;
					  Prefs.setMusicOff(getApplicationContext(),
							  Apputils.PAUSE_MUSIC, 0);
					  Log.i("Abhishek", "serviceoff");
						/*txtPlay.setText("Pause");
						txtPlay.setTextColor(getResources().getColor(R.color.redwine));*/
					  btnPlay.setBackgroundResource(R.drawable.pause);
//                        tvMaxDuration.setText(mediaPlayer.getDuration());
//                                playCycle();
					  UpdateSongTime.run();
				  }
				  else if(ChalisaService.playerFlag == 1)
				  {
					  mediaPlayer.pause();
					  ChalisaService.playerFlag = 0;
					  btnPlay.setBackgroundResource(R.drawable.music);
					  Prefs.setMusicOff(getApplicationContext(),
							  Apputils.PAUSE_MUSIC, 1);
					  Log.i("Verma", "serviceOn");
//						txtPlay.setText("Play");
//						txtPlay.setTextColor(getResources().getColor(R.color.white));

				  }//else
			  }

	      });


		TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		if(mgr != null) {
			mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		}

     }
	   PhoneStateListener phoneStateListener = new PhoneStateListener() {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			if (state == TelephonyManager.CALL_STATE_RINGING) {
				//Incoming call: Pause music
				mediaPlayer.pause();
				ChalisaService.playerFlag=0;
				btnPlay.setBackgroundResource(R.drawable.music);

			} else if(state == TelephonyManager.CALL_STATE_IDLE) {
				//Not in call: Play music
				mediaPlayer.start();
                ChalisaService.playerFlag=1;
                btnPlay.setBackgroundResource(R.drawable.pause);

			} else if(state == TelephonyManager.CALL_STATE_OFFHOOK) {
				//A call is dialing, active or on hold
				mediaPlayer.pause();
                ChalisaService.playerFlag=0;
                btnPlay.setBackgroundResource(R.drawable.music);

			}
			super.onCallStateChanged(state, incomingNumber);
		}};
	protected void onPause(){
	    super.onPause();


	}
		
	protected void onStop() {
		super.onStop();
		Log.e("Abhishek", "OnstopCalled");

	}
		
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
		/**
		 * Override
		 * 
  		 * 2. If the player was not running and screen was off, do nothing. 
		 * 3. If the player was running and onPause Called making player off, we need to resume it. 
		 * 4. If the player was not running and onPause Called, do nothing. 
		 * 5. If the player was running and Screen was Off, on screen on (by any other process), stop the player.
		 */
		protected void onResume()
		{
			/*
			Log.i("English", "onResume");
			super.onResume();
		if(ChalisaService.playerFlag == 0)
			{
			if((Prefs.getMusicOff(getApplicationContext(), Apputils.PAUSE_MUSIC))==off)
			{
				btnPlay.setBackgroundResource(R.drawable.music);
//				ChalisaService.playerFlag = 0;
				Log.i("English", "onResume if");
//				UpdateSongTime.run();
			}
		}
			else if(ChalisaService.playerFlag == 1)
			{
				if((Prefs.getMusicOff(getApplicationContext(), Apputils.PLAY_MUSIC))==on)
				{
					mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
					Log.i("English", "onResume else");
					btnPlay.setBackgroundResource(R.drawable.pause);
//					ChalisaService.playerFlag = 1;
					UpdateSongTime.run();
					Log.i("English", "onResume else");
				}
			}  */                  //else			super.onResume();
			Log.i("English", "onResume");
			super.onResume();
			if(ChalisaService.playerFlag == 0)
			{
				if((Prefs.getMusicOff(getApplicationContext(), Apputils.PAUSE_MUSIC))==off)
				{
					btnPlay.setBackgroundResource(R.drawable.music);
					ChalisaService.playerFlag = 0;
					Log.i("English", "onResume if");
					UpdateSongTime.run();
				}


			}//if
			else if(ChalisaService.playerFlag == 1)
			{
				if((Prefs.getMusicOff(getApplicationContext(), Apputils.PLAY_MUSIC))==on)
				{
					Log.i("English", "onResume else");
					btnPlay.setBackgroundResource(R.drawable.pause);
					ChalisaService.playerFlag = 1;
					mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
					UpdateSongTime.run();

				}


			}//else			super.onResume();

	    }//onResume

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	/*protected void onDestroy()
            {
                super.onDestroy();
                unregisterReceiver(mUpdateUIReceiver);
            }*/
		private final BroadcastReceiver mUpdateUIReceiver = new BroadcastReceiver() 
	     {
			@Override
	 		public void onReceive(Context context, Intent intent) 
	    	{
	 			int playerFlagValue = intent.getIntExtra("Player_FLAG_VALUE", 0);
	 			if(playerFlagValue == 0)
	 			{
	 				/*txtPlay.setText("Play");
					txtPlay.setTextColor(getResources().getColor(R.color.white));*/
					btnPlay.setBackgroundResource(R.drawable.music);
	 			}//if
	 			else
	 			{
	 				/*txtPlay.setText("Pause");
					txtPlay.setTextColor(getResources().getColor(R.color.redwine));
*/					btnPlay.setBackgroundResource(R.drawable.pause);

	 			}//else
	 		}
	 	};

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if(fromUser){
            if (mediaPlayer!=null) {
				mediaPlayer.seekTo(progress);
				tvMusicProgres.setText(milliSecondsToTimer(mediaPlayer.getCurrentPosition()));
				mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
				UpdateSongTime.run();
			}
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		mHandler.removeCallbacks(UpdateSongTime);
		audioManager.abandonAudioFocus(this);

	}

	public String milliSecondsToTimer(long milliseconds){
		String finalTimerString = "";
		String secondsString = "";

		// Convert total duration into time
		int hours = (int)( milliseconds / (1000*60*60));
		int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
		int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
		// Add hours if there
		if(hours > 0){
			finalTimerString = hours + ":";
		}

		// Prepending 0 to seconds if it is one digit
		if(seconds < 10){
			secondsString = "0" + seconds;
		}else{
			secondsString = "" + seconds;}

		finalTimerString = finalTimerString + minutes + ":" + secondsString;

		// return timer string
		return finalTimerString;
	}

	public void initializeMedia(){
		if(mediaPlayer==null) {
			mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.chalisa);
			mediaPlayer.setVolume(100, 100);
			mSeekBar.setMax(mediaPlayer.getDuration());
//			UpdateSongTime.run();
			/*startService(new Intent(Apputils.PAUSE_MUSIC)
					.putExtra("AUDIO_PAUSE_REASON",
							"user"));
			*/

			/*in.setPackage("com.dzo.HanumanChalisaWithAudioAndAlarm");
			in.setAction(Apputils.PLAY_MUSIC).putExtra("AUDIO_PAUSE_REASON","user");

			startService(in);*/
			in.setAction(Apputils.PLAY_MUSIC);
			startService(in);
			ChalisaService.playerFlag = 1;
			Prefs.setMusicOff(getApplicationContext(),
					Apputils.PAUSE_MUSIC, 0);
			Log.i("Abhishek", "serviceoff");
						/*txtPlay.setText("Pause");
						txtPlay.setTextColor(getResources().getColor(R.color.redwine));*/
			btnPlay.setBackgroundResource(R.drawable.pause);

//                        tvMaxDuration.setText(mediaPlayer.getDuration());
//                                playCycle();
			UpdateSongTime.run();

		}else{
			mSeekBar.setMax(mediaPlayer.getDuration());
			mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
			UpdateSongTime.run();
		}
	}

	private Runnable UpdateSongTime = new Runnable() {
		@Override
		public void run() {
			int startTime=0;

			if(mediaPlayer!=null) {
				try{

					startTime = mediaPlayer.getCurrentPosition();
					tvMaxDuration.setText(milliSecondsToTimer(mediaPlayer.getDuration()));
					tvMusicProgres.setText(milliSecondsToTimer(startTime));
					mSeekBar.setProgress((int) startTime);
				}catch (Exception e){
					mediaPlayer= MediaPlayer.create(Play.this, R.raw.chalisa); // Audiofile in raw folder
					mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

					mediaPlayer.setLooping(true);  // for repeat song

					in.setAction(Apputils.PLAY_MUSIC);
					startService(in);
					ChalisaService.playerFlag = 1;
					Prefs.setMusicOff(getApplicationContext(),
							Apputils.PAUSE_MUSIC, 0);				}

				mHandler.postDelayed(this, 100);
			}
		}
	};



	@Override
	public void onAudioFocusChange(int focusChange) {
		if(focusChange<=0) {
			//LOSS -> PAUSE
			SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

			if(sharedPreferences.getBoolean("fromActivity",false)){



			}else{
				mediaPlayer.pause();
                ChalisaService.playerFlag=0;
                btnPlay.setBackgroundResource(R.drawable.music);
			}
		} else {
			//GAIN -> PLAY
			mediaPlayer.start();
            ChalisaService.playerFlag=1;
            btnPlay.setBackgroundResource(R.drawable.pause);

		}
	}

}

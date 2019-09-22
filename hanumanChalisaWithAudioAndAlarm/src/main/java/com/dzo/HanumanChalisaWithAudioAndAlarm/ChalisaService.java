package com.dzo.HanumanChalisaWithAudioAndAlarm;
import com.dzo.HanumanChalisaWithAudioAndAlarm.util.Apputils;
import com.dzo.HanumanChalisaWithAudioAndAlarm.util.Prefs;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;


public class ChalisaService extends Service implements OnCompletionListener
{
	public static MediaPlayer mediaPlayer;
	public static int playerFlag = 0;
	AudioManager audioMgr;
	int off=1;
	int on=0;
	private boolean shouldPlayAllAartis = false;
	/**
	 * 0 for stop/pause
	 * 1 for play*/
	enum State {
		Retrieving, // the MediaRetriever is retrieving music
		Stopped, // media player is stopped and not prepared to play
		Preparing, // media player is preparing...
		Playing, // playback active (media player ready!). (but the media player
					// may actually be
					// paused in this state if we don't have audio focus. But we
					// stay in this state
					// so that we know we have to resume playback once we get
					// focus back)
		Paused // playback paused (media player ready!)
	};

	enum PauseReason {
		PausedByUser, PhoneCall
	};

	State mState;

	PauseReason mPauseReason;
	@Override
	public IBinder onBind(Intent intent) 
	{
		return null;
	}//onBind
	
	@Override
	public void onCreate() 
	{
		super.onCreate();
		mState = State.Retrieving;

		mPauseReason = PauseReason.PausedByUser;
		
		mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.chalisa);
		mediaPlayer.setVolume(100, 100);
		mediaPlayer.setOnCompletionListener(this);
		audioMgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		registerReceiver(CallStateReceiver, new IntentFilter("android.intent.action.PHONE_STATE"));
		//Log.v("Chalisa service onCreate", "onCreate called");
	}//onCreate
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		if(!mediaPlayer.isPlaying())
		{
			mediaPlayer.start();
			playerFlag = 1;
		}//if
		startForeground(0, null);
		//Log.v("Chalisa service onStartCommand", "onStartCommand called");
		return START_NOT_STICKY;
	}//onStartCommand
	
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		if (mediaPlayer.isPlaying()) 
		{
		      mediaPlayer.stop();
		      playerFlag = 0;
		}//if
		unregisterReceiver(CallStateReceiver);
		mediaPlayer.release();
		//Log.v("Chalisa service onDestroy", "onDestroy called");
	}//onDestroy
	
	public void onCompletion(MediaPlayer mp) 
	{
		this.stopSelf();
		playerFlag = 0;
		updateUI();
		//Log.v("Chalisa Service media player", "on completion listener called");
	}
	
	private void updateUI() 
	{
		Intent in = new Intent("com.dzo.HanumanChalisaWithAudioAndAlarm.UPDATE_UI");
        in.putExtra("Player_FLAG_VALUE", playerFlag);
        getApplicationContext().sendBroadcast(in);
	}
	
	
	
	public final BroadcastReceiver 	CallStateReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			String action = intent.getAction();
	        if(action.equalsIgnoreCase("android.intent.action.PHONE_STATE"))
	        {
	            if (intent.getStringExtra(TelephonyManager.EXTRA_STATE)
	            		.equals(TelephonyManager.EXTRA_STATE_RINGING))
	            {
	            	if(mediaPlayer.isPlaying())
	            	{
	            		mediaPlayer.pause();
	            		playerFlag = 0;
	            	}//if
	            	
	            	audioMgr.setStreamMute(AudioManager.STREAM_ALARM, true);
	            	//Log.v("Chalisa Service call state ringing", "call state ringing");
	            }//if
	            else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE)
		        		.equals(TelephonyManager.EXTRA_STATE_IDLE))
		        {
	            	if (!mediaPlayer.isPlaying())
	            	{
	            		if((Prefs.getMusicOff(getApplicationContext(), Apputils.PAUSE_MUSIC))==off)
	            		{
	            			mediaPlayer.pause();
	            			playerFlag = 0;
	            	//		Prefs.setMusicOff(getApplicationContext(), Apputils.PAUSE_MUSIC, 1);
	            		}
	            		else if((Prefs.getMusicOff(getApplicationContext(), Apputils.PLAY_MUSIC))==on) 
	            		{
	            			mediaPlayer.start();
	            		//	playerFlag = 1;
	            			Prefs.setMusicOff(getApplicationContext(), Apputils.PAUSE_MUSIC, 2);
	            			
	            		}
	            	}
		        	audioMgr.setStreamMute(AudioManager.STREAM_ALARM, false);
		        	//Log.v("Chalisa Service call state idle", "call state idle");
		       
		        }//if
		        else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE)
		        		.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) 
		        {
		        	if(mediaPlayer.isPlaying())
	            	{
	            		mediaPlayer.pause();
	            		playerFlag = 0;	
	            	}//if
		        	
		        	audioMgr.setStreamMute(AudioManager.STREAM_ALARM, true);
		        	//Log.v("Chalisa Service call state offhook", "call state offhook");
		        }//if
	        }//if             
		}//onReceive
	};
}//ChalisaService
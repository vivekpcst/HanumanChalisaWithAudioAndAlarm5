package com.dzo.HanumanChalisaWithAudioAndAlarm.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public final class Prefs 
{
    private static SharedPreferences mPrefs;
    
    
    public static int getMusicOff(Context context,String key)
    {
    	mPrefs=PreferenceManager.getDefaultSharedPreferences(context);
    	return mPrefs.getInt(key, 0);
    }
    
    public static void setMusicOff(Context context,String key,int value)
    {
    	mPrefs=PreferenceManager.getDefaultSharedPreferences(context);
    	SharedPreferences.Editor editor=mPrefs.edit();
    	editor.putInt(key, value);
    	editor.apply();
    }

	public static Boolean getBoolean(Context context,String key) 
	{
		mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		return mPrefs.getBoolean(key, false);
	}//getBoolean

	public static void setBoolean(Context context,String key, boolean value) 
	{
		mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putBoolean(key, value);
		editor.apply();
	}//setBoolean
	public static String getGCMDeviceRegistrationID(Context ctx, String key) {
		mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		return mPrefs.getString(key, "");
	}
	public static void saveGcmDeviceRegistrationID(Context ctx, String key,
			String value) {
		mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		Editor mPrefsEditor = mPrefs.edit();
		mPrefsEditor.putString(key, value);
		mPrefsEditor.apply();
	}
	public static int getAppVersion(Context ctx, String key) {
		mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		return mPrefs.getInt(key, Integer.MIN_VALUE);
	}

	public static void saveAppVersion(Context ctx, String key, int appVersion) {
		mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		Editor mPrefsEditor = mPrefs.edit();
		mPrefsEditor.putInt(key, appVersion);
		mPrefsEditor.apply();
	}
}

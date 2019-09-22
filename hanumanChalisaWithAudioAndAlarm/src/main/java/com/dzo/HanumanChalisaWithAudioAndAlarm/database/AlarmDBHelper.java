/**
 * Nitish Kumar 07 Dec, 2012
 * 
 * Database to store details about alarm
 * */
package com.dzo.HanumanChalisaWithAudioAndAlarm.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dzo.HanumanChalisaWithAudioAndAlarm.AlarmComparator;
import com.dzo.HanumanChalisaWithAudioAndAlarm.dao.AlarmTimeDAO;

public class AlarmDBHelper extends SQLiteOpenHelper
{
	private static final int DATABASE_VERSION = 2;
	
	private static final String DATABASE_NAME = "HanuAlarmDB";

	private static final String TABLE_ALARM = "tbl_alarm";
	
	private static final String KEY_ID = "id";
	private static final String KEY_ALARM_TIME = "alarm_time";
	private static final String KEY_REPEAT_STATUS = "repeat_status";
	private static final String KEY_SUNDAY_ID = "sunday_id";
	private static final String KEY_MONDAY_ID = "monday_id";
	private static final String KEY_TUESDAY_ID = "tuesday_id";
	private static final String KEY_WEDNESDAY_ID = "wednesday_id";
	private static final String KEY_THURSDAY_ID = "thursday_id";
	private static final String KEY_FRIDAY_ID = "friday_id";
	private static final String KEY_SATURDAY_ID = "saturday_id";
	
	public AlarmDBHelper(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}//Constructor
	
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		String CREATE_ALARM_TABLE = "CREATE TABLE " + TABLE_ALARM + "("
				+ KEY_ID + " INTEGER PRIMARY KEY, " + KEY_ALARM_TIME + " TEXT, "
				+ KEY_REPEAT_STATUS + " TEXT, "+ KEY_SUNDAY_ID + " TEXT, "
				+ KEY_MONDAY_ID + " TEXT, "+ KEY_TUESDAY_ID + " TEXT, "
				+ KEY_WEDNESDAY_ID + " TEXT, "+ KEY_THURSDAY_ID + " TEXT, "
				+ KEY_FRIDAY_ID + " TEXT, "+ KEY_SATURDAY_ID + " TEXT"+")";
		db.execSQL(CREATE_ALARM_TABLE);
	}//onCreate
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARM);
		onCreate(db);
	}//onUpgrade
	
	public void addAlarmTime(AlarmTimeDAO alarmDAO) 
	{
		SQLiteDatabase sqliteDB = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(KEY_ID, alarmDAO.getId());
		Log.v("HCAlarmTimeAdapter", ""+alarmDAO.getId());
		values.put(KEY_ALARM_TIME, alarmDAO.getAlarmTime());
		Log.v("HCAlarmTimeAdapter", alarmDAO.getAlarmTime());
		values.put(KEY_REPEAT_STATUS, alarmDAO.getDaysToRepeatAlarm());
		Log.v("HCAlarmTimeAdapter", alarmDAO.getDaysToRepeatAlarm());
		sqliteDB.insert(TABLE_ALARM, null, values);
		sqliteDB.close();
	}//addAlarmTime
	
	public List<AlarmTimeDAO> getAllAlarmTimeResults()
	{
		List<AlarmTimeDAO> alarmTimeList = new ArrayList<AlarmTimeDAO>();
		
		String selectQuery = "SELECT  * FROM " + TABLE_ALARM;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) 
		{
			do 
			{
				AlarmTimeDAO alarmDao = new AlarmTimeDAO();
				alarmDao.setId(cursor.getInt(0));
				alarmDao.setAlarmTime(cursor.getString(1));
				alarmDao.setDaysToRepeatAlarm(cursor.getString(2));
				alarmDao.setSundayRepeatId(cursor.getString(3));
				alarmDao.setMondayRepeatId(cursor.getString(4));
				alarmDao.setTuesdayRepeatId(cursor.getString(5));
				alarmDao.setWednesdayRepeatId(cursor.getString(6));
				alarmDao.setThursdayRepeatId(cursor.getString(7));
				alarmDao.setFridayRepeatId(cursor.getString(8));
				alarmDao.setSaturdayRepeatId(cursor.getString(9));
				alarmTimeList.add(alarmDao);
			}//do 
			while (cursor.moveToNext());
		}//if
		cursor.close();
		db.close();
		return alarmTimeList;
	}//getAllAlarmTimeResults
	
	public void updateAlarmTime(AlarmTimeDAO alarmDao) 
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ALARM_TIME, alarmDao.getAlarmTime());
		values.put(KEY_REPEAT_STATUS, alarmDao.getDaysToRepeatAlarm());
		values.put(KEY_SUNDAY_ID, alarmDao.getSundayRepeatId());
		values.put(KEY_MONDAY_ID, alarmDao.getMondayRepeatId());
		values.put(KEY_TUESDAY_ID, alarmDao.getTuesdayRepeatId());
		values.put(KEY_WEDNESDAY_ID, alarmDao.getWednesdayRepeatId());
		values.put(KEY_THURSDAY_ID, alarmDao.getThursdayRepeatId());
		values.put(KEY_FRIDAY_ID, alarmDao.getFridayRepeatId());
		values.put(KEY_SATURDAY_ID, alarmDao.getSaturdayRepeatId());
		
		db.update(TABLE_ALARM, values, KEY_ID + " = ?",
				new String[] { String.valueOf(alarmDao.getId())});
		db.close();
	}//updateAlarmTime

	public void deleteAlarm(AlarmTimeDAO alarmDao) 
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ALARM, KEY_ID + " = ?",
				new String[] { String.valueOf(alarmDao.getId()) });
		db.close();
	}//deleteAlarm
}//AlarmDBHelper

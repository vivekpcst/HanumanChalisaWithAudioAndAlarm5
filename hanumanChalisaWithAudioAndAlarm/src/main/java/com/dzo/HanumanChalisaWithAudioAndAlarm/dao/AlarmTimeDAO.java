/**
 * Nitish Kumar 06 Dec, 2012
 * 
 * DAO to set data in database/
 * hold data fetched from database
 * */

package com.dzo.HanumanChalisaWithAudioAndAlarm.dao;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class AlarmTimeDAO implements Serializable
{
	private static final long serialVersionUID = 8146651089510731076L;
	private int id;
	private String alarmTime;
	private String daysToRepeatAlarm;
	private String sundayRepeatId;
	private String mondayRepeatId;
	private String tuesdayRepeatId;
	private String wednesdayRepeatId;
	private String thursdayRepeatId;
	private String fridayRepeatId;
	private String saturdayRepeatId;
	private String period;
	
	public String getSundayRepeatId() 
	{
		return sundayRepeatId;
	}//getSundayRepeatId

	public void setSundayRepeatId(String sundayRepeatId) 
	{
		this.sundayRepeatId = sundayRepeatId;
	}//setSundayRepeatId

	public String getMondayRepeatId() 
	{
		return mondayRepeatId;
	}//getMondayRepeatId

	public void setMondayRepeatId(String mondayRepeatId) 
	{
		this.mondayRepeatId = mondayRepeatId;
	}//setMondayRepeatId

	public String getTuesdayRepeatId() 
	{
		return tuesdayRepeatId;
	}//getTuesdayRepeatId

	public void setTuesdayRepeatId(String tuesdayRepeatId) 
	{
		this.tuesdayRepeatId = tuesdayRepeatId;
	}//setTuesdayRepeatId

	public String getWednesdayRepeatId() 
	{
		return wednesdayRepeatId;
	}//getWednesdayRepeatId

	public void setWednesdayRepeatId(String wednesdayRepeatId) 
	{
		this.wednesdayRepeatId = wednesdayRepeatId;
	}//setWednesdayRepeatId

	public String getThursdayRepeatId() 
	{
		return thursdayRepeatId;
	}//getThursdayRepeatId

	public void setThursdayRepeatId(String thursdayRepeatId) 
	{
		this.thursdayRepeatId = thursdayRepeatId;
	}//setThursdayRepeatId

	public String getFridayRepeatId() 
	{
		return fridayRepeatId;
	}//getFridayRepeatId

	public void setFridayRepeatId(String fridayRepeatId) 
	{
		this.fridayRepeatId = fridayRepeatId;
	}//setFridayRepeatId

	public String getSaturdayRepeatId() 
	{
		return saturdayRepeatId;
	}//getSaturdayRepeatId

	public void setSaturdayRepeatId(String saturdayRepeatId) 
	{
		this.saturdayRepeatId = saturdayRepeatId;
	}//setSaturdayRepeatId

	public String getDaysToRepeatAlarm() 
	{
		return daysToRepeatAlarm;
	}//getDaysToRepeatAlarm

	public void setDaysToRepeatAlarm(String daysToRepeatAlarm) 
	{
		this.daysToRepeatAlarm = daysToRepeatAlarm;
	}//setDaysToRepeatAlarm

	public int getId() 
	{
		return id;
	}//getId

	public void setId(int id) 
	{
		this.id = id;
	}//setId

	public String getAlarmTime() 
	{
		String string[]=alarmTime.split(" ");
		period=string[1];
		return alarmTime;
	}//getAlarmTime
	
	public void setAlarmTime(String alarmTime) 
	{
		this.alarmTime = alarmTime;
	}//setAlarmTime


	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period){
		this.period=period;
	}


}//AlarmTimeDAO

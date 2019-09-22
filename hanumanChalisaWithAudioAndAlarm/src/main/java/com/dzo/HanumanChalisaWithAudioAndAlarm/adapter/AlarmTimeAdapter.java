package com.dzo.HanumanChalisaWithAudioAndAlarm.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dzo.HanumanChalisaWithAudioAndAlarm.R;
import com.dzo.HanumanChalisaWithAudioAndAlarm.dao.AlarmTimeDAO;

public class AlarmTimeAdapter extends BaseAdapter 
{
	private List<AlarmTimeDAO> alarmTimeList;
	Activity act;
	ViewHolder holder;
	boolean alarmEnabled;
	int pos;
	String time[];

	public AlarmTimeAdapter(Activity act, List<AlarmTimeDAO> alarmTimeList, boolean alarmEnabled)
	{
		this.act = act;
		this.alarmTimeList = alarmTimeList;
		this.alarmEnabled = alarmEnabled;
//		time=new String[alarmTimeList.size()];
//		for(int i=0;i<alarmTimeList.size();i++){
//			time[i]=alarmTimeList.get(i).getAlarmTime();
//		}
//		Arrays.sort(new List[]{alarmTimeList});
//		System.out.printf("Modified arr[] : %s", Arrays.toString(new List[]{alarmTimeList}));
	}//Constructor
	
	public int getCount() 
	{
		return alarmTimeList.size();
	}//getCount

	public Object getItem(int arg0) 
	{
		return alarmTimeList.get(arg0);
	}//getItem

	public long getItemId(int position) 
	{
		return 0;
	}//getItemId

	public View getView(int position, View convertView, ViewGroup parent) 
	{ 
		pos = position;
		LayoutInflater li = act.getLayoutInflater();
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = li.inflate(R.layout.alarm_adapter, null);
			holder.txtAlarmTime = (TextView)convertView.findViewById(R.id.txtAlarmTime);
			holder.imgAlarmEnabled = (ImageView)convertView.findViewById(R.id.imgAlarmEnabled);
			convertView.setTag(holder);
		}//if
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}//else
		
		holder.txtAlarmTime.setText(alarmTimeList.get(pos).getAlarmTime());
//		holder.txtAlarmTime.setText(time[pos]);
		
		/*holder.imgAlarmEnabled.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				if(alarmEnabled == true)
				{
					alarmEnabled = false;
					disableAllAlarm(Integer.parseInt(alarmTimeList.get(pos).getId()),
								alarmTimeList.get(pos).getSundayRepeatId(), 
								alarmTimeList.get(pos).getMondayRepeatId(), 
								alarmTimeList.get(pos).getTuesdayRepeatId(), 
								alarmTimeList.get(pos).getWednesdayRepeatId(), 
								alarmTimeList.get(pos).getThursdayRepeatId(), 
								alarmTimeList.get(pos).getFridayRepeatId(), 
								alarmTimeList.get(pos).getSaturdayRepeatId());
					holder.imgAlarmEnabled.setImageResource(R.drawable.alarm_disabled);
					Log.v("AlarmTimeAdapter onImageClick", "Alarm disabled");
				}//if
				else
				{
					alarmEnabled = true;
					SetAlarmOnReboot setAlarm = new SetAlarmOnReboot(act);
					setAlarm.setAlarmAfterReboot(alarmTimeList.get(pos).getId(), 
							alarmTimeList.get(pos).getAlarmTime(), 
							alarmTimeList.get(pos).getDaysToRepeatAlarm(), 
							alarmTimeList.get(pos).getSundayRepeatId(), 
							alarmTimeList.get(pos).getMondayRepeatId(), 
							alarmTimeList.get(pos).getTuesdayRepeatId(),
							alarmTimeList.get(pos).getWednesdayRepeatId(),
							alarmTimeList.get(pos).getThursdayRepeatId(),
							alarmTimeList.get(pos).getFridayRepeatId(),
							alarmTimeList.get(pos).getSaturdayRepeatId());
					holder.imgAlarmEnabled.setImageResource(R.drawable.alarm_enabled);
					Log.v("AlarmTimeAdapter onImageClick", "Alarm enabled");
				}//else
			}//onClick
		});*/
		return convertView;
	}//getView
	
	static class ViewHolder
	{
		TextView txtAlarmTime;
		ImageView imgAlarmEnabled;
	}//ViewHolder

	/*private void disableAllAlarm(int alarmId, String sundayRepeatId, String mondayRepeatId, 
			String tuesdayRepeatId, String wednesdayRepeatId, String thursdayRepeatId, 
			String fridayRepeatId, String saturdayRepeatId) 
	{
		//Build Intent/Pending Intent for canceling the alarm
    	alarmEnabled = false;
    	
		dismissAlarm(alarmId);
    	
    	if(sundayRepeatId != null && !sundayRepeatId.equals("") 
    			&& !sundayRepeatId.equals("null"))
    	{
    		dismissAlarm(Integer.parseInt(sundayRepeatId));
    	}//if
    	
    	if(mondayRepeatId != null && !mondayRepeatId.equals("") 
    			&& !mondayRepeatId.equals("null"))
    	{
    		dismissAlarm(Integer.parseInt(mondayRepeatId));
    	}//if
    	
    	if(tuesdayRepeatId != null && !tuesdayRepeatId.equals("") 
    			&& !tuesdayRepeatId.equals("null"))
    	{
    		dismissAlarm(Integer.parseInt(tuesdayRepeatId));
    	}//if
    	
    	if(wednesdayRepeatId != null && !wednesdayRepeatId.equals("") 
    			&& !wednesdayRepeatId.equals("null"))
    	{
    		dismissAlarm(Integer.parseInt(wednesdayRepeatId));
    	}//if
    	
    	if(thursdayRepeatId != null && !thursdayRepeatId.equals("") 
    			&& !thursdayRepeatId.equals("null"))
    	{
    		dismissAlarm(Integer.parseInt(thursdayRepeatId));
    	}//if
    	
    	if(fridayRepeatId != null && !fridayRepeatId.equals("") 
    			&& !fridayRepeatId.equals("null"))
    	{
    		dismissAlarm(Integer.parseInt(fridayRepeatId));
    	}//if
    	
    	if(saturdayRepeatId != null && !saturdayRepeatId.equals("") 
    			&& !saturdayRepeatId.equals("null"))
    	{
    		dismissAlarm(Integer.parseInt(saturdayRepeatId));
    	}//if
	}//disableAlarm*/
	
	/*private void dismissAlarm(int alarmRequestCode) 
	{
		Intent AlarmIntent = new Intent(act, AlarmReceiver.class);
		AlarmIntent.putExtra("REQUEST CODE", alarmRequestCode);
		AlarmManager AlmMgr = (AlarmManager)act.getSystemService(Context.ALARM_SERVICE);
    	PendingIntent Sender = PendingIntent.getBroadcast(act, alarmRequestCode, AlarmIntent, 0);  
    	AlmMgr.cancel(Sender);
	}//dismissAlarm*/
}//AlarmTimeAdapter
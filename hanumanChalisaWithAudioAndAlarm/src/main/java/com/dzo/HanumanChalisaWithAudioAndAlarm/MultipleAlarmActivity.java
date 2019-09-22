package com.dzo.HanumanChalisaWithAudioAndAlarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.dzo.HanumanChalisaWithAudioAndAlarm.adapter.AlarmTimeAdapter;
import com.dzo.HanumanChalisaWithAudioAndAlarm.dao.AlarmTimeDAO;
import com.dzo.HanumanChalisaWithAudioAndAlarm.database.AlarmDBHelper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MultipleAlarmActivity extends BottomNav implements OnItemClickListener
{
	ImageButton btnSetAlarm;
	static final int TIME_DIALOG_ID = 0;
	ListView listViewAlarmTime;
	public static final String AMPM = "AMPM";
	String strAmPM;
	int reqCode;
	AlarmDBHelper alarmDBHelper;
	boolean alarmEnabled;
	public static List<AlarmTimeDAO> alarmDAOList;
	Button btnBack;

    @Override
    public int getContentViewId() {
        return R.layout.alarm_set;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.navigation_alarm;
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


		getLayoutInflater().inflate(R.layout.alarm_set, content_frame);


//        setContentView(R.layout.activity_multiple_alarm);
        alarmDBHelper = new AlarmDBHelper(MultipleAlarmActivity.this);
        alarmEnabled = getIntent().getBooleanExtra("ALARM_ENABLED", false);
        
        Log.v("HanumanChalisaMultiple", ""+alarmEnabled);
        
        alarmDAOList = alarmDBHelper.getAllAlarmTimeResults();
        Log.v("HanumanChalisaMultiple", ""+alarmDAOList.size());
        
        listViewAlarmTime = (ListView)findViewById(R.id.listAlarmTime);
//        getSupportActionBar().setBackgroundDrawable(
//                getResources().getDrawable(R.drawable.header));
        
       /* btnBack = (Button)findViewById(R.id.btnBack);*/
        
        if(alarmDAOList != null)
        {
            Collections.sort(alarmDAOList,new AlarmComparator());
        	AlarmTimeAdapter alarmAdapter = new AlarmTimeAdapter(MultipleAlarmActivity.this,
    				alarmDAOList, alarmEnabled);
        	alarmAdapter.notifyDataSetChanged();
        	listViewAlarmTime.setAdapter(alarmAdapter);
        }//if
        
        /*btnBack.setOnClickListener(new View.OnClickListener() 
        {
			public void onClick(View v) 
			{
				Intent in = new Intent(MultipleAlarmActivity.this, HanuAlarm.class);
				startActivity(in);
				finish();
			}//onClick
		});*/
        
        btnSetAlarm = (ImageButton)findViewById(R.id.btnSetAlarm);
		TextView textView2=findViewById(R.id.textView2);
		textView2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent setAlarmIntent = new Intent(MultipleAlarmActivity.this, SetAlarmActivity.class);
				setAlarmIntent.putExtra("FROM_CLASS", "MultipleAlarmActivity");
				startActivity(setAlarmIntent);
				finish();
				Log.v("HanumanChalisaMultiple", "onClick called");
			}
		});
        btnSetAlarm.setOnClickListener(new View.OnClickListener()
        {
			public void onClick(View v) 
			{
				Intent setAlarmIntent = new Intent(MultipleAlarmActivity.this, SetAlarmActivity.class);
				setAlarmIntent.putExtra("FROM_CLASS", "MultipleAlarmActivity");
				startActivity(setAlarmIntent);
				finish();
				Log.v("HanumanChalisaMultiple", "onClick called");
			}//onClick
		});
        listViewAlarmTime.setOnItemClickListener(this);
        Log.v("HCMultipleAlarmActivity", "onCreate called");
    }//onCreate
	
	public void onBackPressed() 
	{
		/*Intent in = new Intent(MultipleAlarmActivity.this, English.class);
		startActivity(in);*/
		super.onBackPressed();
		finish();
		Log.v("HanumanChalisaMultiple", "onbackpressed called");
	}//onBackPressed
	
    public void onItemClick(AdapterView<?> parent, View v, int pos, long id) 
	{
		handleClick(pos);
	}//onItemClick
    
    private void handleClick(int pos)
    {
		Intent alarmDetailIntent = new Intent(MultipleAlarmActivity.this, AlarmTimeDetail.class);
		alarmDetailIntent.putExtra("AlarmDAOToEdit", alarmDAOList.get(pos));
		alarmDetailIntent.putExtra("AlarmDAOPosition", pos);
		alarmDetailIntent.putExtra("ALARM_ENABLED", alarmEnabled);
		startActivity(alarmDetailIntent);
		finish();
	}//handleClick

    protected void onDestroy() 
    {
    	super.onDestroy();
    	if(alarmDBHelper != null)
    	{
    		alarmDBHelper.close();
    	}//if
    	Log.v("HanumanChalisaMultiple", "onDestroy called");
    }//onDestroy
}//MultipleAlarmActivity

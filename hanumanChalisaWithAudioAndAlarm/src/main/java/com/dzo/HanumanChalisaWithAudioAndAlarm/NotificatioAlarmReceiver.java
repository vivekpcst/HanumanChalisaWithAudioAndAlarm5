package com.dzo.HanumanChalisaWithAudioAndAlarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Vivek Vsking on 4/26/2019  2:54 PM
 * E-mail :- vivekpcst.kumar@gmail.com
 * Mobile :- +91-7982591863
 */
public class NotificatioAlarmReceiver extends BroadcastReceiver {
Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        setNotificationReminder();
        notifyUser();


    }

    private void notifyUser() {
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
            c.moveToNext();
        }
        c.close();
//		String query = "SELECT * FROM EventList WHERE event_date='"+formattedDate+"'";
        formattedDate="2019-10-02";
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



    private void setNotificationReminder() {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, NotificatioAlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmIntent.setData((Uri.parse("custom://"+System.currentTimeMillis())));
        alarmManager.cancel(pendingIntent);

        Calendar alarmStartTime = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        alarmStartTime.set(Calendar.HOUR_OF_DAY, 8);
        alarmStartTime.set(Calendar.MINUTE, 00);
        alarmStartTime.set(Calendar.SECOND, 0);
        if (now.after(alarmStartTime)) {
            Log.d("Hey","Added a day");
            alarmStartTime.add(Calendar.DAY_OF_MONTH, 1);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,alarmStartTime.getTimeInMillis(),pendingIntent);
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,alarmStartTime.getTimeInMillis(),pendingIntent);
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


}

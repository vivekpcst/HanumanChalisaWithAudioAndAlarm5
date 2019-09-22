package com.dzo.HanumanChalisaWithAudioAndAlarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;


import java.util.Calendar;
import java.util.Locale;

public abstract class BottomNav extends AppCompatActivity{




    public FrameLayout content_frame;
    BottomNavigationView navigation;
    Locale locale;
    public abstract int getContentViewId();
    public abstract int getNavigationMenuItemId();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                /*case R.id.navigation_home: {
                    SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putBoolean("fromActivity",true);
                    editor.apply();
                    Intent intent = new Intent(BottomNav.this, Home.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    break;
                }*/
                case R.id.navigation_play: {
                    SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putBoolean("fromActivity",true);
                    editor.apply();
                    Intent intent1 = new Intent(BottomNav.this, Play.class);
                    startActivity(intent1);
                    overridePendingTransition(0, 0);
                    break;
                }
                case R.id.navigation_info: {
                    SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putBoolean("fromActivity",true);
                    editor.apply();
                    Intent intent2 = new Intent(BottomNav.this, About.class);
                    startActivity(intent2);
                    overridePendingTransition(0, 0);
                    break;
                }
                case R.id.nav_read_lang: {
                    SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putBoolean("fromActivity",true);
                    editor.apply();
                    String s=getLocalClassName();
                    if(s.contains("English")) {
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "en").apply();
//                        setLangRecreate("en");
                        Intent intent3 = new Intent(BottomNav.this, StartManualActivity.class);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent3);
                        overridePendingTransition(0, 0);
                    }else if(s.contains("StartManualActivity")){
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "hi").apply();
//                        setLangRecreate("hi");
                        Intent intent3 = new Intent(BottomNav.this, English.class);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent3);
                        overridePendingTransition(0, 0);

                    }else {
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "hi").apply();
//                        setLangRecreate("hi");
                        Intent intent3 = new Intent(BottomNav.this, English.class);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent3);
                        overridePendingTransition(0, 0);
                    }
                    break;
                }
                case R.id.navigation_alarm: {
                    SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putBoolean("fromActivity",true);
                    editor.apply();
                    Intent setAlarmIntent = new Intent(BottomNav.this, MultipleAlarmActivity.class);
                    setAlarmIntent.putExtra("FROM_CLASS", "HanuAlarm");
                    startActivity(setAlarmIntent);
                    overridePendingTransition(0, 0);
                    break;
                }
                default:
                    return false;
            }
            return true;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);
        content_frame=findViewById(R.id.content_frame);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(null);
//        BottomNavViewHelper.disableAnimation(navigation);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, displayMetrics);
            setMargins(iconView, 0, 0, 0, 0);
            iconView.setLayoutParams(layoutParams);
        }


//        setAlarmNotificationOnSaturday();
//        setAlarmNotificationOnTuesday();

    }




    private void setAlarmNotificationOnSaturday() {
        Calendar   alarmStartTime = Calendar.getInstance();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, NotificatioAlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmIntent.setData((Uri.parse("custom://"+System.currentTimeMillis())));
        alarmManager.cancel(pendingIntent);

        Calendar now = Calendar.getInstance();
        alarmStartTime.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
        alarmStartTime.set(Calendar.HOUR_OF_DAY, 3);
        alarmStartTime.set(Calendar.MINUTE, 45);
        alarmStartTime.set(Calendar.SECOND, 0);
       /* if (now.after(alarmStartTime)) {
            alarmStartTime.add(Calendar.DAY_OF_MONTH, 7);
        }*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,alarmStartTime.getTimeInMillis(),pendingIntent);
        }else {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), 7*24*60*60*1000, pendingIntent);
        }
    }


    private void setAlarmNotificationOnTuesday() {
        Calendar   alarmStartTime = Calendar.getInstance();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, NotificatioAlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmIntent.setData((Uri.parse("custom://"+System.currentTimeMillis())));
        alarmManager.cancel(pendingIntent);

        Calendar now = Calendar.getInstance();
        alarmStartTime.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY);
        alarmStartTime.set(Calendar.HOUR_OF_DAY, 8);
        alarmStartTime.set(Calendar.MINUTE, 00);
        alarmStartTime.set(Calendar.SECOND, 0);
        if (now.after(alarmStartTime)) {
            alarmStartTime.add(Calendar.DAY_OF_MONTH, 7);
        }
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), 7*24*60*60*1000, pendingIntent);

    }

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    private void updateNavigationBarState() {
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    void selectBottomNavigationBarItem(int itemId) {
        Menu menu = navigation.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            boolean shouldBeChecked = item.getItemId() == itemId;
            if (shouldBeChecked) {
                item.setChecked(true);
                item.setEnabled(true);
                item.expandActionView();
                break;
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }
    public void setLangRecreate(String langval) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        recreate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        View view = new View(getApplicationContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(16,
                ViewGroup.LayoutParams.MATCH_PARENT));
        MenuItem space = menu.findItem(R.id.navigation_home);
        space.setActionView(view);
        return true;
    }
}
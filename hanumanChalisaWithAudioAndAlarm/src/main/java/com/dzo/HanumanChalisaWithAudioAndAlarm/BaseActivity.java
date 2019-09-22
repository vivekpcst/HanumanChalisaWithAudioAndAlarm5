package com.dzo.HanumanChalisaWithAudioAndAlarm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActionBarDrawerToggle;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.dzo.HanumanChalisaWithAudioAndAlarm.adapter.HanuDrawerListAdapter;
import com.dzo.HanumanChalisaWithAudioAndAlarm.dao.HanuDrawerItem;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {
	protected DrawerLayout mDrawerLayout;
	protected ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    protected FrameLayout frameLayout;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    protected static int position;
 // Drawer new add
    private static boolean isLaunch = true;
 	//public static String[] mDrawerMenuList;

 	private TypedArray mHanuDrawerIcons;
 	
 	private ArrayList<HanuDrawerItem> mDrawerItem;
 	
 	private String[] mHanuDrawerTitles;
 	 private HanuDrawerListAdapter adapter;

 	// End
    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);

        mTitle = mDrawerTitle = getTitle();
       // mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        frameLayout = (FrameLayout)findViewById(R.id.content_frame);

        mDrawerList.setBackgroundResource(R.drawable.header);
        mHanuDrawerTitles = getResources().getStringArray(R.array.drawerHanuTitles);
		mHanuDrawerIcons=getResources().obtainTypedArray(R.array.drawerHanuIcon);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerItem=new ArrayList<HanuDrawerItem>();
		mDrawerItem.add(new HanuDrawerItem(mHanuDrawerTitles[0],mHanuDrawerIcons.getResourceId(0, -1)));
		mDrawerItem.add(new HanuDrawerItem(mHanuDrawerTitles[1],mHanuDrawerIcons.getResourceId(1, -1)));
		mDrawerItem.add(new HanuDrawerItem(mHanuDrawerTitles[2],mHanuDrawerIcons.getResourceId(2, -1)));
		mDrawerItem.add(new HanuDrawerItem(mHanuDrawerTitles[3],mHanuDrawerIcons.getResourceId(3, -1)));
		mDrawerItem.add(new HanuDrawerItem(mHanuDrawerTitles[4],mHanuDrawerIcons.getResourceId(4, -1)));
		mDrawerItem.add(new HanuDrawerItem(mHanuDrawerTitles[5],mHanuDrawerIcons.getResourceId(5, -1)));
		// set a custom shadow that overlays the main content when the drawer
		// opens
		mHanuDrawerIcons.recycle();
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		adapter = new HanuDrawerListAdapter(getApplicationContext(), mDrawerItem);
		mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
            	 getSupportActionBar().setTitle(mDrawerTitle);
                //getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);


   		if(isLaunch){
   			 /**
   			  *Setting this flag false so that next time it will not open our first activity.
   			  *We have to use this flag because we are using this BaseActivity as parent activity to our other activity.
   			  *In this case this base activity will always be call when any child activity will launch.
   			  */
   			isLaunch = false;
   			//openActivity(0);
   			selectItem(0);
   		}
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Called whenever we call invalidateOptionsMenu() 
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
        case R.id.action_websearch:
            // create intent to perform web search for this planet
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            // catch event that there's no activity to handle intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
            return true;
        default:}
            return super.onOptionsItemSelected(item);
        //}
    }*/









    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Called whenever we call invalidateOptionsMenu() 
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
    	
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
        case R.id.action_websearch:
            // create intent to perform web search for this planet
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            // catch event that there's no activity to handle intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }



    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
    	
    	switch(position)
    	{
    	
    	case 0:
			startActivity(new Intent(this, English.class));
			break;

    	case 1:
    		if (Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
				Intent intent = new Intent(this,
						StartManualActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}// if
			else {
				/*Intent intent = new Intent(HanuAlarm.this, English.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);*/
				Toast.makeText(
						this,
						"Sorry, Devanagari Script is not supported by your device",
						Toast.LENGTH_LONG).show();
			}// else
    		break;
    	case 2:
    		Intent play=new Intent(this,Play.class);
    		startActivity(play);

    		break;
    	case 3:
    		Intent setAlarmIntent = new Intent(this,
					MultipleAlarmActivity.class);
			setAlarmIntent.putExtra("FROM_CLASS", "HanuAlarm");
			startActivity(setAlarmIntent);
			break;
    	case 4:
    		Intent intent = new Intent(this, About.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
    	case 5:
    		Intent calIntent=new Intent(this,FestivalCalendarActivity.class);
    		startActivity(calIntent);
    		break;
    	}
        
        mDrawerList.setItemChecked(position, true);
        setTitle(mHanuDrawerTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    public Intent shareIntent() {
		String mMailSubject = "Dotzoo Inc. - Best in IT Services";
		String mMailMessage = null;
		mMailMessage = "Hi,\n I found this great App. This app has good collections of aarti with text and audio.";
		mMailMessage += "\n";
		mMailMessage += "Go to: https://market.android.com/details?id=com.dzo.aarti";
		mMailMessage += ",\n Please visit: http://www.dotzoo.net to see more about Dotzoo Inc.";

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "" + mMailSubject);
		intent.putExtra(Intent.EXTRA_TEXT, mMailMessage);
		startActivity(Intent.createChooser(intent, "Share via"));
		return intent;
	}

   
}
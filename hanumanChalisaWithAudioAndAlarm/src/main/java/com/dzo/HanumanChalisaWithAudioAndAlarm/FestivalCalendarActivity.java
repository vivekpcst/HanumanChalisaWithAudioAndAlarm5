package com.dzo.HanumanChalisaWithAudioAndAlarm;





import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dzo.HanumanChalisaWithAudioAndAlarm.account.GenericAccountService;
import com.dzo.HanumanChalisaWithAudioAndAlarm.calendarsync.SyncUtils;
import com.dzo.HanumanChalisaWithAudioAndAlarm.contentprovider.FestivalContract;
import com.dzo.HanumanChalisaWithAudioAndAlarm.reminder.CalendarHelper;
import com.dzo.HanumanChalisaWithAudioAndAlarm.reminder.CalendarListDAO;
import com.makeramen.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import uk.co.deanwild.flowtextview.FlowTextView;

public class FestivalCalendarActivity extends AppCompatActivity implements
LoaderManager.LoaderCallbacks<Cursor>, PanelSlideListener   {
	private static final String TAG = "HanumanCalendarActivity";
	private ActionBar mActionBar;
	private CaldroidFragment caldroidFragment;
	
	private Object mSyncObserverHandle;
	private Menu mOptionsMenu;
    private CalendarHelper mCalendarHelper;
	private static final String[] PROJECTION = new String[] {
		FestivalContract.Festivals.FestivalColumns.FEST_NAME,
		FestivalContract.Festivals.FestivalColumns.FEST_DATE,
		FestivalContract.Festivals.FestivalColumns.FEST_DESC,
		FestivalContract.Festivals.FestivalColumns.FEST_IMG };
	
	private SimpleDateFormat mSimpleDateFormat;

	public static final String SAVED_STATE_ACTION_BAR_HIDDEN = "saved_state_action_bar_hidden";

	private SlidingUpPanelLayout mSlidingUpPanelLayout;

	private TextView festivalTitle, festivalDate;
	
	private Button reminderButton;
	private RoundedImageView festivalImg;

	private FlowTextView festivalDesc;

	private ImageLoader imageLoader = ImageLoader.getInstance();

	private DisplayImageOptions options;

	private ProgressBar progressBar;

	private boolean isDateClicked, isPrevMonth, isNextMonth,
			isLoadingForFirstTime;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_festival_calendar);
		getSupportActionBar().setBackgroundDrawable(
                getResources().getDrawable(R.drawable.header)); 
		// Instantiate Calendar Helper
			  mCalendarHelper = CalendarHelper
			    .getCalendarHelper(FestivalCalendarActivity.this);
		
			  // Create calendar if not exists
			  if (!isCalendarExists()) {
			   mCalendarHelper.createCalendar();
			  }
		  
		isLoadingForFirstTime = true;
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.stub_img)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_empty).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		mActionBar = getSupportActionBar();
		/*mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setHomeButtonEnabled(true);*/
		mActionBar.setTitle(getResources().getString(R.string.calendar));

		// Calendar Fragment
		caldroidFragment = new CaldroidFragment();

		mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",
				Locale.getDefault());

		mSlidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

		festivalTitle = (TextView) findViewById(R.id.festivalTitle);

		festivalDate = (TextView) findViewById(R.id.festivalDate);
		
        reminderButton=(Button)findViewById(R.id.reminderDate);

		festivalImg = (RoundedImageView) findViewById(R.id.festivalImg);

		festivalDesc = (FlowTextView) findViewById(R.id.festivalDesc);

		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		
		
		if (savedInstanceState != null) {
			caldroidFragment.restoreStatesFromKey(savedInstanceState,
					"CALDROID_SAVED_STATE");
		} else {
			Bundle args = new Bundle();
			Calendar cal = Calendar.getInstance();
			args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
			args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
			args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
			args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

			caldroidFragment.setArguments(args);

			FragmentTransaction t = getSupportFragmentManager()
					.beginTransaction();
			t.replace(R.id.calendarContainer, caldroidFragment);
			t.commit();
		}
		caldroidFragment.setCaldroidListener(new CalendarListener());
      
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE,cal.getActualMinimum(Calendar.DATE));
		String firstDateOfMonth = mSimpleDateFormat.format(cal.getTime());

		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
		String lastDateOfMonth = mSimpleDateFormat.format(cal.getTime());
		
		Bundle bundle = new Bundle();
		bundle.putString("firstDayOfMonth", firstDateOfMonth);
		bundle.putString("lastDayOfMonth", lastDateOfMonth);
          
		// The loader will be called initially to highlight the dates on current
		// month's calendar. Whenever, user opens up the calendar, he will see
		// the calendar for current month
		getSupportLoaderManager().initLoader(0, bundle, this);

		boolean actionBarHidden = savedInstanceState != null
				&& savedInstanceState.getBoolean(SAVED_STATE_ACTION_BAR_HIDDEN,
						false);
		if (actionBarHidden) {
			int actionBarHeight = getActionBarHeight();
			setActionBarTranslation(-actionBarHeight);
		}
		
		
	}
	
	@Override
	protected void onStart() {
		Log.e(TAG, "onStart");
		super.onStart();
		SyncUtils.CreateSyncAccount(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		mSyncStatusObserver.onStatusChanged(0);

		// Watch for sync state changes
		final int mask = ContentResolver.SYNC_OBSERVER_TYPE_PENDING
				| ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE;
		mSyncObserverHandle = ContentResolver.addStatusChangeListener(mask,
				mSyncStatusObserver);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (mSyncObserverHandle != null) {
			ContentResolver.removeStatusChangeListener(mSyncObserverHandle);
			mSyncObserverHandle = null;
		}
		
	}
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		mOptionsMenu = menu;
		getMenuInflater().inflate(R.menu.calendar_menu, menu);
		MenuItem item = menu.findItem(R.id.action_toggle);
		if (mSlidingUpPanelLayout != null) {
			if (mSlidingUpPanelLayout.isPanelHidden()) {
				item.setTitle(R.string.action_show);
			} else {
				item.setTitle(R.string.action_hide);
			}
		}
		return true;
	}*/
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			Intent parentActivityIntent = new Intent(this, HanuAlarm.class);
			Log.e(TAG, "onItemClick");
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(parentActivityIntent);
			finish();
			
			return true;
		case R.id.menu_refresh:
			SyncUtils.TriggerRefresh();
			return true;
		case R.id.action_toggle: {
			if (mSlidingUpPanelLayout != null) {
				if (!mSlidingUpPanelLayout.isPanelHidden()) {
					mSlidingUpPanelLayout.hidePanel();
					item.setTitle(R.string.action_show);
				} else {
					mSlidingUpPanelLayout.showPanel();
					item.setTitle(R.string.action_hide);
				}
			}
			return true;
		}
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void setCustomResourceForDates(ArrayList<Date> dateList) {
		if (caldroidFragment != null && !dateList.isEmpty()) {
			for (Date date : dateList) {
				caldroidFragment.setBackgroundResourceForDate(R.color.blue,
						date);
				caldroidFragment.setTextColorForDate(R.color.white, date);
			}
		}
		caldroidFragment.refreshView();
	}
	
	private class CalendarListener extends CaldroidListener {

		@Override
		public void onSelectDate(Date date, View view) {
			Log.e(TAG, "onSelectDate ");
			isDateClicked = true;
			String selectedDate = mSimpleDateFormat.format(date);
			Log.e(TAG, "onSelectDate " + selectedDate);
			
			Bundle bundle = new Bundle();
			bundle.putString("SelectedDate", selectedDate);
			getSupportLoaderManager().restartLoader(1, bundle,
					FestivalCalendarActivity.this);
		}
		
		@Override
		public void onChangeMonth(int month, int year) {
			Log.e(TAG, "onChangeMonth");
			if (isLoadingForFirstTime) {
				Log.e(TAG, "onChangeMonth first time loading");
				isLoadingForFirstTime = false;
			} else {
				Log.e(TAG, "onChangeMonth after first time loading");
				month = month - 1;
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.MONTH, month);
				cal.set(Calendar.YEAR, year);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				String firstDateOfMonth = mSimpleDateFormat.format(cal
						.getTime());
				Log.e(TAG, "onChangeMonth in if firstDate: " + firstDateOfMonth);

				cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
				String lastDateOfMonth = mSimpleDateFormat
						.format(cal.getTime());
				Log.e(TAG, "onChangeMonth in if lastDate: " + lastDateOfMonth);

				Bundle bundle = new Bundle();
				bundle.putString("firstDayOfMonth", firstDateOfMonth);
				bundle.putString("lastDayOfMonth", lastDateOfMonth);
				int curMonth = Calendar.getInstance().get(Calendar.MONTH);

				getSupportLoaderManager().restartLoader(0, bundle,
						FestivalCalendarActivity.this);
				Log.e(TAG, "onChangeMonth current month: " + curMonth);
				Log.e(TAG, "onChangeMonth changed month: " + month);

				if (month < curMonth) {
					Log.e(TAG, "onChangeMonth in if1");
					isPrevMonth = true;
					getSupportLoaderManager().restartLoader(3, bundle,
							FestivalCalendarActivity.this);
				} else if (month > curMonth) {
					Log.e(TAG, "onChangeMonth in else if1");
					isNextMonth = true;
					getSupportLoaderManager().restartLoader(4, bundle,
							FestivalCalendarActivity.this);
				}
			}
		}
		@Override
		public void onLongClickDate(Date date, View view) {
			// TODO Auto-generated method stub
			super.onLongClickDate(date, view);
		}

		@Override
		public void onCaldroidViewCreated() {
			// TODO Auto-generated method stub
			super.onCaldroidViewCreated();
		}
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		CursorLoader c = null;
		switch (id) {
		case 0:
			// Add only those data which fall between specified dates. Here we
			// are interested in getting data for single month hence we are
			// passing bounds as first date of month and last date of month
			// The query will resolve as follows:
			//
			// SELECT fest_name, fest_date, fest_desc, fest_img FROM Festivals
			// WHERE fest_date >= firstDateOfMonth AND fest_date <=
			// lastDateOfMonth
			Log.e(TAG, "onCreateLoader case 0");
			c = new CursorLoader(
					this,
					FestivalContract.Festivals.CONTENT_URI,
					PROJECTION,
					FestivalContract.Festivals.FestivalColumns.FEST_DATE
							+ " >= ? AND "
							+ FestivalContract.Festivals.FestivalColumns.FEST_DATE
							+ " <= ? ", new String[] {
							bundle.getString("firstDayOfMonth"),
							bundle.getString("lastDayOfMonth") }, null);

			break;

		case 1:
			// Find details of festival which falls on this date
			// The query will resolve as follows:
			//
			// SELECT fest_name, fest_date, fest_desc, fest_img FROM Festivals
			// WHERE fest_date = SelectedDate
			Log.e(TAG,
					"onCreateLoader case 1 Date: "
							+ bundle.getString("SelectedDate"));
			c = new CursorLoader(this, FestivalContract.Festivals.CONTENT_URI,
					PROJECTION,
					FestivalContract.Festivals.FestivalColumns.FEST_DATE
							+ " = ? ",
					new String[] { bundle.getString("SelectedDate") }, null);

			break;

		case 2:
			Log.e(TAG, "onCreateLoader case 2");
			// Find the upcoming festival in this month and display on the
			// header of sliding panel
			// The query will resolve as follows:
			//
			// SELECT fest_name, fest_date, fest_desc, fest_img FROM Festivals
			// WHERE fest_date >= SelectedDate ORDER BY fest_date ASC LIMIT 1
			c = new CursorLoader(this, FestivalContract.Festivals.CONTENT_URI,
					PROJECTION,
					FestivalContract.Festivals.FestivalColumns.FEST_DATE
							+ " >= ? ",
					new String[] { bundle.getString("SelectedDate"), },
					FestivalContract.Festivals.FestivalColumns.FEST_DATE
							+ " ASC LIMIT 1");

			break;

		case 3:
			// Find the last festival of the previous month
			// The query will resolve as follows:
			//
			// SELECT fest_name, fest_date, fest_desc, fest_img FROM Festivals
			// WHERE fest_date >= firstDateOfMonth AND fest_date <=
			// lastDateOfMonth ORDER BY DESC LIMIT 1
			Log.e(TAG, "onCreateLoader case 3");
			c = new CursorLoader(
					this,
					FestivalContract.Festivals.CONTENT_URI,
					PROJECTION,
					FestivalContract.Festivals.FestivalColumns.FEST_DATE
							+ " >= ? AND "
							+ FestivalContract.Festivals.FestivalColumns.FEST_DATE
							+ " <= ? ", new String[] {
							bundle.getString("firstDayOfMonth"),
							bundle.getString("lastDayOfMonth") },
					FestivalContract.Festivals.FestivalColumns.FEST_DATE
							+ " DESC LIMIT 1");

			break;

		case 4:
			// Find the first festival of the next month
			// The query will resolve as follows:
			//
			// SELECT fest_name, fest_date, fest_desc, fest_img FROM Festivals
			// WHERE fest_date >= firstDateOfMonth AND fest_date <=
			// lastDateOfMonth ORDER BY ASC LIMIT 1
			Log.e(TAG, "onCreateLoader case 3");
			c = new CursorLoader(
					this,
					FestivalContract.Festivals.CONTENT_URI,
					PROJECTION,
					FestivalContract.Festivals.FestivalColumns.FEST_DATE
							+ " >= ? AND "
							+ FestivalContract.Festivals.FestivalColumns.FEST_DATE
							+ " <= ? ", new String[] {
							bundle.getString("firstDayOfMonth"),
							bundle.getString("lastDayOfMonth") },
					FestivalContract.Festivals.FestivalColumns.FEST_DATE
							+ " ASC LIMIT 1");
			
			
			

			break;
		default:
			break;
		}
		return c;
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
		switch (cursorLoader.getId()) {
		case 0:
			// Highlight dates on calendar which have festival
			Log.e(TAG, "onLoadFinished case 0");
			if ((cursor != null) && (cursor.getCount() > 0)) {
				ArrayList<Date> dateList = new ArrayList<Date>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
						Locale.getDefault());
				while (cursor.moveToNext()) {
					String date = cursor.getString(1);
					Date d = null;
					try {
						d = sdf.parse(date);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					dateList.add(d);
				}
				Log.e(TAG, "DateList count: " + dateList.size());
				setCustomResourceForDates(dateList);
				String todayDate = sdf.format(Calendar.getInstance().getTime());
				Bundle bundle = new Bundle();
				bundle.putString("SelectedDate", todayDate);
				if (isPrevMonth) {
					isPrevMonth = false;
				} else if (isNextMonth) {
					isNextMonth = false;
				} else {
					getSupportLoaderManager().restartLoader(2, bundle,
							FestivalCalendarActivity.this);
				}
			}
			break;

		case 1:
			// Display details of this festival
			Log.e(TAG, "onLoadFinished case 1");
			if ((cursor != null) && (cursor.getCount() > 0)) {
				// Log.e(TAG, "onLoadFinished case 1 in if");
				setFestivalDetails(cursor);
				mSlidingUpPanelLayout.setEnabled(true);
			} else {
				clearFestivalDetails();
				mSlidingUpPanelLayout.setEnabled(false);
			}
			break;

		case 2:
			// Populate details of upcoming festival of this month
			Log.e(TAG, "onLoadFinished case 2");
			if ((cursor != null) && (cursor.getCount() > 0)) {
				// Log.e(TAG, "onLoadFinished case 2 in if");
				setFestivalDetails(cursor);
				mSlidingUpPanelLayout.setEnabled(true);
			} else {
				clearFestivalDetails();
				mSlidingUpPanelLayout.setEnabled(false);
			}
			break;

		case 3:
			// Populate details of last festival of previous month
			Log.e(TAG, "onLoadFinished case 3");
			if ((cursor != null) && (cursor.getCount() > 0)) {
				setFestivalDetails(cursor);
				mSlidingUpPanelLayout.setEnabled(true);
			} else {
				clearFestivalDetails();
				mSlidingUpPanelLayout.setEnabled(false);
			}
			break;

		case 4:
			// Populate details of next upcoming festival of next month
			Log.e(TAG, "onLoadFinished case 4");
			if ((cursor != null) && (cursor.getCount() > 0)) {
				setFestivalDetails(cursor);
				mSlidingUpPanelLayout.setEnabled(true);
			} else {
				clearFestivalDetails();
				mSlidingUpPanelLayout.setEnabled(false);
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onLoaderReset(Loader<Cursor> cursorLoader) {
		Log.e(TAG, "onLoaderReset");
		cursorLoader.reset();
	}
	
	private void clearFestivalDetails() {
		festivalTitle.setText("");
		festivalDate.setText("");
		festivalDesc.setText("");
		festivalImg.setImageDrawable(getResources().getDrawable(R.drawable.stub_img));
				
				
	}
	
	private void setFestivalDetails(Cursor cursor) {
		while (cursor.moveToNext()) {
			String name = cursor.getString(0);
			String date = cursor.getString(1);
			String desc = cursor.getString(2);
			String imgUrl = cursor.getString(3);
             
			festivalTitle.setText(name);
			festivalDate.setText(date);
			festivalDesc.setText(desc);
			reminderButton.setText("Reminder");
			Log.e("setfestival", "setfestival");
			
			 reminderButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						setReminder(festivalTitle.getText().toString(), festivalDate
					     .getText().toString(),"08:00 AM");
					}
				});
				  
			imageLoader.displayImage(imgUrl, festivalImg, options,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							progressBar.setProgress(0);
							progressBar.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							progressBar.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							progressBar.setVisibility(View.GONE);
						}
					}, new ImageLoadingProgressListener() {
						@Override
						public void onProgressUpdate(String imageUri,
								View view, int current, int total) {
							progressBar.setProgress(Math.round(100.0f * current
									/ total));
						}
					});
					
				
		}
		if (isDateClicked) {
			isDateClicked = false;
			mSlidingUpPanelLayout.expandPanel();
		}
	}
	
	private SyncStatusObserver mSyncStatusObserver = new SyncStatusObserver() {
		/** Callback invoked with the sync adapter status changes. */
		@Override
		public void onStatusChanged(int which) {
			runOnUiThread(new Runnable() {
				/**
				 * The SyncAdapter runs on a background thread. To update the
				 * UI, onStatusChanged() runs on the UI thread.
				 */
				@Override
				public void run() {
					// Create a handle to the account that was created by
					// SyncService.CreateSyncAccount(). This will be used to
					// query the system to
					// see how the sync status has changed.
					Account account = GenericAccountService.GetAccount();
					if (account == null) {
						// GetAccount() returned an invalid value. This
						// shouldn't happen, but
						// we'll set the status to "not refreshing".
						setRefreshActionButtonState(false);
						return;
					}

					// Test the ContentResolver to see if the sync adapter is
					// active or pending.
					// Set the state of the refresh button accordingly.
					boolean syncActive = ContentResolver.isSyncActive(account,
							FestivalContract.FESTIVAL_AUTHORITY);
					boolean syncPending = ContentResolver.isSyncPending(
							account, FestivalContract.FESTIVAL_AUTHORITY);
					setRefreshActionButtonState(syncActive || syncPending);
				}
			});
		}
	};
	
	@SuppressLint("NewApi")
	public void setRefreshActionButtonState(boolean refreshing) {
		if (mOptionsMenu == null) {
			return;
		}

		final MenuItem refreshItem = mOptionsMenu.findItem(R.id.menu_refresh);
		if (refreshItem != null) {
			if (refreshing) {
				if (isSDKHoneycombOrGreater()) {
					refreshItem
							.setActionView(R.layout.actionbar_indeterminate_progress);
				} else {
					MenuItemCompat.setActionView(refreshItem,
							R.layout.actionbar_indeterminate_progress);
				}
			} else {
				if (isSDKHoneycombOrGreater()) {
					refreshItem.setActionView(null);
				} else {
					MenuItemCompat.setActionView(refreshItem, null);
				}
			}
		}
	}
	
	private boolean isSDKHoneycombOrGreater() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	@Override
	public void onPanelSlide(View panel, float slideOffset) {
		setActionBarTranslation(mSlidingUpPanelLayout.getCurrentParalaxOffset());
	}

	@Override
	public void onPanelCollapsed(View panel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPanelExpanded(View panel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPanelAnchored(View panel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPanelHidden(View panel) {
		// TODO Auto-generated method stub

	}
	
	private int getActionBarHeight() {
		int actionBarHeight = 0;
		TypedValue tv = new TypedValue();
		if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
			actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
					getResources().getDisplayMetrics());
		}
		return actionBarHeight;
	}

	@SuppressLint("NewApi")
	public void setActionBarTranslation(float y) {
		// Figure out the actionbar height
		int actionBarHeight = getActionBarHeight();
		// A hack to add the translation to the action bar
		ViewGroup content = ((ViewGroup) findViewById(android.R.id.content)
				.getParent());
		int children = content.getChildCount();
		for (int i = 0; i < children; i++) {
			View child = content.getChildAt(i);
			if (child.getId() != android.R.id.content) {
				if (y <= -actionBarHeight) {
					child.setVisibility(View.GONE);
				} else {
					child.setVisibility(View.VISIBLE);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
						child.setTranslationY(y);
					} else {
					}
				}
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		if (mSlidingUpPanelLayout != null
				&& mSlidingUpPanelLayout.isPanelExpanded()
				|| mSlidingUpPanelLayout.isPanelAnchored()) {
			mSlidingUpPanelLayout.collapsePanel();
		} else {
			super.onBackPressed();
		}
	}
	
	private void setReminder(String eventNote, String dateToSet,String timeToset) {
		  String format = "yyyy-mm-dd hh:mm a";
		  String festDate=dateToSet+" "+timeToset;
		 
		  SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		  Date reminderDate = null;
		  try {
		   reminderDate = sdf.parse(festDate);
		  } catch (ParseException e) {
		   e.printStackTrace();
		  }
		  
		  if (reminderDate != null) {
		   long startDate = reminderDate.getTime();
		   long endDate = startDate + 60000;

		   mCalendarHelper.addEvent(eventNote, eventNote, startDate, endDate);
		   Toast.makeText(FestivalCalendarActivity.this, "Reminder added",
		     Toast.LENGTH_SHORT).show();
		  }
		 }
       
        
		private boolean isCalendarExists() {
		  ArrayList<CalendarListDAO> mCalendarListDAOs = mCalendarHelper
		    .getAvailableCalendarList();
		  if ((mCalendarListDAOs != null) && (mCalendarListDAOs.size() > 0)) {
		   for (int i = 0; i < mCalendarListDAOs.size(); i++) {
		    if (mCalendarListDAOs.get(i).getAccountName()
		      .equals("Festival Calendar")
		      && mCalendarListDAOs.get(i).getCalendarName()
		        .equals("Festival_Calendar")) {
		     Log.e(TAG,
		       "Calendar exists ID: "
		         + Long.parseLong(mCalendarListDAOs.get(i)
		           .getCalendarId()));
		     mCalendarHelper.setCalID(Long.parseLong(mCalendarListDAOs
		       .get(i).getCalendarId()));
		     return true;
		    }
		   }
		  }
		  return false;
		 }

}

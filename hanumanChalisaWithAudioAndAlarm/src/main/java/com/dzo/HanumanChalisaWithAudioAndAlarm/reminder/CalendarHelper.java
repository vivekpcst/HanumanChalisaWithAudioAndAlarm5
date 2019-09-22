package com.dzo.HanumanChalisaWithAudioAndAlarm.reminder;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.util.Log;



public class CalendarHelper {
	private Context ctx;

	private static CalendarHelper mCalendarHelper;

	private static String ACCOUNT_NAME;

	private static final String CALENDAR_NAME = "Festival_Calendar";

	/** The main/basic URI for the android calendar table */
	private static final Uri CAL_URI = CalendarContract.Calendars.CONTENT_URI;

	/** The main/basic URI for the android events table */
	private static final Uri EVENT_URI = CalendarContract.Events.CONTENT_URI;

	/** The main/basic URI for the android reminder table */
	private static final Uri REMINDER_URI = CalendarContract.Reminders.CONTENT_URI;

	/** The UNIQUE ID of this calendar */
	private static long CAL_ID;

	private static final String TAG = "ITitheCalendarHelper";

	private CalendarHelper(Context ctx) {
		this.ctx = ctx.getApplicationContext();
	}

	/** Factory method to return instance of this Helper class */
	public static CalendarHelper getCalendarHelper(Context ctx) {
		ACCOUNT_NAME = "Festival_Calendar";
		if (mCalendarHelper == null) {
			Log.e(TAG, "getCalendarHelper if");
			mCalendarHelper = new CalendarHelper(ctx.getApplicationContext());
		}
		return mCalendarHelper;
	}

	/**
	 * Creates {@link ContentValues} with the requested properties for creating
	 * a new calendar
	 * 
	 * @return {@link ContentValues} with requested properties
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static ContentValues buildNewCalContentValues() {
		final ContentValues cv = new ContentValues();
		cv.put(Calendars.ACCOUNT_NAME, ACCOUNT_NAME);
		cv.put(Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
		cv.put(Calendars.NAME, CALENDAR_NAME);
		cv.put(Calendars.CALENDAR_DISPLAY_NAME, CALENDAR_NAME);
		cv.put(Calendars.CALENDAR_COLOR, 0xEA8561);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
			cv.put(Calendars.ALLOWED_REMINDERS,
					CalendarContract.Reminders.METHOD_ALARM);
		else
			cv.put(Calendars.ALLOWED_REMINDERS,
					CalendarContract.Reminders.METHOD_ALERT);

		// user can only read the calendar
		cv.put(Calendars.CALENDAR_ACCESS_LEVEL,
				Calendars.CAL_ACCESS_CONTRIBUTOR);
		cv.put(Calendars.OWNER_ACCOUNT, ACCOUNT_NAME);
		cv.put(Calendars.VISIBLE, 1);
		cv.put(Calendars.SYNC_EVENTS, 1);
		return cv;
	}

	/**
	 * Create and insert new calendar into android database
	 * 
	 * @return the ID of newly created calendar
	 */
	public long createCalendar() {
		ContentResolver cr = ctx.getContentResolver();
		final ContentValues cv = buildNewCalContentValues();
		Uri calUri = buildCalUri();

		// insert the calendar into the database
		Uri newCalendarUri = cr.insert(calUri, cv);

		CAL_ID = Long.parseLong(newCalendarUri.getLastPathSegment());
		return CAL_ID;
	}

	/**
	 * Create event - Add an event to our calendar
	 * 
	 * @param title
	 *            Title of this event
	 * @param description
	 *            Description of this event
	 * @param dtstart
	 *            event start date in milliseconds
	 * @param dtend
	 *            event end date in milliseconds
	 */
	public void addEvent(String title, String description, long dtstart,
			long dtend) {
		ContentResolver cr = ctx.getContentResolver();
		ContentValues cv = new ContentValues();
		cv.put(Events.CALENDAR_ID, CAL_ID);
		cv.put(Events.TITLE, title);
		cv.put(Events.DESCRIPTION, description);
		cv.put(Events.DTSTART, dtstart);
		cv.put(Events.DTEND, dtend);
		cv.put(Events.HAS_ALARM, 1);
		Uri eventUri = cr.insert(buildEventUri(), cv);
		long eventId = Long.parseLong(eventUri.getLastPathSegment());
		Log.e(TAG, "addEvent eventId: " + eventId);

		// Adds reminder for this event
		addReminder(eventId);
	}

	/**
	 * Updates the existing event
	 * 
	 * @param eventId
	 *            The id of the event to update
	 * @param title
	 *            Title of updated event
	 * @param description
	 *            Description of updated event
	 * @param dtstart
	 *            event start date of updated event
	 * @param dtend
	 *            event end date of updated event
	 */
	public void updateEvent(String eventId, String title, String description,
			long dtstart, long dtend, String reminderId) {
		ContentResolver cr = ctx.getContentResolver();
		ContentValues cv = new ContentValues();
		cv.put(Events.CALENDAR_ID, CAL_ID);
		cv.put(Events.TITLE, title);
		cv.put(Events.DESCRIPTION, description);
		cv.put(Events.DTSTART, dtstart);
		cv.put(Events.DTEND, dtend);
		cv.put(Events.HAS_ALARM, 1);
		Uri eventUpdateUri = ContentUris.withAppendedId(buildEventUri(),
				Long.parseLong(eventId));
		int result = cr.update(eventUpdateUri, cv, null, null);
		Log.e(TAG, "Update result: " + result);
		updateReminder(eventId, reminderId);
	}

	/**
	 * Adds reminder for the given event
	 * 
	 * @param eventId
	 *            The ID of the event for which reminder to add
	 */
	private void addReminder(long eventId) {
		ContentResolver cr = ctx.getContentResolver();
		ContentValues reminders = new ContentValues();
		reminders.put(Reminders.EVENT_ID, eventId);
		reminders.put(Reminders.METHOD, Reminders.METHOD_ALERT);
		reminders.put(Reminders.MINUTES, 5);
		Uri reminderUri = cr.insert(buildReminderUri(), reminders);
		long remId = Long.parseLong(reminderUri.getLastPathSegment());
		Log.e(TAG, "Reminder ID: " + remId);
	}

	/**
	 * Updates the reminder for the given event
	 * 
	 * @param eventId
	 *            The ID of the event for which event to update
	 * 
	 * @param reminderId
	 *            The ID of the reminder to update
	 */
	public void updateReminder(String eventId, String reminderId) {
		ContentResolver cr = ctx.getContentResolver();
		ContentValues cv = new ContentValues();
		cv.put(Reminders.EVENT_ID, eventId);
		cv.put(Reminders.METHOD, Reminders.METHOD_ALERT);
		cv.put(Reminders.MINUTES, 5);
		Uri reminderUpdateUri = ContentUris.withAppendedId(buildReminderUri(),
				Long.parseLong(reminderId));
		int result = cr.update(reminderUpdateUri, cv, null, null);
		Log.e(TAG, "Update Reminder result: " + result);
	}

	/**
	 * Returns all the calendars available in the device
	 * 
	 * @return {@link ArrayList} of Calendar Information
	 */
	public ArrayList<CalendarListDAO> getAvailableCalendarList() {
		ArrayList<CalendarListDAO> calendarListDAOs = new ArrayList<CalendarListDAO>();
		ContentResolver contentResolver = ctx.getContentResolver();
		final Cursor cursor = contentResolver.query(CAL_URI,
				new String[] { "_id", Calendars.ACCOUNT_NAME,
						Calendars.CALENDAR_DISPLAY_NAME },
				Calendars.ACCOUNT_NAME + " = ? AND "
						+ Calendars.CALENDAR_DISPLAY_NAME + " = ?",
				new String[] { ACCOUNT_NAME, CALENDAR_NAME }, null);
		while (cursor.moveToNext()) {
			Log.e(TAG, "getAvailableCalendarList in while");
			CalendarListDAO calendarListDAO = new CalendarListDAO();
			calendarListDAO.setCalendarId(cursor.getString(cursor
					.getColumnIndex("_id")));
			calendarListDAO.setAccountName(cursor.getString(cursor
					.getColumnIndex(Calendars.ACCOUNT_NAME)));
			calendarListDAO.setCalendarName(cursor.getString(cursor
					.getColumnIndex(Calendars.CALENDAR_DISPLAY_NAME)));
			calendarListDAOs.add(calendarListDAO);
		}
		return calendarListDAOs;
	}

	/**
	 * Get list of all events from the calendar
	 * 
	 * @return {@link ArrayList} of {@link CalendarEventsDAO} which contain all
	 *         the information for these events
	 */
	public ArrayList<CalendarEventsDAO> getAllEvents() {
		ArrayList<CalendarEventsDAO> calendarEventsDAOs = new ArrayList<CalendarEventsDAO>();
		ContentResolver cr = ctx.getContentResolver();
		Log.e(TAG, "Calendar id: " + CAL_ID);
		final Cursor cursor = cr.query(EVENT_URI, new String[] {
				Events.CALENDAR_ID, Events._ID, Events.TITLE,
				Events.DESCRIPTION, Events.DTSTART }, Events.CALENDAR_ID
				+ " = ?", new String[] { Long.toString(CAL_ID) },
				Events.DTSTART + " DESC");
		while (cursor.moveToNext()) {
			CalendarEventsDAO calendarEventsDAO = new CalendarEventsDAO();
			calendarEventsDAO.setEventID(cursor.getString(cursor
					.getColumnIndex(Events._ID)));
			calendarEventsDAO.setEventDate(cursor.getString(cursor
					.getColumnIndex(Events.DTSTART)));
			calendarEventsDAO.setEventTitle(cursor.getString(cursor
					.getColumnIndex(Events.TITLE)));
			calendarEventsDAOs.add(calendarEventsDAO);
		}
		return calendarEventsDAOs;
	}

	/**
	 * Set the calendar id
	 * 
	 * @param calId
	 *            id of existing calendar
	 */
	public void setCalID(long calId) {
		CAL_ID = calId;
	}

	/**
	 * Deletes the calendar with given ID
	 * 
	 * @param calId
	 *            The ID of the calendar to delete
	 */
	public void deleteCalendar(long calId) {
		ContentResolver cr = ctx.getContentResolver();
		Uri calUri = ContentUris.withAppendedId(buildCalUri(), calId);
		cr.delete(calUri, null, null);
	}

	/** Builds the Uri for your Calendar in android database (as a Sync Adapter) */
	public static Uri buildCalUri() {
		return CAL_URI
				.buildUpon()
				.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER,
						"true")
				.appendQueryParameter(Calendars.ACCOUNT_NAME, ACCOUNT_NAME)
				.appendQueryParameter(Calendars.ACCOUNT_TYPE,
						CalendarContract.ACCOUNT_TYPE_LOCAL).build();
	}

	/** Builds the Uri for events (as a Sync Adapter) */
	public static Uri buildEventUri() {
		return EVENT_URI
				.buildUpon()
				.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER,
						"true")
				.appendQueryParameter(Calendars.ACCOUNT_NAME, ACCOUNT_NAME)
				.appendQueryParameter(Calendars.ACCOUNT_TYPE,
						CalendarContract.ACCOUNT_TYPE_LOCAL).build();
	}

	/** Builds the Uri for reminder (as a Sync Adapter) */
	public static Uri buildReminderUri() {
		return REMINDER_URI
				.buildUpon()
				.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER,
						"true")
				.appendQueryParameter(Calendars.ACCOUNT_NAME, ACCOUNT_NAME)
				.appendQueryParameter(Calendars.ACCOUNT_TYPE,
						CalendarContract.ACCOUNT_TYPE_LOCAL).build();
	}

	public String getReminderID(String eventID) {
		ContentResolver cr = ctx.getContentResolver();

		final Cursor cursor = cr.query(REMINDER_URI,
				new String[] { Reminders._ID }, Reminders.EVENT_ID + " = ? ",
				new String[] { eventID }, null);
		while (cursor.moveToNext()) {
			return cursor.getString(cursor.getColumnIndex(Reminders._ID));
		}
		return null;
	}

	public void deleteEvent(String eventId, String reminderId) {
		ContentResolver cr = ctx.getContentResolver();
		Uri eventUri = ContentUris.withAppendedId(buildEventUri(),
				Long.parseLong(eventId));
		deleteReminder(reminderId);
		int i = cr.delete(eventUri, null, null);
		Log.e(TAG, "Event deleted: " + i);
	}

	private void deleteReminder(String reminderId) {
		ContentResolver cr = ctx.getContentResolver();
		Uri reminderUri = ContentUris.withAppendedId(buildReminderUri(),
				Long.parseLong(reminderId));
		int i = cr.delete(reminderUri, null, null);
		Log.e(TAG, "Reminder deleted: " + i);
	}
}
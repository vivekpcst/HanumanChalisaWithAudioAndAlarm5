package com.dzo.HanumanChalisaWithAudioAndAlarm.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class FestivalCalendarProvider extends ContentProvider {
	private DBHelper mDbHelper = null;
	private static final String TAG = "FestivalCalendar";

	@Override
	public boolean onCreate() {
		mDbHelper = new DBHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		final int match = FestivalContract.URI_MATCHER.match(uri);
		switch (match) {
		case FestivalContract.Festivals.PATH_TOKEN:
			SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
			builder.setTables(FestivalContract.Festivals.NAME);
			Cursor c = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
			c.setNotificationUri(getContext().getContentResolver(), uri);
			return c;
		default:
			throw new IllegalArgumentException("Unsupported Uri for query: "
					+ uri);
		}
	}

	@Override
	public String getType(Uri uri) {
		int match = FestivalContract.URI_MATCHER.match(uri);
		switch (match) {
		case FestivalContract.Festivals.PATH_TOKEN:
			return FestivalContract.Festivals.URI_TYPE_FESTIVAL_DIR;
		case FestivalContract.Festivals.PATH_FOR_ID_TOKEN:
			return FestivalContract.Festivals.URI_TYPE_FESTIVAL_ITEM;
		default:
			throw new IllegalArgumentException("Unsupported Uri: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.e(TAG, "contentprovider insert");
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int token = FestivalContract.URI_MATCHER.match(uri);
		switch (token) {
		case FestivalContract.Festivals.PATH_TOKEN:
			long id = db.insert(FestivalContract.Festivals.NAME, null, values);
			getContext().getContentResolver().notifyChange(uri, null);
			return FestivalContract.Festivals.CONTENT_URI.buildUpon()
					.appendPath(String.valueOf(id)).build();
		default:
			throw new IllegalArgumentException("Unsupported Uri for insert: "
					+ uri);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
}
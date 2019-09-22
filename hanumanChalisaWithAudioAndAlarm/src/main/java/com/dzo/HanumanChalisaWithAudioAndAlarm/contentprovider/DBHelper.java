package com.dzo.HanumanChalisaWithAudioAndAlarm.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final int DB_VERSION = 1;

	public DBHelper(Context context) {
		super(context, DBSchema.DATABASE_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DBSchema.CREATE_DB_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DBSchema.DROP_DB_TABLE);
		onCreate(db);
	}
}
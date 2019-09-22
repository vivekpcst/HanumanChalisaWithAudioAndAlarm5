package com.dzo.HanumanChalisaWithAudioAndAlarm.contentprovider;

import com.dzo.HanumanChalisaWithAudioAndAlarm.contentprovider.FestivalContract.Festivals;

public interface DBSchema {
	String DATABASE_NAME = "Festival_DB";

	String CREATE_DB_TABLE = " CREATE TABLE " + Festivals.NAME + " ("
			+ FestivalContract.Festivals.FestivalColumns.FEST_ID
			+ " INTEGER PRIMARY KEY, "
			+ FestivalContract.Festivals.FestivalColumns.FEST_NAME
			+ " TEXT NOT NULL, "
			+ FestivalContract.Festivals.FestivalColumns.FEST_DATE
			+ " TEXT NOT NULL, "
			+ FestivalContract.Festivals.FestivalColumns.FEST_DESC
			+ " TEXT NOT NULL, "
			+ FestivalContract.Festivals.FestivalColumns.FEST_IMG
			+ " TEXT NOT NULL);";

	String DROP_DB_TABLE = "DROP TABLE IF EXISTS " + Festivals.NAME;
}
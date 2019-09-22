package com.dzo.HanumanChalisaWithAudioAndAlarm.contentprovider;

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;

public final class FestivalContract {
	public static final String FESTIVAL_AUTHORITY = "com.dzo.HanumanChalisaWithAudioAndAlarm.contentprovider.FESTIVAL_CONTENT_PROVIDER";
	public static final String URL = "content://" + FESTIVAL_AUTHORITY;
	public static final Uri BASE_URI = Uri.parse(URL);
	public static final int FESTIVAL_LIST = 1;

	public static final UriMatcher URI_MATCHER = buildUri();

	private static UriMatcher buildUri() {
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

		matcher.addURI(FestivalContract.FESTIVAL_AUTHORITY, Festivals.PATH,
				Festivals.PATH_TOKEN);
		matcher.addURI(FestivalContract.FESTIVAL_AUTHORITY,
				Festivals.PATH_FOR_ID, Festivals.PATH_FOR_ID_TOKEN);
		return matcher;
	}

	public static class Festivals {
		public static final String NAME = "Festivals";

		public static final String PATH = "festivals";
		public static final int PATH_TOKEN = 100;
		public static final String PATH_FOR_ID = "festivals/*";
		public static final int PATH_FOR_ID_TOKEN = 200;
		public static final Uri CONTENT_URI = BASE_URI.buildUpon()
				.appendPath(PATH).build();
		public static final String URI_TYPE_FESTIVAL_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE
				+ "/vnd.com.dz.syncadapterdemo.festival";

		public static final String URI_TYPE_FESTIVAL_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE
				+ "/vnd.com.dz.syncadapterdemo.festivals";

		public static class FestivalColumns {
			public static final String FEST_ID = "_id";
			public static final String FEST_NAME = "fest_name";
			public static final String FEST_DATE = "fest_date";
			public static final String FEST_DESC = "fest_desc";
			public static final String FEST_IMG = "fest_img";
		}
	}
}
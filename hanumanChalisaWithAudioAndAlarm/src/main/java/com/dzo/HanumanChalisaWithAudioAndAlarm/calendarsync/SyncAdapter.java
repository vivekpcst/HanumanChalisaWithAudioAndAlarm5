/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dzo.HanumanChalisaWithAudioAndAlarm.calendarsync;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.dzo.HanumanChalisaWithAudioAndAlarm.contentprovider.FestivalContract;
import com.dzo.HanumanChalisaWithAudioAndAlarm.dao.FestivalDAO;
import com.dzo.HanumanChalisaWithAudioAndAlarm.dao.FestivalDAOs;
import com.dzo.HanumanChalisaWithAudioAndAlarm.parser.FeedParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Define a sync adapter for the app.
 * 
 * <p>
 * This class is instantiated in {@link SyncService}, which also binds
 * SyncAdapter to the system. SyncAdapter should only be initialized in
 * SyncService, never anywhere else.
 * 
 * <p>
 * The system calls onPerformSync() via an RPC call through the IBinder object
 * supplied by SyncService.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
	public static final String TAG = "SyncAdapter";

	/**
	 * URL to fetch content from during a sync.
	 * 
	 * <p>
	 * This points to the Android Developers Blog. (Side note: We highly
	 * recommend reading the Android Developer Blog to stay up to date on the
	 * latest Android platform developments!)
	 */
	private static final String FEED_URL = "http://192.185.193.254/~quraan/aartisangrah/hanuman_festivls.php";

	/**
	 * Network connection timeout, in milliseconds.
	 */
	private static final int NET_CONNECT_TIMEOUT_MILLIS = 15000; // 15 seconds

	/**
	 * Network read timeout, in milliseconds.
	 */
	private static final int NET_READ_TIMEOUT_MILLIS = 10000; // 10 seconds

	/**
	 * Content resolver, for performing database operations.
	 */
	private final ContentResolver mContentResolver;

	/**
	 * Project used when querying content provider. Returns all known fields.
	 */
	private static final String[] PROJECTION = new String[] {
			FestivalContract.Festivals.FestivalColumns.FEST_ID,
			FestivalContract.Festivals.FestivalColumns.FEST_NAME,
			FestivalContract.Festivals.FestivalColumns.FEST_DATE,
			FestivalContract.Festivals.FestivalColumns.FEST_DESC,
			FestivalContract.Festivals.FestivalColumns.FEST_IMG };

	// Constants representing column positions from PROJECTION.
	public static final int FEST_COLUMN_ID = 0;
	public static final int FEST_NAME = 1;
	public static final int FEST_DATE = 2;
	public static final int FEST_DESC = 3;
	public static final int FEST_IMG = 4;

	/**
	 * Constructor. Obtains handle to content resolver for later use.
	 */
	public SyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		mContentResolver = context.getContentResolver();
	}

	/**
	 * Constructor. Obtains handle to content resolver for later use.
	 */
	@SuppressLint("NewApi")
	public SyncAdapter(Context context, boolean autoInitialize,
			boolean allowParallelSyncs) {
		super(context, autoInitialize, allowParallelSyncs);
		mContentResolver = context.getContentResolver();
	}

	/**
	 * Called by the Android system in response to a request to run the sync
	 * adapter. The work required to read data from the network, parse it, and
	 * store it in the content provider is done here. Extending
	 * AbstractThreadedSyncAdapter ensures that all methods within SyncAdapter
	 * run on a background thread. For this reason, blocking I/O and other
	 * long-running tasks can be run <em>in situ</em>, and you don't have to set
	 * up a separate thread for them. .
	 * 
	 * <p>
	 * This is where we actually perform any work required to perform a sync.
	 * {@link AbstractThreadedSyncAdapter} guarantees that this will be called
	 * on a non-UI thread, so it is safe to peform blocking I/O here.
	 * 
	 * <p>
	 * The syncResult argument allows you to pass information back to the method
	 * that triggered the sync.
	 */
	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		Log.e(TAG, "onPerformSync");
		try {
			final URL feedURL = new URL(FEED_URL);
			InputStream stream = null;

			try {
				Log.e(TAG, "Streaming data from network: " + feedURL);
				stream = downloadUrl(feedURL);
				updateLocalFeedData(stream, syncResult);
				// Makes sure that the InputStream is closed after the app is
				// finished using it.
			} catch (IOException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (OperationApplicationException e) {
				e.printStackTrace();
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Read XML from an input stream, storing it into the content provider.
	 * 
	 * <p>
	 * This is where incoming data is persisted, committing the results of a
	 * sync. In order to minimize (expensive) disk operations, we compare
	 * incoming data with what's already in our database, and compute a merge.
	 * Only changes (insert/update/delete) will result in a database write.
	 * 
	 * <p>
	 * As an additional optimization, we use a batch operation to perform all
	 * database writes at once.
	 * 
	 * <p>
	 * Merge strategy: 1. Get cursor to all items in feed<br/>
	 * 2. For each item, check if it's in the incoming data.<br/>
	 * a. YES: Remove from "incoming" list. Check if data has mutated, if so,
	 * perform database UPDATE.<br/>
	 * b. NO: Schedule DELETE from database.<br/>
	 * (At this point, incoming database only contains missing items.)<br/>
	 * 3. For any items remaining in incoming list, ADD to database.
	 */
	@SuppressLint("UseSparseArrays")
	public void updateLocalFeedData(final InputStream stream,
			final SyncResult syncResult) throws IOException, RemoteException,
			OperationApplicationException {
		final FeedParser feedParser = new FeedParser();
		final ContentResolver contentResolver = getContext()
				.getContentResolver();

		Log.e(TAG, "Parsing stream as Atom feed");
		try {


			FestivalDAOs object = feedParser.getData(stream);

			if (object == null) {
				Log.e(TAG, "object null");
			} else {
				Log.e(TAG, "object not null");
			}
			List<FestivalDAO> festivalsList = object.getFestivalsList();

			ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

			// Build hash table of incoming entries
			HashMap<String, FestivalDAO> entryMap = new HashMap<String, FestivalDAO>();

			for (FestivalDAO e : festivalsList) {
				Log.e(TAG, "Add to entryMap");
				entryMap.put(e.getFestivalName(), e);
			}

			// Get list of all items
			Log.e(TAG, "Fetching local entries for merge " + entryMap.size());
			Uri uri = FestivalContract.Festivals.CONTENT_URI; // Get all entries
			Cursor c = contentResolver.query(uri, PROJECTION, null, null, null);
			assert c != null;
			Log.e(TAG, "Found " + c.getCount()
					+ " local entries. Computing merge solution...");

			// Find stale data
			int id;
			String festivalName;
			String festivalDate;
			String festivalDesc;
			String festivalImg;
			while (c.moveToNext()) {
				Log.e(TAG, "in while");
				syncResult.stats.numEntries++;
				id = c.getInt(FEST_COLUMN_ID);
				festivalName = c.getString(FEST_NAME);
				festivalDate = c.getString(FEST_DATE);
				festivalDesc = c.getString(FEST_DESC);
				festivalImg = c.getString(FEST_IMG);
				FestivalDAO match = entryMap.get(festivalName);
				if (match != null) {
					// Entry exists. Remove from entry map to prevent insert later.
					entryMap.remove(festivalName);
					// Check to see if the entry needs to be updated
					Uri existingUri = FestivalContract.Festivals.CONTENT_URI
							.buildUpon().appendPath(Integer.toString(id)).build();
					if ((match.getFestivalName() != null && !match
							.getFestivalName().equals(festivalName))) {
						// Update existing record
						Log.e(TAG, "Scheduling update: " + existingUri + " Date: "
								+ festivalDate);
						batch.add(ContentProviderOperation
								.newUpdate(existingUri)
								.withValue(
										FestivalContract.Festivals.FestivalColumns.FEST_NAME,
										festivalName)
								.withValue(
										FestivalContract.Festivals.FestivalColumns.FEST_DATE,
										festivalDate)
								.withValue(
										FestivalContract.Festivals.FestivalColumns.FEST_DESC,
										festivalDesc)
								.withValue(
										FestivalContract.Festivals.FestivalColumns.FEST_IMG,
										festivalImg).build());
						syncResult.stats.numUpdates++;
					} else {
						Log.e(TAG, "No action: " + existingUri);
					}
				} else {
					// Entry doesn't exist. Remove it from the database.
					Uri deleteUri = FestivalContract.Festivals.CONTENT_URI
							.buildUpon().appendPath(Integer.toString(id)).build();
					Log.e(TAG, "Scheduling delete: " + deleteUri);
					batch.add(ContentProviderOperation.newDelete(deleteUri).build());
					syncResult.stats.numDeletes++;
				}
			}
			c.close();

			Log.e(TAG, "EntryMap size: " + entryMap.size());
			// Add new items
			for (FestivalDAO e : entryMap.values()) {
				Log.e(TAG, "Scheduling insert: entry_id=" + e.getFestivalID());
				Log.e(TAG, "Scheduling insert: entry_id=" + e.getFestivalID()
						+ " Date: " + e.getFestivalDate());
				batch.add(ContentProviderOperation
						.newInsert(FestivalContract.Festivals.CONTENT_URI)
						.withValue(
								FestivalContract.Festivals.FestivalColumns.FEST_ID,
								e.getFestivalID())
						.withValue(
								FestivalContract.Festivals.FestivalColumns.FEST_NAME,
								e.getFestivalName())
						.withValue(
								FestivalContract.Festivals.FestivalColumns.FEST_DATE,
								e.getFestivalDate())
						.withValue(
								FestivalContract.Festivals.FestivalColumns.FEST_DESC,
								e.getFestivalDesc())
						.withValue(
								FestivalContract.Festivals.FestivalColumns.FEST_IMG,
								e.getFestivalImg()).build());
				syncResult.stats.numInserts++;
			}
			Log.e(TAG, "Merge solution ready. Applying batch update");
			mContentResolver.applyBatch(FestivalContract.FESTIVAL_AUTHORITY, batch);
			mContentResolver.notifyChange(FestivalContract.Festivals.CONTENT_URI, // URI
					// where
					// data
					// was
					// modified
					null, // No local observer
					false); // IMPORTANT: Do not sync to network
			// This sample doesn't support uploads, but if *your* code does, make
			// sure you set
		}catch (Exception e){
			// syncToNetwork=false in the line above to prevent duplicate syncs.
		}
	}

	/**
	 * Given a string representation of a URL, sets up a connection and gets an
	 * input stream.
	 */
	private InputStream downloadUrl(final URL url) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(NET_READ_TIMEOUT_MILLIS /* milliseconds */);
		conn.setConnectTimeout(NET_CONNECT_TIMEOUT_MILLIS /* milliseconds */);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		// Starts the query
		conn.connect();
		return conn.getInputStream();
	}
}
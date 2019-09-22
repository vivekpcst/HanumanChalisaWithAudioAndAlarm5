package com.dzo.HanumanChalisaWithAudioAndAlarm.receiver;



import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import com.dzo.HanumanChalisaWithAudioAndAlarm.SetAlarmActivity;
//import com.google.android.gms.gcm.GoogleCloudMessaging;



public class GcmIntentService  extends IntentService {

	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	private final String TAG = "GcmIntentService"; 
	public GcmIntentService() {
		super("name");
		
		
		
	}

	@Override
	protected void onHandleIntent(Intent intent) {/*
		
		
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);
		Log.e(TAG, "GCM Message Type: " + messageType);
		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification("Deleted messages on server: "
						+ extras.toString());
				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				// This loop represents the service doing some work.
				for (int i = 0; i < 5; i++) {
					// Log.i(TAG, "Working... " + (i+1)
					// + "/5 @ " + SystemClock.elapsedRealtime());
					try {
						Thread.sleep(1000);
					  } catch (InterruptedException e) {
					}
				}
				// Log.i(TAG, "Completed work @ " +
				// SystemClock.elapsedRealtime());
				// Post notification of received message.
				sendNotification(extras.getString("message"));

			}
		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);
		*/
	}

	 private void sendNotification(String msg) {
	        mNotificationManager = (NotificationManager)
	                this.getSystemService(Context.NOTIFICATION_SERVICE);

	        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
	                new Intent(this, SetAlarmActivity.class), 0);

	        NotificationCompat.Builder mBuilder =
	                new NotificationCompat.Builder(this)
	        .setSmallIcon(R.drawable.alert_dark_frame)
	        .setContentTitle("GCM Notification")
	        .setStyle(new NotificationCompat.BigTextStyle()
	        .bigText(msg))
	        .setContentText(msg);

	        mBuilder.setContentIntent(contentIntent);
	        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	   }
	
	
		private HashMap<String, String> parsePushMessageResponse(String response)
				throws JSONException {
			JSONObject jObject = new JSONObject(response);
			if (jObject.has("statement")) {
				JSONArray jArray = jObject.getJSONArray("statement");
				JSONObject jObject2 = jArray.getJSONObject(0);
				if (jArray.length() > 0) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("message", jObject2.getString("message"));
					map.put("msg_type", jObject2.getString("msg_type"));
				
					
					return map;
				}
			}
			return null;
		}
}


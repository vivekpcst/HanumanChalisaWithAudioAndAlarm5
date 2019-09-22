package com.dzo.HanumanChalisaWithAudioAndAlarm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import static com.dzo.HanumanChalisaWithAudioAndAlarm.firebase.FirebaseNotificationService.CHANNEL_ID;

/**
 * Created by Vivek Vsking on 4/26/2019  2:58 PM
 * E-mail :- vivekpcst.kumar@gmail.com
 * Mobile :- +91-7982591863
 */
public class NotificationService extends IntentService {

    private NotificationManager notificationManager;
    private PendingIntent pendingIntent;
    private static int NOTIFICATION_ID = 1;
    NotificationCompat.Builder notification;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public NotificationService(String name) {
        super(name);
    }

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String  eventName="";
        if (intent != null) {
            eventName=intent.getStringExtra("eventName");
        }


        Context context = this.getApplicationContext();



        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent mIntent = new Intent(this,Play.class);
        Bundle bundle = new Bundle();
        bundle.putString("test", "test");
        mIntent.putExtras(bundle);
        pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Resources res = this.getResources();
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setTicker("vsking")
                .setAutoCancel(true)
                .setPriority(8)
                .setSound(soundUri)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.icon))
                .setContentTitle("Hanuman chalisa")
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.FLAG_AUTO_CANCEL
                | Notification.DEFAULT_LIGHTS)
                .setContentText(eventName);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            notification.setSmallIcon(R.drawable.ic_icon_trans);
            notification.setColor(getResources().getColor(R.color.orangelight));
        }else {
            notification.setSmallIcon(R.drawable.icon);
        }
       /* notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
        notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
        notification.ledARGB = 0xFFFFA500;
        notification.ledOnMS = 800;
        notification.ledOffMS = 1000;*/
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name ="" +
                    "Hanuman chalisa";
            int importance=android.app.NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel=new NotificationChannel(CHANNEL_ID,name,android.app.NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification.build());
        Log.i("notif","Notifications sent.");

    }
}

package com.dzo.HanumanChalisaWithAudioAndAlarm.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dzo.HanumanChalisaWithAudioAndAlarm.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Vivek Vsking on 4/2/2019  2:42 PM
 * E-mail :- vivekpcst.kumar@gmail.com
 * Mobile :- +91-7982591863
 */
public class FirebaseNotificationService extends FirebaseMessagingService {

    public static String CHANNEL_ID="hanuman_chalisa001";


    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        sendRegTokenToServer(token);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("remoteMsg: ",""+remoteMessage.getFrom());
        if(remoteMessage.getData().size()>0){
            Log.d("remoteMessageData: ",""+remoteMessage.getData());
        }

        if(remoteMessage.getNotification()!=null){
            Log.d("NotifictionBody: ",remoteMessage.getNotification().getBody());
            String title=remoteMessage.getNotification().getTitle();
            showNotification(title,remoteMessage.getNotification().getBody());
        }

    }

    private void showNotification(String title,String body) {
        Bitmap icon= BitmapFactory.decodeResource(this.getResources(),R.drawable.icon);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,CHANNEL_ID);
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setVibrate(new long[]{500,500,1000,1000,500});
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setChannelId(CHANNEL_ID);
        builder.setLargeIcon(icon);
        builder.setAutoCancel(true);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            builder.setSmallIcon(R.drawable.ic_icon_trans);
            builder.setColor(getResources().getColor(R.color.orangelight));
        }else {
            builder.setSmallIcon(R.drawable.icon);

        }

        NotificationManager notificationManager= (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name ="" +
                    "Hanuman chalisa";
            int importance=NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel=new NotificationChannel(CHANNEL_ID,name,importance);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0,builder.build());

    }

    public static void sendRegTokenToServer(String token) {



    }
}

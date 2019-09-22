package com.dzo.HanumanChalisaWithAudioAndAlarm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.dzo.HanumanChalisaWithAudioAndAlarm.firebase.FirebaseNotificationService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class Home extends BottomNav {
    @Override
    public int getContentViewId() {
        return R.layout.home_activity;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.navigation_home;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.home_activity,content_frame);
        String token=getCurrentFirebaseToken();
        FirebaseNotificationService.sendRegTokenToServer(token);
    }
    String token="";

    private String getCurrentFirebaseToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("vsking", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();

                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d("vsking", msg);
//                        Toast.makeText(Home.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
     return token;
    }
}

package com.dzo.HanumanChalisaWithAudioAndAlarm;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Loading extends AppCompatActivity {
 private  final int WAIT_TIME=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
       final String s=getLocalClassName();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(s.equals("English")){
                    Intent intent=new Intent(Loading.this,English.class);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(Loading.this,StartManualActivity.class);
                    startActivity(intent);
                }

            }
        },WAIT_TIME);

    }
}

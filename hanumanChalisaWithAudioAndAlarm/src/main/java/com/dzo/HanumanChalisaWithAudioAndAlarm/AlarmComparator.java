package com.dzo.HanumanChalisaWithAudioAndAlarm;

import android.support.annotation.NonNull;

import com.dzo.HanumanChalisaWithAudioAndAlarm.dao.AlarmTimeDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

public class AlarmComparator implements Comparable<AlarmTimeDAO>, Comparator<AlarmTimeDAO> {
    @Override
    public int compareTo(@NonNull AlarmTimeDAO o) {
        String compareNumber = ((AlarmTimeDAO) o).getPeriod();
        return o.getPeriod().compareTo(compareNumber);     }/*

    @Override
    public int compare(AlarmTimeDAO o1, AlarmTimeDAO o2) {
        String str=o1.getAlarmTime();
        String s[]=str.split(":");
        String str1=o2.getAlarmTime();
        String s1[]=str1.split(":");
        String ap[]=str.split(" ");
        String ap1[]=str1.split(" ");
        if(ap[1].equals("AM")&&ap[1].equals("AM")){
            if((Integer.valueOf(s[0])>Integer.valueOf(s1[0]))) {
                return 1;
            }else{
                return -1;
            }
        }
        if(ap[1].equals("AM")&&ap[1].equals("PM")){
            if((Integer.valueOf(s[0])>Integer.valueOf(s1[0]))) {
                return 1;
            }else{
                return -1;
            }        }
        if(ap[1].equals("PM")&&ap[1].equals("AM")){
            if((Integer.valueOf(s[0])>Integer.valueOf(s1[0]))) {
                return 1;
            }else{
                return -1;
            }        }
        if(ap[1].equals("PM")&&ap[1].equals("PM")){
            if((Integer.valueOf(s[0])>Integer.valueOf(s1[0]))) {
                return 1;
            }else{
                return -1;
            }        }



       *//* if((Integer.valueOf(s[0])>Integer.valueOf(s1[0]))&&ap[1].equals("AM")&&ap[1].equals("AM")) {
                return 1;
        }else{
            return -1;
        }*//*
       return  -1;
    }
*/

    @Override
    public int compare(AlarmTimeDAO o1, AlarmTimeDAO o2) {
        try {
            return new SimpleDateFormat("hh:mm a").parse(o1.getAlarmTime()).compareTo(new SimpleDateFormat("hh:mm a").parse(o2.getAlarmTime()));
        } catch (ParseException e) {
            return 0;
        }    }
}

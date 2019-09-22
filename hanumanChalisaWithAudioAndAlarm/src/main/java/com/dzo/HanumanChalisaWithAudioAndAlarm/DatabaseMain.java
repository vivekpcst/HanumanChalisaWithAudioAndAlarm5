package com.dzo.HanumanChalisaWithAudioAndAlarm;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Vivek Vsking on 4/24/2019  3:42 PM
 * E-mail :- vivekpcst.kumar@gmail.com
 * Mobile :- +91-7982591863
 */
public class DatabaseMain extends SQLiteOpenHelper {

    private static String DB_PATH="data/data/com.dzo.HanumanChalisaWithAudioAndAlarm/databases/";
    private static String DB_NAME="vskingEdu";//"HcEvents_1";
    private SQLiteDatabase dbObj;
    private final Context context;
    private static final int DB_VERSION=1;


    public DatabaseMain(@Nullable Context context){//, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context=context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*try {
            createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>oldVersion) {
            try {
                copyDB();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void createDatabase() throws IOException{

        boolean dbExist = checkDB();

        if(dbExist){

        }else{

            this.getReadableDatabase();
            Log.i("Readable..........","ends");
            try{
                copyDB();
                Log.i("Copy.......","ends");
            }catch (IOException e){
                throw new Error("Error copying database. ");
            }

        }

    }
    private boolean checkDB(){/*
        SQLiteDatabase checkDB = null;
        try{
            String path = DB_PATH + DB_NAME;
            Log.i("myPath ......",path);
            checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
            Log.i("myPath ......",path);
            if (checkDB!=null)
            {
                Cursor c= checkDB.rawQuery("SELECT * FROM HcEvents", null);
                Log.i("Cursor.......",c.getString(0));
                c.moveToFirst();
                String contents[]=new String[80];
                int flag=0;
                while(! c.isAfterLast())
                {
                    String temp="";
                    String s2=c.getString(0);
                    String s3=c.getString(1);
                    temp=temp+"\n event_date:"+s2+"\t event:"+s3;
                    contents[flag]=temp;
                    flag=flag+1;
                    Log.i("DB values.........",temp);
                    c.moveToNext();
               }
            }
            else
            {
            return false;
            }
        }catch(SQLiteException e){
            e.printStackTrace();
        }
        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null ? true : false;*/


        /**
         * Does not open the database instead checks to see if the file exists
         * also creates the databases directory if it does not exists
         * (the real reason why the database is opened, which appears to result in issues)
         */

        File db = new File(context.getDatabasePath(DB_NAME).getPath()); //Get the file name of the database
        Log.d("DBPATH","DB Path is " + db.getPath()); //TODO remove for Live App
        if (db.exists()) return true; // If it exists then return doing nothing

        // Get the parent (directory in which the database file would be)
        File dbdir = db.getParentFile();
        // If the directory does not exits then make the directory (and higher level directories)
        if (!dbdir.exists()) {
            db.getParentFile().mkdirs();
            dbdir.mkdirs();
        }
        return false;


    }
    public void copyDB() throws IOException{
        try {
            Log.i("inside copyDB....","start");
            InputStream ip =  context.getAssets().open(DB_NAME/*+".db"*/);
            Log.i("Input Stream....",ip+"");
            String op=  DB_PATH  +  DB_NAME ;
            OutputStream output = new FileOutputStream( op);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = ip.read(buffer))>0){
                output.write(buffer, 0, length);
                Log.i("Content.... ",length+"");
            }
            output.flush();
            output.close();
            ip.close();
        }
        catch (IOException e) {
            Log.v("error", e.toString());
        }
    }
    public void openDB() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        dbObj = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        Log.i("open DB......",dbObj.toString());
    }
    @Override
    public synchronized void close() {
        if(dbObj != null)
            dbObj.close();
        super.close();
    }
}

package com.time.time;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.text.SimpleDateFormat;


/**
 * Created by Jerry on 2017/9/22.
 */

public class DetectionService extends AccessibilityService {
    final static String TAG = "detectionservice";
    long startTime=System.currentTimeMillis(),endTime;
    static String foregroundPackageName;
    String firstName="com.android.launcher3";
    String secondName;
    //List<appHistory> apphistory;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: 666");
        return Service.START_STICKY; // 根据需要返回不同的语义值
    }


    /**
     * 重载辅助功能事件回调函数，对窗口状态变化事件进行处理
     * @param event
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            /*
             * 如果 与 DetectionService 相同进程，直接比较 foregroundPackageName 的值即可
             * 如果在不同进程，可以利用 Intent 或 bind service 进行通信
             */
            //foregroundPackageName = event.getPackageName().toString();

            /*
             * 基于以下还可以做很多事情，比如判断当前界面是否是 Activity，是否系统应用等，
             * 与主题无关就不再展开。
             */
            // secondName = new ComponentName(event.getPackageName().toString(),
            //        event.getClassName().toString());

            SimpleDateFormat sDateFormat    =   new SimpleDateFormat("yyyyMMdd");
            String    date    =    sDateFormat.format(new    java.util.Date());
            Log.d(TAG, "time"+date);
            MyDatabaseHelper dbHelper=new MyDatabaseHelper(this,"appHistory.db",null,1);
            SQLiteDatabase db=dbHelper.getWritableDatabase();
            //db.execSQL("create table time"+date+"(name text)");
            secondName=event.getPackageName().toString();

            //appHistory app=new appHistory();


            endTime=System.currentTimeMillis();
            Cursor cursor;

            db.execSQL("create table if not exists  time"+date+"(appName text,useFrequency text,useTime text)");
            try {
                //Cursor c = db.select("appName","useFrequency","useTime").where("appName=?" , firstName).find(appHistory.class);
                cursor=db.rawQuery("select * from time"+date+" where appName=?",new String[]{firstName});
            }catch (Exception e){
                throw  e;
            }

            if(!cursor.moveToFirst()){
                db.execSQL("insert into time"+date+"(appName,useFrequency,useTime) values(?,?,?)",new String[]{firstName,"1","0"});
            }
            else if(!firstName.equals(secondName)){
                String usefrequency=String.valueOf(Integer.valueOf(cursor.getString(cursor.getColumnIndex("useFrequency")))+1);
                String usetime=String.valueOf(Long.valueOf(cursor.getString(cursor.getColumnIndex("useTime")))+(endTime-startTime)/1000);

                db.execSQL("update time"+date+" set useFrequency=?,useTime=? where appName=?",new String[]{usefrequency,usetime,firstName});
            }
            else{}
            startTime=endTime;
            firstName=secondName;
        }
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected  void onServiceConnected() {
        super.onServiceConnected();
    }
}

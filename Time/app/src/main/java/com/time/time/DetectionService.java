package com.time.time;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
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
    ScreenStatusReceiver mScreenStatusReceiver;
    Cursor cursor;

    MyDatabaseHelper dbHelper;
    SQLiteDatabase db;

    SimpleDateFormat sDateFormat;
    String    date;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.START_STICKY; // 根据需要返回不同的语义值
    }

    @Override
    public void onCreate() {
        // Log.d(TAG, "onStartCommand: 666");
        super.onCreate();
        dbHelper=new MyDatabaseHelper(this,"appHistory.db",null,1);
        db=dbHelper.getWritableDatabase();

        sDateFormat= new SimpleDateFormat("yyyyMMdd");
        date = sDateFormat.format(new    java.util.Date());
        registSreenStatusReceiver();
        db.execSQL("create table if not exists  time"+date+"(appName text,useFrequency text,useTime text)");
        db.execSQL("create table if not exists fre"+date+"(wakeFrequency text,totalUseTime text)");

        //在前台通知中显示当天唤醒次数和使用总时间
        Cursor cursor5=db.rawQuery("select * from fre"+date,null);
        String fre="0";
        String time="0";
        //任何一次查询数据库得到的cursor，必须先判是否为空
        if(cursor5.moveToFirst()){
            fre=cursor5.getString(cursor5.getColumnIndex("wakeFrequency"));
            time=String.valueOf(Long.valueOf(cursor5.getString(cursor5.getColumnIndex("totalUseTime")))/60);
        }
        else{}
        //当用户点通知时，进入mainactivity
       startForeground(1,getNotification(fre,time));
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
            /*
             * 基于以下还可以做很多事情，比如判断当前界面是否是 Activity，是否系统应用等，
             * 与主题无关就不再展开。
             */

            //获得包名。所以，数据库中存储的实际上是包名，显示出来时，再通过包名获得应用名
            secondName=event.getPackageName().toString();

            endTime=System.currentTimeMillis();

            try {
                cursor=db.rawQuery("select * from time"+date+" where appName=?",new String[]{firstName});
            }catch (Exception e){
                throw  e;
            }

            if(!cursor.moveToFirst()){
                db.execSQL("insert into time"+date+"(appName,useFrequency,useTime) values(?,?,?)",new String[]{firstName,"0","0"});
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
    public void onInterrupt() {}

    @Override
    protected  void onServiceConnected() {
        super.onServiceConnected();
    }

    private void registSreenStatusReceiver() {
        mScreenStatusReceiver = new ScreenStatusReceiver();
        IntentFilter screenStatusIF = new IntentFilter();
        screenStatusIF.addAction(Intent.ACTION_SCREEN_ON);
        screenStatusIF.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mScreenStatusReceiver, screenStatusIF);
    }

    //唤醒屏幕时，wakeFrequency+1
    private String writeFre(){
        Cursor cursor1;
        String wakefrequency="0";
        cursor1=db.rawQuery("select * from fre"+date,null);
        if(!cursor1.moveToFirst()){
            db.execSQL("insert into fre"+date+"(wakeFrequency,totalUseTime) values(?,?)",new String[]{"0","0"});
        }
        else{
            wakefrequency=String.valueOf(Integer.valueOf(cursor1.getString(cursor1.getColumnIndex("wakeFrequency")))+1);
            String usetime=cursor1.getString(cursor1.getColumnIndex("totalUseTime"));

            db.execSQL("update fre"+date+" set wakeFrequency=?,totalUseTime=?",new String[]{wakefrequency,usetime});
        }
        return wakefrequency;
    }

    //锁屏时，统计当天使用总时间
    private String writeTime(){
        Cursor cursor2;
        long time=0;
        String stime="0";

        cursor2=db.rawQuery("select * from time"+date,null);
        if(cursor2.moveToFirst()){
            do{
                time+=Long.valueOf(cursor2.getString(cursor2.getColumnIndex("useTime")));
            }while(cursor2.moveToNext());
            stime=String.valueOf(time);
            db.execSQL("update fre"+date+" set totalUseTime=?",new String[]{stime});
        }
        else{}
        return stime;
    }

    //每次唤醒屏幕时，跟新通知
    private void renewNotification(){
        Cursor cursor5=db.rawQuery("select * from fre"+date,null);
        String fre="0";
        String time="0";
        if(cursor5.moveToFirst()){
            fre=cursor5.getString(cursor5.getColumnIndex("wakeFrequency"));
            time=String.valueOf(Long.valueOf(cursor5.getString(cursor5.getColumnIndex("totalUseTime")))/60);
        }
        else{}
//更新通知
        getNotificationManager().notify(1,getNotification(fre,time));
        Log.d(TAG, "renewNotific!!!!");
    }

    //收到唤醒屏幕，锁屏的广播
    class ScreenStatusReceiver extends BroadcastReceiver {
        String SCREEN_ON = "android.intent.action.SCREEN_ON";
        String SCREEN_OFF = "android.intent.action.SCREEN_OFF";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (SCREEN_ON.equals(intent.getAction())) {
                writeFre();
                Log.d(TAG, "renew!!!!!!!");
                renewNotification();
            }
            else if (SCREEN_OFF.equals(intent.getAction())) {
                writeTime();
            }
        }
    }

    private NotificationManager getNotificationManager(){
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String fre,String time){
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ali);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher));
        builder.setContentIntent(pi);
        builder.setContentTitle("我的一天");
        builder.setContentText("解锁"+fre+"次,使用"+time+"分钟");

        return builder.build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mScreenStatusReceiver);
    }
}
package com.time.time;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import java.text.SimpleDateFormat;


/**
 * Created by Jerry on 2017/9/22.
 */
//测试github
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
        dbHelper=new MyDatabaseHelper(this,"appHistory.db",null,1);
        db=dbHelper.getWritableDatabase();

        sDateFormat= new SimpleDateFormat("yyyyMMdd");
        date = sDateFormat.format(new    java.util.Date());
        registSreenStatusReceiver();
        db.execSQL("create table if not exists  time"+date+"(appName text,useFrequency text,useTime text)");
        db.execSQL("create table if not exists fre"+date+"(wakeFrequency text,totalUseTime text)");
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


            //db.execSQL("create table time"+date+"(name text)");
            secondName=event.getPackageName().toString();

            endTime=System.currentTimeMillis();

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

    private void registSreenStatusReceiver() {
        mScreenStatusReceiver = new ScreenStatusReceiver();
        IntentFilter screenStatusIF = new IntentFilter();
        screenStatusIF.addAction(Intent.ACTION_SCREEN_ON);
        screenStatusIF.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mScreenStatusReceiver, screenStatusIF);
    }

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
    private String writeTime(){
        Cursor cursor2;
        long time=0;
        String stime="0";

        cursor2=db.rawQuery("select * from time"+date,null);
        if(!cursor2.moveToFirst()){

        }
        else{

            do{
                time+=Long.valueOf(cursor2.getString(cursor2.getColumnIndex("useTime")));
            }while(cursor2.moveToNext());
            stime=String.valueOf(time);
            db.execSQL("update fre"+date+" set totalUseTime=?",new String[]{stime});
        }
    return stime;
    }

    class ScreenStatusReceiver extends BroadcastReceiver {
        String SCREEN_ON = "android.intent.action.SCREEN_ON";
        String SCREEN_OFF = "android.intent.action.SCREEN_OFF";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (SCREEN_ON.equals(intent.getAction())) {
                writeFre();
            }
            else if (SCREEN_OFF.equals(intent.getAction())) {
                writeTime();

            }
        }
    }
}

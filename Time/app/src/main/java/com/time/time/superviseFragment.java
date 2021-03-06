package com.time.time;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by guoweijie on 2017/9/26.
 */
public class superviseFragment extends Fragment    {
    private static int state=0;
    private ImageButton swtichButton;
    private  TextView nowTime ;
    private static final int msgKey1 = 1;
    private  TextView getTime;
    private TextView diffTime;
    private long time_start;
    private TimeThread timeThread;
    private  int threadState =1;
    private Date date_start;
    private Date date_end;
    private String strDateStart;
    private String strDateEnd;
    private String strDateDiff;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        return inflater.inflate(R.layout.supervisefragment,null);


    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);



        swtichButton = (ImageButton) getActivity().findViewById(R.id.swtich);



        nowTime = (TextView)getActivity().findViewById(R.id.now_time);
        getTime = (TextView)getActivity().findViewById(R.id.get_time);
        diffTime = (TextView)getActivity().findViewById(R.id.diff_time);
        timeThread = new TimeThread();
        MainActivity activity = (MainActivity) getActivity();
        /*activity.rg_tab_bar.setVisibility(View.INVISIBLE);
        activity.div_tab_bar.setVisibility(View.INVISIBLE);*/

        if(state==1){
            swtichButton.setBackgroundResource(R.drawable.button_on);
            }
        if(state==0){
            swtichButton.setBackgroundResource(R.drawable.button_off);}





        swtichButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drawTime();


            }
        });
    }
        public class TimeThread extends Thread{
            @Override
            public void run(){
                super.run();
                do{
                    try{
                        Thread.sleep(1000);
                        Message message = new Message();
                        message.what = msgKey1;
                        mHandler.sendMessage(message);
                        Log.i("thread","success");

                    }catch (InterruptedException e){
                        e.printStackTrace();;
                    }
                }while (threadState==1);
            }
        }
        private  Handler mHandler = new Handler(){
        @Override
            public  void handleMessage(Message msg){
            super.handleMessage(msg);
            switch(msg.what){
                case msgKey1 :
                    long time = System.currentTimeMillis();
                    date_end = new Date(time);
                    SimpleDateFormat format =new SimpleDateFormat("yyyy.MM.dd HH:mm:ss EEE");
                    strDateEnd = format.format(date_end);
                    nowTime.setText("现在时间:"+strDateEnd);
                    Log.d("nowTime=",strDateEnd);
                    break;
                default:
                    break;


            }

        }
        };


    public  void drawTime(){
        Intent intent = new Intent("com.time.time.BROADCAST");
        if(state==0){
            swtichButton.setBackgroundResource(R.drawable.button_on);
            state=1;
            intent.putExtra("state",state);
            getActivity().sendBroadcast(intent);
            Toast.makeText(getActivity(), "开启功能", Toast.LENGTH_SHORT).show();
            time_start = System.currentTimeMillis();
            date_start = new Date(time_start);
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss EEE");
            strDateStart=format.format(date_start);
            getTime.setText("开始时间："+strDateStart);
            timeThread.start();
            threadState = 1;





        }
        else if(state==1){
            swtichButton.setBackgroundResource(R.drawable.button_off);
            state=0;
            intent.putExtra("state",state);
            getActivity().sendBroadcast(intent);
            Toast.makeText(getActivity(), "关闭功能", Toast.LENGTH_SHORT).show();
            threadState = 0;

           // long diff = date_end.getTime()-date_start.getTime();
            //long diff = 23213213;


            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss ");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            //strDateDiff = format.format(diff);
            //diffTime.setText("共计时间为"+strDateDiff);
            Log.i("11111","time="+strDateEnd);
            Log.i("11112","time="+strDateStart);
            Log.i("11113","time="+strDateDiff);
        }
    }


       /* winStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                if (Build.VERSION.SDK_INT >= 23) {
                    if(!Settings.canDrawOverlays(getActivity().getApplicationContext())) {
                        //启动Activity让用户授权
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        getActivity().startActivity(intent);
                        return;
                    } else {
                        //执行6.0以上绘制代码
                    }
                } else {
                    //执行6.0以下绘制代码
                }
                Intent intent = new Intent(getActivity(), windowService.class);
                //启动MyService
                getActivity().startService(intent);
                getActivity().finish();}

        });*/
        /*winStop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(getActivity(), windowService.class);
                //启动MyService
                getActivity().startService(intent);
            }

        });*/
}

package com.time.time;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by guoweijie on 2017/9/26.
 */
public class superviseFragment extends Fragment    {
    private static int state=0;
    private ImageButton swtich;
    private ServiceConnection conn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        return inflater.inflate(R.layout.supervisefragment,null);


    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);



        swtich = (ImageButton) getActivity().findViewById(R.id.swtich);
       // winStop = (Button) getActivity().findViewById(R.id.remove);
        if(state==1){
            swtich.setBackgroundResource(R.drawable.button_on);}
        if(state==0){
            swtich.setBackgroundResource(R.drawable.button_off);}

        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Toast.makeText(getContext(), "onServiceConnected", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Toast.makeText(getContext(), "offServiceConnected", Toast.LENGTH_SHORT).show();

            }
        };


        swtich.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.time.time.BROADCAST");



                if(state==0){
                    swtich.setBackgroundResource(R.drawable.button_on);
                    state=1;
                    intent.putExtra("state",state);
                    getActivity().sendBroadcast(intent);
                    Toast.makeText(getActivity(), "开启功能", Toast.LENGTH_LONG).show();


                }
                else if(state==1){
                    swtich.setBackgroundResource(R.drawable.button_off);
                    state=0;
                    intent.putExtra("state",state);
                    getActivity().sendBroadcast(intent);
                    Toast.makeText(getActivity(), "关闭功能", Toast.LENGTH_LONG).show();
                }
            }
        });

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
}
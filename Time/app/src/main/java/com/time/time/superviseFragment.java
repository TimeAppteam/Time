package com.time.time;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by guoweijie on 2017/9/26.
 */
public class superviseFragment extends Fragment {
    private  View view;
    private Button winStart,winStop;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        return inflater.inflate(R.layout.supervisefragment,null);


    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){

        super.onActivityCreated(savedInstanceState);

        winStart = (Button) getActivity().findViewById(R.id.start);
        winStop = (Button) getActivity().findViewById(R.id.remove);


        winStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "success2", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), controlService.class);
                //启动MyService

                getActivity().startService(intent);
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
        winStop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(getActivity(), windowService.class);
                //启动MyService
                getActivity().startService(intent);
            }

        });
}
}
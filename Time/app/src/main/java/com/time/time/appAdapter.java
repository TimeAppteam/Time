package com.time.time;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jerry on 2017/9/25.
 */

public class appAdapter extends ArrayAdapter {
    private int resourceId;
    PackageManager pm;

    public appAdapter(Context context, int textViewResourceId, List<appInfomation> objects){
        super(context,textViewResourceId,objects);
         pm= context.getPackageManager();
        resourceId=textViewResourceId;
    }

    @NonNull
    @Override
    //设置app_item中的内容
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        appInfomation appinfo=(appInfomation)getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView appicon=(ImageView)view.findViewById(R.id.app_icon);
        TextView appname=(TextView)view.findViewById(R.id.app_name);
        TextView apptime=(TextView)view.findViewById(R.id.app_time);

    //通过包名获得appicon
        try {
            Drawable da = pm.getApplicationIcon(appinfo.getPackagename());
            appicon.setImageDrawable(da);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

        appname.setText(appinfo.getAppName());
        apptime.setText(appinfo.getUseTime());//此为秒单位，还要换算成分钟

        return view;
    }
}

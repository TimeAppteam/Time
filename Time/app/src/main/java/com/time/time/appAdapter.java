package com.time.time;

import android.content.Context;
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

    public appAdapter(Context context, int textViewResourceId, List<appInfomation> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        appInfomation appinfo=(appInfomation)getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView appicon=(ImageView)view.findViewById(R.id.app_icon);
        TextView appname=(TextView)view.findViewById(R.id.app_name);

        //appicon.setImageResource(appinfo.getAppIcon());
        appname.setText(appinfo.getAppName());
        return view;
    }
}

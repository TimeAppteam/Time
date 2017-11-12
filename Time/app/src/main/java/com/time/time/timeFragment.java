package com.time.time;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Jerry on 2017/10/23.
 */

public class timeFragment extends Fragment {
    private static final String TAG = "timeFragment";
    private List<appInfomation> appinfolist=new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.timefragment,container,false);
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arr);

        //设置显示的数据

        appinfolist=initAppInfoByTime();
        //Toast.makeText(getContext(),
        //     "我相信" + spinner.getItemIdAtPosition(spinner.getSelectedItemPosition()), Toast.LENGTH_LONG).show();

        //注册事件
        appAdapter adapter=new appAdapter(getContext(), R.layout.app_item,appinfolist);
        //注意，必须是view.findViewById
        //没有view. 则错误
        ListView listView=(ListView) view.findViewById(R.id.rb_count_list_view);
        listView.setAdapter(adapter);
        return view;

    }

    public List<appInfomation> initAppInfoByTime(){
        List<appInfomation> appinfolist=new ArrayList<>();
        SimpleDateFormat sDateFormat    =   new SimpleDateFormat("yyyyMMdd");
        String    date    =    sDateFormat.format(new    java.util.Date());
        MyDatabaseHelper dbHelper=new MyDatabaseHelper(getContext(),"appHistory.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor;
        cursor=db.rawQuery("select * from time"+date,null);
//在
        if(cursor.moveToFirst()){
            do{
                String packagename=cursor.getString(cursor.getColumnIndex("appName"));
                String usetime=cursor.getString(cursor.getColumnIndex("useTime"));
                // String usefrequency=cursor.getString(cursor.getColumnIndex("useFrequency"));
                String usefrequency=null;
                appInfomation ai;
                PackageManager pm=getContext().getPackageManager();
                try {
                    ai = new appInfomation(pm.getApplicationLabel(pm.getApplicationInfo(packagename, PackageManager.GET_META_DATA)).toString()
                            , usetime, usefrequency, packagename);

                    appinfolist.add(ai);
                }catch  (PackageManager.NameNotFoundException e) {
                }

            }while(cursor.moveToNext());
        }
        //对list中元素按使用时间排序
        Collections.sort(appinfolist,new SortByTime());
        return appinfolist;
    }
}

//比较器,便于排序


class SortByTime implements Comparator {
    public int compare(Object obj1,Object obj2){
        appInfomation appinfo1=(appInfomation) obj1;
        appInfomation appinfo2=(appInfomation) obj2;
        if(Long.valueOf(appinfo1.getUseTime())>Long.valueOf(appinfo2.getUseTime()))
            return -1;
        else
            return 1;
    }
}
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
 * Created by Jerry on 2017/9/24.
 */
/*大问题 数据更新有问题*/
public class countFragment extends Fragment {
    private List<appInfomation> appinfolist=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.countfragment,container,false);


        List<appInfomation> appinfolist=new ArrayList<>();
        appinfolist=initAppInfoByTime();
        appAdapter adapter=new appAdapter(getContext(), R.layout.app_item,appinfolist);
        //注意，必须是view.findViewById
        //没有view. 则错误
        ListView listView=(ListView) view.findViewById(R.id.rb_count_list_view);
        listView.setAdapter(adapter);
        return view;

    }

    private List<appInfomation> initAppInfoByFre(){
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
                //String usetime=cursor.getString(cursor.getColumnIndex("useTime"));
                String usetime=null;
                String usefrequency=cursor.getString(cursor.getColumnIndex("useFrequency"));
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
        //对list中元素按使用频率排序
        Collections.sort(appinfolist,new SortByFre());

        return appinfolist;
    }

    private List<appInfomation> initAppInfoByTime(){
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

//两个比较器
class SortByFre implements Comparator {
    public int compare(Object obj1,Object obj2){
        appInfomation appinfo1=(appInfomation) obj1;
        appInfomation appinfo2=(appInfomation) obj2;
        if(Integer.valueOf(appinfo1.getUseFrequency())>Integer.valueOf(appinfo2.getUseFrequency()))
            return -1;
        else
            return 1;
    }
}

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

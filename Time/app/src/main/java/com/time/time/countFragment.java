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
        initAppInfo();
        appAdapter adapter=new appAdapter(getContext(), R.layout.app_item,appinfolist);
        //注意，必须是view.findViewById
        //没有view. 则错误
        ListView listView=(ListView) view.findViewById(R.id.rb_count_list_view);
        listView.setAdapter(adapter);
        return view;
    }

    private void initAppInfo(){
        SimpleDateFormat sDateFormat    =   new SimpleDateFormat("yyyyMMdd");
        String    date    =    sDateFormat.format(new    java.util.Date());
        MyDatabaseHelper dbHelper=new MyDatabaseHelper(getContext(),"appHistory.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor;
        cursor=db.rawQuery("select * from time"+date,null);

        if(cursor.moveToFirst()){
            do{
                String packagename=cursor.getString(cursor.getColumnIndex("appName"));
                String usetime=cursor.getString(cursor.getColumnIndex("useTime"));
                String usefrequency=cursor.getString(cursor.getColumnIndex("useFrequency"));
                appInfomation ai;
                PackageManager pm=getContext().getPackageManager();
                try {
                     ai = new appInfomation(pm.getApplicationLabel(pm.getApplicationInfo(packagename, PackageManager.GET_META_DATA)).toString()
                            , usetime, usefrequency, 1);
                    appinfolist.add(ai);
                }catch  (PackageManager.NameNotFoundException e) {
                }

            }while(cursor.moveToNext());
        }
    }
}

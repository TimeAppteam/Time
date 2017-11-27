package com.time.time;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


/**
 * Created by Jerry on 2017/11/10.
 */

public class totalFragment extends Fragment{
    private Spinner spinner;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.totalfragment, container, false);
        replaceFragment(new totalTimeFragment());
        //根据ID获取对象
        spinner = (Spinner) view.findViewById(R.id.spinnerAttotalfragment);
        //spinner中显示的数据
        final String[] arr = {
                "使用时间",
                "解锁次数"
        };
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, arr);
        //dropdown_style：下拉框中文字的样式
        arrayAdapter.setDropDownViewResource(R.layout.dropdown_style);
        spinner.setAdapter(arrayAdapter);
        //注册事件
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Spinner spinner = (Spinner) parent;
                if (spinner.getItemAtPosition(position).equals("使用时间")) {
                    replaceFragment(new totalTimeFragment());
                } else {
                    replaceFragment(new totalFreFragment());
                }
            }

            //有什么用？？？？？？
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

                //Toast.makeText(getContext(), "没有改变的处理", Toast.LENGTH_LONG).show();
            }
        });
        return  view;
    }
    private   void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.showtotal_layout,fragment);
        transaction.commit();
    }
}

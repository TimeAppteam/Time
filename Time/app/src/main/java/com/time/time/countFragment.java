package com.time.time;

import android.os.Bundle;
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
 * Created by Jerry on 2017/9/24.
 */
/*大问题 数据更新有问题*/
/*将显示的内容放在独立的fragment中，便于切换*/
public class countFragment extends Fragment {
    private static final String TAG = "countFragment";
    private Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.countfragment, container, false);
        replaceFragment(new timeFragment());
        //根据ID获取对象
        spinner = (Spinner) view.findViewById(R.id.spinner1);
        //spinner中显示的数据
        final String[] arr = {
                "时间",
                "次数"
        };
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arr);
        spinner.setAdapter(arrayAdapter);
        //注册事件
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Spinner spinner = (Spinner) parent;
                if (spinner.getItemAtPosition(position).equals("时间")) {
                    replaceFragment(new timeFragment());
                } else {
                    replaceFragment(new freFragment());
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
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.show_layout,fragment);
        transaction.commit();
    }
}
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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.BatchUpdateException;

/**
 * Created by Jerry on 2017/9/24.
 */
/*大问题 数据更新有问题*/
/*将显示的内容放在独立的fragment中，便于切换*/
public class countFragment extends Fragment {
    private static final String TAG = "countFragment";

    //countfragment弄成三层fragment嵌套，最外面countfragment,中间spinner，最里面是数据显示
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        //必须调用replacefragment才能换fragment
        View view = inflater.inflate(R.layout.countfragment, container, false);
        replaceFragment(new dayFragment());

        Button app=(Button)view.findViewById(R.id.app);
        Button total=(Button)view.findViewById(R.id.total);
        app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new dayFragment());
            }
        });
        total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new totalFragment());
            }
        });
        return  view;
    }

    //更换fragment
    private   void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.middle_fragment,fragment);
        transaction.commit();
    }
}
package com.time.time;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idtk.smallchart.chart.BarChart;
import com.idtk.smallchart.data.BarData;
import com.idtk.smallchart.interfaces.iData.IBarData;

import java.util.ArrayList;

/**
 * Created by Jerry on 2017/11/10.
 */

public class totalTimeFragment extends BaseFragment {
    private ArrayList<IBarData> mDataList = new ArrayList<>();
    private BarData mBarData = new BarData();
    private ArrayList<PointF> mPointFList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.totaltimefragment, container, false);
        BarChart barChart = (BarChart) view.findViewById(R.id.barChartattime);
        initData();
        barChart.setDataList(mDataList);
        barChart.setXAxisUnit("周");
        barChart.setYAxisUnit("时间(分)");
        return view;
    }

    private void initData() {
        for (int i = 0; i < 7; i++) {
            mPointFList.add(new PointF(points[i][0], points[i][1]));
        }
        mBarData.setValue(mPointFList);
        mBarData.setColor(Color.CYAN);
        mBarData.setPaintWidth(pxTodp(5));
        mBarData.setTextSize(pxTodp(10));
        mDataList.add(mBarData);
    }
}

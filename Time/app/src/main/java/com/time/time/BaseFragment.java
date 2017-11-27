package com.time.time;

import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by Jerry on 2017/11/10.
 */

public class BaseFragment extends Fragment {
    protected float[][] points = new float[][]{{1, 10}, {2, 47}, {3, 11}, {4, 38}, {5, 9}, {6, 52}, {7, 14}, {8, 37}, {9, 29},
            {10, 31}};

    protected float pxTodp(float value) {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float valueDP =TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,metrics);
        return valueDP;
    }

}

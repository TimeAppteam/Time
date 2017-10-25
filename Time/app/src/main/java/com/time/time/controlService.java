package com.time.time;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class controlService extends AccessibilityService {

    final static String TAG = "DetectionService";
    final static String PKG = "com.example.guoweijie.control";




    static String foregroundPackageName;
    public static boolean isForegroundPkgViaDetectionService(String packageName) {
        Log.i(TAG,"wwwwww");
        boolean a = packageName.equals(DetectionService.foregroundPackageName);

        return packageName.equals(DetectionService.foregroundPackageName);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");

        return 0;
    }

    /**
     * 重载辅助功能事件回调函数，对窗口状态变化事件进行处理
     * @param event
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            /*
             * 如果 与 DetectingService 相同进程，直接比较 foregroundPackageName 的值即可
             * 如果在不同进程，可以利用 Intent 或 bind service 进行通信
             */
            foregroundPackageName = event.getPackageName().toString();

            /*
             * 基于以下还可以做很多事情，比如判断当前界面是否是 Activity，是否系统应用等，
             * 与主题无关就不再展开。
             */
            ComponentName cName = new ComponentName(event.getPackageName().toString(),
                    event.getClassName().toString());
            if(isForegroundPkgViaDetectionService(PKG)){
                Log.i(TAG,"true");

            }
            else {Log.i(TAG,"false");
                /*Intent intent = new Intent(getBaseContext(), MainActivity.class);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);// "android.intent.action.MAIN"
                intent.addCategory(Intent.CATEGORY_HOME); //"android.intent.category.HOME"
                startActivity(intent);}*/}

            Log.d(TAG, "onAccessibilityEvent: "+foregroundPackageName);
        }
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected  void onServiceConnected() {
        super.onServiceConnected();
    }


}


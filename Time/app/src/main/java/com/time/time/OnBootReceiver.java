package com.time.time;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Jerry on 2017/9/22.
 */

public class OnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

            // 开启应用
           // Intent sintent = context.getPackageManager().getLaunchIntentForPackage( "app应用包名" );
           // context.startActivity( sintent );

            // 开启服务代码
            context.startService( new Intent(context, DetectionService.class) );

        }

        
    }


}

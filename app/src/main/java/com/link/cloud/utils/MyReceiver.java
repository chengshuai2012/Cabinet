package com.link.cloud.utils;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2017/9/8.
 */

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver()
    {
    }
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            Intent start = new Intent(Intent.ACTION_MAIN);
            start.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName("com.link.cloud", "SplashActivity");
            intent.setComponent(cn);
            context.startActivity(intent);
        }
    }

}

package com.link.cloud;

import android.os.Handler;
import android.os.Looper;

import com.zitech.framework.BaseApplication;

public class CabinetApplication extends BaseApplication {

    private Handler mainThreadHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    public void post(Runnable r) {
        mainThreadHandler.post(r);
    }
    public static CabinetApplication getInstance() {
        return (CabinetApplication) BaseApplication.getInstance();
    }
}

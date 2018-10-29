package com.link.cloud;

import android.os.Handler;
import android.os.Looper;

import com.zitech.framework.BaseApplication;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class CabinetApplication extends BaseApplication {

    private Handler mainThreadHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mainThreadHandler = new Handler(Looper.getMainLooper());
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("cabinet.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
    }

    public void post(Runnable r) {
        mainThreadHandler.post(r);
    }
    public static CabinetApplication getInstance() {
        return (CabinetApplication) BaseApplication.getInstance();
    }
}

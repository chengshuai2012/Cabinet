package com.link.cloud;

import android.os.Handler;
import android.os.Looper;

import com.link.cloud.utils.Venueutils;
import com.zitech.framework.BaseApplication;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class CabinetApplication extends BaseApplication {

    private Handler mainThreadHandler;
    public static Venueutils venueUtils;


    public static Venueutils getVenueUtils() {
        synchronized (Venueutils.class) {
            if (venueUtils == null) {
                venueUtils = new Venueutils();
            }
            return venueUtils;
        }
    }



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

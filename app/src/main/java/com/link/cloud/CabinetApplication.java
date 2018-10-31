package com.link.cloud;

import android.os.Handler;
import android.os.Looper;

import com.link.cloud.utils.Venueutils;
import com.zitech.framework.BaseApplication;

import java.io.File;
import java.io.IOException;

import android_serialport_api.SerialPort;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class CabinetApplication extends BaseApplication {

    private Handler mainThreadHandler;
    public static Venueutils venueUtils;

    public SerialPort serialPortOne=null;
    public SerialPort serialPortTwo=null;
    public SerialPort serialPortThree=null;
    private User user;



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
        user = new User();
        mainThreadHandler = new Handler(Looper.getMainLooper());
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("cabinet.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);

        intSerialPort();
    }




    public User getUser() {
        return user;
    }

    private void intSerialPort() {

        try {
            serialPortOne=new SerialPort(new File("/dev/ttysWK1"),9600,0);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            serialPortTwo=new SerialPort(new File("/dev/ttysWK2"),9600,0);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            serialPortThree=new SerialPort(new File("/dev/ttysWK3"),9600,0);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void post(Runnable r) {
        mainThreadHandler.post(r);
    }
    public static CabinetApplication getInstance() {
        return (CabinetApplication) BaseApplication.getInstance();
    }
}

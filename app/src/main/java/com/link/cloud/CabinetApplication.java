package com.link.cloud;

import android.os.Handler;
import android.os.Looper;

import com.iflytek.cloud.Setting;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
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

    public SerialPort serialPortOne = null;
    public SerialPort serialPortTwo = null;
    public SerialPort serialPortThree = null;

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

        intSerialPort();
        intSpeak();


    }


    private void intSpeak(){
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=5b3d9df5");//=号后面写自己应用的APPID
        Setting.setShowLog(false);

    }
    private void intSerialPort() {

        try {
            serialPortOne = new SerialPort(new File("/dev/ttysWK1"), 9600, 0);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            serialPortTwo = new SerialPort(new File("/dev/ttysWK2"), 9600, 0);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            serialPortThree = new SerialPort(new File("/dev/ttysWK3"), 9600, 0);
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

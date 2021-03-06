package com.link.cloud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.iflytek.cloud.Setting;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.link.cloud.activity.SplashActivity;
import com.link.cloud.utils.TimeService;
import com.link.cloud.utils.Venueutils;
import com.orhanobut.logger.Logger;
import com.zitech.framework.BaseApplication;

import java.io.File;
import java.io.IOException;

import android_serialport_api.SerialPort;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class CabinetApplication extends BaseApplication {

    private Handler mainThreadHandler;
    public static Venueutils venueUtils;
    public  int count =0;
    public static final String COUNT_CHANGE = "change_count";
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
        Thread.setDefaultUncaughtExceptionHandler(restartHandler);
        Intent intent = new Intent(getApplicationContext(), TimeService.class);
        startService(intent);
        registerLifeCircle();
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
    private void registerLifeCircle(){
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }
            @Override
            public void onActivityStarted(Activity activity) {
                count++;
                Log.e("onActivityStarted: ",count+"" );
                Intent countIntent = new Intent(COUNT_CHANGE);
                countIntent.putExtra("count",count);
                sendBroadcast(countIntent);
            }
            @Override
            public void onActivityResumed(Activity activity) {

            }
            @Override
            public void onActivityPaused(Activity activity) {

            }
            @Override
            public void onActivityStopped(Activity activity) {
                count--;
                Log.e("onActivityStarted: ",count+"" );
                Intent countIntent = new Intent(COUNT_CHANGE);
                countIntent.putExtra("count",count);
                sendBroadcast(countIntent);
            }
            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            }
            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

    }
    private Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            Throwable cause = ex.getCause();
            StringBuilder builder = new StringBuilder();
            builder.append(ex.getCause().toString()+"\r\n");
            for(int x=0;x<cause.getStackTrace().length;x++){
                builder.append("FileName:"+cause.getStackTrace()[x].getFileName()+">>>>Method:"+cause.getStackTrace()[x].getMethodName()+">>>>FileLine:"+cause.getStackTrace()[x].getLineNumber()+"\r\n");
            }

            Logger.e(builder.toString());
            restartApp();
        }
    };
    public void restartApp() {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
    }
}

package com.link.cloud.utils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.link.cloud.CabinetApplication;
import com.link.cloud.Constants;
import com.link.cloud.activity.MainActivity;
import com.link.cloud.activity.RegularActivity;
import com.link.cloud.activity.VipActivity;
import com.link.cloud.bean.DeviceInfo;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * Created by OFX002 on 2018/4/24.
 */

public class TimeService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(CabinetApplication.COUNT_CHANGE);
        registerReceiver(receiver,filter);
        return START_STICKY;
    }
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("onReceive: ","count"+count );
            if (intent.getAction().equals(Intent.ACTION_TIME_TICK)&&count==0) {
                Realm defaultInstance = Realm.getDefaultInstance();
                RealmResults<DeviceInfo> all = defaultInstance.where(DeviceInfo.class).findAll();
                int type=0;
                if(all.size()>0){
                    DeviceInfo deviceInfo = all.get(0);
                    type=deviceInfo.getDeviceTypeId();
                    Constants.CABINET_TYPE = type;
                }
                switch (Constants.CABINET_TYPE){
                    case Constants.REGULAR_CABINET:
                        Intent sayHelloIntent1 = new Intent(context, RegularActivity.class);
                        sayHelloIntent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(sayHelloIntent1);
                        break;
                        case Constants.VIP_CABINET:
                            Intent sayHelloIntent3= new Intent(context, VipActivity.class);
                            sayHelloIntent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(sayHelloIntent3);
                        break;
                        case Constants.VIP_REGULAR_CABINET:
                            Intent sayHelloIntent2 = new Intent(context, MainActivity.class);
                            sayHelloIntent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(sayHelloIntent2);
                        break;
                }

            }
            if(intent.getAction().equals(CabinetApplication.COUNT_CHANGE)){
                count=intent.getIntExtra("count",0);
            }
        }

    };
    int count =0;
}

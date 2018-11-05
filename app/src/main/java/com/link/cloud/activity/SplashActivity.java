package com.link.cloud.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.link.cloud.Constants;
import com.link.cloud.R;
import com.link.cloud.User;
import com.link.cloud.base.BaseActivity;
import com.link.cloud.bean.DeviceInfo;
import com.link.cloud.controller.MainController;
import com.link.cloud.network.HttpConfig;
import com.link.cloud.network.bean.AllUser;
import com.link.cloud.network.bean.BindUser;
import com.link.cloud.network.bean.CabinetInfo;
import com.link.cloud.network.bean.CabnetDeviceInfoBean;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by 49488 on 2018/10/20.
 */

public class SplashActivity extends BaseActivity implements MainController.MainControllerListener {

    int pageNum, total;
    private MainController mainController;
    Realm realm;


    @Override
    protected void initViews() {
        pageNum = 100;
        realm = Realm.getDefaultInstance();
        mainController = new MainController(this);
        if (TextUtils.isEmpty(User.get().getPassWord())) {
            skipActivity(SettingActivity.class);
        } else {
            if (TextUtils.isEmpty(User.get().getToken())) {
                getToken();
            } else {
                showNextActivity();
            }
        }
    }

    private void getToken() {
        RealmResults<DeviceInfo> all = realm.where(DeviceInfo.class).findAll();
        if (!all.isEmpty() && all.size() >= 0) {
            DeviceInfo deviceInfo = all.get(0);
            mainController.login(deviceInfo.getDeviceId().trim(), User.get().getPassWord());
        } else {
            skipActivity(SettingActivity.class);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void modelMsg(int state, String msg) {

    }

    @Override
    public void onLoginSuccess(CabnetDeviceInfoBean cabnetDeviceInfoBean) {
        HttpConfig.TOKEN = cabnetDeviceInfoBean.getToken();
        User.get().setToken(cabnetDeviceInfoBean.getToken());
        User.get().setCabinetType(cabnetDeviceInfoBean.getDeviceInfo().getDeviceTypeId());
        mainController.getUser(pageNum, 1);
    }

    @Override
    public void onMainErrorCode(String msg) {
    }

    @Override
    public void onMainFail(Throwable e, boolean isNetWork) {

    }

    @Override
    public void getUserSuccess(final BindUser data) {
        Logger.e(data.getData().get(0).getUuid());
        int totalPage = total / pageNum + 1;
        ExecutorService executorService = Executors.newFixedThreadPool(totalPage);
        List<Future<Boolean>> futures = new ArrayList();
        if (totalPage >= 2) {
            for (int i = 2; i < totalPage; i++) {
                final int finalI = i;
                Callable<Boolean> task = new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        mainController.getUser(pageNum, finalI);
                        return true;
                    }
                };

                futures.add(executorService.submit(task));
            }
            for (Future<Boolean> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            executorService.shutdown();
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(AllUser.class).findAll().deleteAllFromRealm();
                realm.copyToRealm(data.getData());
            }
        });
       showNextActivity();
    }

    private void showNextActivity() {
        if (User.get().getCabinetType() >= 0) {
            Bundle bundle = new Bundle();
            if (User.get().getCabinetType() == Constants.REGULAR_CABINET) {
                bundle.putString(Constants.ActivityExtra.TYPE, "regularactivity");
                skipActivity(RegularActivity.class, bundle);
            } else if (User.get().getCabinetType() == Constants.VIP_CABINET) {
                bundle.putString(Constants.ActivityExtra.TYPE, "VipActivity");
                skipActivity(VipActivity.class, bundle);
            } else if (User.get().getCabinetType()== Constants.VIP_REGULAR_CABINET) {
                skipActivity(MainActivity.class);
            }
        }
        finish();
    }

    @Override
    public void onCabinetInfoSuccess(final RealmList<CabinetInfo> data) {
        final RealmResults<CabinetInfo> cabinetInfoRealmList = realm.where(CabinetInfo.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                cabinetInfoRealmList.deleteAllFromRealm();
                realm.copyToRealm(data);
            }
        });
    }

    @Override
    public void temCabinetSuccess(CabinetInfo cabinetBean) {

    }

}

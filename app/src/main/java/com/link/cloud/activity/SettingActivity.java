package com.link.cloud.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.link.cloud.CabinetApplication;
import com.link.cloud.R;
import com.link.cloud.base.BaseActivity;
import com.link.cloud.bean.DeviceInfo;
import com.link.cloud.controller.MainController;
import com.link.cloud.network.HttpConfig;
import com.link.cloud.network.bean.CabinetInfo;
import com.link.cloud.utils.OpenDoorUtil;
import com.link.cloud.utils.RxTimerUtil;
import com.link.cloud.utils.Utils;
import com.orhanobut.logger.Logger;
import com.zitech.framework.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by OFX002 on 2018/9/28.
 */

@SuppressLint("Registered")
public class SettingActivity extends BaseActivity {

    private TextView member;
    private TextView manager;
    private TextView deviceId;
    private LinearLayout pswLl;
    private android.widget.EditText editPsw;
    private android.widget.EditText lockNum;
    private TextView save;
    private LinearLayout openOrClose;
    private TextView open;
    private TextView close;
    private TextView backSystemSetting;
    private TextView backApp;
    private TextView backSystemMain;
    private TextView restartApp;
    private MainController mainController;
    private String mac;
    private TextView clean;
    private TextView openOne;
    private RxTimerUtil rxTimerUtil;

    @Override
    protected void initViews() {
        initView();
        mac = Utils.getMac();
        rxTimerUtil = new RxTimerUtil();
        deviceId.setText(getResources().getString(R.string.device_id) + mac);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_app:


            case R.id.member:
                finish();
                break;
            case R.id.save:
                String edit_pswText = editPsw.getText().toString();
                String fisrt = Utils.getMD5(edit_pswText).toUpperCase();
                final String second = Utils.getMD5(fisrt).toUpperCase();
                final RealmResults<DeviceInfo> all = realm.where(DeviceInfo.class).findAll();

                if (all.size() != 0) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            DeviceInfo deviceInfo = all.get(0);
                            deviceInfo.setPsw(second);
                            deviceInfo.setToken("");
                            HttpConfig.TOKEN = null;
                            realm.copyToRealm(deviceInfo);
                        }
                    });
                } else {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            DeviceInfo deviceBean = new DeviceInfo();
                            deviceBean.setDeviceId(mac);
                            deviceBean.setPsw(second);
                            realm.copyToRealm(deviceBean);
                        }
                    });
                }
                Toast.makeText(this, getResources().getString(R.string.save_success), Toast.LENGTH_LONG).show();


                skipActivity(SplashActivity.class);
                break;
            case R.id.back_system_main:
                Intent intent1 = new Intent(Intent.ACTION_MAIN, null);
                intent1.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent1);

                break;
            case R.id.back_system_setting:
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                break;
            case R.id.restart_app:
                Intent intent2 = new Intent(this, SplashActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
            case R.id.close:
                close.setTextColor(getResources().getColor(R.color.almost_white));
                close.setBackgroundResource(R.drawable.border_red_gradient);
                open.setBackgroundResource(R.drawable.border_gray_gradient);
                open.setTextColor(getResources().getColor(R.color.dark_black));
                break;
            case R.id.open:
                open.setTextColor(getResources().getColor(R.color.almost_white));
                open.setBackgroundResource(R.drawable.border_red_gradient);
                close.setBackgroundResource(R.drawable.border_gray_gradient);
                close.setTextColor(getResources().getColor(R.color.dark_black));
                break;

            case R.id.openOne:
                if (lockNum.getVisibility() == View.GONE) {
                    lockNum.setVisibility(View.VISIBLE);
                    return;
                }
                String lockNo = lockNum.getText().toString().trim();
                if (!TextUtils.isEmpty(lockNo)) {
                    CabinetInfo cabinetInfo = realm.where(CabinetInfo.class).equalTo("lockNo", Integer.parseInt(lockNo)).findFirst();
                    if (cabinetInfo != null) {
                        openLock(cabinetInfo);
                    } else {
                        speak(getResources().getString(R.string.please_input_right_lock_num));
                    }
                }
                break;

            case R.id.clean:

                RealmResults<CabinetInfo> users = realm.where(CabinetInfo.class).findAll();
                final List<CabinetInfo> cabinetInfos = new ArrayList<>();
                cabinetInfos.addAll(realm.copyFromRealm(users));
                rxTimerUtil.interval(2000, new RxTimerUtil.IRxNext() {
                    @Override
                    public void doNext(long number) {
                        if ((int) number < cabinetInfos.size()){
                            openLock(cabinetInfos.get((int)number));
                        }else {
                            rxTimerUtil.cancel();
                        }

                    }
                });
                break;
        }
    }


    private void openLock(CabinetInfo cabinetBean) {
        speak(cabinetBean.getLockNo() + getResources().getString(R.string.aready_open_string));
        int lockplate = cabinetBean.getLockNo();
        int nuberlock = cabinetBean.getLineNo();
        if (nuberlock > 10) {
            nuberlock = nuberlock % 10;
            Logger.e("SecondFragment===" + nuberlock);
            if (nuberlock == 0) {
                nuberlock = 10;
                Logger.e("SecondFragment===" + nuberlock);
            }
        }
        try {
            if (lockplate <= 10) {
                CabinetApplication.getInstance().serialPortOne.getOutputStream().write(OpenDoorUtil.openOneDoor(lockplate, nuberlock));
            } else if (lockplate > 10 && lockplate <= 20) {
                CabinetApplication.getInstance().serialPortTwo.getOutputStream().write(OpenDoorUtil.openOneDoor(lockplate % 10, nuberlock));
            } else if (lockplate > 20 && lockplate <= 30) {
                CabinetApplication.getInstance().serialPortThree.getOutputStream().write(OpenDoorUtil.openOneDoor(lockplate % 10, nuberlock));
            }

        } catch (Exception e) {

        } finally {

        }

    }

    private void initView() {
        member = (TextView) findViewById(R.id.member);
        manager = (TextView) findViewById(R.id.manager);
        deviceId = (TextView) findViewById(R.id.device_id);
        pswLl = (LinearLayout) findViewById(R.id.psw_ll);
        editPsw = (EditText) findViewById(R.id.edit_psw);
        lockNum = (EditText) findViewById(R.id.lockNum);
        save = (TextView) findViewById(R.id.save);
        openOrClose = (LinearLayout) findViewById(R.id.open_or_close);
        open = (TextView) findViewById(R.id.open);
        close = (TextView) findViewById(R.id.close);
        backSystemSetting = (TextView) findViewById(R.id.back_system_setting);
        backApp = (TextView) findViewById(R.id.back_app);
        backSystemMain = (TextView) findViewById(R.id.back_system_main);
        restartApp = (TextView) findViewById(R.id.restart_app);
        clean = (TextView) findViewById(R.id.clean);
        openOne = (TextView) findViewById(R.id.openOne);


        ViewUtils.setOnClickListener(save, this);
        ViewUtils.setOnClickListener(member, this);
        ViewUtils.setOnClickListener(backSystemMain, this);
        ViewUtils.setOnClickListener(backSystemSetting, this);
        ViewUtils.setOnClickListener(restartApp, this);
        ViewUtils.setOnClickListener(close, this);
        ViewUtils.setOnClickListener(close, this);
        ViewUtils.setOnClickListener(clean, this);
        ViewUtils.setOnClickListener(openOne, this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rxTimerUtil.cancel();
    }


    @Override
    public void modelMsg(int state, String msg) {

    }
}

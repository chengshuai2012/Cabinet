package com.link.cloud.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.link.cloud.CabinetApplication;
import com.link.cloud.R;
import com.link.cloud.UserCabinetDetails;
import com.link.cloud.base.BaseActivity;
import com.link.cloud.bean.CabinetUserDatail;
import com.link.cloud.bean.DeviceInfo;
import com.link.cloud.controller.MainController;
import com.link.cloud.network.HttpConfig;
import com.link.cloud.network.bean.CabinetInfo;
import com.link.cloud.utils.OpenDoorUtil;
import com.link.cloud.utils.RxTimerDelayUtil;
import com.link.cloud.utils.RxTimerUtil;
import com.link.cloud.utils.Utils;
import com.orhanobut.logger.Logger;
import com.zitech.framework.utils.ViewUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android_serialport_api.SerialPort;
import io.reactivex.annotations.NonNull;
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
    private TextView clear_user;
    private TextView open_use_detail;

    @Override
    protected void initViews() {
        initView();
        mac = Utils.getMac();
        deviceId.setText(getResources().getString(R.string.device_id) + mac);
        checkReadPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,REQUEST_SD_PERMISSION);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_app:
                finish();
   case R.id.clear_user:
       AlertDialog.Builder builder = new AlertDialog.Builder(this);
       builder.setMessage("确定清除使用记录？");
       builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {

           }
       });
       builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
               final RealmResults<CabinetUserDatail> all1 = realm.where(CabinetUserDatail.class).findAll();
               realm.executeTransaction(new Realm.Transaction() {
                   @Override
                   public void execute(Realm realm) {
                       all1.deleteAllFromRealm();
                   }
               });
           }
       });
       builder.show();

       break;
   case R.id.open_use_detail:
       startActivity(new Intent(this, UserCabinetDetails.class));
       break;
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
                    CabinetInfo cabinetInfo = realm.where(CabinetInfo.class).equalTo("cabinetNo", lockNo).findFirst();
                    if (cabinetInfo != null) {
                        openLock(cabinetInfo);
                    } else {
                        speak(getResources().getString(R.string.please_input_right_lock_num));
                    }
                }
                break;

            case R.id.clean:

                try {

                    CabinetApplication.getInstance().serialPortOne.getOutputStream().write(OpenDoorUtil.openAllDoor());
                }catch (IOException e){
                    e.printStackTrace();
                }
                try {
                    CabinetApplication.getInstance().serialPortTwo.getOutputStream().write(OpenDoorUtil.openAllDoor());
                }catch (IOException e){
                    e.printStackTrace();
                }
                try {
                    CabinetApplication.getInstance().serialPortThree.getOutputStream().write(OpenDoorUtil.openAllDoor());
                }catch (IOException e){
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void gotoSetting(String pass) {

    }


    private void openLock(CabinetInfo cabinetBean) {
        speak(cabinetBean.getCabinetNo() + getResources().getString(R.string.aready_open_string));
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
        clear_user = (TextView) findViewById(R.id.clear_user);
        open_use_detail = (TextView) findViewById(R.id.open_use_detail);


        ViewUtils.setOnClickListener(save, this);
        ViewUtils.setOnClickListener(member, this);
        ViewUtils.setOnClickListener(backSystemMain, this);
        ViewUtils.setOnClickListener(backSystemSetting, this);
        ViewUtils.setOnClickListener(restartApp, this);
        ViewUtils.setOnClickListener(close, this);
        ViewUtils.setOnClickListener(close, this);
        ViewUtils.setOnClickListener(clean, this);
        ViewUtils.setOnClickListener(openOne, this);
        ViewUtils.setOnClickListener(backApp, this);
        ViewUtils.setOnClickListener(clear_user,this);
        ViewUtils.setOnClickListener(open_use_detail,this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static final int REQUEST_SD_PERMISSION = 10111;



    /**

     * 判断是否有某项权限

     * @param string_permission 权限

     * @param request_code 请求码

     * @return

     */

    public boolean checkReadPermission(String string_permission,int request_code) {

        boolean flag = false;

        if (ContextCompat.checkSelfPermission(this, string_permission) == PackageManager.PERMISSION_GRANTED) {//已有权限

            flag = true;

        } else {//申请权限

            ActivityCompat.requestPermissions(this, new String[]{string_permission}, request_code);

        }

        return flag;

    }



    /**

     * 检查权限后的回调

     * @param requestCode 请求码

     * @param permissions  权限

     * @param grantResults 结果

     */

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case REQUEST_SD_PERMISSION:

                if (permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {//失败

                    Toast.makeText(this, getString(R.string.sd_permession),Toast.LENGTH_SHORT).show();

                }

                break;

        }

    }
    @Override
    public void modelMsg(int state, String msg) {

    }
}

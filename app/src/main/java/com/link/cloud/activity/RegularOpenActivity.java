package com.link.cloud.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.link.cloud.CabinetApplication;
import com.link.cloud.Constants;
import com.link.cloud.R;
import com.link.cloud.base.BaseActivity;
import com.link.cloud.controller.RegularOpenController;
import com.link.cloud.network.HttpConfig;
import com.link.cloud.network.bean.CabinetInfo;
import com.link.cloud.network.bean.PassWordValidate;
import com.link.cloud.utils.OpenDoorUtil;
import com.link.cloud.utils.TTSUtils;
import com.orhanobut.logger.Logger;
import com.zitech.framework.utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 作者：qianlu on 2018/11/1 15:14
 * 邮箱：zar.l@qq.com
 */
public class RegularOpenActivity extends BaseActivity implements RegularOpenController.RegularOpenControllerListener {


    private android.widget.LinearLayout openLayout;
    private android.widget.LinearLayout returnLayout;
    private String type;
    private String uuid;
    private RegularOpenController regularOpenController;
    private TextView finsh;
    private CabinetInfo cabinetInfo;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
    }

    @Override
    protected void initViews() {
        handler.sendEmptyMessageDelayed(0,10000);
        regularOpenController = new RegularOpenController(this);
        openLayout = findViewById(R.id.openLayout);
        returnLayout = findViewById(R.id.returnLayout);
        finsh = findViewById(R.id.finsh);
        openLayout.setOnClickListener(this);
        ViewUtils.setOnClickListener(finsh, this);
        ViewUtils.setOnClickListener(returnLayout, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        type = getIntent().getExtras().getString(Constants.ActivityExtra.TYPE);
        if (!type.equals(Constants.ActivityExtra.PASSWORD)) {
            uuid = getIntent().getExtras().getString(Constants.ActivityExtra.UUID);

        } else {

        }
    }
    long start ,end;
    Boolean isReturn;
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.returnLayout:
                isReturn=true;
                regularOpenController.returnCabinet(uuid);
                break;
            case R.id.openLayout:
                isReturn=false;
                    if (cabinetInfo == null) {
                        CabinetInfo first = realm.where(CabinetInfo.class).equalTo("uuid", uuid).findFirst();
                        Log.e("onClick: ",realm.where(CabinetInfo.class).equalTo("uuid", uuid).findFirst()+"");
                        if (first != null) {
                            openLock(first);
                            Log.e("onClick: ",first.getUuid()+"000" );
                        } else {
                            start =System.currentTimeMillis();
                            Log.e("onClick: ", start+"");
                            if(start-end>2000){
                                Log.e("onClick: ", end+"");
                                end =System.currentTimeMillis();
                                regularOpenController.temCabinet(uuid);
                            }

                        }
                    } else {
                        Log.e("onClick: ",cabinetInfo +"222");
                        openLock(cabinetInfo);
                    }


                break;

            case R.id.finsh:
                if(Constants.CABINET_TYPE==Constants.VIP_REGULAR_CABINET){
                    skipActivity(MainActivity.class);
                }else {
                    skipActivity(RegularActivity.class);
                }

                break;
        }
    }

    @Override
    public void gotoSetting(String pass) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_regularopen;
    }

    @Override
    public void modelMsg(int state, String msg) {
    }


    private void openLock(CabinetInfo cabinetBean) {
        Bundle bundle = new Bundle();
        CabinetInfo cabinetBean1 = new CabinetInfo();
        cabinetBean1.setUuid(cabinetBean.getUuid());
        cabinetBean1.setNickname(cabinetBean.getNickname() == null ? "" : cabinetBean.getNickname());
        cabinetBean1.setPhone(cabinetBean.getPhone() == null ? "" : cabinetBean.getPhone());
        cabinetBean1.setCabinetNo(cabinetBean.getCabinetNo());
        if (type.equals(Constants.ActivityExtra.FINGER)) {
            cabinetBean1.setOpenWay(getResources().getString(R.string.open_finger));
        } else if (type.equals(Constants.ActivityExtra.XIAOCHENGXU)) {
            cabinetBean1.setOpenWay(getResources().getString(R.string.open_xiaochengxu));
        } else {
            cabinetBean1.setOpenWay(getResources().getString(R.string.password_open));
        }
        bundle.putSerializable(Constants.ActivityExtra.ENTITY, cabinetBean1);
        showActivity(RegularOpenSuccessActivity.class, bundle);
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

    @Override
    public void OpenSuccess(final CabinetInfo cabinetInfo) {
        final RealmResults<CabinetInfo> cabinetInfos = realm.where(CabinetInfo.class).equalTo("cabinetNo", cabinetInfo.getCabinetNo()).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                cabinetInfos.deleteAllFromRealm();
                realm.copyToRealm(cabinetInfo);
            }
        });
        openLock(cabinetInfo);
        speak(cabinetInfo.getCabinetNo() + getResources().getString(R.string.aready_open_string));
    }

    @Override
    public void returnSuccess(final CabinetInfo cabinetInfo) {
        openLock(cabinetInfo);
        speak(cabinetInfo.getCabinetNo() + getResources().getString(R.string.remove_leave));
        Log.e("returnSuccess: ", cabinetInfo.getCabinetNo());
        final CabinetInfo cabinetInfos = realm.where(CabinetInfo.class).equalTo("cabinetNo", cabinetInfo.getCabinetNo()).findFirst();
        if(cabinetInfos!=null){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Log.e("returnSuccess: ", cabinetInfos.getCabinetNo());
                    cabinetInfos.setUuid("");
                }
            });
        }

    }

    @Override
    public void openFaild(String message, String code) {

        if (code.equals("400000100000") ) {
            TTSUtils.getInstance().speak(getString(R.string.login_fail));
        }else if(code.equals("400000999102")) {
            HttpConfig.TOKEN = "";
            Intent intent1 = new Intent(this, SplashActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
            android.os.Process.killProcess(android.os.Process.myPid());
        }else if(code.equals("400000999999")){
            TTSUtils.getInstance().speak(getString(R.string.cheack_fail)+","+getString(R.string.again_finger));
        }else {
            speak(message);
        }
        if(TextUtils.isEmpty(HttpConfig.TOKEN)){
            restartApp();
        }

    }

    @Override
    public void returnFail(String message, String code) {
        speak(message);
        if (code.equals("400000100000") ) {
            TTSUtils.getInstance().speak(getString(R.string.login_fail));
        }else if(code.equals("400000999102")) {
            HttpConfig.TOKEN = "";
            Intent intent1 = new Intent(this, SplashActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        if(TextUtils.isEmpty(HttpConfig.TOKEN)){
            restartApp();
        }

    }
    public void restartApp() {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
    }
    @Override
    public void onFail(Throwable e, boolean isNetWork) {
        if(isNetWork){
            speak(getResources().getString(R.string.network_unavailable));
        }else {
            speak("解析异常");
        }
        if(TextUtils.isEmpty(HttpConfig.TOKEN)){
            restartApp();
        }
    }

    @Override
    public void SuccessByQr(final CabinetInfo cabinetInfo) {

        if(cabinetInfo.isLocked()==true){
            openLock(cabinetInfo);
            speak(cabinetInfo.getCabinetNo() + getResources().getString(R.string.aready_open_string));
        }
        if(isReturn){
            if(cabinetInfo.isLocked()==true){
                openLock(cabinetInfo);
                regularOpenController.returnCabinet(cabinetInfo.getUuid());
            }else {
                speak("请先开柜");
            }

        }
    }

    @Override
    public void OpenLockByPass(PassWordValidate appVersionBean) {

    }


}

package com.link.cloud.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.link.cloud.CabinetApplication;
import com.link.cloud.Constants;
import com.link.cloud.R;
import com.link.cloud.base.BaseActivity;
import com.link.cloud.controller.RegularOpenController;
import com.link.cloud.network.bean.CabinetInfo;
import com.link.cloud.utils.OpenDoorUtil;
import com.orhanobut.logger.Logger;
import com.zitech.framework.utils.ViewUtils;

import io.realm.Realm;

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

    @Override
    protected void initViews() {
        regularOpenController = new RegularOpenController(this);
        type = getIntent().getExtras().getString(Constants.ActivityExtra.TYPE);
        if (!type.equals(Constants.ActivityExtra.PASSWORD)) {
            uuid = getIntent().getExtras().getString(Constants.ActivityExtra.UUID);
        } else {
            cabinetInfo = (CabinetInfo) getIntent().getExtras().getSerializable(Constants.ActivityExtra.ENTITY);
        }
        openLayout = findViewById(R.id.openLayout);
        returnLayout = findViewById(R.id.returnLayout);
        finsh = findViewById(R.id.finsh);
        openLayout.setOnClickListener(this);
        ViewUtils.setOnClickListener(finsh, this);
        ViewUtils.setOnClickListener(returnLayout, this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.returnLayout:
                if (cabinetInfo == null)
                    regularOpenController.returnCabinet(uuid);
                break;
            case R.id.openLayout:
                if (cabinetInfo == null) {
                    CabinetInfo first = realm.where(CabinetInfo.class).equalTo("uuid", uuid).findFirst();
                    if (first != null) {
                        openLock(first);
                    } else {
                        regularOpenController.temCabinet(uuid);
                    }
                } else {
                    openLock(cabinetInfo);
                }
                break;

            case R.id.finsh:
                finish();
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
        cabinetBean1.setLockNo(cabinetBean.getLockNo());
        if (type.equals(Constants.ActivityExtra.FINGER)) {
            cabinetBean1.setOpenWay(getResources().getString(R.string.open_finger));
        } else if (type.equals(Constants.ActivityExtra.XIAOCHENGXU)) {
            cabinetBean1.setOpenWay(getResources().getString(R.string.open_xiaochengxu));
        } else {
            cabinetBean1.setOpenWay(getResources().getString(R.string.password_open));
        }
        bundle.putSerializable(Constants.ActivityExtra.ENTITY, cabinetBean1);
        showActivity(RegularOpenSuccessActivity.class, bundle);
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

    @Override
    public void OpenSuccess(final CabinetInfo cabinetInfo) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(cabinetInfo);
            }
        });
        openLock(cabinetInfo);
        speak(cabinetInfo.getLockNo() + getResources().getString(R.string.aready_open_string));
    }

    @Override
    public void returnSuccess(final CabinetInfo cabinetInfo) {
        openLock(cabinetInfo);
        speak(cabinetInfo.getLockNo() + getResources().getString(R.string.remove_leave));
        final CabinetInfo cabinetInfos = realm.where(CabinetInfo.class).equalTo("uuid", cabinetInfo.getUuid()).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                cabinetInfos.deleteFromRealm();
            }
        });
    }

    @Override
    public void openFaild(String message) {
        speak(message);
    }

    @Override
    public void returnFail(String message) {
        speak(message);
    }
}

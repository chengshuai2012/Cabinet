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
import com.zitech.framework.utils.ViewUtils;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;

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
    private boolean hasUuid = false;
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
                if (cabinetInfo==null)
                regularOpenController.returnCabinet(uuid);
                break;
            case R.id.openLayout:
                if (cabinetInfo==null){
                    final RealmResults<CabinetInfo> all = realm.where(CabinetInfo.class).findAll();
                    for (CabinetInfo info : all) {
                        if (info.getUuid().trim().equals(uuid.trim())) {
                            hasUuid = true;
                            openLock(info);
                            break;
                        }
                    }
                    if (!hasUuid) {
                        regularOpenController.temCabinet(uuid);
                    }
                }else {
                    openLock(cabinetInfo);
                }
                break;

            case R.id.finsh:
                finish();
                break;
        }
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
        speak("柜子开了");
        try {
            if (cabinetBean.getDeviceId() <= 10) {
                CabinetApplication.getInstance().serialPortOne.getOutputStream().write(OpenDoorUtil.openOneDoor(cabinetBean.getDeviceId(), cabinetBean.getLockId()));
            } else if (cabinetBean.getDeviceId() > 10 && cabinetBean.getDeviceId() <= 20) {
                CabinetApplication.getInstance().serialPortTwo.getOutputStream().write(OpenDoorUtil.openOneDoor(cabinetBean.getDeviceId() % 10, cabinetBean.getLockId()));
            } else if (cabinetBean.getDeviceId() > 20 && cabinetBean.getDeviceId() <= 30) {
                CabinetApplication.getInstance().serialPortThree.getOutputStream().write(OpenDoorUtil.openOneDoor(cabinetBean.getDeviceId() % 10, cabinetBean.getLockId()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finish();

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
    public void returnSuccess(CabinetInfo cabinetInfo) {
        openLock(cabinetInfo);
        speak(cabinetInfo.getLockNo() + getResources().getString(R.string.remove_leave));
        final RealmResults<CabinetInfo> all = realm.where(CabinetInfo.class).findAll();

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getUuid().trim().equals(uuid.trim())) {
                final int finalI = i;
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        all.deleteFromRealm(finalI);
                    }
                });
                break;
            }
        }
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

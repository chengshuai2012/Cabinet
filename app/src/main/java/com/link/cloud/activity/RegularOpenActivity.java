package com.link.cloud.activity;

import android.view.View;
import android.widget.TextView;

import com.link.cloud.CabinetApplication;
import com.link.cloud.Constants;
import com.link.cloud.R;
import com.link.cloud.base.BaseActivity;
import com.link.cloud.controller.MainController;
import com.link.cloud.network.bean.BindUser;
import com.link.cloud.network.bean.CabinetInfo;
import com.link.cloud.network.bean.CabnetDeviceInfoBean;
import com.link.cloud.utils.OpenDoorUtil;
import com.zitech.framework.utils.ViewUtils;

import java.io.IOException;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * 作者：qianlu on 2018/11/1 15:14
 * 邮箱：zar.l@qq.com
 */
public class RegularOpenActivity extends BaseActivity implements MainController.MainControllerListener {


    private android.widget.LinearLayout openLayout;
    private android.widget.LinearLayout returnLayout;
    private String type;
    private String uuid;
    private MainController mainController;
    private boolean hasUuid = false;
    private TextView finsh;


    @Override
    protected void initViews() {
        mainController = new MainController(this);
        type = getIntent().getExtras().getString(Constants.ActivityExtra.TYPE);
        uuid = getIntent().getExtras().getString(Constants.ActivityExtra.UUID);

        openLayout = findViewById(R.id.openLayout);
        returnLayout = findViewById(R.id.returnLayout);
        finsh = findViewById(R.id.finsh);


        ViewUtils.setOnClickListener(openLayout, this);
        ViewUtils.setOnClickListener(finsh, this);
        ViewUtils.setOnClickListener(returnLayout, this);


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.returnLayout:
                mainController.returnCabinet(uuid);
                break;
            case R.id.openLayout:
                try {
                    CabinetApplication.getInstance().serialPortOne.getOutputStream().write(OpenDoorUtil.openOneDoor(1, 5));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final RealmResults<CabinetInfo> all = realm.where(CabinetInfo.class).findAll();
                for (CabinetInfo info : all) {
                    if (info.getUuid().equals(uuid)) {
                        hasUuid = true;
                        openLock(info);
                        break;
                    }
                }
                if (!hasUuid){
                mainController.temCabinet(uuid);
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



    @Override
    public void onLoginSuccess(CabnetDeviceInfoBean cabnetDeviceInfoBean) {

    }

    @Override
    public void onMainErrorCode(String msg) {

    }

    @Override
    public void onMainFail(Throwable e, boolean isNetWork) {

    }

    @Override
    public void getUserSuccess(BindUser data) {

    }

    @Override
    public void onCabinetInfoSuccess(RealmList<CabinetInfo> data) {

    }

    @Override
    public void temCabinetSuccess(CabinetInfo cabinetBean) {
        //开柜子
        openLock(cabinetBean);
    }


    private void openLock(CabinetInfo cabinetBean) {
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

    }
}

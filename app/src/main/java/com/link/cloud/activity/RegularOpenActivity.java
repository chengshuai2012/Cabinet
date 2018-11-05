package com.link.cloud.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextClock;

import com.link.cloud.Constants;
import com.link.cloud.R;
import com.link.cloud.base.BaseActivity;
import com.link.cloud.controller.MainController;
import com.link.cloud.network.bean.BindUser;
import com.link.cloud.network.bean.CabinetBean;
import com.link.cloud.network.bean.CabinetInfo;
import com.zitech.framework.utils.ViewUtils;

import io.realm.RealmList;

/**
 * 作者：qianlu on 2018/11/1 15:14
 * 邮箱：zar.l@qq.com
 */
public class RegularOpenActivity extends BaseActivity implements MainController.MainControllerListener {


    private android.widget.TextClock textclock;
    private android.widget.LinearLayout openLayout;
    private android.widget.LinearLayout returnLayout;
    private String type;
    private String uuid;

    private MainController mainController;

    @Override
    protected void initViews() {
        mainController = new MainController(this);
        type = getIntent().getExtras().getString(Constants.ActivityExtra.TYPE);
        uuid = getIntent().getExtras().getString(Constants.ActivityExtra.UUID);
        textclock = (TextClock) findViewById(R.id.textclock);
        openLayout = (LinearLayout) findViewById(R.id.openLayout);
        returnLayout = (LinearLayout) findViewById(R.id.returnLayout);
        ViewUtils.setOnClickListener(openLayout, this);
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
                mainController.temCabinet(uuid);
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
    public void onLoginSuccess(String token) {

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
    public void temCabinetSuccess(CabinetBean cabinetBean) {
        //开柜子


    }
}

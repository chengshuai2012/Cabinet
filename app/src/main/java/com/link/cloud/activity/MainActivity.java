package com.link.cloud.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.link.cloud.Constants;
import com.link.cloud.R;
import com.link.cloud.base.BaseActivity;
import com.link.cloud.bean.DeviceInfo;
import com.link.cloud.controller.MainController;
import com.link.cloud.utils.NettyClientBootstrap;
import com.zitech.framework.utils.ViewUtils;

import io.realm.RealmResults;


@SuppressLint("Registered")
public class MainActivity extends BaseActivity {

    private TextView member;
    private TextView manager;
    private TextClock textclock;
    private LinearLayout openLayout;
    private LinearLayout closeLayout;
    private LinearLayout regularLayout;
    private LinearLayout vipLayout;
    private MainController mainController;

    private NettyClientBootstrap nettyClientBootstrap;
    private DeviceInfo deviceInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void initViews() {
        getDeviceInfo();
        if (deviceInfo != null && deviceInfo.getToken() != null && !TextUtils.isEmpty(deviceInfo.getToken())) {
            nettyClientBootstrap = new NettyClientBootstrap(this, Constants.TCP_PORT, Constants.TCP_URL, "{\"data\":{},\"msgType\":\"HEART_BEAT\",\"token\":\"" + deviceInfo.getToken() + "\"}");
            nettyClientBootstrap.start();
        } else {
            skipActivity(SettingActivity.class);
        }
    }

    private void getDeviceInfo() {
        final RealmResults<DeviceInfo> all = realm.where(DeviceInfo.class).findAll();
        if (!all.isEmpty()) {
            deviceInfo = all.get(0);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    private void initView() {
        member = (TextView) findViewById(R.id.member);
        manager = (TextView) findViewById(R.id.manager);
        textclock = (TextClock) findViewById(R.id.textclock);
        regularLayout = (LinearLayout) findViewById(R.id.regularLayout);
        vipLayout = (LinearLayout) findViewById(R.id.vipLayout);

        ViewUtils.setOnClickListener(regularLayout, this);
        ViewUtils.setOnClickListener(vipLayout, this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.regularLayout:
                bundle.putString(Constants.ActivityExtra.TYPE, "REGULAR");
                showActivity(RegularActivity.class, bundle);
                break;
            case R.id.vipLayout:
                bundle.putString(Constants.ActivityExtra.TYPE, "VIP");
                showActivity(VipActivity.class, bundle);
                break;
        }
    }


    @Override
    public void modelMsg(int state, String msg) {

    }
}

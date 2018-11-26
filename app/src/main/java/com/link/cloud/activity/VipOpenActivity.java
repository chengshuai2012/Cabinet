package com.link.cloud.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextClock;

import com.link.cloud.Constants;
import com.link.cloud.R;
import com.link.cloud.base.BaseActivity;
import com.link.cloud.network.bean.CabinetInfo;
import com.zitech.framework.utils.ViewUtils;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * 作者：qianlu on 2018/11/1 15:14
 * 邮箱：zar.l@qq.com
 */
public class VipOpenActivity extends BaseActivity {
    Realm realm ;

    private android.widget.TextClock textclock;
    private android.widget.LinearLayout openLayout;
    private android.widget.LinearLayout returnLayout;
    private RealmResults<CabinetInfo> allCabinetInfo;

    @Override
    protected void initViews() {
        textclock = (TextClock) findViewById(R.id.textclock);
        openLayout = (LinearLayout) findViewById(R.id.openLayout);
        returnLayout = (LinearLayout) findViewById(R.id.returnLayout);
        realm=Realm.getDefaultInstance();
        ViewUtils.setOnClickListener(openLayout, this);
        ViewUtils.setOnClickListener(returnLayout, this);
        allCabinetInfo = realm.where(CabinetInfo.class).findAll();


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vipopen;
    }

    @Override
    public void modelMsg(int state, String msg) {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.openLayout:

                break;

            case R.id.returnLayout:
                Bundle bundle1 = new Bundle();
                bundle1.putString(Constants.ActivityExtra.TYPE, "SUCCESS");
                showActivity(VipOpenSuccessActivity.class,bundle1);
                finish();
                break;
        }
    }

    @Override
    public void gotoSetting(String pass) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}

package com.link.cloud.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextClock;

import com.link.cloud.Constants;
import com.link.cloud.R;
import com.link.cloud.base.BaseActivity;
import com.zitech.framework.utils.ViewUtils;

/**
 * 作者：qianlu on 2018/11/1 15:14
 * 邮箱：zar.l@qq.com
 */
public class VipOpenActivity extends BaseActivity {


    private android.widget.TextClock textclock;
    private android.widget.LinearLayout openLayout;
    private android.widget.LinearLayout returnLayout;

    @Override
    protected void initViews() {
        textclock = (TextClock) findViewById(R.id.textclock);
        openLayout = (LinearLayout) findViewById(R.id.openLayout);
        returnLayout = (LinearLayout) findViewById(R.id.returnLayout);

        ViewUtils.setOnClickListener(openLayout, this);
        ViewUtils.setOnClickListener(returnLayout, this);
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
}

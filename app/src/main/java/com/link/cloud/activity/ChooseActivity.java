package com.link.cloud.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.link.cloud.R;
import com.link.cloud.base.AppBarActivity;
import com.zitech.framework.utils.ViewUtils;

/**
 * 作者：qianlu on 2018/10/10 11:13
 * 邮箱：zar.l@qq.com
 * 选择开柜方式
 */
@SuppressLint("Registered")
public class ChooseActivity extends AppBarActivity {


    private android.widget.LinearLayout zhijingmaiLayout;
    private android.widget.LinearLayout xiaochengxuLayout;
    private android.widget.LinearLayout passwordLayout;

    @Override
    protected void initViews() {
        zhijingmaiLayout = (LinearLayout) findViewById(R.id.zhijingmaiLayout);
        xiaochengxuLayout = (LinearLayout) findViewById(R.id.xiaochengxuLayout);
        passwordLayout = (LinearLayout) findViewById(R.id.passwordLayout);

        ViewUtils.setOnClickListener(zhijingmaiLayout, this);
        ViewUtils.setOnClickListener(xiaochengxuLayout, this);
        ViewUtils.setOnClickListener(passwordLayout, this);


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.zhijingmaiLayout:

                break;
            case R.id.xiaochengxuLayout:

                break;
            case R.id.passwordLayout:
                Bundle bundle = new Bundle();
                showActivity(PassWordOpenActivity.class, bundle);
                break;
        }
    }
}

package com.link.cloud.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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


    private android.widget.RelativeLayout zhijingmaiLayout;
    private android.widget.RelativeLayout xiaochengxuLayout;
    private android.widget.RelativeLayout passwordLayout;

    @Override
    protected void initViews() {
        getToolbar().setBackground(getResources().getDrawable(R.drawable.ic_appbar_image));

        zhijingmaiLayout = (RelativeLayout) findViewById(R.id.zhijingmaiLayout);
        xiaochengxuLayout = (RelativeLayout) findViewById(R.id.xiaochengxuLayout);
        passwordLayout = (RelativeLayout) findViewById(R.id.passwordLayout);

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

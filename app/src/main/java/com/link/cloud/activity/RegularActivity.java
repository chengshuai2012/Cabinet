package com.link.cloud.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.link.cloud.Constants;
import com.link.cloud.R;
import com.link.cloud.base.AppBarActivity;
import com.link.cloud.widget.InputPassWordDialog;
import com.link.cloud.widget.PublicTitleView;
import com.link.cloud.widget.QRCodeDialog;
import com.zitech.framework.utils.ViewUtils;

/**
 * 作者：qianlu on 2018/10/10 11:13
 * 邮箱：zar.l@qq.com
 * 选择开柜方式
 */
@SuppressLint("Registered")
public class RegularActivity extends AppBarActivity {


    private RelativeLayout zhijingmaiLayout;
    private RelativeLayout xiaochengxuLayout;
    private RelativeLayout passwordLayout;

    private PublicTitleView publicTitleView;

    @Override
    protected void initViews() {
        getToolbar().setBackground(getResources().getDrawable(R.drawable.ic_regular_banner));

        zhijingmaiLayout = (RelativeLayout) findViewById(R.id.zhijingmaiLayout);
        xiaochengxuLayout = (RelativeLayout) findViewById(R.id.xiaochengxuLayout);
        passwordLayout = (RelativeLayout) findViewById(R.id.passwordLayout);
        publicTitleView = (PublicTitleView) findViewById(R.id.publicTitle);

        ViewUtils.setOnClickListener(zhijingmaiLayout, this);
        ViewUtils.setOnClickListener(xiaochengxuLayout, this);
        ViewUtils.setOnClickListener(passwordLayout, this);
        publicTitleView.setItemClickListener(new PublicTitleView.onItemClickListener() {
            @Override
            public void itemClickListener() {
                finish();
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_regular;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.zhijingmaiLayout:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.ActivityExtra.TYPE, getIntent().getStringExtra(Constants.ActivityExtra.TYPE));
                showActivity(OpenActivity.class, bundle);
                break;
            case R.id.xiaochengxuLayout:
                QRCodeDialog qrCodeDialog=new QRCodeDialog(this,"");
                qrCodeDialog.show();
                break;
            case R.id.passwordLayout:
                InputPassWordDialog inputPassWordDialog = new InputPassWordDialog(this,false);
                inputPassWordDialog.show();
                break;
        }
    }

}
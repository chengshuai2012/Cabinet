package com.link.cloud.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.link.cloud.R;
import com.link.cloud.base.AppBarActivity;
import com.link.cloud.widget.BottomSelectDialog;
import com.link.cloud.widget.PublicTitleView;
import com.zitech.framework.utils.ViewUtils;

/**
 * 作者：qianlu on 2018/10/10 17:24
 * 邮箱：zar.l@qq.com
 */
@SuppressLint("Registered")
public class PassWordOpenActivity extends AppBarActivity {
    private android.widget.LinearLayout lockLayout;
    private android.widget.TextView lockLocation;
    private android.widget.TextView lastButton;
    private android.widget.TextView sureButton;

    @Override
    protected void initViews() {
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_passwordopen;
    }

    private void initView() {
        lockLayout = (LinearLayout) findViewById(R.id.lockLayout);
        lockLocation = (TextView) findViewById(R.id.lockLocation);
        lastButton = (TextView) findViewById(R.id.lastButton);
        sureButton = (TextView) findViewById(R.id.sureButton);
        PublicTitleView publicTitleView = (PublicTitleView) findViewById(R.id.publicTitle);
        publicTitleView.setItemClickListener(new PublicTitleView.onItemClickListener() {
            @Override
            public void itemClickListener() {
                finish();
            }
        });
        ViewUtils.setOnClickListener(lockLayout, this);
        ViewUtils.setOnClickListener(sureButton, this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.lockLayout:
                showSelectDialog();
                break;
            case R.id.sureButton:
                showActivity(VipOpenSuccessActivity.class);
                break;


        }
    }

    private void showSelectDialog() {
        BottomSelectDialog bottomSelectDialog = new BottomSelectDialog(this);
        bottomSelectDialog.show();
    }
}

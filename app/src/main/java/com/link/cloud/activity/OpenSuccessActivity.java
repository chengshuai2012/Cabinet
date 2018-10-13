package com.link.cloud.activity;

import android.annotation.SuppressLint;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.link.cloud.R;
import com.link.cloud.base.AppBarActivity;

/**
 * 作者：qianlu on 2018/10/10 11:13
 * 邮箱：zar.l@qq.com
 * 打开成功
 */
@SuppressLint("Registered")
public class OpenSuccessActivity extends AppBarActivity {


    private android.widget.TextView cardNameText;
    private android.widget.TextView nameText;
    private android.widget.TextView phoneText;
    private android.widget.TextView lockId;
    private android.widget.TextView sureButton;

    @Override
    protected void initViews() {
        initView();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_opensuccess;
    }

    private void initView() {
        cardNameText = (TextView) findViewById(R.id.cardNameText);
        nameText = (TextView) findViewById(R.id.nameText);
        phoneText = (TextView) findViewById(R.id.phoneText);
        lockId = (TextView) findViewById(R.id.lockId);
        sureButton = (TextView) findViewById(R.id.sureButton);
    }
}

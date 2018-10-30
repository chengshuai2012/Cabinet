package com.link.cloud.activity;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.link.cloud.R;
import com.link.cloud.base.AppBarActivity;
import com.link.cloud.widget.PublicTitleView;

/**
 * 作者：qianlu on 2018/10/10 11:13
 * 邮箱：zar.l@qq.com
 * 打开成功
 */
@SuppressLint("Registered")
public class RegularOpenSuccessActivity extends AppBarActivity {


    private TextView cardNameText;
    private TextView nameText;
    private TextView phoneText;
    private TextView lockId;
    private TextView sureButton;
    private PublicTitleView publicTitle;

    @Override
    protected void initViews() {
        initView();
        getToolbar().setBackground(getResources().getDrawable(R.drawable.ic_appbar_image));


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_regularopensuccess;
    }

    private void initView() {
        cardNameText = (TextView) findViewById(R.id.cardNameText);
        nameText = (TextView) findViewById(R.id.nameText);
        phoneText = (TextView) findViewById(R.id.phoneText);
        lockId = (TextView) findViewById(R.id.lockId);
        sureButton = (TextView) findViewById(R.id.sureButton);
        publicTitle = (PublicTitleView) findViewById(R.id.publicTitle);
        publicTitle.setItemClickListener(new PublicTitleView.onItemClickListener() {
            @Override
            public void itemClickListener() {
                finish();
            }
        });
    }

    @Override
    public void modelMsg(int state, String msg) {

    }
}

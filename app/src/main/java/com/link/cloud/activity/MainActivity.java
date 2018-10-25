package com.link.cloud.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.link.cloud.R;
import com.link.cloud.base.BaseActivity;
import com.zitech.framework.utils.ViewUtils;

public class MainActivity extends BaseActivity {

    private TextView member;
    private TextView manager;
    private TextClock textclock;
    private LinearLayout openLayout;
    private LinearLayout closeLayout;
    private LinearLayout regularLayout;
    private LinearLayout vipLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    @Override
    protected void initViews() {

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
        switch (v.getId()) {
            case R.id.regularLayout:
                showActivity(RegularActivity.class);
                break;
            case R.id.vipLayout:
                showActivity(VipActivity.class);
                break;


        }
    }
}

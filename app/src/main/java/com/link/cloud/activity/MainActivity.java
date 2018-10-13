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
        openLayout = (LinearLayout) findViewById(R.id.openLayout);
        closeLayout = (LinearLayout) findViewById(R.id.closeLayout);

        ViewUtils.setOnClickListener(openLayout,this);
        ViewUtils.setOnClickListener(closeLayout,this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.openLayout:
            case R.id.closeLayout:

                showActivity(ChooseActivity.class);
                break;


        }
    }
}

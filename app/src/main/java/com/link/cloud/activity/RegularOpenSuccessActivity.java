package com.link.cloud.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.link.cloud.Constants;
import com.link.cloud.R;
import com.link.cloud.base.BaseActivity;
import com.link.cloud.network.bean.CabinetInfo;
import com.link.cloud.utils.RxTimerUtil;
import com.link.cloud.widget.PublicTitleView;
import com.zitech.framework.utils.ViewUtils;

/**
 * 作者：qianlu on 2018/10/10 11:13
 * 邮箱：zar.l@qq.com
 * 打开成功
 */
@SuppressLint("Registered")
public class RegularOpenSuccessActivity extends BaseActivity {


    private TextView cardNameText;
    private TextView nameText;
    private TextView phoneText;
    private TextView lockId;
    private TextView sureButton;
    private PublicTitleView publicTitle;
    private android.widget.TextView openWay;
    private android.widget.TextView openSuccessText;
    private android.widget.LinearLayout openSuccessLayout;
    private android.widget.LinearLayout passwordLayout;
    private TextView finishDealButton;
    private android.widget.EditText containerNo;
    private android.widget.EditText inputPassword;
    private android.widget.Button bindKeypad1;
    private android.widget.Button bindKeypad2;
    private android.widget.Button bindKeypad3;
    private android.widget.Button bindKeypad4;
    private android.widget.Button bindKeypad5;
    private android.widget.Button bindKeypad6;
    private android.widget.Button bindKeypad7;
    private android.widget.Button bindKeypad8;
    private android.widget.Button bindKeypad9;
    private android.widget.Button cleanButton;
    private android.widget.Button bindKeypad0;
    private android.widget.Button deleteButton;
    private android.widget.Button backButton;
    private CabinetInfo cabinetInfo;
    RxTimerUtil rxTimerUtil;

    @Override
    protected void initViews() {
        rxTimerUtil = new RxTimerUtil();
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_regularopensuccess;
    }

    private void initView() {
        rxTimerUtil.timer(20000, new RxTimerUtil.IRxNext() {
            @Override
            public void doNext(long number) {
                finish();
            }
        });
        cardNameText = (TextView) findViewById(R.id.cardNameText);
        nameText = (TextView) findViewById(R.id.nameText);
        phoneText = (TextView) findViewById(R.id.phoneText);
        openSuccessText = (TextView) findViewById(R.id.openSuccessText);
        lockId = (TextView) findViewById(R.id.lockId);
        sureButton = (TextView) findViewById(R.id.sureButton);
        publicTitle = (PublicTitleView) findViewById(R.id.publicTitle);
        publicTitle.setItemClickListener(new PublicTitleView.onItemClickListener() {
            @Override
            public void itemClickListener() {
                finish();
            }
        });
        openWay = findViewById(R.id.openWay);
        openSuccessLayout = (LinearLayout) findViewById(R.id.openSuccessLayout);
        finishDealButton = (TextView) findViewById(R.id.finishDealButton);
        containerNo = (EditText) findViewById(R.id.containerNo);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        bindKeypad1 = (Button) findViewById(R.id.bind_keypad_1);
        bindKeypad2 = (Button) findViewById(R.id.bind_keypad_2);
        bindKeypad3 = (Button) findViewById(R.id.bind_keypad_3);
        bindKeypad4 = (Button) findViewById(R.id.bind_keypad_4);
        bindKeypad5 = (Button) findViewById(R.id.bind_keypad_5);
        bindKeypad6 = (Button) findViewById(R.id.bind_keypad_6);
        bindKeypad7 = (Button) findViewById(R.id.bind_keypad_7);
        bindKeypad8 = (Button) findViewById(R.id.bind_keypad_8);
        bindKeypad9 = (Button) findViewById(R.id.bind_keypad_9);
        cleanButton = (Button) findViewById(R.id.cleanButton);
        bindKeypad0 = (Button) findViewById(R.id.bind_keypad_0);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        backButton = (Button) findViewById(R.id.backButton);
        passwordLayout = findViewById(R.id.passwordLayout);
        ViewUtils.setOnClickListener(finishDealButton, this);


        if (getIntent().getExtras().getString(Constants.ActivityExtra.TYPE) != null && getIntent().getExtras().getString(Constants.ActivityExtra.TYPE).equals("PASSWORD")) {
            passwordLayout.setVisibility(View.VISIBLE);
        } else {
            openSuccessLayout.setVisibility(View.VISIBLE);
            if (getIntent().getSerializableExtra(Constants.ActivityExtra.ENTITY) != null) {
                cabinetInfo = (CabinetInfo) getIntent().getSerializableExtra(Constants.ActivityExtra.ENTITY);
            }
            if (cabinetInfo != null) {
                nameText.setText(cabinetInfo.getNickname() == null ? "" : getResources().getString(R.string.name) + cabinetInfo.getNickname());
                phoneText.setText(cabinetInfo.getPhone() == null ? "" : getResources().getString(R.string.phone) + cabinetInfo.getPhone());
                lockId.setText(cabinetInfo.getLockNo() + getResources().getString(R.string.open_nun));
                openWay.setText(cabinetInfo.getOpenWay());
                openSuccessText.setBackground(getResources().getDrawable(R.drawable.border_gray_gradient));
                openSuccessText.setTextColor(getResources().getColor(R.color.black));
                openWay.setTextColor(getResources().getColor(R.color.color_FFABB6C8));
                openWay.setBackground(null);
            }
        }
    }

    @Override
    public void modelMsg(int state, String msg) {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.finishDealButton:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rxTimerUtil.cancel();
    }
}

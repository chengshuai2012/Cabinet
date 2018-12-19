package com.link.cloud.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.link.cloud.Constants;
import com.link.cloud.R;
import com.link.cloud.base.BaseActivity;
import com.link.cloud.controller.RegularOpenController;
import com.link.cloud.network.HttpConfig;
import com.link.cloud.network.bean.APPVersionBean;
import com.link.cloud.network.bean.CabinetInfo;

import com.link.cloud.utils.TTSUtils;
import com.link.cloud.utils.Utils;
import com.link.cloud.widget.PublicTitleView;
import com.zitech.framework.utils.ViewUtils;


/**
 * 作者：qianlu on 2018/10/10 11:13
 * 邮箱：zar.l@qq.com
 * 打开成功
 */
@SuppressLint("Registered")
public class RegularOpenSuccessActivity extends BaseActivity implements RegularOpenController.RegularOpenControllerListener {


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
    private TextView inputNumAndPass;
    private CabinetInfo cabinetInfo;
    private StringBuilder lockNumber;
    private StringBuilder password;
    private RegularOpenController regularOpenController;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initViews() {
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_regularopensuccess;
    }
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            finish();
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        regularOpenController = new RegularOpenController(this);
        handler.sendEmptyMessageDelayed(1,10000);
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
                if(Constants.CABINET_TYPE==Constants.VIP_REGULAR_CABINET){
                    skipActivity(MainActivity.class);
                }else {
                    skipActivity(RegularActivity.class);
                }
            }
        });
        lockNumber = new StringBuilder();
        password = new StringBuilder();
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
        inputNumAndPass = findViewById(R.id.inputNumAndPass);
        ViewUtils.setOnClickListener(finishDealButton, this);
        ViewUtils.setOnClickListener(bindKeypad1, this);
        ViewUtils.setOnClickListener(bindKeypad2, this);
        ViewUtils.setOnClickListener(bindKeypad3, this);
        ViewUtils.setOnClickListener(bindKeypad4, this);
        ViewUtils.setOnClickListener(bindKeypad5, this);
        ViewUtils.setOnClickListener(bindKeypad6, this);
        ViewUtils.setOnClickListener(bindKeypad7, this);
        ViewUtils.setOnClickListener(bindKeypad8, this);
        ViewUtils.setOnClickListener(bindKeypad9, this);
        ViewUtils.setOnClickListener(cleanButton, this);
        ViewUtils.setOnClickListener(bindKeypad0, this);
        ViewUtils.setOnClickListener(deleteButton, this);
        ViewUtils.setOnClickListener(backButton, this);
        ViewUtils.setOnClickListener(sureButton, this);

        containerNo.setShowSoftInputOnFocus(false);
        inputPassword.setShowSoftInputOnFocus(false);

        if (getIntent().getExtras().getString(Constants.ActivityExtra.TYPE) != null && getIntent().getExtras().getString(Constants.ActivityExtra.TYPE).equals("PASSWORD")) {
            passwordLayout.setVisibility(View.VISIBLE);
            inputNumAndPass.setVisibility(View.VISIBLE);
            openWay.setText(getResources().getString(R.string.password_open));

        } else {
            openSuccessLayout.setVisibility(View.VISIBLE);
            if (getIntent().getSerializableExtra(Constants.ActivityExtra.ENTITY) != null) {
                cabinetInfo = (CabinetInfo) getIntent().getSerializableExtra(Constants.ActivityExtra.ENTITY);
            }
            if (cabinetInfo != null) {
                nameText.setText(cabinetInfo.getNickname() == null ? "" : getResources().getString(R.string.name) + cabinetInfo.getNickname());
                phoneText.setText(cabinetInfo.getPhone() == null ? "" : getResources().getString(R.string.phone) + cabinetInfo.getPhone());
                lockId.setText(cabinetInfo.getCabinetNo() + getResources().getString(R.string.open_nun));
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
                if(Constants.CABINET_TYPE==Constants.VIP_REGULAR_CABINET){
                    skipActivity(MainActivity.class);
                }else {
                    skipActivity(RegularActivity.class);
                }
                break;
            case R.id.bind_keypad_0:
            case R.id.bind_keypad_1:
            case R.id.bind_keypad_2:
            case R.id.bind_keypad_3:
            case R.id.bind_keypad_4:
            case R.id.bind_keypad_5:
            case R.id.bind_keypad_6:
            case R.id.bind_keypad_7:
            case R.id.bind_keypad_8:
            case R.id.bind_keypad_9:
                if (containerNo.isFocused()) {
                    lockNumber.append(((TextView) v).getText());
                    if (containerNo != null) {
                        containerNo.setText(lockNumber.toString());
                        containerNo.setSelection(lockNumber.length());
                    }
                } else {
                    password.append(((TextView) v).getText());
                    if (password != null) {
                        inputPassword.setText(password.toString());
                        inputPassword.setSelection(password.length());
                    }
                }

                break;
            case R.id.deleteButton:
                if (containerNo.isFocused()) {
                    if (lockNumber.length() >= 1) {
                        lockNumber.deleteCharAt(lockNumber.length() - 1);
                        lockNumber.setLength(lockNumber.length());
                    } else {
                        lockNumber.setLength(lockNumber.length());
                    }
                    if (containerNo != null) {
                        containerNo.setText(lockNumber.toString());
                    }
                    containerNo.setSelection(lockNumber.length());
                } else {
                    if (password.length() >= 1) {
                        password.deleteCharAt(password.length() - 1);
                        password.setLength(password.length());
                    } else {
                        password.setLength(password.length());
                    }
                    if (password != null) {
                        inputPassword.setText(password.toString());
                    }
                    containerNo.setSelection(password.length());
                }
                break;
            case R.id.cleanButton:
                if (containerNo.isFocused()) {
                    lockNumber.delete(0, lockNumber.length());
                    lockNumber.setLength(lockNumber.length());
                    if (containerNo != null) {
                        containerNo.setText(lockNumber.toString());
                    }
                    containerNo.setSelection(lockNumber.length());

                } else {
                    password.delete(0, password.length());
                    password.setLength(password.length());
                    if (password != null) {
                        inputPassword.setText(password.toString());
                    }
                    inputPassword.setSelection(password.length());
                }
                break;
            case R.id.backButton:
                if(Constants.CABINET_TYPE==Constants.VIP_REGULAR_CABINET){
                    skipActivity(MainActivity.class);
                }else {
                    skipActivity(RegularActivity.class);
                }

                break;

            case R.id.sureButton:

                String edit_pswText = inputPassword.getText().toString();
                String containerNo_text = containerNo.getText().toString();

                if (TextUtils.isEmpty(edit_pswText) || TextUtils.isEmpty(containerNo_text)) {
                    speak(getResources().getString(R.string.please_input_right));
                    return;
                }

                String fisrt = Utils.getMD5(edit_pswText).toUpperCase();
                final String second = Utils.getMD5(fisrt).toUpperCase();
                 cabinetInfo = realm.where(CabinetInfo.class).equalTo("cabinetNo", containerNo_text).findFirst();
                if (cabinetInfo == null) {
                    speak(getResources().getString(R.string.please_input_right));
                } else {
                    regularOpenController.OpenLockByPass(cabinetInfo.getUuid(),edit_pswText);
                }
                break;


        }
    }

    @Override
    public void gotoSetting(String pass) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(1);
    }

    @Override
    public void OpenSuccess(CabinetInfo cabinetInfo) {

    }

    @Override
    public void returnSuccess(CabinetInfo cabinetInfo) {

    }

    @Override
    public void openFaild(String message, String code) {
        if (code.equals("400000100000") ) {
            skipActivity(SettingActivity.class);
            TTSUtils.getInstance().speak(getString(R.string.login_fail));
        }else if(code.equals("400000999102")) {
            HttpConfig.TOKEN = "";
            Intent intent1 = new Intent(this, SplashActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
            android.os.Process.killProcess(android.os.Process.myPid());
        }else {
            speak(message);
        }
    }

    @Override
    public void returnFail(String message, String code) {

    }

    @Override
    public void onFail(Throwable e, boolean isNetWork) {
        if(isNetWork){
            speak(getResources().getString(R.string.network_unavailable));
        }else {
            speak(getResources().getString(R.string.parse_error));
        }
    }

    @Override
    public void SuccessByQr(CabinetInfo cabinetInfo) {

    }

    @Override
    public void OpenLockByPass(APPVersionBean appVersionBean) {

    }
}

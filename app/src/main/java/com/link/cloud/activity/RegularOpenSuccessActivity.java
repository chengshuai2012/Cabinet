package com.link.cloud.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
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
import com.link.cloud.utils.TTSUtils;
import com.link.cloud.utils.Utils;
import com.link.cloud.widget.PublicTitleView;
import com.zitech.framework.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

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
    private TextView inputNumAndPass;
    private CabinetInfo cabinetInfo;
    RxTimerUtil rxTimerUtil;
    private StringBuilder lockNumber;
    private StringBuilder password;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initViews() {
        rxTimerUtil = new RxTimerUtil();
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_regularopensuccess;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        rxTimerUtil.timer(30000, new RxTimerUtil.IRxNext() {
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
                finish();
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


                final RealmResults<CabinetInfo> all = realm.where(CabinetInfo.class).findAll();
                List<CabinetInfo> cabinetInfos = new ArrayList<>();
                cabinetInfos.addAll(realm.copyFromRealm(all));
                CabinetInfo cabinetInfo = null;
                for (CabinetInfo info : cabinetInfos) {
                    if (info.getPasswd() != null && info.getPasswd().equals(second) && String.valueOf(info.getLockNo()).equals(containerNo_text)) {
                        cabinetInfo = info;
                        break;
                    }
                }
                if (cabinetInfo == null) {
                    speak(getResources().getString(R.string.please_input_right));
                } else {
                    TTSUtils.getInstance().speak(getResources().getString(R.string.finger_success));
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.ActivityExtra.TYPE, Constants.ActivityExtra.PASSWORD);
                    bundle.putSerializable(Constants.ActivityExtra.ENTITY, cabinetInfo);
                    showActivity(RegularOpenActivity.class, bundle);
                    finish();
                }
                break;


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rxTimerUtil.cancel();
    }
}

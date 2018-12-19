package com.link.cloud.activity;

import android.annotation.SuppressLint;
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

import com.link.cloud.CabinetApplication;
import com.link.cloud.Constants;
import com.link.cloud.R;
import com.link.cloud.base.AppBarActivity;
import com.link.cloud.base.BaseActivity;
import com.link.cloud.network.bean.CabinetInfo;
import com.link.cloud.utils.OpenDoorUtil;
import com.link.cloud.utils.TTSUtils;
import com.link.cloud.utils.Utils;
import com.link.cloud.widget.PublicTitleView;
import com.orhanobut.logger.Logger;
import com.zitech.framework.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * 作者：qianlu on 2018/10/10 11:13
 * 邮箱：zar.l@qq.com
 * 打开成功
 */
@SuppressLint("Registered")
public class VipOpenSuccessActivity extends BaseActivity {


    private TextView cardNameText;
    private TextView nameText;
    private TextView phoneText;
    private TextView lockId;
    private TextView sureButton;
    private PublicTitleView publicTitle;
    private android.widget.Button openWay;
    private android.widget.LinearLayout openSuccessLayout;
    private TextView finishDealButton;
    private android.widget.EditText containerNo;
    private android.widget.EditText inputPassword;
    private android.widget.LinearLayout passwordLayout;
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
    private android.widget.Button open_way_one;

    Realm realm ;
    private StringBuilder lockNumber;
    private StringBuilder password;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initViews() {
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vipopensuccess;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        realm = Realm.getDefaultInstance();
        cardNameText = (TextView) findViewById(R.id.cardNameText);
        nameText = (TextView) findViewById(R.id.nameText);
        phoneText = (TextView) findViewById(R.id.phoneText);
        lockId = (TextView) findViewById(R.id.lockId);
        sureButton = (TextView) findViewById(R.id.sureButton);
        PublicTitleView publicTitleView = (PublicTitleView) findViewById(R.id.publicTitle);
        publicTitleView.setItemClickListener(new PublicTitleView.onItemClickListener() {
            @Override
            public void itemClickListener() {
                finish();
                handler.removeMessages(18);
            }
        });
        publicTitle = (PublicTitleView) findViewById(R.id.publicTitle);
        openWay = (Button) findViewById(R.id.openWay);
        openSuccessLayout = (LinearLayout) findViewById(R.id.openSuccessLayout);
        finishDealButton = (TextView) findViewById(R.id.finishDealButton);
        containerNo = (EditText) findViewById(R.id.containerNo);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        passwordLayout = (LinearLayout) findViewById(R.id.passwordLayout);
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
        open_way_one = (Button) findViewById(R.id.open_way_one);
        openWay = (Button) findViewById(R.id.openWay);
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
        ViewUtils.setOnClickListener(deleteButton, this);

        containerNo.setShowSoftInputOnFocus(false);
        inputPassword.setShowSoftInputOnFocus(false);

        String uuid = getIntent().getExtras().getString(Constants.ActivityExtra.UUID);
        CabinetInfo first = realm.where(CabinetInfo.class).equalTo("uuid", uuid).findFirst();
        if(first!=null){
            openLock(first);
            open_way_one.setText(getString(R.string.open_finger));
            open_way_one.setBackground(null);
            open_way_one.setTextColor(getResources().getColor(R.color.white));
            nameText.setText(first.getNickname()+"");
            phoneText.setText(first.getPhone()+"");
            lockId.setText(first.getCabinetNo()+getString(R.string.open_success));
            TTSUtils.getInstance().speak(first.getCabinetNo()+getString(R.string.open_success));
        }
        Log.e("initView: ", getIntent().getExtras().getString(Constants.ActivityExtra.TYPE));
        if (getIntent().getExtras().getString(Constants.ActivityExtra.TYPE).equals("PASSWORD")) {
            passwordLayout.setVisibility(View.VISIBLE);
            openSuccessLayout.setVisibility(View.INVISIBLE);
            lockNumber =new StringBuilder();
            password= new StringBuilder();
            open_way_one.setText(getString(R.string.password_open));
            open_way_one.setBackgroundResource(R.drawable.border_gray_vip_gradient);
            open_way_one.setTextColor(getResources().getColor(R.color.black));
            openWay.setText(getString(R.string.openSuccess));
            openWay.setTextColor(getResources().getColor(R.color.white));
            openWay.setBackground(null);

        } else {
            openSuccessLayout.setVisibility(View.VISIBLE);
            passwordLayout.setVisibility(View.INVISIBLE);

        }

    }
    public void openLock(CabinetInfo info){
        int lockplate=info.getLockNo();
        int nuberlock=info.getLineNo();
        if (nuberlock>10){
            nuberlock=nuberlock%10;
            Logger.e("SecondFragment==="+nuberlock);
            if (nuberlock==0){
                nuberlock=10;
                Logger.e("SecondFragment==="+nuberlock);
            }
        }
        try {
            if (lockplate<=10) {
                CabinetApplication.getInstance().serialPortOne.getOutputStream().write(OpenDoorUtil.openOneDoor(lockplate, nuberlock));
            }else if (lockplate>10&&lockplate<=20){
                CabinetApplication.getInstance().serialPortTwo.getOutputStream().write(OpenDoorUtil.openOneDoor(lockplate%10, nuberlock));
            }else if (lockplate>20&&lockplate<=30){
                CabinetApplication.getInstance().serialPortThree.getOutputStream().write(OpenDoorUtil.openOneDoor(lockplate%10, nuberlock));
            }

        }catch (Exception e){

        }finally {

        }
        if(openSuccessLayout.getVisibility()!=View.VISIBLE){
            openSuccessLayout.setVisibility(View.VISIBLE);
            passwordLayout.setVisibility(View.INVISIBLE);
            open_way_one.setBackground(null);
            open_way_one.setTextColor(getResources().getColor(R.color.white));
            openWay.setText(getString(R.string.openSuccess));
            openWay.setTextColor(getResources().getColor(R.color.black));
            openWay.setBackgroundResource(R.drawable.border_gray_vip_gradient);
            nameText.setText(info.getNickname()+"");
            phoneText.setText(info.getPhone()+"");
            lockId.setText(info.getCabinetNo()+getString(R.string.open_success));
            TTSUtils.getInstance().speak(info.getCabinetNo()+getString(R.string.open_success));
        }
        handler.sendEmptyMessageDelayed(18,3000);
    }
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            finish();
        }
    };
    @Override
    public void modelMsg(int state, String msg) {
    }
    @Override
    public void onClick(View v) {
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
                handler.removeMessages(18);
                break;

            case R.id.sureButton:

                String edit_pswText = inputPassword.getText().toString();
                String containerNo_text = containerNo.getText().toString();

                if (TextUtils.isEmpty(edit_pswText) || TextUtils.isEmpty(containerNo_text)) {
                    speak(getResources().getString(R.string.please_input_right));
                    return;
                }
                CabinetInfo cabinetNo = realm.where(CabinetInfo.class).equalTo("cabinetNo", containerNo_text).findFirst();
                if(cabinetNo!=null){

                }else {
                    speak(getResources().getString(R.string.cabinet_not_found));
                }
                String fisrt = Utils.getMD5(edit_pswText).toUpperCase();
                final String second = Utils.getMD5(fisrt).toUpperCase();

                if(second.equals(cabinetNo.getPasswd())){
                    openLock(cabinetNo);
                }
                break;


        }
    }

    @Override
    public void gotoSetting(String pass) {

    }

}

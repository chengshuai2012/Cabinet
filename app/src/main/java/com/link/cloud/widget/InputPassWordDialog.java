package com.link.cloud.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.link.cloud.R;
import com.link.cloud.utils.Utils;
import com.zitech.framework.utils.ViewUtils;
import com.zitech.framework.widget.ValidDialog;

/**
 * 作者：qianlu on 2018/10/26 15:08
 * 邮箱：zar.l@qq.com
 */
public class InputPassWordDialog extends ValidDialog implements View.OnClickListener {


    private EditText phoneNum;
    private Button bindKeypad1;
    private Button bindKeypad2;
    private Button bindKeypad3;
    private Button bindKeypad4;
    private Button bindKeypad5;
    private Button bindKeypad6;
    private Button bindKeypad7;
    private Button bindKeypad8;
    private Button bindKeypad9;
    private Button deleteButton;
    private Button bindKeypad0;
    private Button cleanButton;
    private Button sureButton;
    private ImageView closeButton;
    private StringBuilder builder;
    private boolean isVip;


    public InputPassWordDialog(Context context, boolean isVip) {
        super(context, R.style.CommonDialog);
        this.isVip = isVip;
        setContentView(R.layout.dialog_inputphone);
        initView();
    }


    private void initView() {
        phoneNum = (EditText) findViewById(R.id.phoneNum);
        bindKeypad1 = (Button) findViewById(R.id.bind_keypad_1);
        bindKeypad2 = (Button) findViewById(R.id.bind_keypad_2);
        bindKeypad3 = (Button) findViewById(R.id.bind_keypad_3);
        bindKeypad4 = (Button) findViewById(R.id.bind_keypad_4);
        bindKeypad5 = (Button) findViewById(R.id.bind_keypad_5);
        bindKeypad6 = (Button) findViewById(R.id.bind_keypad_6);
        bindKeypad7 = (Button) findViewById(R.id.bind_keypad_7);
        bindKeypad8 = (Button) findViewById(R.id.bind_keypad_8);
        bindKeypad9 = (Button) findViewById(R.id.bind_keypad_9);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        bindKeypad0 = (Button) findViewById(R.id.bind_keypad_0);
        cleanButton = (Button) findViewById(R.id.cleanButton);
        sureButton = (Button) findViewById(R.id.sureButton);
        closeButton = (ImageView) findViewById(R.id.closeButton);
        if (isVip) {
            sureButton.setBackground(getContext().getResources().getDrawable(R.drawable.ic_vip_btn_bg));
        } else {
            sureButton.setBackground(getContext().getResources().getDrawable(R.drawable.ic_regural_btn_bg));
        }
        Utils.setCanNotEditAndClick(phoneNum);
        builder = new StringBuilder();
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
        ViewUtils.setOnClickListener(sureButton, this);
        ViewUtils.setOnClickListener(closeButton, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closeButton:
                dismiss();
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
                builder.append(((TextView) v).getText());
                if (phoneNum != null) {
                    phoneNum.setText(builder.toString());
                }
                break;
            case R.id.deleteButton:
                if (builder.length() >= 1) {
                    builder.deleteCharAt(builder.length() - 1);
                    builder.setLength(builder.length());
                } else {
                    builder.setLength(builder.length());
                }
                if (phoneNum != null) {
                    phoneNum.setText(builder.toString());
                }
                break;

            case R.id.cleanButton:
                builder.delete(0, builder.length());
                builder.setLength(builder.length());
                if (phoneNum != null) {
                    phoneNum.setText(builder.toString());
                }
                break;

        }

    }
}


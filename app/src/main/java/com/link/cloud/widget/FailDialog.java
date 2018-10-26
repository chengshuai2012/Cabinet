package com.link.cloud.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.link.cloud.R;
import com.zitech.framework.utils.ViewUtils;
import com.zitech.framework.widget.ValidDialog;

/**
 * 作者：qianlu on 2018/10/26 17:39
 * 邮箱：zar.l@qq.com
 */
public class FailDialog extends ValidDialog implements View.OnClickListener {


    private ImageView qrcodeImage;
    private ImageView closeButton;
    private TextView faildReason;
    private String reason;


    public FailDialog(Context context, String reason) {
        super(context);
        setContentView(R.layout.dialog_fail);
        this.reason=reason;
        initView();
    }

    private void initView() {
        qrcodeImage = (ImageView) findViewById(R.id.qrcodeImage);
        closeButton = (ImageView) findViewById(R.id.closeButton);
        faildReason = (TextView) findViewById(R.id.faildReason);
        if (TextUtils.isEmpty(reason)) {
            faildReason.setText(reason);
        }
        ViewUtils.setOnClickListener(closeButton,this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closeButton:
                dismiss();
                break;
        }
    }
}

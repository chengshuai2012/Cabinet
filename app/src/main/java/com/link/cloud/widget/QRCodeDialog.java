package com.link.cloud.widget;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.link.cloud.R;
import com.zitech.framework.utils.ViewUtils;
import com.zitech.framework.widget.ValidDialog;

/**
 * 作者：qianlu on 2018/10/26 17:22
 * 邮箱：zar.l@qq.com
 */
public class QRCodeDialog extends ValidDialog implements View.OnClickListener {


    private TextView passwordCheack;
    private ImageView qrcodeImage;
    private ImageView closeButton;
    private String imageUrl;

    public QRCodeDialog(Context context,String imageUrl) {
        super(context, R.style.CommonDialog);
        setContentView(R.layout.dialog_qrcode);
        initView();
    }

    private void initView() {
        passwordCheack = (TextView) findViewById(R.id.passwordCheack);
        qrcodeImage = (ImageView) findViewById(R.id.qrcodeImage);
        closeButton = (ImageView) findViewById(R.id.closeButton);

        ViewUtils.setOnClickListener(passwordCheack, this);
        ViewUtils.setOnClickListener(qrcodeImage, this);
        ViewUtils.setOnClickListener(closeButton, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.passwordCheack:

                break;
            case R.id.qrcodeImage:

                break;
            case R.id.closeButton:
                dismiss();
                break;


        }


    }
}

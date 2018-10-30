package com.link.cloud.activity;

import android.annotation.SuppressLint;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;

import com.link.cloud.CabinetApplication;
import com.link.cloud.Constants;
import com.link.cloud.R;
import com.link.cloud.base.AppBarActivity;
import com.link.cloud.network.bean.AllUser;
import com.link.cloud.utils.RxTimerUtil;
import com.link.cloud.widget.InputPassWordDialog;
import com.link.cloud.widget.PublicTitleView;
import com.link.cloud.widget.QRCodeDialog;
import com.zitech.framework.utils.ToastMaster;
import com.zitech.framework.utils.ViewUtils;

import java.util.logging.Logger;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * 作者：qianlu on 2018/10/10 11:13
 * 邮箱：zar.l@qq.com
 * 选择开柜方式
 */
@SuppressLint("Registered")
public class RegularActivity extends AppBarActivity {


    private RelativeLayout zhijingmaiLayout;
    private RelativeLayout xiaochengxuLayout;
    private RelativeLayout passwordLayout;

    private PublicTitleView publicTitleView;
    private RxTimerUtil rxTimerUtil;
    Realm realm;

    @Override
    protected void initViews() {
        getToolbar().setBackground(getResources().getDrawable(R.drawable.ic_regular_banner));

        realm = Realm.getDefaultInstance();
        zhijingmaiLayout = findViewById(R.id.zhijingmaiLayout);
        xiaochengxuLayout = findViewById(R.id.xiaochengxuLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        publicTitleView = findViewById(R.id.publicTitle);

        ViewUtils.setOnClickListener(zhijingmaiLayout, this);
        ViewUtils.setOnClickListener(xiaochengxuLayout, this);
        ViewUtils.setOnClickListener(passwordLayout, this);
        publicTitleView.setItemClickListener(new PublicTitleView.onItemClickListener() {
            @Override
            public void itemClickListener() {
                finish();
            }
        });
        rxTimerUtil = new RxTimerUtil();
        finger();
    }

    private void finger() {
        rxTimerUtil.interval(2000, new RxTimerUtil.IRxNext() {
            @Override
            public void doNext(long number) {
                int state = CabinetApplication.getVenueUtils().getState();
                if (state == 3) {
                    RealmResults<AllUser> users = realm.where(AllUser.class).findAll();
                    if (null != CabinetApplication.getVenueUtils().identifyNewImg(users)) {
                        rxTimerUtil.cancel();
                    } else {
                        ToastMaster.shortToast(getResources().getString(R.string.cheack_fail));

                    }
                }
                if (state == 4) {
                    ToastMaster.shortToast(getResources().getString(R.string.move_finger));
                }
                if (state != 4 && state != 3) {
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        rxTimerUtil.cancel();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_regular;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.zhijingmaiLayout:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.ActivityExtra.TYPE, getIntent().getStringExtra(Constants.ActivityExtra.TYPE));
                showActivity(OpenActivity.class, bundle);
                break;
            case R.id.xiaochengxuLayout:
                QRCodeDialog qrCodeDialog = new QRCodeDialog(this, "");
                qrCodeDialog.show();
                break;
            case R.id.passwordLayout:
                InputPassWordDialog inputPassWordDialog = new InputPassWordDialog(this, false);
                inputPassWordDialog.show();
                break;
        }
    }

    @Override
    public void modelMsg(int state, String msg) {

    }
}

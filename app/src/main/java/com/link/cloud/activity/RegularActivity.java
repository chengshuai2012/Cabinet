package com.link.cloud.activity;

import android.annotation.SuppressLint;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.link.cloud.CabinetApplication;
import com.link.cloud.Constants;
import com.link.cloud.R;
import com.link.cloud.base.AppBarActivity;
import com.link.cloud.base.BaseActivity;
import com.link.cloud.controller.MainController;
import com.link.cloud.network.BaseEntity;
import com.link.cloud.network.bean.AllUser;
import com.link.cloud.network.bean.BindUser;
import com.link.cloud.network.bean.CabinetInfo;
import com.link.cloud.utils.RxTimerUtil;
import com.link.cloud.widget.InputPassWordDialog;
import com.link.cloud.widget.PublicTitleView;
import com.link.cloud.widget.QRCodeDialog;
import com.zitech.framework.utils.ToastMaster;
import com.zitech.framework.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import android_serialport_api.SerialPort;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * 作者：qianlu on 2018/10/10 11:13
 * 邮箱：zar.l@qq.com
 * 选择开柜方式
 */
@SuppressLint("Registered")
public class RegularActivity extends BaseActivity implements MainController.MainControllerListener {


    private LinearLayout zhijingmaiLayout;
    private LinearLayout xiaochengxuLayout;
    private TextView passwordLayout;
    private PublicTitleView publicTitleView;
    private RxTimerUtil rxTimerUtil;
    Realm realm;
    private MainController mainController;
    private String uuid;
    private LinearLayout setLayout;
    private TextView member;
    private TextView manager;


    @Override
    protected void initViews() {
        realm = Realm.getDefaultInstance();
        zhijingmaiLayout = findViewById(R.id.zhijingmaiLayout);
        xiaochengxuLayout = findViewById(R.id.xiaochengxuLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        publicTitleView = findViewById(R.id.publicTitle);
        setLayout = (LinearLayout) findViewById(R.id.setLayout);
        member = (TextView) findViewById(R.id.member);
        manager = (TextView) findViewById(R.id.manager);

        mainController = new MainController(this);
        ViewUtils.setOnClickListener(zhijingmaiLayout, this);
        ViewUtils.setOnClickListener(xiaochengxuLayout, this);
        ViewUtils.setOnClickListener(passwordLayout, this);
        ViewUtils.setOnClickListener(manager, this);
        publicTitleView.setItemClickListener(new PublicTitleView.onItemClickListener() {
            @Override
            public void itemClickListener() {
                finish();
            }
        });
        rxTimerUtil = new RxTimerUtil();
        finger();
        if (!TextUtils.isEmpty(getIntent().getStringExtra(Constants.ActivityExtra.TYPE))) {
            setLayout.setVisibility(View.GONE);
        }

    }

    private void finger() {
        rxTimerUtil.interval(2000, new RxTimerUtil.IRxNext() {
            @Override
            public void doNext(long number) {
                int state = CabinetApplication.getVenueUtils().getState();
                if (state == 3) {
                    RealmResults<AllUser> users = realm.where(AllUser.class).findAll();
                    List<AllUser> peoples = new ArrayList<>();
                    peoples.addAll(realm.copyFromRealm(users));
                    String uid = CabinetApplication.getVenueUtils().identifyNewImg(peoples);
                    if (null != uid && !TextUtils.isEmpty(uid)) {
                        unlocking(uid);

                    } else {
                        ToastMaster.shortToast(getResources().getString(R.string.cheack_fail));
                    }
                }
                if (state == 4) {
                    ToastMaster.shortToast(getResources().getString(R.string.cheack_fail));
                }
                if (state != 4 && state != 3) {
                }
            }
        });
    }

    private void unlocking(String uid) {
        uuid = uid;
        mainController.temCabinet(uid);
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
                showActivity(RegularOpenActivity.class, bundle);
                break;
            case R.id.xiaochengxuLayout:

                break;
            case R.id.passwordLayout:
                Bundle bundle1 = new Bundle();
                bundle1.putString(Constants.ActivityExtra.TYPE, "PASSWORD");
                showActivity(RegularOpenSuccessActivity.class, bundle1);
                break;

            case R.id.manager:
                skipActivity(SettingActivity.class);
                break;
        }
    }

    @Override
    public void modelMsg(int state, String msg) {

    }

    @Override
    public void onLoginSuccess(String token) {

    }

    @Override
    public void onMainErrorCode(String msg) {
        if (TextUtils.isEmpty(uuid) && msg.equals("400000500026")) {
            mainController.returnCabinet(uuid);
        }
    }

    @Override
    public void onMainFail(Throwable e, boolean isNetWork) {
        if (TextUtils.isEmpty(uuid)) {
            mainController.returnCabinet(uuid);
        }
    }

    @Override
    public void getUserSuccess(BindUser data) {

    }

    @Override
    public void onCabinetInfoSuccess(RealmList<CabinetInfo> data) {


    }

    @Override
    public void temCabinetSuccess(BaseEntity baseEntity) {
        rxTimerUtil.cancel();
//        try {
//            if (Integer.parseInt(data) <= 10) {
//                activity.serialpprt_wk1.getOutputStream().write(openDoorUtil.openOneDoor(Integer.parseInt(lockplate), nuberlock));
//                com.orhanobut.logger.Logger.e("FirstFragment===1" + Integer.parseInt(lockplate) + "====" + nuberlock);
//            } else if (Integer.parseInt(lockplate) > 10 && Integer.parseInt(lockplate) <= 20) {
//                activity.serialpprt_wk2.getOutputStream().write(openDoorUtil.openOneDoor(Integer.parseInt(lockplate) % 10, nuberlock));
//                com.orhanobut.logger.Logger.e("FirstFragment===2" + Integer.parseInt(lockplate) + "====" + nuberlock);
//            } else if (Integer.parseInt(lockplate) > 20 && Integer.parseInt(lockplate) <= 30) {
//                activity.serialpprt_wk3.getOutputStream().write(openDoorUtil.openOneDoor(Integer.parseInt(lockplate) % 10, nuberlock));
//                com.orhanobut.logger.Logger.e("FirstFragment===3" + Integer.parseInt(lockplate) + "====" + nuberlock);
//            }
//        } catch (Exception e) {
//        }
    }

}

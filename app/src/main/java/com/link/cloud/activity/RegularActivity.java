package com.link.cloud.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.link.cloud.CabinetApplication;
import com.link.cloud.Constants;
import com.link.cloud.R;
import com.link.cloud.base.BaseActivity;
import com.link.cloud.controller.MainController;
import com.link.cloud.network.bean.AllUser;
import com.link.cloud.network.bean.BindUser;
import com.link.cloud.network.bean.CabinetInfo;
import com.link.cloud.network.bean.CabnetDeviceInfoBean;
import com.link.cloud.utils.RxTimerUtil;
import com.link.cloud.utils.TTSUtils;
import com.link.cloud.widget.PublicTitleView;
import com.zitech.framework.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

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
    private MainController mainController;
    private LinearLayout setLayout;
    private TextView member;
    private TextView manager;
    private EditText editText;
    private String mType;
    private boolean isScanning = false;

    @Override
    protected void initViews() {
        rxTimerUtil = new RxTimerUtil();
        zhijingmaiLayout = findViewById(R.id.zhijingmaiLayout);
        xiaochengxuLayout = findViewById(R.id.xiaochengxuLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        publicTitleView = findViewById(R.id.publicTitle);
        setLayout = (LinearLayout) findViewById(R.id.setLayout);
        member = (TextView) findViewById(R.id.member);
        manager = (TextView) findViewById(R.id.manager);
        editText = findViewById(R.id.infoId);
        if (getIntent() != null)
            mType = getIntent().getExtras().getString(Constants.ActivityExtra.TYPE);

        if (!TextUtils.isEmpty(mType)) {
            publicTitleView.setFinsh(View.GONE);
        }

        mainController = new MainController(this);
        ViewUtils.setOnClickListener(zhijingmaiLayout, this);
        ViewUtils.setOnClickListener(xiaochengxuLayout, this);
        ViewUtils.setOnClickListener(passwordLayout, this);
        ViewUtils.setOnClickListener(manager, this);
        finger();
        publicTitleView.setItemClickListener(new PublicTitleView.onItemClickListener() {
            @Override
            public void itemClickListener() {
                finish();
            }
        });
        if (!TextUtils.isEmpty(getIntent().getStringExtra(Constants.ActivityExtra.TYPE))) {
            setLayout.setVisibility(View.GONE);
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("输入过程中执行该方法", "文字变化:" + editText.getText().toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("输入前确认执行该方法", "开始输入:" + editText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("输入结束执行该方法", "输入结束:" + editText.getText().toString());

            }
        });

    }

    private void finger() {
        rxTimerUtil.interval(2000, new RxTimerUtil.IRxNext() {
            @Override
            public void doNext(long number) {
                System.out.println(String.valueOf(number));
                if (isScanning){
                    int state = CabinetApplication.getVenueUtils().getState();
                    if (state == 3) {
                        RealmResults<AllUser> users = realm.where(AllUser.class).findAll();
                        List<AllUser> peoples = new ArrayList<>();
                        peoples.addAll(realm.copyFromRealm(users));
                        String uid = CabinetApplication.getVenueUtils().identifyNewImg(peoples);
                        if (null != uid && !TextUtils.isEmpty(uid)) {
                            unlocking(uid, Constants.ActivityExtra.FINGER);
                        } else {
                            TTSUtils.getInstance().speak(getResources().getString(R.string.cheack_fail));
                        }
                    }
                    if (state == 4) {
                        TTSUtils.getInstance().speak(getResources().getString(R.string.please_move_finger));
                    }
                }
            }
        });
    }

    private void unlocking(String uid, String type) {
        TTSUtils.getInstance().speak(getResources().getString(R.string.finger_success));
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ActivityExtra.TYPE, type);
        bundle.putString(Constants.ActivityExtra.UUID, uid);
        showActivity(RegularOpenActivity.class, bundle);
        if (TextUtils.isEmpty(mType)) {
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isScanning = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isScanning = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    public void onLoginSuccess(CabnetDeviceInfoBean cabnetDeviceInfoBean) {

    }

    @Override
    public void onMainErrorCode(String msg) {
    }

    @Override
    public void onMainFail(Throwable e, boolean isNetWork) {

    }

    @Override
    public void getUserSuccess(BindUser data) {

    }

    @Override
    public void onCabinetInfoSuccess(RealmList<CabinetInfo>  data) {


    }

    @Override
    public void temCabinetSuccess(CabinetInfo cabinetBean) {


    }

}

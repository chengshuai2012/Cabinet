package com.link.cloud.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.link.cloud.CabinetApplication;
import com.link.cloud.Constants;
import com.link.cloud.R;
import com.link.cloud.base.BaseActivity;
import com.link.cloud.controller.VipController;
import com.link.cloud.network.bean.AllUser;
import com.link.cloud.network.bean.BindUser;
import com.link.cloud.network.bean.CabinetInfo;
import com.link.cloud.utils.HexUtil;
import com.link.cloud.utils.RxTimerUtil;
import com.link.cloud.widget.PublicTitleView;
import com.zitech.framework.utils.ToastMaster;
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
public class VipActivity extends BaseActivity implements VipController.VipControllerListener {


    private LinearLayout zhijingmaiLayout;
    private LinearLayout xiaochengxuLayout;
    private TextView passwordLayout;

    private PublicTitleView publicTitleView;
    private LinearLayout setLayout;
    private TextView member;
    private TextView manager;
    private VipController vipController;
    private String uid;
    private boolean isScanning = false;
    private RxTimerUtil rxTimerUtil;

    @Override
    protected void initViews() {
        zhijingmaiLayout = findViewById(R.id.zhijingmaiLayout);
        xiaochengxuLayout = findViewById(R.id.xiaochengxuLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        publicTitleView =  findViewById(R.id.publicTitle);
        setLayout = (LinearLayout) findViewById(R.id.setLayout);
        member = (TextView) findViewById(R.id.member);
        manager = (TextView) findViewById(R.id.manager);
        publicTitleView.setItemClickListener(new PublicTitleView.onItemClickListener() {
            @Override
            public void itemClickListener() {
                finish();
            }
        });
        rxTimerUtil = new RxTimerUtil();
        ViewUtils.setOnClickListener(zhijingmaiLayout, this);
        ViewUtils.setOnClickListener(xiaochengxuLayout, this);
        ViewUtils.setOnClickListener(passwordLayout, this);
        ViewUtils.setOnClickListener(setLayout, this);
        if (!TextUtils.isEmpty(getIntent().getStringExtra(Constants.ActivityExtra.TYPE))){
            setLayout.setVisibility(View.GONE);
        }
        vipController = new VipController(this);
        finger();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vip;
    }
    private void finger() {
        rxTimerUtil.interval(1000, new RxTimerUtil.IRxNext() {
            @Override
            public void doNext(long number) {
                if (isScanning){
                    int state = CabinetApplication.getVenueUtils().getState();
                    if (state == 3) {
                        RealmResults<AllUser> users = realm.where(AllUser.class).findAll();
                        List<AllUser> peoples = new ArrayList<>();
                        peoples.addAll(realm.copyFromRealm(users));
                        uid = CabinetApplication.getVenueUtils().identifyNewImg(peoples);
                        CabinetInfo uuid = realm.where(CabinetInfo.class).equalTo("uuid", uid).findFirst();
                        if (uuid!=null) {
                            unlocking(uid, Constants.ActivityExtra.FINGER);
                        } else {
                            if(uid!=null){
                                vipController.OpenVipCabinet("",uid);
                            }else {
                                String finger = HexUtil.bytesToHexString(CabinetApplication.getVenueUtils().img);
                                vipController.OpenVipCabinet(finger,"");
                            }

                        }
                    }
                    if (state == 4) {
                        ToastMaster.shortToast(getResources().getString(R.string.again_finger));
                    }
                    if (state != 4 && state != 3) {

                    }}
            }
        });
    }
    private void unlocking(String uid, String type) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ActivityExtra.TYPE, type);
        bundle.putString(Constants.ActivityExtra.UUID, uid);
        showActivity(VipOpenSuccessActivity.class, bundle);
        finish();
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
                showActivity(VipOpenSuccessActivity.class, bundle1);
                break;
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
    public void modelMsg(int state, String msg) {

    }

    @Override
    public void onVipErrorCode(String msg) {
            if("您没有租用储物柜".equals(msg)){
                Toast.makeText(this,getString(R.string.sorry_for_is_not_vip),Toast.LENGTH_LONG).show();
            }
    }

    @Override
    public void onVipFail(Throwable e, boolean isNetWork) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rxTimerUtil.cancel();
    }

    @Override
    public void getUserSuccess(BindUser data) {

    }

    @Override
    public void onCabinetInfoSuccess(RealmList<CabinetInfo> data) {

    }

    @Override
    public void temCabinetSuccess(CabinetInfo cabinetBean) {

    }
}

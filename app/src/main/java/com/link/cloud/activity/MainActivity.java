package com.link.cloud.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.link.cloud.CabinetApplication;
import com.link.cloud.Constants;
import com.link.cloud.R;
import com.link.cloud.base.BaseActivity;
import com.link.cloud.bean.DeviceInfo;
import com.link.cloud.controller.MainController;
import com.link.cloud.controller.SplashController;
import com.link.cloud.network.HttpConfig;
import com.link.cloud.network.bean.AllUser;
import com.link.cloud.network.bean.PasswordBean;
import com.link.cloud.utils.DialogCancelListener;
import com.link.cloud.utils.DialogUtils;
import com.link.cloud.utils.RxTimerDelayUtil;
import com.link.cloud.utils.RxTimerUtil;
import com.link.cloud.utils.TTSUtils;
import com.zitech.framework.utils.ViewUtils;

import java.util.ArrayList;

import io.realm.RealmResults;


@SuppressLint("Registered")
public class MainActivity extends BaseActivity implements DialogCancelListener, MainController.MainControllerListener {
    private DialogUtils dialogUtils;
    private TextView member;
    private TextView manager;
    private TextClock textclock;
    private LinearLayout openLayout;
    private LinearLayout closeLayout;
    private LinearLayout regularLayout;
    private LinearLayout vipLayout;
    private RxTimerUtil rxTimerUtil;
    private DeviceInfo deviceInfo;
    private MainController mainController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        dialogUtils = DialogUtils.getDialogUtils(this);
        dialogUtils.setDialogCanceListener(this);
        rxTimerUtil = new RxTimerUtil();
        mainController = new MainController(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        member.setBackground(getResources().getDrawable(R.drawable.border_red));
        manager.setBackground(null);
        member.setTextColor(getResources().getColor(R.color.almost_white));
        manager.setTextColor(getResources().getColor(R.color.text_gray));
    }

    @Override
    protected void initViews() {
        getDeviceInfo();
        if(Constants.CABINET_TYPE==Constants.VIP_REGULAR_CABINET){
            RegisteReciver();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(Constants.CABINET_TYPE==Constants.VIP_REGULAR_CABINET){
            RegisteReciver();
        }
    }

    private void getDeviceInfo() {
        final RealmResults<DeviceInfo> all = realm.where(DeviceInfo.class).findAll();
        if (!all.isEmpty()) {
            deviceInfo = all.get(0);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    private void initView() {
        member = (TextView) findViewById(R.id.member);
        manager = (TextView) findViewById(R.id.manager);
        textclock = (TextClock) findViewById(R.id.textclock);
        regularLayout = (LinearLayout) findViewById(R.id.regularLayout);
        vipLayout = (LinearLayout) findViewById(R.id.vipLayout);
        textclock.setTimeZone("GMT+08:00");
        ViewUtils.setOnClickListener(regularLayout, this);
        ViewUtils.setOnClickListener(vipLayout, this);
        ViewUtils.setOnClickListener(member, this);
        ViewUtils.setOnClickListener(manager, this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.regularLayout:
                bundle.putString(Constants.ActivityExtra.TYPE, "REGULAR");
                showActivity(RegularActivity.class, bundle);
                break;
            case R.id.vipLayout:
                bundle.putString(Constants.ActivityExtra.TYPE, "VIP");
                showActivity(VipActivity.class, bundle);
                break;
                case R.id.member:
                    member.setBackground(getResources().getDrawable(R.drawable.border_red));
                    manager.setBackground(null);
                    member.setTextColor(getResources().getColor(R.color.almost_white));
                    manager.setTextColor(getResources().getColor(R.color.text_gray));
                break;
                case R.id.manager:
                    View view = View.inflate(MainActivity.this, R.layout.veune_dialog, null);
                    dialogUtils.showManagerDialog(view);
                    finger();
                    manager.setBackground(getResources().getDrawable(R.drawable.border_red));
                    member.setBackground(null);
                    member.setTextColor(getResources().getColor(R.color.text_gray));
                    manager.setTextColor(getResources().getColor(R.color.almost_white));
                    RxTimerDelayUtil rxTimerDelayUtil = new RxTimerDelayUtil();
                    rxTimerDelayUtil.timer(10000,new  RxTimerDelayUtil.IRxNext(){
                        @Override
                        public void doNext(long number) {
                            dialogUtils.dissMiss();
                        }
                    });
                break;
        }
    }

    @Override
    public void gotoSetting(String pass) {
        mainController.password(pass);
    }


    @Override
    public void modelMsg(int state, String msg) {

    }

    @Override
    public void dialogCancel() {
        member.setBackground(getResources().getDrawable(R.drawable.border_red));
        manager.setBackground(null);
        member.setTextColor(getResources().getColor(R.color.almost_white));
        manager.setTextColor(getResources().getColor(R.color.text_gray));
        rxTimerUtil.cancel();
    }
    private void finger() {
        rxTimerUtil.interval(1000, new RxTimerUtil.IRxNext() {
            @Override
            public void doNext(long number) {
                    int state = CabinetApplication.getVenueUtils().getState();
                    if (state == 3) {
                        if (dialogUtils.isShowing()) {
                            String uid = null;
                            RealmResults < AllUser>managersRealm = realm.where(AllUser.class).equalTo("isadmin", 1).findAll();
                            ArrayList<AllUser> managers = new ArrayList<>();
                            managers.addAll(realm.copyFromRealm(managersRealm));
                            uid = CabinetApplication.getVenueUtils().identifyNewImg(managers);
                            if (uid != null) {
                                showActivity(SettingActivity.class);
                            } else {
                                TTSUtils.getInstance().speak(getString(R.string.no_manager));

                            }
                        }
                    }
                }
        });
    }

    @Override
    public void onVenuePay() {

    }

    @Override
    public void onMainErrorCode(String msg) {
        TTSUtils.getInstance().speak(msg);
    }

    @Override
    public void onMainFail(Throwable e, boolean isNetWork) {

    }

    @Override
    public void MainPassWord(PasswordBean passwordBean) {
        showActivity(SettingActivity.class);
    }
}

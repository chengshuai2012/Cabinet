package com.link.cloud.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.link.cloud.controller.RegularController;
import com.link.cloud.network.HttpConfig;
import com.link.cloud.network.bean.AllUser;
import com.link.cloud.network.bean.CabinetInfo;
import com.link.cloud.network.bean.EdituserRequest;
import com.link.cloud.network.bean.PasswordBean;
import com.link.cloud.utils.DialogCancelListener;
import com.link.cloud.utils.DialogUtils;
import com.link.cloud.utils.HexUtil;
import com.link.cloud.utils.RxTimerDelayUtil;
import com.link.cloud.utils.RxTimerUtil;
import com.link.cloud.utils.TTSUtils;
import com.link.cloud.widget.PublicTitleView;
import com.zitech.framework.utils.ToastMaster;
import com.zitech.framework.utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 作者：qianlu on 2018/10/10 11:13
 * 邮箱：zar.l@qq.com
 * 选择开柜方式
 */
@SuppressLint("Registered")
public class RegularActivity extends BaseActivity implements RegularController.RegularControllerListener, DialogCancelListener {

    private LinearLayout zhijingmaiLayout;
    private LinearLayout xiaochengxuLayout;
    private TextView passwordLayout;
    private PublicTitleView publicTitleView;
    private RxTimerUtil rxTimerUtil;
    private RegularController regularController;
    private LinearLayout setLayout;
    private TextView member;
    private TextView manager;
    private TextView number;
    private EditText editText;
    private String mType;
    private boolean isScanning = false;
    private boolean canGetCode;
    private RealmResults<AllUser> users;
    List<AllUser> peoples = new ArrayList<>();
    private RealmResults<AllUser> peopleIn;
    private ArrayList<AllUser> peoplesIn;
    private DialogUtils dialogUtils;
    private RealmResults<AllUser> managersRealm;
    private RealmResults<CabinetInfo> cabinetInfos;
    List<AllUser> managers = new ArrayList<>();

    boolean IsNoPerson = false;
    boolean isDeleteAll = false;
    long lastTime;
    private long allCabinet;

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
        number = (TextView) findViewById(R.id.number);
        allCabinet = realm.where(CabinetInfo.class).count();
        cabinetInfos = realm.where(CabinetInfo.class).equalTo("locked",false).findAll();
         long use = allCabinet - cabinetInfos.size();
        number.setText("已使用:"+use+"    未使用:"+cabinetInfos.size());
        cabinetInfos.addChangeListener(new RealmChangeListener<RealmResults<CabinetInfo>>() {
            @Override
            public void onChange(RealmResults<CabinetInfo> cabinetInfos) {
                long used = allCabinet - cabinetInfos.size();
                number.setText("已使用:"+used+"    未使用:"+cabinetInfos.size());
            }
        });

        editText = findViewById(R.id.infoId);
        if (Constants.CABINET_TYPE==Constants.REGULAR_CABINET) {
            publicTitleView.setFinsh(View.GONE);
        }
        if (Constants.CABINET_TYPE == Constants.REGULAR_CABINET) {
            RegisteReciver();
            managersRealm = realm.where(AllUser.class).equalTo("isadmin", 1).findAll();
            managers.addAll(realm.copyFromRealm(managersRealm));

            managersRealm.addChangeListener(new RealmChangeListener<RealmResults<AllUser>>() {
                @Override
                public void onChange(RealmResults<AllUser> allUsers) {
                    managers.clear();
                    managers.addAll(realm.copyFromRealm(managersRealm));
                }
            });
        } else {
            setLayout.setVisibility(View.GONE);
        }
        users = realm.where(AllUser.class).findAll();
        peoples.addAll(realm.copyFromRealm(users));
        users.addChangeListener(new RealmChangeListener<RealmResults<AllUser>>() {
            @Override
            public void onChange(RealmResults<AllUser> allUsers) {
                peoples.clear();
                peoples.addAll(realm.copyFromRealm(users));
            }
        });

        peopleIn = realm.where(AllUser.class).equalTo("isIn", 1).findAll();
        peoplesIn = new ArrayList<>();
        peopleIn.addChangeListener(new RealmChangeListener<RealmResults<AllUser>>() {
            @Override
            public void onChange(RealmResults<AllUser> allUsers) {
                peoplesIn.clear();
                peoplesIn.addAll(realm.copyFromRealm(peopleIn));
            }
        });
        peoplesIn.addAll(realm.copyFromRealm(peopleIn));
        regularController = new RegularController(this);
        ViewUtils.setOnClickListener(zhijingmaiLayout, this);
        ViewUtils.setOnClickListener(xiaochengxuLayout, this);
        ViewUtils.setOnClickListener(passwordLayout, this);
        ViewUtils.setOnClickListener(manager, this);
        ViewUtils.setOnClickListener(setLayout, this);
        ViewUtils.setOnClickListener(member, this);
        publicTitleView.setItemClickListener(new PublicTitleView.onItemClickListener() {
            @Override
            public void itemClickListener() {
                finish();
            }
        });
        dialogUtils = DialogUtils.getDialogUtils(this);
        dialogUtils.setDialogCanceListener(this);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
                    String str=editText.getText().toString();
                    if (str.contains("\n")) {
                        if(System.currentTimeMillis()-lastTime<1500){
                            editText.setText("");
                            return;
                        }
                        lastTime=System.currentTimeMillis();
                        JSONObject object = null;
                        try {
                            object = new JSONObject(editText.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (object == null) {
                            return;
                        }
                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
                        regularController.findUserByQr(requestBody);

                        editText.setText("");
                    }

                }
            }
        });
    finger();
    }

    String uid;

    private void finger() {
        rxTimerUtil.interval(1000, new RxTimerUtil.IRxNext() {
            @Override
            public void doNext(long number) {
                if(number>=10&&Constants.CABINET_TYPE!=Constants.REGULAR_CABINET){
                    finish();
                }
                if (isScanning) {
                    int state = CabinetApplication.getVenueUtils().getState();

                    if (state == 3) {
                        if (dialogUtils.isShowing()) {
                            uid = null;
                            uid = CabinetApplication.getVenueUtils().identifyNewImg(managers);
                            if (uid != null) {
                                showActivity(SettingActivity.class);
                            } else {
                                TTSUtils.getInstance().speak(getString(R.string.no_manager));

                            }
                        } else {
                            uid = null;
                            IsNoPerson = false;
                            uid = CabinetApplication.getVenueUtils().identifyNewImg(peoplesIn);
                            if (uid == null) {
                                uid = CabinetApplication.getVenueUtils().identifyNewImg(peoples);
                            }

                            if (uid != null) {
                                unlocking(uid, Constants.ActivityExtra.FINGER);
                            } else {
                                if (CabinetApplication.getVenueUtils().img != null) {
                                    String finger = HexUtil.bytesToHexString(CabinetApplication.getVenueUtils().img);
                                    regularController.findUser(finger);
                                    IsNoPerson = true;
                                    isDeleteAll = false;
                                }

                            }

                        }
                    }
                }
            }

        });
    }

    private void unlocking(String uid, String type) {
        if (type.equals(Constants.ActivityExtra.FINGER)) {
            speak(getResources().getString(R.string.finger_success));
        } else if (type.equals(Constants.ActivityExtra.XIAOCHENGXU)) {
            speak(getResources().getString(R.string.code_success));
        } else {
            speak(getResources().getString(R.string.password_success));
        }
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ActivityExtra.TYPE, type);
        bundle.putString(Constants.ActivityExtra.UUID, uid);
        showActivity(RegularOpenActivity.class, bundle);
        if (Constants.CABINET_TYPE!=Constants.REGULAR_CABINET) {
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
        member.setBackground(getResources().getDrawable(R.drawable.border_red));
        manager.setBackground(null);
        member.setTextColor(getResources().getColor(R.color.almost_white));
        manager.setTextColor(getResources().getColor(R.color.text_gray));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy: ","11111");
        if(Constants.CABINET_TYPE==Constants.REGULAR_CABINET){
            unRegisterReceiver();
        }
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

            case R.id.member:
                member.setBackground(getResources().getDrawable(R.drawable.border_red));
                manager.setBackground(null);
                member.setTextColor(getResources().getColor(R.color.almost_white));
                manager.setTextColor(getResources().getColor(R.color.text_gray));
                break;
            case R.id.manager:
                View view = View.inflate(RegularActivity.this, R.layout.veune_dialog, null);
                dialogUtils.showManagerDialog(view);
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
        regularController.password(pass);
    }

    @Override
    public void modelMsg(int state, String msg) {

    }


    @Override
    public void successful(final AllUser allUser) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insert(allUser);
            }
        });
        speak(getResources().getString(R.string.finger_success));
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ActivityExtra.TYPE, Constants.ActivityExtra.FINGER);
        bundle.putString(Constants.ActivityExtra.UUID, allUser.getUuid());
        showActivity(RegularOpenActivity.class, bundle);
        if (Constants.CABINET_TYPE!=Constants.REGULAR_CABINET) {
            finish();
        }
    }

    @Override
    public void qrSuccess(EdituserRequest allUser) {
        if(allUser!=null){
            unlocking(allUser.getUuid(), Constants.ActivityExtra.XIAOCHENGXU);
        }

    }

    @Override
    public void failed(String message, String code) {
        if (code.equals("400000100000") ) {
            skipActivity(SettingActivity.class);
            TTSUtils.getInstance().speak(getString(R.string.login_fail));
        }else if(code.equals("400000999102")) {
            HttpConfig.TOKEN = "";
            Intent intent1 = new Intent(this, SplashActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
            android.os.Process.killProcess(android.os.Process.myPid());
        }else if(code.equals("400000999999")){
            TTSUtils.getInstance().speak(getString(R.string.cheack_fail)+","+getString(R.string.again_finger));
        }else {
            speak(message);
        }

    }

    public void onRegularFail(Throwable e, boolean isNetWork) {
            if(isNetWork){
                speak(getResources().getString(R.string.network_unavailable));
            }
    }

    @Override
    public void openByFingerPrints() {

    }

    @Override
    public void onPassWord(PasswordBean passwordBean) {
        showActivity(SettingActivity.class);
    }

    @Override
    public void dialogCancel() {
        member.setBackground(getResources().getDrawable(R.drawable.border_red));
        manager.setBackground(null);
        member.setTextColor(getResources().getColor(R.color.almost_white));
        manager.setTextColor(getResources().getColor(R.color.text_gray));
    }

    @Override
    public void onVenuePay() {

    }
}

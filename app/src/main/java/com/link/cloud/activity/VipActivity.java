package com.link.cloud.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.RequiresApi;
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
import com.link.cloud.controller.VipController;
import com.link.cloud.network.bean.AllUser;
import com.link.cloud.network.bean.BindUser;
import com.link.cloud.network.bean.CabinetInfo;
import com.link.cloud.network.bean.EdituserRequest;
import com.link.cloud.network.bean.PasswordBean;
import com.link.cloud.utils.DialogCancelListener;
import com.link.cloud.utils.DialogUtils;
import com.link.cloud.utils.HexUtil;
import com.link.cloud.utils.OpenDoorUtil;
import com.link.cloud.utils.RxTimerDelayUtil;
import com.link.cloud.utils.RxTimerUtil;
import com.link.cloud.utils.TTSUtils;
import com.link.cloud.utils.Venueutils;
import com.link.cloud.widget.PublicTitleView;
import com.orhanobut.logger.Logger;
import com.zitech.framework.utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
public class VipActivity extends BaseActivity implements VipController.VipControllerListener, DialogCancelListener {

    private static final String TAG = "VipActivity";
    private RealmResults<AllUser> managersRealm;
    List<AllUser> managers = new ArrayList<>();
    private LinearLayout zhijingmaiLayout;
    private LinearLayout xiaochengxuLayout;
    private TextView passwordLayout;
    private EditText code_mumber;
    private PublicTitleView publicTitleView;
    private LinearLayout setLayout;
    private TextView member;
    private TextView manager;
    private VipController vipController;
    private String uid;
    private RxTimerUtil rxTimerUtil;
    boolean IsNoPerson, isDeleteAll;
    int pageNum = 100;
    int total;
    private RealmResults<AllUser> users;
    private List<AllUser> peoples;
    private RealmResults<AllUser> peopleIn;
    private ArrayList<AllUser> peoplesIn;
    private DialogUtils dialogUtils;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initViews() {
        zhijingmaiLayout = findViewById(R.id.zhijingmaiLayout);
        xiaochengxuLayout = findViewById(R.id.xiaochengxuLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        publicTitleView = findViewById(R.id.publicTitle);
        setLayout = (LinearLayout) findViewById(R.id.setLayout);
        code_mumber = (EditText) findViewById(R.id.code_number);
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
        ViewUtils.setOnClickListener(member, this);
        ViewUtils.setOnClickListener(manager, this);

        dialogUtils = DialogUtils.getDialogUtils(this);
        users = realm.where(AllUser.class).findAll();
        peoples = new ArrayList<>();
        users.addChangeListener(new RealmChangeListener<RealmResults<AllUser>>() {
            @Override
            public void onChange(RealmResults<AllUser> allUsers) {
                peoples.clear();
                peoples.addAll(realm.copyFromRealm(users));
            }
        });
        peoples.addAll(realm.copyFromRealm(users));

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
        vipController = new VipController(this);
        if (Constants.CABINET_TYPE == Constants.VIP_CABINET) {
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
            publicTitleView.hideBack();
        } else {
            setLayout.setVisibility(View.GONE);

        }
        initData();
        try {
            CabinetApplication.getInstance().serialPortOne.getOutputStream().write(OpenDoorUtil.handShake());
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialogUtils.setDialogCanceListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vip;
    }


    public static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";


    public static long convert2long(String date, String format) {

        try {

            if (!TextUtils.isEmpty(date) && !TextUtils.isEmpty(format)) {

                SimpleDateFormat sf = new SimpleDateFormat(format);

                return sf.parse(date).getTime();

            }

        } catch (ParseException e) {

            e.printStackTrace();

        }

        return 0l;

    }


    private void finger() {
        rxTimerUtil.interval(1000, new RxTimerUtil.IRxNext() {
            @Override
            public void doNext(long number) {
                System.out.println(String.valueOf(number));
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
                        final CabinetInfo uuid = realm.where(CabinetInfo.class).equalTo("uuid", uid).findFirst();
                        if (uuid != null) {
                            String endTime = uuid.getEndTime();
                            long endTimeLong = convert2long(endTime, TIME_FORMAT);
                            long now = System.currentTimeMillis();
                            if (endTimeLong > now) {
                                unlocking(uid, Constants.ActivityExtra.FINGER);
                            } else {
                                vipController.OpenVipCabinet("", uid);
                            }

                        } else {
                            if (uid != null) {
                                vipController.OpenVipCabinet("", uid);
                            } else {
                                if (CabinetApplication.getVenueUtils().img != null) {
                                    String finger = HexUtil.bytesToHexString(CabinetApplication.getVenueUtils().img);
                                    vipController.OpenVipCabinet(finger, "");
                                    IsNoPerson = true;
                                    isDeleteAll = false;
                                } else {

                                }

                            }

                        }
                    }
                }


            }
        });
    }

    private void unlocking(String uid, String type) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ActivityExtra.TYPE, type);
        bundle.putString(Constants.ActivityExtra.UUID, uid);
        showActivity(VipOpenSuccessActivity.class, bundle);
        Log.e(TAG, "unlocking: ");
        if (Constants.CABINET_TYPE != Constants.VIP_CABINET) {
            Log.e(TAG, "unlocking: ");
            finish();
        }
    }

    InputStream mInputStream;

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


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.zhijingmaiLayout:

                break;
            case R.id.xiaochengxuLayout:

                break;
            case R.id.member:
                member.setBackground(getResources().getDrawable(R.drawable.border_red));
                manager.setBackground(null);
                member.setTextColor(getResources().getColor(R.color.almost_white));
                manager.setTextColor(getResources().getColor(R.color.text_gray));
                break;
            case R.id.manager:
                View view = View.inflate(VipActivity.this, R.layout.veune_dialog, null);
                dialogUtils.showManagerDialog(view);
                manager.setBackground(getResources().getDrawable(R.drawable.border_red));
                member.setBackground(null);
                member.setTextColor(getResources().getColor(R.color.text_gray));
                manager.setTextColor(getResources().getColor(R.color.almost_white));
                RxTimerDelayUtil rxTimerDelayUtil = new RxTimerDelayUtil();
                rxTimerDelayUtil.timer(10000, new RxTimerDelayUtil.IRxNext() {
                    @Override
                    public void doNext(long number) {
                        dialogUtils.dissMiss();
                    }
                });
                break;
            case R.id.passwordLayout:
                Bundle bundle1 = new Bundle();
                bundle1.putString(Constants.ActivityExtra.TYPE, "PASSWORD");
                showActivity(VipOpenSuccessActivity.class, bundle1);
                break;
        }
    }

    @Override
    public void gotoSetting(String pass) {
        vipController.password(pass);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void initData() {
        code_mumber.setFocusable(true);
        code_mumber.setCursorVisible(true);
        code_mumber.setFocusableInTouchMode(true);
        code_mumber.requestFocus();
        code_mumber.setShowSoftInputOnFocus(false);
        /**
         * EditText编辑框内容发生变化时的监听回调
         */
        code_mumber.addTextChangedListener(new EditTextChangeListener());
    }

    public class EditTextChangeListener implements TextWatcher {
        long lastTime;

        /**
         * 编辑框的内容发生改变之前的回调方法
         */
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        /**
         * 编辑框的内容正在发生改变时的回调方法 >>用户正在输入
         * 我们可以在这里实时地 通过搜索匹配用户的输入
         */
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        /**
         * 编辑框的内容改变以后,用户没有继续输入时 的回调方法
         */
        @Override
        public void afterTextChanged(Editable editable) {
            String str = code_mumber.getText().toString();
            if (str.contains("\n")) {
                if (System.currentTimeMillis() - lastTime < 1500) {
                    code_mumber.setText("");
                    return;
                }
                lastTime = System.currentTimeMillis();
                JSONObject object = null;
                try {
                    object = new JSONObject(code_mumber.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (object == null) {
                    return;
                }
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
                vipController.findUserByQr(requestBody);
                code_mumber.setText("");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        rxTimerUtil.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
        member.setBackground(getResources().getDrawable(R.drawable.border_red));
        manager.setBackground(null);
        member.setTextColor(getResources().getColor(R.color.almost_white));
        manager.setTextColor(getResources().getColor(R.color.text_gray));
        rxTimerUtil.cancel();
        finger();


    }


    @Override
    public void modelMsg(int state, String msg) {

    }

    @Override
    public void onVipErrorCode(String msg) {
        if ("您没有租用储物柜".equals(msg)) {
            TTSUtils.getInstance().speak(getResources().getString(R.string.sorry_for_is_not_vip));
        } else {
            TTSUtils.getInstance().speak(getResources().getString(R.string.cheack_fail) + "," + msg);
        }

    }

    @Override
    public void onVipFail(Throwable e, boolean isNetWork) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (Constants.CABINET_TYPE == Constants.VIP_CABINET) {
            unRegisterReceiver();
        }
    }

    @Override
    public void getUserSuccess(final BindUser data) {
        final RealmResults<AllUser> all = realm.where(AllUser.class).findAll();
        total = data.getTotal();
        if (!isDeleteAll) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    all.deleteAllFromRealm();
                    isDeleteAll = true;

                }
            });
            int totalPage = total / pageNum + 1;
            ExecutorService executorService = Executors.newFixedThreadPool(totalPage);
            List<Future<Boolean>> futures = new ArrayList();
            if (totalPage >= 2) {
                for (int i = 2; i <= totalPage; i++) {
                    final int finalI = i;
                    Callable<Boolean> task = new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            vipController.getUser(pageNum, finalI);
                            return true;
                        }
                    };

                    futures.add(executorService.submit(task));
                }
                for (Future<Boolean> future : futures) {
                    try {
                        future.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                executorService.shutdown();
            }
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(data.getData());
            }
        });


    }


    @Override
    public void VipCabinetSuccess(final CabinetInfo vipCabinetUser) {
        String cabinetNo = vipCabinetUser.getCabinetNo();
        final RealmResults<CabinetInfo> cabinetNos = realm.where(CabinetInfo.class).equalTo("cabinetNo", cabinetNo).findAll();
        if (cabinetNos.size() > 0) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    cabinetNos.deleteAllFromRealm();
                }
            });
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(vipCabinetUser);
            }
        });

        if (IsNoPerson) {
            vipController.getUser(1, pageNum);
        }
        unlocking(uid, Constants.ActivityExtra.FINGER);
    }

    @Override
    public void qrSuccess(EdituserRequest allUser) {
        String uuid = allUser.getUuid();
        final RealmResults<CabinetInfo> cabinetNos = realm.where(CabinetInfo.class).equalTo("cabinetNo", uuid).findAll();
//        if(cabinetNos.size()>0){
//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    cabinetNos.deleteAllFromRealm();
//                }
//            });
//        }
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                realm.copyToRealm(codeInBean);
//            }
//        });

        unlocking(uuid, Constants.ActivityExtra.FINGER);
    }


    @Override
    public void VipPassWord(PasswordBean passwordBean) {
        showActivity(SettingActivity.class);
    }


}

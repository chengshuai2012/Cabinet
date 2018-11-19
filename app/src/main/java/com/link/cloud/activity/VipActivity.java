package com.link.cloud.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.link.cloud.utils.TTSUtils;
import com.link.cloud.widget.PublicTitleView;
import com.orhanobut.logger.Logger;
import com.zitech.framework.utils.ToastMaster;
import com.zitech.framework.utils.ViewUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.realm.Realm;
import io.realm.RealmChangeListener;
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
    boolean IsNoPerson,isDeleteAll ;
    int pageNum =100;
    int total;
    private RealmResults<AllUser> users;
    private List<AllUser> peoples;
    private RealmResults<AllUser> peopleIn;
    private ArrayList<AllUser> peoplesIn;

    @Override
    protected void initViews() {
        zhijingmaiLayout = findViewById(R.id.zhijingmaiLayout);
        xiaochengxuLayout = findViewById(R.id.xiaochengxuLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        publicTitleView =  findViewById(R.id.publicTitle);
        setLayout = (LinearLayout) findViewById(R.id.setLayout);
        member = (TextView) findViewById(R.id.member);
        manager = (TextView) findViewById(R.id.manager);
        publicTitleView.hideBack();
        rxTimerUtil = new RxTimerUtil();
        ViewUtils.setOnClickListener(zhijingmaiLayout, this);
        ViewUtils.setOnClickListener(xiaochengxuLayout, this);
        ViewUtils.setOnClickListener(passwordLayout, this);
        ViewUtils.setOnClickListener(setLayout, this);
        if (!TextUtils.isEmpty(getIntent().getStringExtra(Constants.ActivityExtra.TYPE))){
            setLayout.setVisibility(View.GONE);
        }
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

        peopleIn = realm.where(AllUser.class).equalTo("isIn",1).findAll();
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
        if(Constants.CABINET_TYPE==Constants.VIP_CABINET){
           RegisteReciver();
        }
        finger();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vip;
    }


        public static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";


        public static long convert2long(String date, String format) {

            try {

                if (!TextUtils.isEmpty(date)&&!TextUtils.isEmpty(format)) {

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
                if(isScanning){
                int state = CabinetApplication.getVenueUtils().getState();
                if (state == 3) {
                    IsNoPerson =false;
                    uid = CabinetApplication.getVenueUtils().identifyNewImg(peoplesIn);
                    if(uid==null){
                        uid = CabinetApplication.getVenueUtils().identifyNewImg(peoples);
                    }
                    final CabinetInfo uuid = realm.where(CabinetInfo.class).equalTo("uuid", uid).findFirst();
                    if (uuid != null) {
                        String endTime = uuid.getEndTime();
                        long endTimeLong = convert2long(endTime, TIME_FORMAT);
                        long now =System.currentTimeMillis();
                        if(endTimeLong>now){
                            unlocking(uid, Constants.ActivityExtra.FINGER);
                        }else {
                            vipController.OpenVipCabinet("", uid);
                        }

                    } else {
                        if (uid != null) {
                            vipController.OpenVipCabinet("", uid);
                        } else {
                            String finger = HexUtil.bytesToHexString(CabinetApplication.getVenueUtils().img);
                            vipController.OpenVipCabinet(finger, "");
                            IsNoPerson = true;
                            isDeleteAll =false;
                        }

                    }
                }
                if (state == 4) {
                    //TTSUtils.getInstance().speak(getResources().getString(R.string.again_finger));
                }
                if (state != 4 && state != 3) {

                }
            }}
        });
    }
    private void unlocking(String uid, String type) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ActivityExtra.TYPE, type);
        bundle.putString(Constants.ActivityExtra.UUID, uid);
        showActivity(VipOpenSuccessActivity.class, bundle);
        if(Constants.CABINET_TYPE!=Constants.VIP_CABINET){
            finish();
        }
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
        if ("您没有租用储物柜".equals(msg)) {
            TTSUtils.getInstance().speak(getResources().getString(R.string.sorry_for_is_not_vip));
        }else {
            TTSUtils.getInstance().speak(getResources().getString(R.string.cheack_fail)+","+getResources().getString(R.string.verify_agin));
        }

    }

    @Override
    public void onVipFail(Throwable e, boolean isNetWork) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rxTimerUtil.cancel();
        if(Constants.CABINET_TYPE==Constants.VIP_CABINET){
            unRegisterReceiver();
        }
    }

    @Override
    public void getUserSuccess(final BindUser data) {
        final RealmResults<AllUser> all = realm.where(AllUser.class).findAll();
        total =data.getTotal();
            if(!isDeleteAll){
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        all.deleteAllFromRealm();
                        isDeleteAll=true;

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
                    executorService.shutdown();}
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
        if(cabinetNos.size()>0){
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

        if(IsNoPerson){
            vipController.getUser(1,pageNum);
        }
        unlocking(uid, Constants.ActivityExtra.FINGER);
    }


}

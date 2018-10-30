package com.link.cloud.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.link.cloud.Constants;
import com.link.cloud.R;
import com.link.cloud.base.BaseActivity;
import com.link.cloud.controller.MainController;
import com.link.cloud.network.HttpConfig;
import com.link.cloud.network.bean.BindUser;
import com.link.cloud.network.bean.CabinetInfo;
import com.link.cloud.network.bean.RequestBindFinger;
import com.orhanobut.logger.Logger;
import com.zitech.framework.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.realm.Realm;
import io.realm.RealmList;


public class MainActivity extends BaseActivity implements MainController.MainControllerListener {

    private TextView member;
    private TextView manager;
    private TextClock textclock;
    private LinearLayout openLayout;
    private LinearLayout closeLayout;
    private LinearLayout regularLayout;
    private LinearLayout vipLayout;
    private MainController mainController;
    int pageNum,total;
    Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        pageNum=100;
        realm =Realm.getDefaultInstance();
        mainController = new MainController(this);
        mainController.login("CHINA00001","0D874A5A3B0C3AAB71E35EE325693762");

    }

    @Override
    protected void initViews() {

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

        ViewUtils.setOnClickListener(regularLayout, this);
        ViewUtils.setOnClickListener(vipLayout, this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Bundle bundle =new Bundle();
        switch (v.getId()) {
            case R.id.regularLayout:
                bundle.putString(Constants.ActivityExtra.TYPE,"REGULAR");
                showActivity(RegularActivity.class,bundle);
                break;
            case R.id.vipLayout:
                bundle.putString(Constants.ActivityExtra.TYPE,"VIP");
                showActivity(VipActivity.class,bundle);
                break;


        }
    }

    @Override
    public void onLoginSuccess(String token) {
        HttpConfig.TOKEN =token;
        mainController.getUser(pageNum,1);
        mainController.temCabinet("c2ef5a5e995e48b08b7650f0648b52b2");
        mainController.useCabinet("c2ef5a5e995e48b08b7650f0648b52b2");
        mainController.returnCabinet("c2ef5a5e995e48b08b7650f0648b52b2");
        mainController.getCabinetInfo();
    }

    @Override
    public void onMainErrorCode(String msg) {

    }

    @Override
    public void onMainFail(Throwable e, boolean isNetWork) {

    }

    @Override
    public void getUserSuccess(final BindUser data) {
        Logger.e(data.getData().get(0).getUuid());
        int totalPage = total / pageNum + 1;
        ExecutorService executorService = Executors.newFixedThreadPool(totalPage);
        List<Future<Boolean>> futures = new ArrayList();
        if (totalPage >= 2) {
            for (int i = 2; i < totalPage; i++) {
                final int finalI = i;
                Callable<Boolean> task = new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        mainController.getUser(pageNum,finalI);
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
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(data.getData());
            }
        });
    }

    @Override
    public void onCabinetInfoSuccess(final RealmList<CabinetInfo> data) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(data);
            }
        });
    }

    @Override
    public void modelMsg(int state, String msg) {

    }
}

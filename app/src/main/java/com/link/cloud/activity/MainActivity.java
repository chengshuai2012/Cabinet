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
import com.link.cloud.network.BaseEntity;
import com.link.cloud.network.BaseObserver;
import com.link.cloud.network.HttpConfig;
import com.link.cloud.network.IOMainThread;
import com.link.cloud.network.RetrofitFactory;
import com.orhanobut.logger.Logger;
import com.zitech.framework.utils.ViewUtils;



public class MainActivity extends BaseActivity {

    private TextView member;
    private TextView manager;
    private TextClock textclock;
    private LinearLayout openLayout;
    private LinearLayout closeLayout;
    private LinearLayout regularLayout;
    private LinearLayout vipLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        RetrofitFactory.getInstence().API()
                .appLogin("CHINA00001","0D874A5A3B0C3AAB71E35EE325693762")
                .compose(IOMainThread.<BaseEntity<String>>composeIO2main())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccees(BaseEntity<String> t) throws Exception {
                        Logger.e("success");
                        HttpConfig.TOKEN=t.getData();
                        RetrofitFactory.getInstence().API().sendVCode("18574107629").compose(IOMainThread.<BaseEntity>composeIO2main()).subscribe(new BaseObserver() {
                            @Override
                            public void onNext(Object o) {

                            }

                            @Override
                            protected void onSuccees(BaseEntity t) throws Exception {

                            }

                            @Override
                            protected void onCodeError(String msg) throws Exception {

                            }

                            @Override
                            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

                            }
                        });
                    }

                    @Override
                    protected void onCodeError(String msg) throws Exception {
                        Log.e("onCodeError: ",msg );
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        Logger.e("success");
                    }
                });
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
}
